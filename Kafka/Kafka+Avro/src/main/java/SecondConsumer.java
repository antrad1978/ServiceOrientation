import example.avro.Order;
import example.avro.OrderItem;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SecondConsumer {
    public static final String SECOND_TEST = "second_test";

    public static void main(String[] args){
        List<String> topicList = new ArrayList<>();
        topicList.add(SECOND_TEST);
        KafkaConsumer<String, Order> demoKafkaConsumer = createConsumer();

        demoKafkaConsumer.subscribe(topicList);
        try {
            while (true) {
                ConsumerRecords<String, Order> records = demoKafkaConsumer.poll(500);
                for (ConsumerRecord<String, Order> record : records){
                    System.out.println("offset = " + record.offset() + " key =" + record.key() + " value =" + record.value());
                }
                //TODO : Do processing for data here
                demoKafkaConsumer.commitAsync(new OffsetCommitCallback() {
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {

                    }
                });
            }
        } catch (Exception ex) {
            //TODO : Log Exception Here
        }

        finally {
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

    private static KafkaConsumer<String, Order> createConsumer() {
        Properties consumerProperties = new Properties();
        consumerProperties.put("bootstrap.servers", "127.0.0.1:9092");
        consumerProperties.put("group.id", "Second_Demo_Group");
        consumerProperties.put("key.deserializer",
                "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        consumerProperties.put("value.deserializer",
                "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        consumerProperties.put("schema.registry.url", "http://localhost:8081");

        consumerProperties.put("enable.auto.commit", "true");
        consumerProperties.put("auto.commit.interval.ms", "1000");
        consumerProperties.put("session.timeout.ms", "30000");

        return new KafkaConsumer<String, Order>(consumerProperties);
    }
}
