package org.coderdreams.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class BaseEnumUserType<T extends Enum<T>> implements UserType, Serializable {
	private static final Logger log = LoggerFactory.getLogger(BaseEnumUserType.class);
	private static final long serialVersionUID = 1L;
	private final Class<T> typeParameterClass;
	
	public BaseEnumUserType(Class<T> typeParameterClass) {
		super();
		this.typeParameterClass = typeParameterClass;
	}
	
	protected abstract T getById(int id);
	protected abstract int getValue(T obj);
	
	@Override public int[] sqlTypes() { return new int[]{Types.INTEGER}; }
	@Override public Class<T> returnedClass() { return typeParameterClass; }
	@Override public boolean equals(Object x, Object y) throws HibernateException { return x == y; }

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x == null ? 0 : x.hashCode();
	}
	
	
	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
		int id = rs.getInt(names[0]);
	    if(rs.wasNull()) {
	        return null;
        }
	    
	    T obj = getById(id);
	    if(obj != null) {
	        return obj;
        }

		log.warn("Unknown {} with id {}", returnedClass().getSimpleName(), id);
	    return null;
	}
	
	
	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
		if (value == null) {
            st.setNull(index, Types.INTEGER);
        } else {
        	st.setInt(index, getValue(((T)value)));           
        }		
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}
	
  
}
