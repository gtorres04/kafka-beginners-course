package io.conduktor.demos.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Properties;
import java.util.stream.IntStream;

public class ProducerDemoKeys {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemoKeys.class.getSimpleName());

    public static void main(String[] args) {
        log.info("I am a Kafka Producer");

        // Crear las propiedades del productor
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092"); // IP y PUERTO del servidor de Kafka
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // Serializador de la clave del mensaje a enviar
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // Serializador del valor del mensaje a enviar

        // Crear el productor
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);


        IntStream.range(0, 10).forEach(index -> {
            final String topic = "demo_java";
            final String value = String.format("hello world %s", index);
            final String key = String.format("ID_%s", index);

            //Crear un registro a producir
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);

            // Enviar datos - asincronamente
            producer.send(producerRecord, (metadata, exception) -> {
                // Se ejecuta cada vez que un registro llega satisfactoriamente a Kafka y si no se recibe la exception.
                Optional.ofNullable(exception)
                        .ifPresentOrElse(
                                e -> log.error("Error mientras se producia el mensaje: {}", e.toString()),
                                () -> log.info("Recibida nueva metadata/ \n" +
                                        "Topic: {}\n" +
                                        "Key: {}\n" +
                                        "Partition: {}\n" +
                                        "Offset: {}\n" +
                                        "Timestamp: {}", metadata.topic(), producerRecord.key(), metadata.partition(), metadata.offset(), metadata.timestamp()));
            });
        });


        /* Vaciar datos - sincronamente. Esto lo que quiere decir es,
          bloquea esta linea de codigo hasta que kafka envie y reciba
          todos los datos de mi productor.*/
        producer.flush();

        // vacia y cierra el productor, este motodo tambien llamada a flush() internamente.
        producer.close();
    }
}
