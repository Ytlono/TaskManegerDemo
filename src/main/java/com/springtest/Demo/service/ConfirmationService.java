package com.springtest.Demo.service;

import com.springtest.Demo.auth.dto.RegisterDTO;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class ConfirmationService {

    private final JavaMailSenderService mailSenderService;
    private final Map<String, String> confirmationCodes = new ConcurrentHashMap<>();
    private final Map<String, RegisterDTO> pendingUsers = new ConcurrentHashMap<>();

    public ConfirmationService(JavaMailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    private String generateCode() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    public void sendConfirmationEmail(String email) {
        String code = generateCode();
        confirmationCodes.put(email, code);

        String subject = "Подтверждение регистрации";
        String body = "Ваш код подтверждения: " + code;
        mailSenderService.send(email, subject, body);

        Executors.newSingleThreadScheduledExecutor().schedule(() -> confirmationCodes.remove(email), 5, TimeUnit.MINUTES);
    }

    public boolean isConfirmed(String email, String inputCode) {
        if (confirmationCodes.containsKey(email) && confirmationCodes.get(email).equals(inputCode)) {
            removeConfirmedEmail(email);
            return true;
        }
        return false;
    }

    public void removeConfirmedEmail(String email) {
        confirmationCodes.remove(email);
        pendingUsers.remove(email);
    }

    public void savePendingUser(String email, RegisterDTO registerDTO) {
        pendingUsers.put(email, registerDTO);
    }

    public RegisterDTO getPendingUser(String email) {
        return pendingUsers.get(email);
    }

    public void removePendingUser(String email) {
        pendingUsers.remove(email);
    }
}
