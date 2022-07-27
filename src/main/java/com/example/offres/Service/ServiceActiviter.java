package com.example.offres.Service;


import com.example.offres.Model.Activiter;
import com.example.offres.Model.Admin;
import com.example.offres.Model.Offre;
import com.example.offres.Repository.ActiviterRepository;
import com.example.offres.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ServiceActiviter {
    @Autowired
    private ActiviterRepository activiterRepository;


    public List<Activiter> getAllactiviter(){
        return (List<Activiter>) activiterRepository.findAll();
    }
    public List<Activiter> activiterdesc(){return activiterRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));}

    public void enregistrerActiviter(Activiter activiter){
        this.activiterRepository.save(activiter);
    }

    public void saveAdmin(Activiter activiter){
        this.activiterRepository.save(activiter);

    }

    public Activiter getActiviteById(Integer id) {


        Optional<Activiter> optional = activiterRepository.findById(id);
        Activiter activiter = null;
        if (optional.isPresent()) {
            activiter = optional.get();
        } else {
            throw new RuntimeException(" activiter n'existe pas :: " + id);
        }
        return activiter;
    }

    public void deleteActiviter(Integer id){
        this.activiterRepository.deleteById(id);
    }




}
