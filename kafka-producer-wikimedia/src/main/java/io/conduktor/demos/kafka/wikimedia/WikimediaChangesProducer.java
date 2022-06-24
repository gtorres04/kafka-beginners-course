package io.conduktor.demos.kafka.wikimedia;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WikimediaChangesProducer {
    public static void main(String[] args) throws InterruptedException {
        String bootstrapServers = "127.0.0.1:9092";

        // Crear las propiedades del productor
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers); // IP y PUERTO del servidor de Kafka
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // Serializador de la clave del mensaje a enviar
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // Serializador del valor del mensaje a enviar

        // Configuracion de productor seguro (Cliente Kafka con version <=2.8)
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, Boolean.TRUE.toString()); // No permite que duplique mensajes publicados por el productor
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all"); // Esperamos confirmacion de escritura por kafka
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE)); // En caso de error la cantidad maxima de reintentos que se harian.
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, Integer.toString(5));
        properties.setProperty(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, Integer.toString(120000)); // Tiempo maximo que tiene el productor para publicar un mensaje en kafka despues de un error.

        // Crear el productor
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        String topic = "wikimedia.recentchange";

        EventHandler eventHandler = new WikimediaChangeHandler(producer, topic);

        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));
        EventSource eventSource = builder.build();

        // Iniciar el productor en otro hilo
        eventSource.start();

        //
        TimeUnit.MINUTES.sleep(10);

    }
}
