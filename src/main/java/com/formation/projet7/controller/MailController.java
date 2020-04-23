package com.formation.projet7.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.formation.projet7.Constants.Constants;

@RestController
public class MailController {

	@Autowired
	public JavaMailSender emailSender;

	@ResponseBody
	@RequestMapping("biblio/mail/sendSimpleEmail")
	public String sendSimpleEmail() {

		// Create a Simple MailMessage.
		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(Constants.USER_EMAIL);
		message.setSubject("Bibliothèque municipale - relance");
		message.setText("Bonjour,\nNous constatons un retard dans vos retour d'emprunts.\n"
				+ "Merci de bien vouloir restituer les ouvrages empruntés.\nCordialement.");

		// Send Message!
		this.emailSender.send(message);

		return "Email Sent!";

	}
}