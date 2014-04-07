package com.nissatech.proasense.eventplayer;

import com.nissatech.proasense.eventplayer.model.PlaybackRequest;
import java.util.concurrent.Semaphore;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;


/**
 * Single event scheduled for sending
 * @author aleksandar
 */
public class EventSender implements Runnable
{

    
    private final Semaphore availability;
    private final Producer kafkaProducer;
    private final String payload;
    private final PlaybackRequest request;

    public EventSender(Semaphore availability, Producer producer, String payload, PlaybackRequest request)
    {
        this.availability = availability;
        this.kafkaProducer = producer;
        this.payload=payload;
        this.request = request;
    }

    @Override
    public void run()
    {
        KeyedMessage<String, String> message = new KeyedMessage<String, String>(request.getTopic(), payload);
        kafkaProducer.send(message);
        availability.release();

    }

}
