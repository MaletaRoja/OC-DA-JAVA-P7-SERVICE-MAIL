package com.formation.projet7.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.formation.projet7.model.Utilisateur;

public interface UserRepo extends JpaRepository<Utilisateur, Integer> {

}
