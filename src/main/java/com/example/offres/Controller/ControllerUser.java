package com.example.offres.Controller;

import com.example.offres.Model.Admin;
import com.example.offres.Model.Offre;
import com.example.offres.Model.Personne;
import com.example.offres.Repository.OffreRepository;
import com.example.offres.Service.ServiceOffre;
import com.example.offres.Service.ServicePersonne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.HttpResource;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Controller
public class ControllerUser {

//    @Value("${static.path}")




    @Autowired
    private ServicePersonne servicePersonne;
    @Autowired
    private ServiceOffre  serviceOffre;

    @Autowired
    private OffreRepository offreRepository;


    @GetMapping("/user/login")
    public String adminHomePage(Model model){
        Personne personne = new Personne();

        model.addAttribute("personne", personne);
        return "user/login";
    }
    @GetMapping("/user/postoffre/")
    public String fileForm(Model m, @RequestParam(name = "id") Integer id){

        m.addAttribute("id",id);

        return  "file";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file){
        try{

            String fileName = "tese";
            File f = new File(Paths.staticPath + file.getOriginalFilename());
            file.transferTo(f);
        }catch(Exception e){
        }
        return "redirect:/user/file";
    }


    @GetMapping("/offre/pdf")
    void generatePdf(HttpServletResponse r) throws IOException {

        Admin admin = new Admin();
        admin.setNom("mohamedVall");
        admin.setPrenom("Mohamed");
        Offre offer = new Offre();
        offer.setEditeur(admin);
        offer.setDateAttribution(new Date());
        offer.setDateFin(new Date());
        offer.setDatePublication(new Date());
        offer.setDelai(75);
        offer.setObjet("Creation du project");
        offer.setMontant(500000);
        offer.setDescription("la premiere kjkad jfs knfa .kb.mfn amn amn mn fen m, ljlk en kjnemna " +
                "lkhklfanmn mnnklnadf mn aln vad ,mnlkndva ,nklndva ,mnkldva ,nkdva ,mn ka lknam " +
                "khlafdkljn lkjm eaflkjmn lekvjn an knvanimn eavjn efkaljmn vakj qeauhfq3eb lj" +
                "khlihaef mn;ojena jn;keamn lkjavn lknj;lnevan lknven.");


        serviceOffre.GeneratePdf(r,offer);
    }




}
