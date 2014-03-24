package com.nissatech.proasense.eventplayer;

import java.util.Properties;
import java.util.concurrent.Semaphore;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

import kafka.producer.ProducerConfig;

/**
 *
 * @author aleksandar
 */
public class EventSender implements Runnable
{

    private final Semaphore availability;
    private final Producer kafkaProducer;
    private final String payload;

    public EventSender(Semaphore availability, Producer producer, String payload)
    {
        this.availability = availability;
        this.kafkaProducer = producer;
        this.payload=payload;
    }

    @Override
    public void run()
    {

        System.out.println("sending something");
//        KeyedMessage<String, String> message = new KeyedMessage<String, String>("some_topic", "MSG"+System.currentTimeMillis());
        KeyedMessage<String, String> message = new KeyedMessage<String, String>("some_topic", payload);
        kafkaProducer.send(message);
        availability.release();

    }

}
