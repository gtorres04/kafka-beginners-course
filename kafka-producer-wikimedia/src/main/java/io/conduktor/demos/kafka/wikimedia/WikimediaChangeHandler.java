package io.conduktor.demos.kafka.wikimedia;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikimediaChangeHandler implements EventHandler {
    private final KafkaProducer<String, String> kafkaProducer;
    private final String topic;

    private final Logger log = LoggerFactory.getLogger(WikimediaChangeHandler.class.getSimpleName());

    public WikimediaChangeHandler(KafkaProducer<String, String> producer, String topic) {
        this.kafkaProducer = producer;
        this.topic = topic;
    }

    /**
     * Es cuando la transmision esta abierta
     */
    @Override
    public void onOpen() {

    }

    /**
     * Cuando se cierra la trasmision se cierra el productor
     */
    @Override
    public void onClosed() {
        this.kafkaProducer.close();
    }

    /**
     * La trasmision a recibido un mensaje proveniente de la transmision HTTP y por lo tanto se envia el mensaje a Kafka mediante el productor.
     *
     * @param event        the event name, from the {@code event:} line in the stream
     * @param messageEvent a {@link MessageEvent} object containing all the other event properties
     */
    @Override
    public void onMessage(String event, MessageEvent messageEvent) {
        log.info(messageEvent.getData());
        kafkaProducer.send(new ProducerRecord<>(topic, messageEvent.getData()));
    }

    @Override
    public void onComment(String comment) {

    }

    @Override
    public void onError(Throwable t) {
        log.error("Error in Stream Reading");
    }
}
