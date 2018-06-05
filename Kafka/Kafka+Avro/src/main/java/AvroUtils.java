import example.avro.Order;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AvroUtils {
    public static ByteArrayOutputStream GenerateAvroStream(Order obj) throws IOException
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
        DatumWriter<Order> writer = new SpecificDatumWriter<Order>(Order.getClassSchema());

        writer.write(obj, encoder);
        encoder.flush();
        outputStream.close();
        byte[] serializedBytes = outputStream.toByteArray();
        System.out.println("Encode outputStream: " + outputStream);

        return outputStream;
    }
}
