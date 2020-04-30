package com.formation.projet7.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.formation.projet7.Constants.Constants;

@Service
public class MailController {

	@Autowired
	public JavaMailSender emailSender;

	
	public String sendSimpleEmail(String emprunteur, String email, String titre, LocalDateTime fin) {

		// Create a Simple MailMessage.
		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(email);
		message.setSubject("Bibliothèque municipale - relance");
		message.setText("Bonjour M,Mme " + emprunteur +"\n" + "Nous constatons un retard dans vos retour d'emprunts.\n\n"
				+ "Titre de l'ouvrage emprunté: " + titre +"\n\n"
				+ "Date de retour dépassée : " + fin + "\n\n"
				+ "Merci de bien vouloir restituer les ouvrages empruntés.\nCordialement.");

		// Send Message!
		this.emailSender.send(message);

		return "Email envoyé!";

	}
}