package org.coderdreams.dao;


import org.coderdreams.dom.ComplexUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplexUserRepository extends JpaRepository<ComplexUser, Integer> {

}
