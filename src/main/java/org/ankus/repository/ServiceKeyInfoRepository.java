package org.ankus.repository;

import org.ankus.model.ServiceKeyInfo;
import org.ankus.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 서비스 Key(인증키) 정보 DAO
 */
@Repository
public interface ServiceKeyInfoRepository extends JpaRepository<ServiceKeyInfo, Long> {

    List<ServiceKeyInfo> findByUserAndDeleted(User user, Boolean deleted);

    ServiceKeyInfo findByServiceKeyAndDeleted(String serviceKey, Boolean deleted);

}
