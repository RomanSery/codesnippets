package org.coderdreams.webapp.page;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.coderdreams.dom.User;
import org.coderdreams.enums.CountryCode;
import org.coderdreams.enums.State;
import org.coderdreams.service.UserService;
import org.coderdreams.webapp.BasePage;
import org.coderdreams.webapp.components.SingleClickAjaxButton;

public class ModelTestPage extends BasePage implements IBasePage {

    @SpringBean private UserService userService;

    private User user;
    private DropDownChoice<State> stateField;
    private DropDownChoice<CountryCode> countryField;
    private Label stateCountryLbl;
    private User selectedUser;

    public ModelTestPage() {
        super();
        user = new User();

        add(new DebugBar("debug"));

        Form<Void> testForm = new Form<Void>("testForm");
        addOrReplace(testForm);

        testForm.addOrReplace(new TextField<>("displayName", objModel(user::getDisplayName, user::setDisplayName)));
        testForm.addOrReplace(new ListMultipleChoice<String>("favGenres", objListModel(user::getFavGenres, user::setFavGenres), new ListModel<>(List.of("Action","Comedy","Romance","Sci-fi"))));

        countryField = new DropDownChoice<CountryCode>("country", LambdaModel.of(user::getCountryCode, user::setCountryCode), new ListModel<CountryCode>(CountryCode.VALUES));
        countryField.add(new OnChangeAjaxBehavior() {
            @Override protected void onUpdate(AjaxRequestTarget target) { target.add(stateField, stateCountryLbl); }
        });
        testForm.addOrReplace(countryField);

        stateField = new DropDownChoice<State>("state", LambdaModel.of(user::getState, user::setState), new LoadableDetachableModel<List<State>>() {
            @Override
            protected List<State> load() {
                return user.getCountryCode() == null ? Collections.emptyList() : State.getByCountry(user.getCountryCode());
            }
        });
        stateField.add(new OnChangeAjaxBehavior() {
            @Override protected void onUpdate(AjaxRequestTarget target) { target.add(stateCountryLbl); }
        });
        testForm.addOrReplace(stateField.setOutputMarkupId(true));

        stateCountryLbl = new Label("stateCountry", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return String.format("%s, %s", user.getState() != null ? user.getState().getAbbreviation() : "",
                        user.getCountryCode() != null ? user.getCountryCode().getName() : "");
            }
        });
        testForm.addOrReplace(stateCountryLbl.setOutputMarkupId(true));

        DropDownChoice<User> userSelectionField = new DropDownChoice<User>("userSelection", new PropertyModel<User>(this, "selectedUser"), new LoadableDetachableModel<List<User>>() {
            @Override protected List<User> load() { return userService.getAllUsers(); }
        });
        testForm.addOrReplace(userSelectionField);

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
