package com.giogar.parser;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created with IntelliJ IDEA.
 * User: giovanni
 * Date: 29/03/13
 * Time: 11:59
 * To change this template use File | Settings | File Templates.
 */
public class JsonVersionParser implements MessageBodyReader<String> {

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String readFrom(Class<String> stringClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> stringStringMultivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
