package org.ankus.repository;

import org.ankus.model.PersistentLogins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersistentLoginsRepository extends JpaRepository<PersistentLogins, String> {
    PersistentLogins findBySeriesAndToken(String series,String token);

    List<PersistentLogins> findByUsername(String username);
}
