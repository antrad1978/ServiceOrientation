import example.avro.Order;
import example.avro.OrderItem;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FirstSample {
    public static void main(String[] args){
        Order order = createSampleOrder();

        DatumWriter<Order> orderDatumWriter = new SpecificDatumWriter<Order>(Order.class);
        DataFileWriter<Order> dataFileWriter = new DataFileWriter<Order>(orderDatumWriter);
        try {
            dataFileWriter.create(order.getSchema(), new File("order.avro"));
            dataFileWriter.append(order);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                dataFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Deserialize Users from disk
        DatumReader<Order> userDatumReader = new SpecificDatumReader<Order>(Order.class);
        try {
            DataFileReader<Order> dataFileReader = new DataFileReader<Order>(new File("order.avro"), userDatumReader);
            Order order2 = null;
            while (dataFileReader.hasNext()) {
                order2 = dataFileReader.next(order2);
                System.out.println(order2);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static Order createSampleOrder() {
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
