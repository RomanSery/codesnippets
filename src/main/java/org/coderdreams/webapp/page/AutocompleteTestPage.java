package org.coderdreams.webapp.page;

import org.apache.wicket.markup.html.form.Form;
import org.coderdreams.dom.Institution;
import org.coderdreams.enums.StatusType;
import org.coderdreams.util.UIHelpers;
import org.coderdreams.webapp.BasePage;
import org.coderdreams.webapp.autocomplete.AutocompleteDropDown;
import org.coderdreams.webapp.autocomplete.AutocompleteFilters;
import org.coderdreams.webapp.autocomplete.SearchType;

public class AutocompleteTestPage extends BasePage implements IBasePage {

    private Institution selectedInstitution;
    private Institution selectedPendingInstitution;

    public AutocompleteTestPage() {
        super();

        Form<Void> testForm = new Form<Void>("testForm");
        addOrReplace(testForm);

        testForm.addOrReplace(new AutocompleteDropDown<>("selectInstitution", Institution.class,
                objModel(this::getSelectedInstitution, this::setSelectedInstitution), SearchType.INSTITUTIONS, UIHelpers.getInstitutionChoiceRenderer()));

        testForm.addOrReplace(new AutocompleteDropDown<>("selectPendingInstitution", Institution.class,
                objModel(this::getSelectedPendingInstitution, this::setSelectedPendingInstitution), SearchType.INSTITUTIONS, UIHelpers.getInstitutionChoiceRenderer()) {
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
}
