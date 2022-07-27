package com.example.offres.Service;

import com.example.offres.Model.Admin;
import com.example.offres.Model.Entreprise;
import com.example.offres.Model.Personne;
import com.example.offres.Repository.EntrepriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceEntreprise {
        @Autowired
        private EntrepriseRepository entrepriseRepository;

    public Entreprise enregistrerEntreprise(Entreprise entreprise) {
        return this.entrepriseRepository.save(entreprise);
    }

    public List<Entreprise> getAllEntreprise(){
        return (List<Entreprise>) entrepriseRepository.findAll();
    }

    public Entreprise getEntrepriseById(Integer id) {


        Optional<Entreprise> optional = entrepriseRepository.findById(id);
        Entreprise entreprise = null;
        if (optional.isPresent()) {
            entreprise = optional.get();
        } else {
            throw new RuntimeException(" entreprise n'existe pas :: " + id);
        }
        return entreprise;
    }

    public void deleteEntreprise(Integer id){
        this.entrepriseRepository.deleteById(id);
    }
}
