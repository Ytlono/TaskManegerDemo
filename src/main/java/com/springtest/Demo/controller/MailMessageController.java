package com.springtest.Demo.controller;
import com.springtest.Demo.service.ConfirmationService;
import org.springframework.mail.MailSender;
import com.springtest.Demo.service.JavaMailSenderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MailMessageController {
    private final ConfirmationService confirmationService;

    public MailMessageController(ConfirmationService confirmationService) {
        this.confirmationService = confirmationService;
    }

    @GetMapping("/hello")
    public String sayHello(){
        confirmationService.sendConfirmationEmail("egorohrem@gmail.com");
        return "home";
    }
}
