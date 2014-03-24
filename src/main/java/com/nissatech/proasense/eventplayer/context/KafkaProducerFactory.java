package com.nissatech.proasense.eventplayer.context;

import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

/**
 *
 * @author aleksandar
 * @param <T> Type of the message key
 * @param <R> Type of the message payload
 */
public class KafkaProducerFactory<T,R>
{

    public KafkaProducerFactory()
    {
        
    }
    
    public Producer<T,R> createProducer()
    {
        Properties props = new Properties();
        props.put("metadata.broker.list", "127.0.0.1:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("producer.type", "async");
        props.put("queue.enqueue.timeout.ms", "-1");
        props.put("batch.num.messages", "200");
        props.put("compression.codec", "1");
        ProducerConfig config = new ProducerConfig(props);
        return new Producer<T, R>(config);
    }
}
