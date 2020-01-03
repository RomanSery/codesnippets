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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    private @Autowired ComplexUserRepository complexUserRepository;
    private @Autowired InstitutionRepository institutionRepository;

    public <V extends BaseEntity> Optional<V> getById(Class<V> entityClass, Integer pk) {
        if(pk == null || pk <= 0) {
            return null;
        }

        if(entityClass == Institution.class) {
            return (Optional<V>) institutionRepository.findById(pk);
        } else if(entityClass == ComplexUser.class) {
            return (Optional<V>) complexUserRepository.findById(pk);
        }
        return null;
    }

    public List<Institution> searchInstitutions(String term, AutocompleteFilters filters) {
        if(StringUtils.isBlank(term)) {
            return Collections.emptyList();
        }
        return institutionRepository.searchInstitutions(term, filters);
    }

    public List<ComplexUser> searchUsers(String term, AutocompleteFilters filters) {
        if(StringUtils.isBlank(term)) {
            return Collections.emptyList();
        }
        return complexUserRepository.searchUsers(term, filters);
    }
}
