package org.coderdreams.service;

import org.coderdreams.dom.BaseEntity;
import org.coderdreams.hibernate.HibernateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CrudService {

	@Autowired private HibernateDao hibernateDao;

    public <V extends BaseEntity> V findById(Class<V> entityClass, Integer pk) {
        if(pk == null || pk <= 0) {
            return null;
        }
        return hibernateDao.findById(entityClass, pk);
    }


	synchronized public <T extends BaseEntity> T create(T obj) {
		if(obj == null) return null;
		if(obj.getId() != 0) {
			save(obj);
			return obj;
		}
        hibernateDao.saveUpdate(obj);
		return obj;
	}

	synchronized public <T extends BaseEntity> void save(T obj) {
		if(obj == null) {
		    return;
        }
        hibernateDao.merge(obj);
	}

    synchronized public <T extends BaseEntity> void delete(T obj) {
        if(obj == null) return;
        if(obj.getId() == 0) {
            obj = null;
            return;
        }
        hibernateDao.delete(obj);
    }

    synchronized public <T extends BaseEntity> void delete(Class<T> entityClass, Integer pk) {
        if(pk == null) return;
        hibernateDao.delete(entityClass, pk);
    }

}
