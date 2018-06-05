import example.avro.Order;
import example.avro.OrderItem;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Producer {

    public static final String TEST = "test";

    public static void main(String[] args){
        try
        {
            Order order = createTestOrder();
            // Get the Apache AVRO message
            ByteArrayOutputStream data = AvroUtils.GenerateAvroStream(order);
            System.out.println("Here comes the data: " + data);

            KafkaProducer<String, byte[]> messageProducer = createProducer();
            ProducerRecord<String, byte[]> producerRecord = null;
            producerRecord = new ProducerRecord<String, byte[]>(TEST,"1",data.toByteArray());
            messageProducer.send(producerRecord);
            messageProducer.close();
        }catch(IOException ex)
        {
            System.out.println (ex.toString());
            ex.printStackTrace();
        }
    }

    private static KafkaProducer<String, byte[]> createProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        return new KafkaProducer<String, byte[]>(props);
    }

    private static Order createTestOrder() {
        Order order=new Order();
        order.setCustomer("customer");
        order.setDate("11/11/2018");
        order.setNotes("notes");
        OrderItem item=new OrderItem();
        item.setDescription("description");
        item.setItem("product");
        item.setQtt(3);
        item.setUm("qt");
        List<OrderItem> items=new ArrayList<>();
        items.add(item);
        order.setItems(items);
        return order;
    }
}
