package org.coderdreams.dao;


import java.util.List;

import org.coderdreams.dom.Institution;
import org.coderdreams.webapp.autocomplete.AutocompleteFilters;

public interface SearchInstitutionsRepository {

    List<Institution> searchInstitutions(String term, boolean useFuzzySearch, AutocompleteFilters filters);
}
