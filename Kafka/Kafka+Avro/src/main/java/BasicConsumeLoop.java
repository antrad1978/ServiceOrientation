import example.avro.Order;
import org.apache.kafka.common.errors.WakeupException;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

public class BasicConsumeLoop extends ConsumeLoop {
    private final KafkaConsumer<String, Order> consumer;
    private final List<String> topics;
    private final CountDownLatch shutdownLatch;

    public BasicConsumeLoop(KafkaConsumer<String, Order> consumer, List<String> topics) {
        this.consumer = consumer;
        this.topics = topics;
        this.shutdownLatch = new CountDownLatch(1);
    }

    @Override
    public void process(ConsumerRecord<String, Order> record) {

    }

    public void run() {
        try {
            consumer.subscribe(topics);

            while (true) {
                ConsumerRecords<String, Order> records = consumer.poll(Long.MAX_VALUE);
                records.forEach(record -> process(record));
            }
        } catch (WakeupException e) {
            // ignore, we're closing
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            consumer.close();
            shutdownLatch.countDown();
        }
    }

    public void shutdown() throws InterruptedException {
        consumer.wakeup();
        shutdownLatch.await();
    }
}
