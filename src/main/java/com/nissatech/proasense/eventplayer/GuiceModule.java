package com.nissatech.proasense.eventplayer;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.nissatech.proasense.eventplayer.context.ContextListener;
import com.nissatech.proasense.eventplayer.context.DateTimeConverter;
import com.nissatech.proasense.eventplayer.context.JacksonConfigurator;
import com.nissatech.proasense.eventplayer.context.KafkaProducerFactory;
import com.nissatech.proasense.eventplayer.exception.mappers.InvalidPartnerExceptionMapper;
import com.nissatech.proasense.eventplayer.model.CassandraClient;
import com.nissatech.proasense.eventplayer.partnerconfigurations.PartnerConfigurationResolver;
import java.util.Properties;
import kafka.javaapi.producer.Producer;

/**
 *
 * @author aleksandar
 */
public class GuiceModule implements Module
{

    @Override
    public void configure(Binder binder)
    {
        /** Endponts **/
        binder.bind(EventPlayer.class);
        binder.bind(BatchEvents.class);
        
        /** Listeners **/
        binder.bind(ContextListener.class);
        binder.bind(JacksonConfigurator.class);
        
        /** Providers **/
        binder.bind(DateTimeConverter.class);
        
        /** Exception mappers **/
        binder.bind(InvalidPartnerExceptionMapper.class);
        
        /**Custom classes**/
        binder.bind(PartnerConfigurationResolver.class);
        binder.bind(CassandraClient.class);
        binder.bind(Properties.class).toProvider(PropertyProvider.class);
        //binder.bind(AsyncRequestWorker.class);
        binder.bind(KafkaProducerFactory.class).toInstance(new KafkaProducerFactory<String,String>());
       
        
    }
    
}
