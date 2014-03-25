package com.nissatech.proasense.eventplayer.context;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;



@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON) 
public class JacksonConfigurator extends ResteasyJackson2Provider
{
    
    @Override
    public void writeTo(Object value, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException
    {        
        ObjectMapper mapper = locateMapper(type, mediaType);
        mapper.setDateFormat(new ISO8601DateFormat());
        mapper.registerModule(new JodaModule());
        super.writeTo(value, type, genericType, annotations, mediaType, httpHeaders, entityStream);
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException
    {
        ObjectMapper mapper = locateMapper(type, mediaType);
        mapper.setDateFormat(new ISO8601DateFormat());
        mapper.registerModule(new JodaModule());
        return super.readFrom(type, genericType, annotations, mediaType, httpHeaders, entityStream); 
    }
    
    
    
    

}
