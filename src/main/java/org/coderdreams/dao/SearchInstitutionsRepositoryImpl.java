package org.coderdreams.dao;


import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.coderdreams.dom.Institution;
import org.coderdreams.util.Utils;
import org.coderdreams.webapp.autocomplete.AutocompleteFilters;
import org.springframework.beans.factory.annotation.Autowired;

public class SearchInstitutionsRepositoryImpl implements SearchInstitutionsRepository {
    @Autowired private EntityManager entityManager;

    @Override
    public List<Institution> searchInstitutions(String term, boolean useFuzzySearch, AutocompleteFilters filters) {

        StringBuilder hql = new StringBuilder("from Institution u WHERE ");

        if(useFuzzySearch) {
            hql.append(" levenshtein (:term, u.name, :maxDistance) <= :maxDistance ");
        } else {
            hql.append(" u.name like :term ");
        }

        if(filters.getStatusType() != null) {
            hql.append(" and u.status = :status ");
        }

        if(useFuzzySearch) {
            hql.append(" order by levenshtein (:term, u.name, :maxDistance) asc");
        } else if(!StringUtils.isBlank(term)) {
            hql.append("order by u.name");
        }

        TypedQuery<Institution> q = entityManager.createQuery(hql.toString(), Institution.class);

        if(useFuzzySearch) {
            q.setParameter("term", Utils.getApproximateMatchSearchTerm(term));
            q.setParameter("maxDistance", filters.getMaxDistance());
        } else {
            q.setParameter("term", '%' +term+ '%');
        }


        if(filters.getStatusType() != null) {
            q.setParameter("status", filters.getStatusType());
        }

        if(filters.getMaxResults() > 0) {
            q.setMaxResults(filters.getMaxResults());
        }
        if(filters.getSkip() > 0) {
            q.setFirstResult(filters.getSkip());
        }

        return q.getResultList();
    }



}
