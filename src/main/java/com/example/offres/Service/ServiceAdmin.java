package com.example.offres.Service;

import com.example.offres.Model.Admin;
import com.example.offres.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceAdmin {
    @Autowired
    private AdminRepository adminRepository;


    public List<Admin> getAlladmins(){
        return (List<Admin>) adminRepository.findAll();
    }

    public void enregistrerAdmin(Admin admin){
        this.adminRepository.save(admin);
    }

    public void saveAdmin(Admin admin){
        this.adminRepository.save(admin);

    }

    public Admin getAdminById(Integer id) {


        Optional<Admin> optional = adminRepository.findById(id);
        Admin admin = null;
        if (optional.isPresent()) {
            admin = optional.get();
        } else {
            throw new RuntimeException(" Administrateur n'existe pas :: " + id);
        }
        return admin;
    }

    public void deleteAdmin(Integer id){
        this.adminRepository.deleteById(id);
    }




}
