package org.coderdreams.dao;


import java.util.List;

import org.coderdreams.dom.UserLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLockRepository extends JpaRepository<UserLock, Integer> {

    List<UserLock> findByRecordId(int recordId);

    List<UserLock> findByRecordIdAndUserId(int recordId, int userId);

    long countByRecordId(int recordId);

}
