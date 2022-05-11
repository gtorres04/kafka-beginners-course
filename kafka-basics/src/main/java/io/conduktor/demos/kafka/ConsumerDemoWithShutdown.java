package io.conduktor.demos.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

public class ConsumerDemoWithShutdown {

    private static final Logger log = LoggerFactory.getLogger(ConsumerDemoWithShutdown.class.getSimpleName());

    public static void main(String[] args) {
        log.info("I am a Kafka Consumer");

        // Crear las propiedades del consumidor
        Properties properties = new Properties();
        properties.setProperty(BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092"); // IP y PUERTO del servidor de Kafka
        properties.setProperty(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); // Deserializador de la clave del mensaje a recibir
        properties.setProperty(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); // Deserializador del valor del mensaje a recibir
        properties.setProperty(GROUP_ID_CONFIG, "my-third-application"); // ID del grupo al que pertenecera el consumidor
        properties.setProperty(AUTO_OFFSET_RESET_CONFIG, OffsetResetStrategy.EARLIEST.name().toLowerCase()); // Desde donde iniciara la lectura de los mensajes en base al offset

        // Crear el consumidor
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // obtengo una referencia al hilo de ejecucion actual
        final Thread mainThread = Thread.currentThread();

        // Adicionando el hook de apagado
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Detectado un apagado, salgamos mediante el llamado a concumer.wakerup()...");
            consumer.wakeup();

            // Unir el hilo principal para permitir la ejecucion de el codigo en el hilo principal
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));

        try {
            // Subscribir el consumidor a nuestro topico
            consumer.subscribe(Collections.singleton("demo_java"));

            // Sondear para obtener nuevos datos
            while (true) {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(100));
                consumerRecords.forEach(consumerRecord -> log.info("Key: {}\n" +
                        "Value: {}\n" +
                        "Partition: {}\n" +
                        "Offset: {}", consumerRecord.key(), consumerRecord.value(), consumerRecord.partition(), consumerRecord.offset()));
            }
        } catch (WakeupException wakeupException) {
            log.info("Wake up exception!");
            // Ignoramos esto por lo que es una exception esperada cuando se cierra un consumidor
        } catch (Exception exception) {
            log.error("Error inesperado: {}", exception.toString());
        } finally {
            consumer.close(); // Esto ademas confirmara los offsets si es necesario
            log.info("El consumidor esta ahora elegantemente cerrado");
        }
    }
}
