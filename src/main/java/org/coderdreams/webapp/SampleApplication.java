package org.coderdreams.webapp;

import org.apache.wicket.Page;
import org.apache.wicket.core.request.handler.ComponentNotFoundException;
import org.apache.wicket.core.request.handler.EmptyAjaxRequestHandler;
import org.apache.wicket.core.request.handler.ListenerInvocationNotAllowedException;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.IRequestLogger;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.settings.RequestCycleSettings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.coderdreams.dao.UserLockRepository;
import org.coderdreams.service.EmailService;
import org.coderdreams.util.ActiveAjaxListener;
import org.coderdreams.util.CustomRequestLogger;
import org.coderdreams.util.TxtContentResourceLoader;
import org.coderdreams.webapp.autocomplete.DropdownSuggestionsPage;
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
    @Autowired private UserLockRepository userLockRepository;
    private @Autowired PanelFactory panelFactory;

    @Override
    public void init() {
        super.init();
        this.getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

        getAjaxRequestTargetListeners().add(new ActiveAjaxListener());

        getResourceSettings().getStringResourceLoaders().add(new TxtContentResourceLoader());

        getDebugSettings().setDevelopmentUtilitiesEnabled(true);
        getRequestLoggerSettings().setRequestLoggerEnabled(true);
        getRequestLoggerSettings().setRequestsWindowSize(5); //set # of requests to store
        getRequestCycleSettings().setRenderStrategy(RequestCycleSettings.RenderStrategy.ONE_PASS_RENDER);

        userLockRepository.deleteAll();

        this.mountPage("/searchsuggestions", DropdownSuggestionsPage.class);

        getRequestCycleListeners().add(new IRequestCycleListener() {
            @Override
            public IRequestHandler onException(RequestCycle cycle, Exception ex) {
                emailService.sendErrorEmail(cycle, ex, SampleApplication.this.getRequestLogger());
                if (ex instanceof ListenerInvocationNotAllowedException || ex instanceof ComponentNotFoundException) {
                    //if this is an ajax request, just return an empty response to avoid sending user to error page
                    return EmptyAjaxRequestHandler.getInstance();
                }
                return null;
            }
        });

        this.mountResource("/releaseinfo", new ResourceReference("/releaseinfo") {
            private static final long serialVersionUID = 1L;
            VersionDetailsEndpoints versionDetailsEndpoints = new VersionDetailsEndpoints();
            @Override
            public IResource getResource() {
                Injector.get().inject(versionDetailsEndpoints);
                return versionDetailsEndpoints;
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
