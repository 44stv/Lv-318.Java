package org.uatransport.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.uatransport.entity.TemporaryDataConfirmation;
import org.uatransport.service.TemporaryDataConfirmationService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Service
public class ExpirationCheckService {

    @Value("${expiration-time-password-change-confirmation}")
    Long passwordChangeExpiredTime;

    @Autowired
    private TemporaryDataConfirmationService temporaryDataConfirmationService;

    public Optional<TemporaryDataConfirmation> getTemporaryDataConfirmationWithExpirationChecking(String uuidFromUrl) {
        Optional<TemporaryDataConfirmation> uncheckedTemporaryDataConfirmation = Optional
                .of(temporaryDataConfirmationService.findByUuid(uuidFromUrl));
        if (isExpired(uncheckedTemporaryDataConfirmation.get())) {
            temporaryDataConfirmationService.delete(uncheckedTemporaryDataConfirmation.get());
            return Optional.empty();
        }
        return uncheckedTemporaryDataConfirmation;
    }

    @Scheduled(cron = "${temporary-data-confirmation-clear-schedule}")
    public void scheduleCleanTaskByExpirationTime() {
        List<TemporaryDataConfirmation> listUntilCleaning = temporaryDataConfirmationService.findAll();
        listUntilCleaning.stream().filter(c -> isExpired(c)).forEach(temporaryDataConfirmation -> {
            temporaryDataConfirmationService.delete(temporaryDataConfirmation);
        });
    }

    private boolean isExpired(TemporaryDataConfirmation temporaryDataConfirmation) {
        return LocalDateTime.now().toInstant(ZoneOffset.UTC)
                .isAfter(temporaryDataConfirmation.getTimeStamp().plusSeconds(passwordChangeExpiredTime));
    }
}
