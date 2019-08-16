package org.coderdreams.webapp.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.coderdreams.webapp.BasePage;
import org.coderdreams.webapp.components.SingleClickAjaxButton;

public class SingleClickPage extends BasePage {

    private String testInput;

    public SingleClickPage() {
        super();

        Form<Void> testForm = new Form<Void>("testForm");
        addOrReplace(testForm);

        TextField<String> txtField = new TextField<>("txtField", new PropertyModel<String>(this, "testInput"));
        testForm.addOrReplace(txtField);

        SingleClickAjaxButton singleClickBtn = new SingleClickAjaxButton("singleClickBtn", testForm, true, null) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                try {
                    Thread.sleep(800); //simulate some process
                } catch (InterruptedException e) {

                }
                System.out.println(testInput);
            }
        };
        testForm.addOrReplace(singleClickBtn);


        AjaxButton badBtn = new AjaxButton("badBtn", testForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                try {
                    Thread.sleep(800); //simulate some process
                } catch (InterruptedException e) {

                }
                System.out.println(System.nanoTime());
            }
        };
        testForm.addOrReplace(badBtn);
    }
}
