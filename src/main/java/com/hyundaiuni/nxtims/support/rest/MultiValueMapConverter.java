package com.hyundaiuni.nxtims.support.rest;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

public class MultiValueMapConverter {
    private static final Log log = LogFactory.getLog(MultiValueMapConverter.class);
    
    private MultiValueMap<String, Object> multiValueMap;

    private Object bean;

    public MultiValueMapConverter(Object bean) {
        this.multiValueMap = new LinkedMultiValueMap<>();
        this.bean = bean;
    }

    @SuppressWarnings("rawtypes")
    public MultiValueMap convert() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IntrospectionException{
        this.addMultiValueFromBean(this.multiValueMap, "", this.bean);
        return this.multiValueMap;
    }

    @SuppressWarnings("unused")
    private boolean isPrimitiveType(Object object) {
        if((object instanceof String) || (object instanceof Integer) || (object instanceof Float)
           || (object instanceof Void) || (object instanceof Boolean) || (object instanceof Long)) {
            return true;
        }
        else {
            return false;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private MultiValueMap addMultiValueFromBean(MultiValueMap multiValueMap, String name, Object object)
        throws IntrospectionException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        MultiValueMap mvm = multiValueMap;

        Field[] fields = object.getClass().getDeclaredFields();

        for(Field field : fields) {
            String _name = ("").equals(name) ? field.getName() : name + "." + field.getName();

            try {
                Object value = new PropertyDescriptor(field.getName(), object.getClass()).getReadMethod().invoke(
                    object);

                if(value == null) {}
                else {
                    if(value instanceof Map) {
                        mvm = this.addMultiValueFromMap(multiValueMap, _name, (Map)value);
                    }
                    else if(value instanceof Iterable) {
                        mvm = this.addMultiValueFromIterable(multiValueMap, _name, (Iterable)value);
                    }
                    else if(value instanceof MultipartFile) {
                        MultipartFile multipartFile = (MultipartFile)value;
                        ByteArrayResource resource = null;
                        try {
                            resource = new ByteArrayResource(multipartFile.getBytes()) {
                                @Override
                                public String getFilename() throws IllegalStateException {
                                    return multipartFile.getOriginalFilename();
                                }
                            };
                        }
                        catch(IOException e) {
                            log.info(e);
                        }
                        mvm.add(_name, resource);
                    }
                    else {
                        value = new PropertyDescriptor(field.getName(), object.getClass()).getReadMethod().invoke(
                            object);
                        mvm.add(_name, value);
                    }
                }
            }
            catch(Exception e) {
                log.info(e);
            }
        }

        return mvm;
    }

    @SuppressWarnings("rawtypes")
    private MultiValueMap addMultiValueFromIterable(MultiValueMap multiValueMap, String name, Iterable iterable)
        throws NoSuchMethodException, IntrospectionException, IllegalAccessException, InvocationTargetException {
        MultiValueMap mvm = multiValueMap;

        int i = 0;
        for(Object object : iterable) {
            String _name = name + "[" + i + "]";
            if(object instanceof Map) {
                mvm = this.addMultiValueFromMap(mvm, _name, (Map)object);
            }
            else if(object instanceof Iterable) {
                mvm = this.addMultiValueFromIterable(mvm, _name, (Iterable)object);
            }
            else {
                mvm = this.addMultiValueFromBean(mvm, _name, object);
                i++;
            }
        }
        return mvm;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private MultiValueMap addMultiValueFromMap(MultiValueMap multiValueMap, String name, Map map) {
        MultiValueMap mvm = multiValueMap;
        Set<String> keys = map.keySet();

        for(String key : keys) {
            String _name = name + "." + key;

            Object value = map.get(key);
            if(value instanceof Map) {
                mvm = this.addMultiValueFromMap(mvm, _name, (Map)value);
            }
            else if(value instanceof Iterable) {}
            else {
                mvm.add(_name, value);
            }
        }

        return mvm;
    }
}
