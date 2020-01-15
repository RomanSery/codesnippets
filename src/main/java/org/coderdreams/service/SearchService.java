package org.coderdreams.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.coderdreams.dao.ComplexUserRepository;
import org.coderdreams.dao.InstitutionRepository;
import org.coderdreams.dom.BaseEntity;
import org.coderdreams.dom.ComplexUser;
import org.coderdreams.dom.Institution;
import org.coderdreams.webapp.autocomplete.AutocompleteFilters;
import org.coderdreams.webapp.autocomplete.SearchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    private @Autowired ComplexUserRepository complexUserRepository;
    private @Autowired InstitutionRepository institutionRepository;

    public <V extends BaseEntity> Optional<V> getById(SearchType searchType, Integer pk) {
        if(pk == null || pk <= 0) {
            return null;
        }

        if(searchType == SearchType.INSTITUTIONS) {
            return (Optional<V>) institutionRepository.findById(pk);
        } else if(searchType == SearchType.USERS) {
            return (Optional<V>) complexUserRepository.findById(pk);
        }
        return null;
    }

    public List<Institution> searchInstitutions(String term, AutocompleteFilters filters) {
        if(StringUtils.isBlank(term)) {
            return Collections.emptyList();
        }
        List<Institution> results = institutionRepository.searchInstitutions(term, false, filters);
        if(results.isEmpty()) {
            results = institutionRepository.searchInstitutions(term, true, filters);
        }
        return results;
    }

    public List<ComplexUser> searchUsers(String term, AutocompleteFilters filters) {
        if(StringUtils.isBlank(term)) {
            return Collections.emptyList();
        }
        return complexUserRepository.searchUsers(term, filters);
    }
}
