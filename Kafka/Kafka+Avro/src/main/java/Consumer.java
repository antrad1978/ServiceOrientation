import example.avro.Order;
import example.avro.OrderItem;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.util.Utf8;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Consumer {
    private static String TEST_TOPIC = "test";

    public static void main(String[] args){
        List<String> topicList = new ArrayList<>();
        topicList.add(TEST_TOPIC);
        KafkaConsumer<String, byte[]> demoKafkaConsumer = createConsumer();

        demoKafkaConsumer.subscribe(topicList);
        Schema classSchema = Order.getClassSchema();
        final DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(classSchema);
        try {
            while (true) {
                ConsumerRecords<String, byte[]> records = demoKafkaConsumer.poll(500);
                for (ConsumerRecord<String, byte[]> record : records){
                    System.out.println("offset = " + record.offset() + "key =" + record.key() + "value =" + record.value());

                    BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(record.value(), null);
                    GenericRecord data = datumReader.read(null, decoder);

                    Order order = createOrderFromAvroSerializedObject(data);

                    System.out.println("Order Customer:"+ order.getCustomer() + " " + "Order date:" + order.getDate());
                }
                //TODO : Do processing for data here
                demoKafkaConsumer.commitAsync(new OffsetCommitCallback() {
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {

                    }
                });
            }
        } catch (Exception ex) {
            //TODO : Log Exception Here
        } finally {
            try {
                demoKafkaConsumer.commitSync();

            } finally {
                demoKafkaConsumer.close();
            }
        }
    }

    private static Order createOrderFromAvroSerializedObject(GenericRecord data) {
        Order order=new Order();
        order.setCustomer(data.get("customer").toString());
        order.setDate(data.get("date").toString());
        order.setNotes(data.get("notes").toString());

        GenericData.Array items = (GenericData.Array)data.get("Items");

        List<OrderItem> orderItems=new ArrayList<>();

        for (Object row : items) {
            OrderItem item=new OrderItem();
            GenericData.Record recordItem=(GenericData.Record)row;
            item.setUm(recordItem.get("um").toString());
            item.setQtt((Integer)recordItem.get("qtt"));
            item.setItem(recordItem.get("um").toString());
            item.setDescription(recordItem.get("description").toString());
            orderItems.add(item);
        }
        order.setItems(orderItems);
        return order;
    }

    private static KafkaConsumer<String, byte[]> createConsumer() {
        Properties consumerProperties = new Properties();
        consumerProperties.put("bootstrap.servers", "127.0.0.1:9092");
        consumerProperties.put("group.id", "Demo_Group");
        consumerProperties.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("value.deserializer",
                "org.apache.kafka.common.serialization.ByteArrayDeserializer");

        consumerProperties.put("enable.auto.commit", "true");
        consumerProperties.put("auto.commit.interval.ms", "1000");
        consumerProperties.put("session.timeout.ms", "30000");

        return new KafkaConsumer<String, byte[]>(consumerProperties);
    }
}
