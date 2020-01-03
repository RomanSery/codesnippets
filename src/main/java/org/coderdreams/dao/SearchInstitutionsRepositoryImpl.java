package org.coderdreams.dao;


import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.coderdreams.dom.Institution;
import org.coderdreams.webapp.autocomplete.AutocompleteFilters;
import org.springframework.beans.factory.annotation.Autowired;

public class SearchInstitutionsRepositoryImpl implements SearchInstitutionsRepository {

    @Autowired private EntityManager entityManager;

    @Override
    public List<Institution> searchInstitutions(String term, AutocompleteFilters filters) {

        StringBuilder hql = new StringBuilder("from Institution u WHERE u.name like :term ");
        if(filters.getStatusType() != null) {
            hql.append(" and u.status = :status ");
        }
        hql.append("order by u.name");
        TypedQuery<Institution> q = entityManager.createQuery(hql.toString(), Institution.class);

        q.setParameter("term", '%' +term+ '%');
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
