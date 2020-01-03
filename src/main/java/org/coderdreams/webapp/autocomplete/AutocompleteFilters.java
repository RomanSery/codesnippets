package org.coderdreams.webapp.autocomplete;

import java.io.Serializable;

import org.coderdreams.enums.StatusType;

public class AutocompleteFilters implements Serializable {
    private static final long serialVersionUID = 1L;

    private int skip = 0;
    private int maxResults = 30;
    private StatusType statusType;

    public AutocompleteFilters() {

    }


    public int getSkip() {
        return skip;
    }

    public AutocompleteFilters setSkip(int skip) {
        this.skip = skip;
        return this;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public AutocompleteFilters setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public StatusType getStatusType() {
        return statusType;
    }
    public AutocompleteFilters setStatusType(StatusType statusType) {
        this.statusType = statusType;
        return this;
    }
}
