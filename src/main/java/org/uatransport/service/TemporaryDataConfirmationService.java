package org.uatransport.service;

import org.uatransport.entity.TemporaryDataConfirmation;

import java.util.List;

public interface TemporaryDataConfirmationService {

    TemporaryDataConfirmation findByUuid(String uuid);

    TemporaryDataConfirmation makePasswordConfirmationEntity(String uuid, String newPassword, String userEmail);

    TemporaryDataConfirmation makeRegistrationConfirmationEntity(String uuid, String userEmail);

    void save(TemporaryDataConfirmation temporaryDataConfirmation);

    void delete(TemporaryDataConfirmation temporaryDataConfirmation);

    List<TemporaryDataConfirmation> findAll();
}
