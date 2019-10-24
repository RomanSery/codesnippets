package org.coderdreams.webapp.page;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.coderdreams.service.UserService;
import org.coderdreams.webapp.BasePage;

public class AjaxTestPage extends BasePage implements IBasePage {

    @SpringBean private UserService userService;
    private Label usersCountLbl;

    public AjaxTestPage() {
        super();

        usersCountLbl = new Label("usersCountLbl", new LoadableDetachableModel<Integer>() {
            @Override protected Integer load() { return userService.getUserCount(); }
        });
        add(usersCountLbl.setOutputMarkupId(true));

        add(new MyAbstractDefaultAjaxBehavior());
    }

    private class MyAbstractDefaultAjaxBehavior extends AbstractDefaultAjaxBehavior {
        private static final long serialVersionUID = 1L;
        protected void respond(final AjaxRequestTarget target) {
            StringValue data1 = getRequest().getRequestParameters().getParameterValue("data1");
            StringValue data2 = getRequest().getRequestParameters().getParameterValue("data2");
            target.add(usersCountLbl);
        }
        @Override
        public void renderHead(Component component, IHeaderResponse response){
            super.renderHead(component, response);

            String callbackUrl = getCallbackUrl().toString();
            response.render(JavaScriptHeaderItem.forScript("refresh_user_count_url = '" + callbackUrl + "';", "refresh_user_count_url"));
        }
    }
}
