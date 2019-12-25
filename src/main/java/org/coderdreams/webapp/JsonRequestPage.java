package org.coderdreams.webapp;

import org.apache.wicket.markup.MarkupType;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;


public abstract class JsonRequestPage extends WebPage {
	private static final long serialVersionUID = 1L;
	
	protected abstract String sendResponse(final PageParameters pp);

	public JsonRequestPage(final PageParameters pp) {
		super(pp);
		getRequestCycle().scheduleRequestHandlerAfterCurrent(new TextRequestHandler("application/json", "UTF-8", sendResponse(pp)));				
	}
	
	@Override
	public MarkupType getMarkupType() {
		return new MarkupType("html","application/json");
	}
}
