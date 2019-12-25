package org.coderdreams.webapp.autocomplete;

import java.util.Collections;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.coderdreams.dom.ComplexUser;
import org.coderdreams.dom.Institution;
import org.coderdreams.service.SearchService;
import org.coderdreams.util.EntityUtil;
import org.coderdreams.util.UIHelpers;
import org.coderdreams.webapp.JsonRequestPage;

import com.github.openjson.JSONException;
import com.github.openjson.JSONObject;

import static java.util.stream.Collectors.toList;

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
        IRequestParameters requestParameters = getRequest().getRequestParameters();
        StringValue termPP = requestParameters.getParameterValue("term");
        StringValue suggestionTypePP = requestParameters.getParameterValue("searchType");
        StringValue pagePP = requestParameters.getParameterValue("page");
        if (suggestionTypePP == null || suggestionTypePP.isEmpty()) {
            return getEmptyResponse();
        }

        String term = termPP == null || termPP.isEmpty() ? null : termPP.toString();

        SearchType st = SearchType.valueOf(suggestionTypePP.toString());
        int page = pagePP == null || pagePP.isEmpty() ? 0 : pagePP.toInt();
        AutocompleteFilters filters = getFilters(requestParameters);

        if (!isTermOk(term)) {
            return getEmptyResponse();
        }

        if (st == SearchType.INSTITUTIONS) {
            return getInstitutions(term, page, filters);
        } else if (st == SearchType.USERS) {
            return getUsers(term, page, filters);
        }

        return getEmptyResponse();
    }

    private boolean isTermOk(String term) {
        return term != null && term.length() > 0;
    }

    private String getInstitutions(String term, int page, AutocompleteFilters filters) {
        List<Institution> fullList = searchService.searchInstitutions(term, filters);
        if (fullList.isEmpty()) {
            return getEmptyResponse();
        }
        return getStringResults(fullList, UIHelpers.getInstitutionChoiceRenderer(), page);
    }

    private String getUsers(String term, int page, AutocompleteFilters filters) {
        List<ComplexUser> fullList = searchService.searchUsers(term, filters);
        if (fullList.isEmpty()) {
            return getEmptyResponse();
        }
        return getStringResults(fullList, UIHelpers.getComplexUserChoiceRenderer(), page);
    }

    private <T> String getStringResults(List<T> fullList, IChoiceRenderer<T> cr, int page) {
        JSONObject json = new JSONObject();
        try {
            json.put("count", fullList.size());
            List<T> listWithPagination = fullList.stream().skip(COUNT_PER_REQUEST * (page - 1)).limit(COUNT_PER_REQUEST).collect(toList());
            for (T obj : listWithPagination) {
                JSONObject jObj = new JSONObject().put("id", cr.getIdValue(obj, 0)).put("text", cr.getDisplayValue(obj));
                json.append("results", jObj);
            }
        } catch (JSONException e) {
        }
        return json.toString();
    }

    private AutocompleteFilters getFilters(IRequestParameters pp) {
        StringValue filtersPP = pp.getParameterValue("filters");
        if (filtersPP == null || filtersPP.isEmpty()) {
            return new AutocompleteFilters();
        }
        return EntityUtil.jsonToObject(filtersPP.toString(), AutocompleteFilters.class);
    }

    private String getEmptyResponse() {
        JSONObject json = new JSONObject();
        try {
            json.put("count", 0);
            json.accumulate("results", Collections.emptyList());
        } catch (JSONException e) {
        }
        return json.toString();
    }

}
