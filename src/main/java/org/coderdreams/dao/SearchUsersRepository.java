package org.coderdreams.dao;


import java.util.List;

import org.coderdreams.dom.ComplexUser;
import org.coderdreams.webapp.autocomplete.AutocompleteFilters;

public interface SearchUsersRepository {

    List<ComplexUser> searchUsers(String term, AutocompleteFilters filters);
}
