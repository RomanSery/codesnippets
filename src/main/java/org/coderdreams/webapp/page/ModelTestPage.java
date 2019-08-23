package org.coderdreams.webapp.page;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.util.ListModel;
import org.coderdreams.webapp.BasePage;
import org.coderdreams.webapp.components.SingleClickAjaxButton;

public class ModelTestPage extends BasePage implements IBasePage {

    private String testInput;
    private List<String> selectInput;

    public ModelTestPage() {
        super();

        Form<Void> testForm = new Form<Void>("testForm");
        addOrReplace(testForm);

        testForm.addOrReplace(new TextField<>("txtField", objModel(this::getTestInput, this::setTestInput)));

        testForm.addOrReplace(new ListMultipleChoice<String>("selectField", objListModel(this::getSelectInput, this::setSelectInput), new ListModel<>(List.of("Choice 1","Choice 2","Choice 3","Choice 4"))));

        SingleClickAjaxButton singleClickBtn = new SingleClickAjaxButton("singleClickBtn", testForm, true, null) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                System.out.println(testInput);
                System.out.println(StringUtils.join(selectInput, ","));
            }
        };
        testForm.addOrReplace(singleClickBtn);

    }

    private String getTestInput() {
        return testInput;
    }
    private void setTestInput(String testInput) {
        this.testInput = testInput;
    }
    private List<String> getSelectInput() {
        return selectInput;
    }
    private void setSelectInput(List<String> selectInput) {
        this.selectInput = selectInput;
    }
}
