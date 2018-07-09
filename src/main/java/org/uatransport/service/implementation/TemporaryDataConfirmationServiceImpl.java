package org.uatransport.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.uatransport.entity.ConfirmationType;
import org.uatransport.entity.TemporaryDataConfirmation;
import org.uatransport.repository.TemporaryDataConfirmationRepository;
import org.uatransport.service.TemporaryDataConfirmationService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class TemporaryDataConfirmationServiceImpl implements TemporaryDataConfirmationService {

    @Autowired
    private TemporaryDataConfirmationRepository repository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public TemporaryDataConfirmation findByUuid(String uuid) {
        return repository.findByUuid(uuid);
    }

    @Override
    public TemporaryDataConfirmation makePasswordConfirmationEntity(String uuid, String newPassword, String userEmail) {
        TemporaryDataConfirmation temporaryDataConfirmation = new TemporaryDataConfirmation();
        temporaryDataConfirmation.setUuid(uuid);
        temporaryDataConfirmation.setTimeStamp(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        temporaryDataConfirmation.setNewPassword(bcryptEncoder.encode(newPassword));
        temporaryDataConfirmation.setUserEmail(userEmail);
        temporaryDataConfirmation.setConfirmationType(ConfirmationType.PASSWORD_CONFIRM);
        return temporaryDataConfirmation;
    }

    @Override
    public TemporaryDataConfirmation makeRegistrationConfirmationEntity(String uuid, String userEmail) {
        TemporaryDataConfirmation temporaryDataConfirmation = new TemporaryDataConfirmation();
        temporaryDataConfirmation.setUuid(uuid);
        temporaryDataConfirmation.setUserEmail(userEmail);
        temporaryDataConfirmation.setTimeStamp(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        temporaryDataConfirmation.setConfirmationType(ConfirmationType.REGISTRATION_CONFIRM);
        return temporaryDataConfirmation;
    }

    @Override
    public void save(TemporaryDataConfirmation temporaryDataConfirmation) {
        repository.save(temporaryDataConfirmation);
    }

    @Override
    public void delete(TemporaryDataConfirmation temporaryDataConfirmation) {
        repository.delete(temporaryDataConfirmation);
    }

    @Override
    public List<TemporaryDataConfirmation> findAll() {
        return repository.findAll();
    }

}
