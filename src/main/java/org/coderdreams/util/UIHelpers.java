package org.coderdreams.util;

import java.io.Serializable;

import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.util.string.ComponentRenderer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.coderdreams.dom.ComplexUser;
import org.coderdreams.dom.Institution;
import org.coderdreams.enums.GenericEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UIHelpers implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger( UIHelpers.class ) ;

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



	public static String renderPanelToString(Panel panel) {
		try {
			if(panel == null) return "";
			CharSequence cs = ComponentRenderer.renderComponent(panel);
			if(cs == null) return "";
			return cs.toString();
		} catch (Exception e) {
			log.error("renderPanelToString failed", e);
			return "";
		}
	}

	public static String renderPageToString(PageProvider pp) {
		try {
			CharSequence cs = ComponentRenderer.renderPage(pp);
			if(cs == null) return "";
			return cs.toString();
		} catch (Exception e) {
			log.error("renderPageToString failed", e);
			return "";
		}
	}
}
