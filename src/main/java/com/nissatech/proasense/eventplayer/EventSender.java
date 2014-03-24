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

    public EventSender(Semaphore availability, Producer producer)
    {
        this.availability = availability;
        this.kafkaProducer = producer;
    }

    @Override
    public void run()
    {

        System.out.println("sending something");
        //Kafka Producer implementation goes here.
        //Thread.sleep(1500);
        KeyedMessage<String, String> message = new KeyedMessage<String, String>("some_topic", "test", "MSG"+System.currentTimeMillis());
        kafkaProducer.send(message);
        availability.release();

    }

}
