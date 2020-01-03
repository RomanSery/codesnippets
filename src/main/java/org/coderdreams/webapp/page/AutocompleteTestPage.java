package org.coderdreams.webapp.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.LoadableDetachableModel;
import org.coderdreams.dom.ComplexUser;
import org.coderdreams.dom.Institution;
import org.coderdreams.enums.StatusType;
import org.coderdreams.util.UIHelpers;
import org.coderdreams.webapp.BasePage;
import org.coderdreams.webapp.autocomplete.AjaxAutocompleteDropDown;
import org.coderdreams.webapp.autocomplete.AutocompleteDropDown;
import org.coderdreams.webapp.autocomplete.AutocompleteFilters;
import org.coderdreams.webapp.autocomplete.SearchType;

public class AutocompleteTestPage extends BasePage implements IBasePage {

    private Institution selectedInstitution;
    private Institution selectedPendingInstitution;

    private ComplexUser selectedUser;
    private ComplexUser selectedPendingUser;

    private Label selectedInstitutionLbl;

    public AutocompleteTestPage() {
        super();

        Form<Void> testForm = new Form<Void>("testForm");
        addOrReplace(testForm);

        selectedInstitutionLbl = new Label("selectedInstitutionLbl", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return selectedInstitution == null ? "" : selectedInstitution.getName();
            }
        });
        selectedInstitutionLbl.setOutputMarkupId(true);
        testForm.addOrReplace(selectedInstitutionLbl);

        testForm.addOrReplace(new AutocompleteDropDown<>("selectInstitution", Institution.class,
                objModel(this::getSelectedInstitution, this::setSelectedInstitution), SearchType.INSTITUTIONS, UIHelpers.getInstitutionChoiceRenderer()));

        testForm.addOrReplace(new AutocompleteDropDown<>("selectPendingInstitution", Institution.class,
                objModel(this::getSelectedPendingInstitution, this::setSelectedPendingInstitution), SearchType.INSTITUTIONS, UIHelpers.getInstitutionChoiceRenderer()) {
            @Override
            protected AutocompleteFilters getFilters() {
                return new AutocompleteFilters().setStatusType(StatusType.PENDING);
            }
        });


        testForm.addOrReplace(new AjaxAutocompleteDropDown<>("selectInstitutionAjax", Institution.class,
                objModel(this::getSelectedInstitution, this::setSelectedInstitution), SearchType.INSTITUTIONS, UIHelpers.getInstitutionChoiceRenderer()) {
            @Override
            protected void onDropDownChanged(AjaxRequestTarget target) {
                target.add(selectedInstitutionLbl);
            }
        });

        testForm.addOrReplace(new AutocompleteDropDown<>("selectUser", ComplexUser.class,
                objModel(this::getSelectedUser, this::setSelectedUser), SearchType.USERS, UIHelpers.getComplexUserChoiceRenderer()));

        testForm.addOrReplace(new AutocompleteDropDown<>("selectPendingUser", ComplexUser.class,
                objModel(this::getSelectedPendingUser, this::setSelectedPendingUser), SearchType.USERS, UIHelpers.getComplexUserChoiceRenderer()) {
            @Override
            protected AutocompleteFilters getFilters() {
                return new AutocompleteFilters().setStatusType(StatusType.PENDING);
            }
        });
    }


    public Institution getSelectedInstitution() { return selectedInstitution; }
    public void setSelectedInstitution(Institution selectedInstitution) { this.selectedInstitution = selectedInstitution; }

    public Institution getSelectedPendingInstitution() { return selectedPendingInstitution; }
    public void setSelectedPendingInstitution(Institution selectedPendingInstitution) { this.selectedPendingInstitution = selectedPendingInstitution; }

    public ComplexUser getSelectedUser() { return selectedUser; }
    public void setSelectedUser(ComplexUser selectedUser) { this.selectedUser = selectedUser; }

    public ComplexUser getSelectedPendingUser() { return selectedPendingUser; }
    public void setSelectedPendingUser(ComplexUser selectedPendingUser) { this.selectedPendingUser = selectedPendingUser; }
}
