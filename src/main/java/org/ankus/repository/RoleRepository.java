package org.ankus.repository;

import org.ankus.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findRoleByName(String name);

    Role getByName(String name);
}
