package io.conduktor.demos.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemo {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());

    public static void main(String[] args) {
        log.info("I am a Kafka Producer");

        // Crear las propiedades del productor
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092"); // IP y PUERTO del servidor de Kafka
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // Serializador de la clave del mensaje a enviar
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // Serializador del valor del mensaje a enviar

        // Crear el productor
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        //Crear un registro a producir
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("demo_java", "hello world");

        // Enviar datos - asincronamente
        producer.send(producerRecord);

        /* Vaciar datos - sincronamente. Esto lo que quiere decir es,
          bloquea esta linea de codigo hasta que kafka envie y reciba
          todos los datos de mi productor.*/
        producer.flush();

        // vacia y cierra el productor, este motodo tambien llamada a flush() internamente.
        producer.close();
    }
}
