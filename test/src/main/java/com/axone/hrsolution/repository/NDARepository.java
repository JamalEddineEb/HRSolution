package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.NDA;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NDA entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NDARepository extends JpaRepository<NDA, Long> {}
