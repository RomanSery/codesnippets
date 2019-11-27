package org.coderdreams.hibernate;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.BasicBinder;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

public class StringBasicBinder<X> extends BasicBinder<X> {
    private final JavaTypeDescriptor<X> javaTypeDescriptor;

    StringBasicBinder(JavaTypeDescriptor<X> javaTypeDescriptor, SqlTypeDescriptor sqlDescriptor) {
        super(javaTypeDescriptor, sqlDescriptor);
        this.javaTypeDescriptor = javaTypeDescriptor;
    }

    @Override
    protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options) throws SQLException {
        st.setString(index, javaTypeDescriptor.unwrap(value, String.class, options));
    }

    @Override
    protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
            throws SQLException {
        st.setString(name, javaTypeDescriptor.unwrap(value, String.class, options));
    }
}
