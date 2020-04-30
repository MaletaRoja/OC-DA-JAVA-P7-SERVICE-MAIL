package com.formation.projet7;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.formation.projet7.controller.MailController;
import com.formation.projet7.model.EmpruntAuxMail;
import com.formation.projet7.model.Login;
import com.formation.projet7.model.Utilisateur;
import com.formation.projet7.proxy.MicroServiceBibliotheque;
import com.formation.projet7.service.UserConnexion;

@EnableFeignClients("com.formation.projet7")
@SpringBootApplication
public class OcDaJavaP7ServiceMailApplication {
	
	@Autowired
	MicroServiceBibliotheque microServiceBibliotheque;
	
	@Autowired
	UserConnexion userConnexion;
	
	@Autowired
	MailController mailController;
	
	public static void main(String[] args) {
		SpringApplication.run(OcDaJavaP7ServiceMailApplication.class, args);
	}
	
	@Scheduled(initialDelay = 1000L, fixedDelay = 10000L)
	void scrutation() throws InterruptedException {
		
		Login login = new Login("mailservice","mail");
		
		String token = userConnexion.identifierUtilisateur(login);
		System.out.println("Heure actuelle: " + new Date());
		
		System.out.println("chaine token dans scheduler: " + token);
		
		
		List<EmpruntAuxMail> empruntsActifs = microServiceBibliotheque.obtenirEmpruntsActif(token);
		System.out.println("Nbre d'emprunts actifs: " + empruntsActifs.size());
		
		
		for (EmpruntAuxMail em : empruntsActifs) {
			
			System.out.println(em.getEmprunteur() + ">>>>" + em.getTitre());
			
			String emprunteur = em.getEmprunteur();
			String email = em.getEmail();
			String titre = em.getTitre();
			LocalDateTime fin = em.getFin();
			
			mailController.sendSimpleEmail(emprunteur, email, titre, fin);
		}
		
		Thread.sleep(1000L * 60 * 5);  // 5 min
	
	}
	
	@Configuration
	@EnableScheduling
	@ConditionalOnProperty(name = "scheduling.enabled", matchIfMissing = true)
	class SchedulingConfiguration{
	
	}
	
	
	
	

}
