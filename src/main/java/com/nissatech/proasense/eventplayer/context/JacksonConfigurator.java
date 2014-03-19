package com.nissatech.proasense.eventplayer.context;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.util.ISO8601DateFormat;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON) 
public class JacksonConfigurator extends ResteasyJacksonProvider
{

    @Override
    public void writeTo(Object value, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException
    {        
        ObjectMapper mapper = locateMapper(type, mediaType);
        mapper.setSerializationConfig(mapper.getSerializationConfig().withDateFormat(new ISO8601DateFormat()));
        mapper.setDeserializationConfig(mapper.getDeserializationConfig().withDateFormat(new ISO8601DateFormat()));
        super.writeTo(value, type, genericType, annotations, mediaType, httpHeaders, entityStream);
        
    }
    

}
