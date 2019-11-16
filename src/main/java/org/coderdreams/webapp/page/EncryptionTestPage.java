package org.coderdreams.webapp.page;

import java.util.Arrays;

import org.apache.shiro.codec.Base64;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.coderdreams.shiro.CipherService;
import org.coderdreams.shiro.EncryptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptionTestPage extends WebPage {
	private static final long serialVersionUID = 1L;
	
	private final static Logger logger = LoggerFactory.getLogger(EncryptionTestPage.class);
	private @SpringBean CipherService cipherService;

	public EncryptionTestPage(final PageParameters parameters) {
		super(parameters);

		add(new FeedbackPanel("feedback"));
		add(new EarnLinkForm("earnLinkform"));	
    }

	private class EarnLinkForm extends StatelessForm<EarnLinkForm> {

		private static final long serialVersionUID = 1L;
							
		private String privateKey;
		private String plainText;
		private String encText;
		private EncryptionType encType = EncryptionType.GCM;
		
		EarnLinkForm(final String id) {
			super(id);
			this.setDefaultModel(new CompoundPropertyModel<EarnLinkForm>(this));
						
			add(new TextField<String>("privateKey"));
			add(new TextField<String>("plainText"));
			add(new TextField<String>("encText"));

			add(new DropDownChoice<EncryptionType>("encType", new PropertyModel<EncryptionType>(this, "encType"), Arrays.asList(EncryptionType.values())).setRequired(true));
		}
		
		@Override
		public final void onSubmit() {				
			
			if(privateKey == null || privateKey.length() == 0) {
				error("Enter the privateKey");
				return;
			}
			
			if(plainText == null) plainText = "";
			if(encText == null) encText = "";
			if(encType == null ) encType = EncryptionType.GCM;
			
			try {
				if(plainText.length() > 0) {
					String encryptedString = cipherService.encryptAndBase64Encode(plainText, Base64.decode(privateKey), encType);
					this.get("encText").setDefaultModelObject(encryptedString);
					this.get("plainText").setDefaultModelObject(null);
				} else if(encText.length() > 0) {
					String decryptedString = cipherService.base64DecodeAndDecrypt(encText, Base64.decode(privateKey), encType);
					this.get("encText").setDefaultModelObject(null);
					this.get("plainText").setDefaultModelObject(decryptedString);
				}
				
				
			} catch (Exception e) {
				logger.error("error dercrypting",e);
				error(e.getMessage());
			}
			
		}
	}
}
