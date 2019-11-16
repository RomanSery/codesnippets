package org.coderdreams.util;

import java.io.Serializable;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.coderdreams.enums.GenericEnum;
import org.coderdreams.shiro.CipherService;


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


	public static <T extends WebPage> String createEncryptedUrl(CipherService cipherService, Class<T> pageClass, PageParameters params) {
	    String secretStr = Utils.mapToString(params);
	    String encryptedStr = cipherService.encryptAndBase64Encode(secretStr);

        Url url = RequestCycle.get().mapUrlFor(pageClass, new PageParameters().add("enc", encryptedStr));
        return RequestCycle.get().getUrlRenderer().renderFullUrl(url);
	}
}
