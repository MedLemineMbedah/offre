package com.example.offres.Service;

import com.example.offres.Environement;
import com.example.offres.Model.Entreprise;
import com.example.offres.Model.Offre;
import com.example.offres.Model.Poster;
import com.example.offres.Repository.PosterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
public class ServicePoster {
    @Autowired
    private PosterRepository posterRepository;

    public List<Poster> allopster(){
       return posterRepository.findAll();
    }
    public Poster get(Integer id){
        return posterRepository.findById(id).get();
    }
    public List<Poster> allOffrePoster(Integer id){ return  posterRepository.offrePoster(id);}
    public void savePoster(MultipartFile file, String description, Offre offer, Entreprise entreprise){
        try{
            // create directory if it doesn't existe

            String dirName = Environement.staticPath+"\\"+offer.getId()+"\\";
            File dir = new File(dirName);
            if(!dir.exists()) dir.mkdir();
            dirName += entreprise.getId()+"/";
            dir = new File(dirName );
            if(!dir.exists()) dir.mkdir();


            String fileName = file.getOriginalFilename();

            File f = new File(dirName + fileName);

            Poster post = new Poster();
            post.setOffre(offer);
            post.setEntreprise(entreprise);
            post.setDescription(description);
            post.setFile(fileName);


            posterRepository.save(post);

            file.transferTo(f);



        }catch(Exception e){
            System.out.println(":::"+e.getMessage());
        }

    }





}
