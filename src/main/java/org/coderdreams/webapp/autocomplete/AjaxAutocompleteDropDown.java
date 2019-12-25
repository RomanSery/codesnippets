package org.coderdreams.webapp.autocomplete;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.coderdreams.dom.BaseEntity;


public abstract class AjaxAutocompleteDropDown<T extends BaseEntity> extends AutocompleteDropDown<T> {
    private static final long serialVersionUID = 1L;

    public AjaxAutocompleteDropDown(String id, Class<T> classType, IModel<T> model, SearchType searchType, IChoiceRenderer<T> renderer) {
        super(id, classType, model, searchType, renderer);
        add(new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                onDropDownChanged(target);
            }
        });
    }

    protected abstract void onDropDownChanged(AjaxRequestTarget target);
}
