package org.coderdreams.webapp;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.MarkupType;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.coderdreams.util.EntityUtil;
import org.coderdreams.webapp.autocomplete.AutocompleteFilters;
import org.coderdreams.webapp.autocomplete.SearchType;

import com.github.openjson.JSONException;
import com.github.openjson.JSONObject;

import static java.util.stream.Collectors.toList;


public abstract class JsonRequestPage extends WebPage {
	private static final long serialVersionUID = 1L;
	
	protected abstract String sendResponse(final PageParameters pp);

	public JsonRequestPage(final PageParameters pp) {
		super(pp);
		getRequestCycle().scheduleRequestHandlerAfterCurrent(new TextRequestHandler("application/json", "UTF-8", sendResponse(pp)));				
	}
	
	@Override
	public MarkupType getMarkupType() {
		return new MarkupType("html","application/json");
	}

	protected String getEmptyResponse() {
		JSONObject json = new JSONObject();
		try {
			json.put("count", 0);
			json.accumulate("results", Collections.emptyList());
		} catch (JSONException e) {
		}
		return json.toString();
	}


	//helper methods
	protected int getPageNum(IRequestParameters reqParams) {
		StringValue pagePP = reqParams.getParameterValue("page");
		return pagePP == null || pagePP.isEmpty() ? 0 : pagePP.toInt();
	}

	protected SearchType getSearchType(IRequestParameters reqParams) {
		StringValue suggestionTypePP = reqParams.getParameterValue("searchType");
		return (suggestionTypePP == null || suggestionTypePP.isEmpty()) ? null : SearchType.valueOf(suggestionTypePP.toString());
	}

	protected String getSearchTerm(IRequestParameters reqParams) {
		StringValue termPP = reqParams.getParameterValue("term");
		return termPP == null || termPP.isEmpty() ? null : termPP.toString();
	}

	protected <T> String getStringResults(List<T> fullList, IChoiceRenderer<T> cr, int page, int countPerRequest) {
		JSONObject json = new JSONObject();
		try {
			json.put("count", fullList.size());
			List<T> listWithPagination = fullList.stream().skip(countPerRequest * (page - 1)).limit(countPerRequest).collect(toList());
			for (T obj : listWithPagination) {
				JSONObject jObj = new JSONObject().put("id", cr.getIdValue(obj, 0)).put("text", cr.getDisplayValue(obj));
				json.append("results", jObj);
			}
		} catch (JSONException e) {
		}
		return json.toString();
	}

	protected AutocompleteFilters getFilters(IRequestParameters pp) {
		StringValue filtersPP = pp.getParameterValue("filters");
		if (filtersPP == null || filtersPP.isEmpty()) {
			return new AutocompleteFilters();
		}
		return EntityUtil.jsonToObject(filtersPP.toString(), AutocompleteFilters.class);
	}
}
