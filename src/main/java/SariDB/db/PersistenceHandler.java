package SariDB.db;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.hadoop.util.HadoopOutputFile;
import org.apache.parquet.io.InputFile;
import org.apache.parquet.io.OutputFile;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public record PersistenceHandler(Path path) {

    private static final Schema SCHEMA = new Schema.Parser().parse("{\"type\":\"record\",\"name\":\"KeyValue\",\"fields\":[{\"name\":\"key\",\"type\":\"string\"},{\"name\":\"value\",\"type\":\"string\"}]}");
    private static final Logger logger = Logger.getLogger(PersistenceHandler.class.getName());
    private static final Configuration CONF = new Configuration();

    private static GenericRecord createRecord(String key, String value) {
        GenericRecord rec = new GenericData.Record(SCHEMA);
        rec.put("key", key);
        rec.put("value", value);
        return rec;
    }

    private static String getAvroSchemaString() {
        // Define the Avro schema
        return "{\"type\":\"record\",\"name\":\"KeyValue\",\"fields\":[{\"name\":\"key\",\"type\":\"string\"},{\"name\":\"value\",\"type\":\"string\"}]}";
    }

    public ConcurrentHashMap<String, String> readFromParquetFile(Path parquetFilePath) {
        ConcurrentHashMap<String, String> dataMap = new ConcurrentHashMap<>();
        try {
            ParquetReader<GenericRecord> parquetReader = getParquetReader();
            GenericRecord record;
            while ((record = parquetReader.read()) != null) {
                dataMap.put(record.get("key").toString(), record.get("value").toString());
            }
        } catch (IOException ignored) {
            logger.log(Level.SEVERE, "Failed to read file!");
        }
        return dataMap;
    }

    public void writeToFile(Map<String, String> map) throws IOException {
        try (ParquetWriter<GenericRecord> writer = getParquetWriter()) {
            // Write data to the Parquet file
            for (Map.Entry<String, String> entry : map.entrySet()) {
                writer.write(createRecord(entry.getKey(), entry.getValue())); // required Void got GenericRecord
            }
        }
    }

    private ParquetReader<GenericRecord> getParquetReader() throws IOException {
        CompressionCodecName codec = CompressionCodecName.SNAPPY;
        InputFile inputFile = HadoopInputFile.fromPath(path, CONF);
        return AvroParquetReader.<GenericRecord>builder(inputFile)
                .withConf(CONF)
                .build();
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
}
