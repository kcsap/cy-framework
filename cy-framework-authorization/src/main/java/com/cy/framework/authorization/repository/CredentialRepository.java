package com.cy.framework.authorization.repository;


import com.cy.framework.authorization.domain.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialRepository extends JpaRepository<Credentials,Long> {

    Credentials findByName(String name);
}
