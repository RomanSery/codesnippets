package org.coderdreams.webapp.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.coderdreams.webapp.BasePage;
import org.coderdreams.webapp.components.SingleClickAjaxButton;

public class TestDuplicateJSPage extends BasePage implements IBasePage {

    public TestDuplicateJSPage() {
        super();

        Form<Void> testForm = new Form<Void>("testForm");
        addOrReplace(testForm);

        SingleClickAjaxButton singleClickBtn = new SingleClickAjaxButton("singleClickBtn", testForm, true, null) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                for(int i = 0; i < 10; i++) {
                    target.appendJavaScript("console.log('test');");
                }
            }
        };
        testForm.addOrReplace(singleClickBtn);

    }
}
