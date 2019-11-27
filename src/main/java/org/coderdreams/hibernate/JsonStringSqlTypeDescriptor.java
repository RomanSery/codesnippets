package org.coderdreams.hibernate;

import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;

import com.vladmihalcea.hibernate.type.json.internal.AbstractJsonSqlTypeDescriptor;


public class JsonStringSqlTypeDescriptor extends AbstractJsonSqlTypeDescriptor {

	private static final long serialVersionUID = 1L;
	static final JsonStringSqlTypeDescriptor INSTANCE = new JsonStringSqlTypeDescriptor();

    @Override
    public <X> ValueBinder<X> getBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
        return new StringBasicBinder<>(javaTypeDescriptor, JsonStringSqlTypeDescriptor.this);
    }
}