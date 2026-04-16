package com.financegov.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.financegov.enums.RoleType;
import com.financegov.model.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    
    Optional<Role> findByRoleName(RoleType roleName);
}