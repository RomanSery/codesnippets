package org.coderdreams.hibernate;


import org.coderdreams.util.EntityUtil;
import org.hibernate.type.descriptor.java.MutableMutabilityPlan;

public final class JsonColumnValueMutabilityPlan extends MutableMutabilityPlan<Object> {
    private static final long serialVersionUID = 1L;
    static final JsonColumnValueMutabilityPlan INSTANCE = new JsonColumnValueMutabilityPlan();

    private JsonColumnValueMutabilityPlan() {
    }

    @Override
    public Object deepCopyNotNull(Object value) {
        return EntityUtil.clone(value);
    }
}
