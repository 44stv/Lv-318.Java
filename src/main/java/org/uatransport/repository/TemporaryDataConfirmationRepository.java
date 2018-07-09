package org.uatransport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uatransport.entity.TemporaryDataConfirmation;

public interface TemporaryDataConfirmationRepository extends JpaRepository<TemporaryDataConfirmation, Long> {

    TemporaryDataConfirmation findByUuid(String uuid);
}
