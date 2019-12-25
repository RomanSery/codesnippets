package org.coderdreams.util;

import java.io.Serializable;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.coderdreams.dom.ComplexUser;
import org.coderdreams.dom.Institution;
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

	private static class InstitutionChoiceRenderer extends ChoiceRenderer<Institution> {
		private static final long serialVersionUID = 1L;
		@Override
		public Object getDisplayValue(Institution object) {
			return object == null ? "" : object.getName();
		}
		@Override
		public String getIdValue(Institution object, int index) {
			return String.valueOf(object.getId());
		}
	}

	private static class ComplexUserChoiceRenderer extends ChoiceRenderer<ComplexUser> {
		private static final long serialVersionUID = 1L;
		@Override
		public Object getDisplayValue(ComplexUser object) {
			return object == null ? "" : object.getDisplayName() + " (" + object.getEmail() + ")";
		}
		@Override
		public String getIdValue(ComplexUser object, int index) {
			return String.valueOf(object.getId());
		}
	}

	public static <T extends GenericEnum> IChoiceRenderer<T> getEnumChoiceRenderer() {
		return new GenericEnumChoiceRenderer<T>();
	}

	public static IChoiceRenderer<Institution> getInstitutionChoiceRenderer() {
		return new InstitutionChoiceRenderer();
	}
	public static IChoiceRenderer<ComplexUser> getComplexUserChoiceRenderer() {
		return new ComplexUserChoiceRenderer();
	}
}
