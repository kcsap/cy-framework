package com.cy.framework.authorization.repository;


import com.cy.framework.authorization.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {
}
