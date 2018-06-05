import example.avro.Order;
import example.avro.OrderItem;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SecondProducer {
    public static final String SECOND_TEST = "second_test";

    public static void main(String[] args){
        try
        {
            Order order = createTestOrder();
            // Get the Apache AVRO message
            ByteArrayOutputStream data = AvroUtils.GenerateAvroStream(order);
            System.out.println("Here comes the data: " + data);

            KafkaProducer<String, Order> messageProducer = createProducer();
            ProducerRecord<String, Order> producerRecord = new ProducerRecord<String, Order>(SECOND_TEST,"1",order);
            messageProducer.send(producerRecord);
            messageProducer.close();
        }catch(IOException ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
    }

    private static KafkaProducer<String, Order> createProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("key.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        props.put("schema.registry.url", "http://localhost:8081");

        return new KafkaProducer<String, Order>(props);
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
