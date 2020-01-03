package org.coderdreams.dao;


import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.coderdreams.dom.ComplexUser;
import org.coderdreams.webapp.autocomplete.AutocompleteFilters;
import org.springframework.beans.factory.annotation.Autowired;

public class SearchUsersRepositoryImpl implements SearchUsersRepository {

    @Autowired private EntityManager entityManager;

    @Override
    public List<ComplexUser> searchUsers(String term, AutocompleteFilters filters) {

        StringBuilder hql = new StringBuilder("from ComplexUser u WHERE u.displayName like :term ");
        if(filters.getStatusType() != null) {
            hql.append(" and u.status = :status ");
        }
        hql.append("order by u.displayName");
        TypedQuery<ComplexUser> q = entityManager.createQuery(hql.toString(), ComplexUser.class);

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
