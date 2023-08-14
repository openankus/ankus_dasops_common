package org.ankus.repository;

import org.ankus.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 권한 정보 DAO
 */
@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long>{
    Privilege findPrivilegeByName(String name);
}
