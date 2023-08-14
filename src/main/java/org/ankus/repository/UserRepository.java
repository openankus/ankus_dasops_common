package org.ankus.repository;

import org.ankus.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 사용자 정보 DAO
 */
@Repository
public interface UserRepository extends JpaRepository<User, String>{

    User findUserByLoginId(String loginId);

    List<User> findByLoginId(String loginId);

    @Transactional
    void deleteByloginId(String loginId);

    @Transactional
    void deleteById(Long aLong);
}
