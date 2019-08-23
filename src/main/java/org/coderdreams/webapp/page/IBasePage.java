package org.coderdreams.webapp.page;


import java.io.Serializable;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.danekja.java.util.function.serializable.SerializableSupplier;

public interface IBasePage {

    default <T extends Serializable> IModel<T> objModel(SerializableSupplier<T> getter, SerializableConsumer<T> setter) {
        return LambdaModel.of(getter, setter);
    }
    default <T extends Serializable> IModel<List<T>> objListModel(SerializableSupplier<List<T>> getter, SerializableConsumer<List<T>> setter) {
        return LambdaModel.of(getter, setter);
    }

}
