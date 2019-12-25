package org.coderdreams.webapp.autocomplete;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.coderdreams.dom.BaseEntity;
import org.coderdreams.service.SearchService;
import org.coderdreams.util.EntityUtil;


public abstract class AutocompleteDropDown<T extends BaseEntity> extends DropDownChoice<T> {
    private static final long serialVersionUID = 1L;

    @SpringBean private SearchService searchService;

    private final SearchType searchType;
    private final Class<T> classType;

    public AutocompleteDropDown(String id, Class<T> classType, IModel<T> model, SearchType searchType, IChoiceRenderer<T> renderer) {
        super(id, model, Collections.emptyList(), renderer);
        this.searchType = searchType;
        this.classType = classType;
    }

    @Override
    protected void onComponentTag(final ComponentTag tag) {
        super.onComponentTag(tag);
        if(searchType != null) {
            tag.put("search-type", searchType.toString());
        }
        tag.put("data-minimum-input-length", String.valueOf(getMinCharacters()));

        String classAttr = StringUtils.defaultString(tag.getAttribute("class"));
        tag.put("class", classAttr + " coderdreams-autocomplete");

        tag.put("search-filters", EntityUtil.objectToJson(getFilters()));
    }

    protected int getMinCharacters() { return 3; }
    protected AutocompleteFilters getFilters() { return null; }

    @Override
    public IModel<? extends List<? extends T>> getChoicesModel() {
        T obj = getModel().getObject();
        if(obj == null) {
            return new ListModel<>(Collections.emptyList());
        }
        return new ListModel<>(Collections.singletonList(obj));
    }

    @Override
    protected T convertChoiceIdToChoice(String id) {
        if(StringUtils.isBlank(id)) {
            return null;
        }

        Integer pkId = null;
        try {
            pkId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return null;
        }

        Optional<T> found = searchService.getById(classType, pkId);
        return found.isPresent() ? found.get() : null;
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setNullValid(true);
        setOutputMarkupId(true);
    }
}
