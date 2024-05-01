package com.example.backend.entities.conf;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Configure the mail sender properties
        //Almadar
//        mailSender.setHost("Mail.almadar.ly");
//        mailSender.setPort(25);
//        mailSender.setUsername("raftools@almadar.ly");
//        mailSender.setPassword("M#dar@1223");

        //dowaleya
//        mailSender.setHost("smtp.office365.com");
//        mailSender.setPort(587);
//        mailSender.setUsername("raftool@litc.ly");
//        mailSender.setPassword("%R^T5r6t");

        //soutelma
        mailSender.setHost("mail.sotelma.ml");
        mailSender.setPort(587);
        mailSender.setUsername("raftools@sotelma.ml");
        mailSender.setPassword("R@ft00l24");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "false");

        return mailSender;
    }
}