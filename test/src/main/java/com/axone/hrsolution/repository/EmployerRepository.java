package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.Employer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Employer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {}
