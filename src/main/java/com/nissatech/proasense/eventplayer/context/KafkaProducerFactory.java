package com.nissatech.proasense.eventplayer.context;

import com.google.inject.Inject;
import com.nissatech.proasense.eventplayer.PropertyProvider;
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

    @Inject
    Properties props;
    
    @Inject
    public KafkaProducerFactory()
    {
        this.props= new PropertyProvider().get();
    }
    
    public Producer<T, R> createProducer()
    {
        props.put("metadata.broker.list", props.getProperty("kafka.host"));
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("producer.type", "sync");
        props.put("queue.enqueue.timeout.ms", "-1");
        props.put("batch.num.messages", "200");
        props.put("compression.codec", "1");
        props.put("request.required.acks", "0");
        ProducerConfig config = new ProducerConfig(props);
        return new Producer<T, R>(config);
    }
}
