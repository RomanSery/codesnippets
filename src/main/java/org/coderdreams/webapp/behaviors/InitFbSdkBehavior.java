package org.coderdreams.webapp.behaviors;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.coderdreams.service.FacebookService;


public class InitFbSdkBehavior extends Behavior {
	private static final long serialVersionUID = 1L;
	@Override
	public void renderHead(final Component component, final IHeaderResponse response) {
		Map<String, Object> variables = new HashMap<>();
        variables.put("fbAppId", FacebookService.getAppId());
        
        try (PackageTextTemplate template = new PackageTextTemplate(getClass(), "fb_init.js")) {
	        response.render(JavaScriptHeaderItem.forScript(template.asString(variables), "fb_init"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

