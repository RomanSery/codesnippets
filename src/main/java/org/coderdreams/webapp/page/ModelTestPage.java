package org.coderdreams.webapp.page;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.util.ListModel;
import org.coderdreams.dom.User;
import org.coderdreams.webapp.BasePage;
import org.coderdreams.webapp.components.SingleClickAjaxButton;

public class ModelTestPage extends BasePage implements IBasePage {

    private User user;

    public ModelTestPage() {
        super();
        user = new User();

        Form<Void> testForm = new Form<Void>("testForm");
        addOrReplace(testForm);

        testForm.addOrReplace(new TextField<>("displayName", objModel(user::getDisplayName, user::setDisplayName)));
        testForm.addOrReplace(new ListMultipleChoice<String>("favGenres", objListModel(user::getFavGenres, user::setFavGenres), new ListModel<>(List.of("Action","Comedy","Romance","Sci-fi"))));

        SingleClickAjaxButton singleClickBtn = new SingleClickAjaxButton("singleClickBtn", testForm, true, null) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                System.out.println(user.getDisplayName());
                System.out.println(StringUtils.join(user.getFavGenres(), ","));
            }
        };
        testForm.addOrReplace(singleClickBtn);

    }

}
