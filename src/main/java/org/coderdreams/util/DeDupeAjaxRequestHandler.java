package org.coderdreams.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.core.request.handler.logger.PageLogData;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.page.PartialPageUpdate;
import org.apache.wicket.page.XmlPartialPageUpdate;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.response.StringResponse;
import org.apache.wicket.response.filter.IResponseFilter;
import org.apache.wicket.util.encoding.UrlDecoder;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//a copy of AjaxRequestHandler.
// only change is to override update.writeNormalEvaluations() and update.writePriorityEvaluations()
public class DeDupeAjaxRequestHandler implements AjaxRequestTarget
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DeDupeAjaxRequestHandler.class);

    /**
     * Collector of page updates.
     */
    private final PartialPageUpdate update;

    /** a list of listeners */
    private List<IListener> listeners = null;

    /** */
    private final Set<ITargetRespondListener> respondListeners = new HashSet<>();

    /** see https://issues.apache.org/jira/browse/WICKET-3564 */
    protected transient boolean respondersFrozen;
    protected transient boolean listenersFrozen;

    /** The associated Page */
    private final Page page;

    private PageLogData logData;

    /**
     * Constructor
     *
     * @param page the currently active page
     */
    public DeDupeAjaxRequestHandler(final Page page) {
        this.page = Args.notNull(page, "page");

        update = new XmlPartialPageUpdate(page) {
            /**
             * Freezes the {@link DeDupeAjaxRequestHandler#listeners} before firing the event and
             * un-freezes them afterwards to allow components to add more
             * {@link IListener}s for the second event.
             */
            @Override
            protected void onBeforeRespond(final Response response) {
                listenersFrozen = true;

                if (listeners != null)
                {
                    for (IListener listener : listeners)
                    {
                        listener.onBeforeRespond(markupIdToComponent, DeDupeAjaxRequestHandler.this);
                    }
                }

                listenersFrozen = false;
            }

            /**
             * Freezes the {@link DeDupeAjaxRequestHandler#listeners}, and does not un-freeze them as the
             * events will have been fired by now.
             *
             * @param response
             *      the response to write to
             */
            @Override
            protected void onAfterRespond(final Response response) {
                listenersFrozen = true;

                // invoke onafterresponse event on listeners
                if (listeners != null)
                {
                    final Map<String, Component> components = Collections
                            .unmodifiableMap(markupIdToComponent);

                    // create response that will be used by listeners to append
                    // javascript
                    final IJavaScriptResponse jsresponse = new IJavaScriptResponse()
                    {
                        @Override
                        public void addJavaScript(String script)
                        {
                            writeNormalEvaluations(response,
                                    Collections.<CharSequence> singleton(script));
                        }
                    };

                    for (IListener listener : listeners)
                    {
                        listener.onAfterRespond(components, jsresponse);
                    }
                }
            }


            @Override
            protected void writeNormalEvaluations(final Response response, final Collection<CharSequence> scripts) {
                writeEvaluationsDeDupe(response, "evaluate", scripts);
            }
            @Override
            protected void writePriorityEvaluations(Response response, Collection<CharSequence> scripts) {
                writeEvaluationsDeDupe(response, "priority-evaluate", scripts);
            }

            private void writeEvaluationsDeDupe(final Response response, String elementName, Collection<CharSequence> scripts) {
                if (scripts.size() > 0) {
                    Collection<CharSequence> distinct = scripts.stream().distinct().collect(Collectors.toList());
                    if(distinct.size() > 0) {
                        StringBuilder combinedScript = new StringBuilder(1024);
                        for (CharSequence script : distinct) {
                            combinedScript.append("(function(){").append(script).append("})();");
                        }
                        writeEvaluation(elementName, response, combinedScript);
                    }
                }
            }

            /**
             * @param invocation
             *            type of invocation tag, usually {@literal evaluate} or
             *            {@literal priority-evaluate}
             * @param response
             * @param js
             */
            private void writeEvaluation(final String invocation, final Response response, final CharSequence js)
            {
                response.write("<");
                response.write(invocation);
                response.write(">");

                response.write("<![CDATA[");
                response.write(encode(js));
                response.write("]]>");

                response.write("</");
                response.write(invocation);
                response.write(">");

                bodyBuffer.reset();
            }
        };
    }

    /**
     * @see org.apache.wicket.core.request.handler.IPageRequestHandler#getPage()
     */
    @Override
    public Page getPage() {
        return page;
    }

    @Override
    public void addListener(IListener listener) throws IllegalStateException {
        Args.notNull(listener, "listener");
        assertListenersNotFrozen();

        if (listeners == null)
        {
            listeners = new LinkedList<>();
        }

        if (!listeners.contains(listener))
        {
            listeners.add(listener);
        }
    }

    @Override
    public final void addChildren(MarkupContainer parent, Class<?> childCriteria) {
        Args.notNull(parent, "parent");
        Args.notNull(childCriteria, "childCriteria");

        parent.visitChildren(childCriteria, new IVisitor<Component, Void>()
        {
            @Override
            public void component(final Component component, final IVisit<Void> visit)
            {
                add(component);
                visit.dontGoDeeper();
            }
        });
    }

    @Override
    public void add(Component... components) {
        for (final Component component : components) {
            if(component == null) {
                LOGGER.error("Adding null component to target!", new Exception("component null"));
                continue;
            }

            if (component.getOutputMarkupId() == false && !(component instanceof Page)) {
                throw new IllegalArgumentException(
                        "cannot update component that does not have setOutputMarkupId property set to true. Component: " +
                                component.toString());
            }
            add(component, component.getMarkupId());
        }
    }

    @Override
    public void add(Component component, String markupId) {
        update.add(component, markupId);
    }

    @Override
    public final Collection<? extends Component> getComponents() {
        return update.getComponents();
    }

    @Override
    public final void focusComponent(Component component) {
        if (component != null && component.getOutputMarkupId() == false) {
            throw new IllegalArgumentException(
                    "cannot update component that does not have setOutputMarkupId property set to true. Component: " +
                            component.toString());
        }
        final String id = component != null ? ("'" + component.getMarkupId() + "'") : "null";
        appendJavaScript("Wicket.Focus.setFocusOnId(" + id + ");");
    }

    @Override
    public final void appendJavaScript(CharSequence javascript) {

        update.appendJavaScript(javascript);
    }

    /**
     * @see org.apache.wicket.core.request.handler.IPageRequestHandler#detach(IRequestCycle)
     */
    @Override
    public void detach(final IRequestCycle requestCycle) {
        if (logData == null) {
            logData = new PageLogData(page);
        }

        update.detach(requestCycle);
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof DeDupeAjaxRequestHandler) {
            DeDupeAjaxRequestHandler that = (DeDupeAjaxRequestHandler) obj;
            return update.equals(that.update);
        }
        return false;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = "AjaxRequestHandler".hashCode();
        result += update.hashCode() * 17;
        return result;
    }

    @Override
    public final void prependJavaScript(CharSequence javascript) {
        update.prependJavaScript(javascript);
    }

    @Override
    public void registerRespondListener(ITargetRespondListener listener) {
        assertRespondersNotFrozen();
        respondListeners.add(listener);
    }

    /**
     * @see org.apache.wicket.core.request.handler.IPageRequestHandler#respond(IRequestCycle)
     */
    @Override
    public final void respond(final IRequestCycle requestCycle) {
        final RequestCycle rc = (RequestCycle)requestCycle;
        final WebResponse response = (WebResponse)requestCycle.getResponse();

        if (shouldRedirectToPage(requestCycle))
        {
            // the page itself has been added to the request target, we simply issue a redirect
            // back to the page
            IRequestHandler handler = new RenderPageRequestHandler(new PageProvider(page));
            final String url = rc.urlFor(handler).toString();
            response.sendRedirect(url);
            return;
        }

        respondersFrozen = true;

        for (ITargetRespondListener listener : respondListeners)
        {
            listener.onTargetRespond(this);
        }

        final Application app = page.getApplication();

        page.send(app, Broadcast.BREADTH, this);

        // Determine encoding
        final String encoding = app.getRequestCycleSettings().getResponseRequestEncoding();

        // Set content type based on markup type for page
        update.setContentType(response, encoding);

        // Make sure it is not cached by a client
        response.disableCaching();

        final StringResponse bodyResponse = new StringResponse();
        update.writeTo(bodyResponse, encoding);
        CharSequence filteredResponse = invokeResponseFilters(bodyResponse);
        response.write(filteredResponse);
    }

    private boolean shouldRedirectToPage(IRequestCycle requestCycle)
    {
        if (update.containsPage())
        {
            return true;
        }

        if (((WebRequest)requestCycle.getRequest()).isAjax() == false)
        {
            // the request was not sent by wicket-ajax.js - this can happen when an Ajax request was
            // intercepted with #redirectToInterceptPage() and then the original request is re-sent
            // by the browser on a subsequent #continueToOriginalDestination()
            return true;
        }

        return false;
    }

    /**
     * Runs the configured {@link IResponseFilter}s over the constructed Ajax response
     *
     * @param contentResponse
     *            the Ajax {@link Response} body
     * @return filtered response
     */
    private AppendingStringBuffer invokeResponseFilters(final StringResponse contentResponse)
    {
        AppendingStringBuffer responseBuffer = new AppendingStringBuffer(
                contentResponse.getBuffer());

        List<IResponseFilter> responseFilters = Application.get()
                .getRequestCycleSettings()
                .getResponseFilters();

        if (responseFilters != null)
        {
            for (IResponseFilter filter : responseFilters)
            {
                responseBuffer = filter.filter(responseBuffer);
            }
        }
        return responseBuffer;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "[AjaxRequestHandler@" + hashCode() + " responseObject [" + update + "]";
    }

    @Override
    public IHeaderResponse getHeaderResponse() {
        return update.getHeaderResponse();
    }

    /**
     * @return the markup id of the focused element in the browser
     */
    @Override
    public String getLastFocusedElementId()
    {
        WebRequest request = (WebRequest)page.getRequest();

        String id = request.getHeader("Wicket-FocusedElementId");

        // WICKET-6568 might contain non-ASCII
        return Strings.isEmpty(id) ? null : UrlDecoder.QUERY_INSTANCE.decode(id, request.getCharset());
    }

    /**
     * @see org.apache.wicket.core.request.handler.IPageRequestHandler#getPageClass()
     */
    @Override
    public Class<? extends IRequestablePage> getPageClass() {
        return page.getPageClass();
    }

    /**
     * @see org.apache.wicket.core.request.handler.IPageRequestHandler#getPageId()
     */
    @Override
    public Integer getPageId() {
        return page.getPageId();
    }

    /**
     * @see org.apache.wicket.core.request.handler.IPageRequestHandler#getPageParameters()
     */
    @Override
    public PageParameters getPageParameters() {
        return page.getPageParameters();
    }

    @Override
    public final boolean isPageInstanceCreated() {
        return true;
    }

    @Override
    public final Integer getRenderCount() {
        return page.getRenderCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageLogData getLogData() {
        return logData;
    }

    private void assertNotFrozen(boolean frozen, Class<?> clazz) {
        if (frozen) {
            throw new IllegalStateException(Classes.simpleName(clazz) + "s can no longer be added");
        }
    }

    private void assertRespondersNotFrozen() {
        assertNotFrozen(respondersFrozen, ITargetRespondListener.class);
    }

    private void assertListenersNotFrozen() {
        assertNotFrozen(listenersFrozen, IListener.class);
    }
}
