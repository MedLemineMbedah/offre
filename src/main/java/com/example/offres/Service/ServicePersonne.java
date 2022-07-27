package com.example.offres.Service;

import com.example.offres.Model.Personne;
import com.example.offres.Repository.PersonneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicePersonne {
    @Autowired
    private PersonneRepository personneRepository;

    public List<Personne> getAllPersonnes() {
        return (List<Personne>) personneRepository.findAll();
    }

    public void enregistrerPersonne(Personne personne) {
        this.personneRepository.save(personne);
    }

    public Personne finPersonne(String email){

        return personneRepository.findPerso(email).get(0);
    }

    public double total(Integer valeur, Integer total){
        return valeur/total;
    }

}
