package SariDB.db;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.hadoop.util.HadoopOutputFile;
import org.apache.parquet.io.OutputFile;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;

import java.io.IOException;
import java.util.Map;

public record PersistanceHandler(Path path) {

    private static final Schema SCHEMA = new Schema.Parser().parse("{\"type\":\"record\",\"name\":\"KeyValue\",\"fields\":[{\"name\":\"key\",\"type\":\"string\"},{\"name\":\"value\",\"type\":\"string\"}]}");
    private static final Configuration CONF = new Configuration();

    private static MessageType getMessageType() {
        // Define the schema for the Parquet file
        return MessageTypeParser.parseMessageType(
                "message KeyValue { required binary key (UTF8); required binary value (UTF8); }"
        );
    }

    private static GenericRecord getRecord(String key, String value) {
        // Create a GenericRecord using Avro

        GenericRecord rec = new GenericData.Record(SCHEMA);
        rec.put("key", key);
        rec.put("value", value);
        return rec;
    }

    private static String getAvroSchemaString() {
        // Define the Avro schema
        return "{\"type\":\"record\",\"name\":\"KeyValue\",\"fields\":[{\"name\":\"key\",\"type\":\"string\"},{\"name\":\"value\",\"type\":\"string\"}]}";
    }

    public void writeToFile(Map<String, String> map) throws IOException {
        // Define the Parquet schema
        MessageType schema = getMessageType();

        // Configure ParquetWriter

        try (ParquetWriter<GenericRecord> writer = getParquetWriter()) {
            // Write data to the Parquet file
            for (Map.Entry<String, String> entry : map.entrySet()) {
                writer.write(getRecord(entry.getKey(), entry.getValue())); // required Void got GenericRecord
            }
        }
    }

    private ParquetWriter<GenericRecord> getParquetWriter() throws IOException {
        // Specify the compression codec
        CompressionCodecName codec = CompressionCodecName.SNAPPY;

        // Create a ParquetWriter
        OutputFile outputFile = HadoopOutputFile.fromPath(path, CONF);
        return AvroParquetWriter.<GenericRecord>builder(outputFile)
                .withConf(CONF)
                .withSchema(SCHEMA) //
                .withCompressionCodec(codec)
                .build();
    }

    public static void main(String[] args) {
        // Example usage
        Map<String, String> data = Map.of("key1", "value1", "key2", "value2");

        try {
            new PersistanceHandler(new Path("here.parquet")).writeToFile(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
