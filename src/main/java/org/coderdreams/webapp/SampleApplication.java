package org.coderdreams.webapp;

import org.apache.wicket.Page;
import org.apache.wicket.core.request.handler.ComponentNotFoundException;
import org.apache.wicket.core.request.handler.EmptyAjaxRequestHandler;
import org.apache.wicket.core.request.handler.ListenerInvocationNotAllowedException;
import org.apache.wicket.protocol.http.IRequestLogger;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.coderdreams.service.EmailService;
import org.coderdreams.util.CustomRequestLogger;
import org.coderdreams.webapp.page.HomePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Service
public class SampleApplication extends WebApplication {
    private static final Logger log = LoggerFactory.getLogger(SampleApplication.class);

    @Autowired private EmailService emailService;
	private @Autowired PanelFactory panelFactory;

	@Override
    public void init() {
    	super.init();
    	this.getComponentInstantiationListeners().add(new SpringComponentInjector(this));
    	SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);


        getRequestLoggerSettings().setRequestLoggerEnabled(true);
        getRequestLoggerSettings().setRequestsWindowSize(5); //set # of requests to store

        getRequestCycleListeners().add(new IRequestCycleListener() {
			@Override
			public IRequestHandler onException(RequestCycle cycle, Exception ex) {
				emailService.sendErrorEmail(cycle, ex, SampleApplication.this.getRequestLogger());
				if(ex instanceof ListenerInvocationNotAllowedException || ex instanceof ComponentNotFoundException) {
					//if this is an ajax request, just return an empty response to avoid sending user to error page
					return EmptyAjaxRequestHandler.getInstance();
				}
				return null;
			}
        });

		panelFactory.initFactory();
    }

	@Override
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}

	@Override
	protected IRequestLogger newRequestLogger() {
		return new CustomRequestLogger();
	}

}