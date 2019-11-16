package org.coderdreams.util;

import java.io.Serializable;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.coderdreams.enums.GenericEnum;


public class UIHelpers implements Serializable {
	private static final long serialVersionUID = 1L;


	private static final class GenericEnumChoiceRenderer<T extends GenericEnum> extends ChoiceRenderer<T> {
		private static final long serialVersionUID = 1L;
        @Override
		public Object getDisplayValue(T object) {
        	return object == null ? "" : object.getDescription();
		}
        @Override
		public String getIdValue(T object, int index) {
			return String.valueOf(object.getId());
		}
	}


	public static <T extends GenericEnum> IChoiceRenderer<T> getEnumChoiceRenderer() {
		return new GenericEnumChoiceRenderer<T>();
	}
}
