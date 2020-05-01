package com.formation.projet7.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formation.projet7.controller.MailController;
import com.formation.projet7.model.EmpruntAuxMail;
import com.formation.projet7.model.Login;
import com.formation.projet7.model.Relance;
import com.formation.projet7.proxy.MicroServiceBibliotheque;

@Service
public class EmpruntService {
	
	@Autowired
	MicroServiceBibliotheque microServiceBibliotheque;

	@Autowired
	UserConnexion userConnexion;

	@Autowired
	MailController mailController;
	
	public void relancer() {
		
		
		Login login = new Login("mailservice", "mail");

		String token = userConnexion.identifierUtilisateur(login);
		System.out.println("Heure actuelle: " + new Date());

		System.out.println("chaine token dans scheduler: " + token);

		List<EmpruntAuxMail> empruntsActifs = microServiceBibliotheque.obtenirEmpruntsActif(token);
		System.out.println("Nbre d'emprunts actifs: " + empruntsActifs.size());

		HashMap<String, List<Relance>> emprunteurs = new HashMap<String, List<Relance>>();

		for (EmpruntAuxMail em : empruntsActifs) {

			// System.out.println(em.getEmprunteur() + ">>>>" + em.getTitre());

			String emprunteur = em.getEmprunteur();
			String email = em.getEmail(); // clé
			String titre = em.getTitre();
			LocalDateTime fin = em.getFin();

			Boolean key = emprunteurs.containsKey(email);
			if (!key) {

				List<Relance> relances = new ArrayList<Relance>();
				Relance relance = new Relance();

				relance.setEmail(email);
				relance.setEmprunteur(emprunteur);
				relance.setFin(fin);
				relance.setTitre(titre);
				
				relances.add(relance);
				emprunteurs.put(email, relances);

			} else {

				List<Relance> relances = emprunteurs.get(email);
				Relance relance = new Relance();
				relance.setEmail(email);
				relance.setEmprunteur(emprunteur);
				relance.setFin(fin);
				relance.setTitre(titre);
				relances.add(relance);
				emprunteurs.put(email, relances);

			}

		}
		
		List<String> titres = new ArrayList<String>();
		List<LocalDateTime> dates = new ArrayList<LocalDateTime>();
		String destinataire = null;
		String email = null;
		for (List<Relance> relances : emprunteurs.values()) {

			titres = new ArrayList<String>();
			dates = new ArrayList<LocalDateTime>();
			
			for (Relance relance : relances) {
				
				destinataire = relance.getEmprunteur();
				email = relance.getEmail();
				LocalDateTime fin = relance.getFin();
				String titre = relance.getTitre();
				titres.add(titre);
				dates.add(fin);

			}
			
			String listeTitres = "";
			
			for (String titre : titres) {
				
				listeTitres = listeTitres + titre + "\n";
			}
			
			String listeDates = "";
			
			for (LocalDateTime date : dates) {
				
				listeDates = listeDates + date.toString() + "\n";
			}
			
			String texte = "Bonjour M,Mme " + destinataire + "\n\n" 
					+ "Nous constatons un retard dans la restitition de vos derniers emprunts\n" 
					+ "Nous vous prions de bien vouloir restituer les ouvrages suivants:\n\n"
					+ listeTitres + "\n"
					+ "\n"
					+ "dont les dates de restitution sont:\n\n"
					+ listeDates + "\n"
					+ "\n"
					+ "Cordiales salutations\n"
					+ "\n"
					+ "Le responsable de la bibliothèque municipale";
					
		
			 mailController.sendSimpleEmail(email, texte);
			 texte = "";
			 listeDates = "";
			 listeTitres = "";
		}
		
		
	}

}
