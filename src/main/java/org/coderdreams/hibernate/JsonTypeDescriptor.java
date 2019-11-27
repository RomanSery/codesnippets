package org.coderdreams.hibernate;

import java.util.Objects;
import java.util.Properties;

import org.coderdreams.util.EntityUtil;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.usertype.DynamicParameterizedType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;


public final class JsonTypeDescriptor extends AbstractTypeDescriptor<Object> implements DynamicParameterizedType {

	private static final long serialVersionUID = 1L;
	private Class<? extends Object> jsonObjectClass;

    JsonTypeDescriptor() {
        super( Object.class, JsonColumnValueMutabilityPlan.INSTANCE);
    }

    @Override
    public void setParameterValues(Properties parameters) {
        jsonObjectClass = ( (ParameterType) parameters.get( PARAMETER_TYPE ) ).getReturnedClass();
    }

    @Override
    public boolean areEqual(Object one, Object another) {
        if ( one == another ) {
            return true;
        }
        if ( one == null || another == null ) {
            return false;
        }

        JsonNode n1 = EntityUtil.toJsonNode(EntityUtil.objectToJson(one));
        JsonNode n2 = EntityUtil.toJsonNode(EntityUtil.objectToJson(another));
        return Objects.equals(n1, n2);
    }

    @Override
    public String toString(Object value) {
        return EntityUtil.objectToJson(value);
    }

    @Override
    public Object fromString(String string) {
        return EntityUtil.jsonToObject(string, jsonObjectClass);
    }


    @Override
    public <X> X unwrap(Object value, Class<X> type, WrapperOptions options) {
        if ( value == null ) {
            return null;
        }
        if ( String.class.isAssignableFrom( type ) ) {
            return (X) toString(value);
        }
        if ( Object.class.isAssignableFrom( type ) ) {
            return (X) EntityUtil.toJsonNode(toString(value));
        }
        throw unknownUnwrap( type );
    }

    @Override
    public <X> Object wrap(X value, WrapperOptions options) {
        if ( value == null ) {
            return null;
        }
        return fromString(value.toString());
    }

}
