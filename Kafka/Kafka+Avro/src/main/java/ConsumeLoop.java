import example.avro.Order;

import org.apache.kafka.clients.consumer.*;

public abstract class ConsumeLoop implements Runnable {

    public abstract void process(ConsumerRecord<String, Order> record);

    @Override
    public void run() {

    }
}
