package org.coderdreams.webapp.autocomplete;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.coderdreams.dom.ComplexUser;
import org.coderdreams.dom.Institution;
import org.coderdreams.service.SearchService;
import org.coderdreams.util.UIHelpers;
import org.coderdreams.webapp.JsonRequestPage;

@RequiresAuthentication
public class DropdownSuggestionsPage extends JsonRequestPage {
    private static final long serialVersionUID = 1L;

    private static final int COUNT_PER_REQUEST = 15;
    @SpringBean private SearchService searchService;

    public DropdownSuggestionsPage(final PageParameters pp) {
        super(pp);
    }

    @Override
    protected String sendResponse(PageParameters pp) {
        IRequestParameters reqParams = getRequest().getRequestParameters();
        SearchType st = getSearchType(reqParams);
        String term = getSearchTerm(reqParams);
        int page = getPageNum(reqParams);
        AutocompleteFilters filters = getFilters(reqParams);

        if (st == null || !isTermOk(term)) {
            return getEmptyResponse();
        }

        switch (st) {
            case INSTITUTIONS:
                return getInstitutions(term, page, filters);
            case USERS:
                return getUsers(term, page, filters);
            default:
                return getEmptyResponse();
        }
    }

    private boolean isTermOk(String term) {
        return term != null && term.length() > 0;
    }

    private String getInstitutions(String term, int page, AutocompleteFilters filters) {
        List<Institution> fullList = searchService.searchInstitutions(term, filters);
        if (fullList.isEmpty()) {
            return getEmptyResponse();
        }
        return getStringResults(fullList, UIHelpers.getInstitutionChoiceRenderer(), page, COUNT_PER_REQUEST);
    }

    private String getUsers(String term, int page, AutocompleteFilters filters) {
        List<ComplexUser> fullList = searchService.searchUsers(term, filters);
        if (fullList.isEmpty()) {
            return getEmptyResponse();
        }
        return getStringResults(fullList, UIHelpers.getComplexUserChoiceRenderer(), page, COUNT_PER_REQUEST);
    }

}
