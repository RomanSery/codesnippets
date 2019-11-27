package org.coderdreams.hibernate;


import javax.persistence.EntityNotFoundException;

import org.coderdreams.dom.BaseEntity;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@EnableTransactionManagement
public class HibernateDao {

	private static final Logger log = LoggerFactory.getLogger(HibernateDao.class);

    @Autowired
    private SessionFactory sessionFactory;


    public <V extends BaseEntity> V findById(Class<V> entityClass, Integer pk) {
        return currentSession().get(entityClass, pk);
    }

    public <V extends BaseEntity> void saveUpdate(V obj) {
        currentSession().saveOrUpdate(obj);
    }

    public <V extends BaseEntity> void merge(V obj) {
        mergeEntity(obj);
    }

    public <V extends BaseEntity> void delete(V obj) {
        currentSession().delete(obj);
        currentSession().flush();
        obj = null;
    }

    public <V extends BaseEntity> void delete(Class<V> entityClass, Integer id) {
        if(entityClass == null || id == null) {
            return;
        }

        try {
            V o = currentSession().getReference(entityClass, id);
            if(o != null) {
                currentSession().delete(o);
                currentSession().flush();
            }
        } catch (EntityNotFoundException e) {
            //ignore
        } catch (Exception ex) {
            log.error("delete({}, {}) {}", entityClass.getSimpleName(), id, ex.getMessage());
        }
    }

    public <V extends BaseEntity> void createOrUpdate(V obj, boolean create) {
        if(create) {
            currentSession().saveOrUpdate(obj);
        } else {
            try {
                currentSession().saveOrUpdate(obj);
            } catch (NonUniqueObjectException e) {
                currentSession().merge(obj);
            }
        }
    }

    <V extends BaseEntity> void mergeEntity(V entity) {
        try {
            currentSession().saveOrUpdate(entity);
        } catch (NonUniqueObjectException e) {
            currentSession().merge(entity);
        }
        currentSession().flush();
    }

    protected Session currentSession() {
        return getSessionFactory().getCurrentSession();
    }
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}