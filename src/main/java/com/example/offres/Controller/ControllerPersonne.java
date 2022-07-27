package com.example.offres.Controller;



import com.example.offres.Environement;
import com.example.offres.Exception.OffreNotFoundException;
import com.example.offres.Model.*;

import com.example.offres.Repository.EntrepriseRepository;
import com.example.offres.Service.ServiceActiviter;
import com.example.offres.Service.ServiceOffre;
import com.example.offres.Service.ServicePersonne;
import com.example.offres.Service.ServicePoster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.HttpResource;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

// personn conntroller
@Controller
public class ControllerPersonne {

    @Autowired
    private ServicePersonne serviceAdmin;
    @Autowired
    private ServiceOffre serviceOffre;
    @Autowired
    private ServicePersonne servicePersonne;

    @Autowired
    private ServiceActiviter serviceActiviter;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EntrepriseRepository  entrepriseRepository;

    @Autowired
    private ServicePoster servicePoster;
    public Personne user (){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        Personne personne = servicePersonne.finPersonne(user);
        return personne;
    }

    @RequestMapping("/user/login")
    public ModelAndView login() {
        return new ModelAndView("user/login");
    }

    @GetMapping("/user/home")
    public String userHome(Model model){
        Personne personne = user();
        int offreattribuer = serviceOffre.offreattribuer(personne.getEntreprise().getId()).size();
        int offrenoattribuer = serviceOffre.offrenoattribuer(personne.getEntreprise().getId()).size();
        int offreposter = serviceOffre.offrePoster(personne.getEntreprise().getId()).size();
        int offrenoposter = serviceOffre.offreNoPoster(personne.getEntreprise().getId()).size();
        int alloffre = serviceOffre.alloffres().size();
        String page = "Tableau de bord";
        int light = 1;


        model.addAttribute("personne", personne);
        model.addAttribute("offreattribuer", offreattribuer);
        model.addAttribute("offreposter", offreposter);
        model.addAttribute("offrenoposter", offrenoposter);
        model.addAttribute("offrenoattribuer", offrenoattribuer);
        model.addAttribute("alloffre", alloffre);
        model.addAttribute("page", page);
        model.addAttribute("light", light);


        return "user/interface/home";
    }

    @GetMapping("/user/profile")
    public String profileUser(Model model){
        String page = "Profile";
        int light = 3;
        Personne personne = user();
        model.addAttribute("personne",personne);
        model.addAttribute("page", page);
        model.addAttribute("light", light);
        return "user/interface/profile";
    }

    @RequestMapping("/user/profile/update")
    public String updateUser(Personne personne){
        Personne p = user();

        p.setTelephone(personne.getTelephone());
        p.setNom(personne.getNom());
        p.setPrenom(personne.getPrenom());
        p.setAdresse(personne.getAdresse());
        p.setPassword(bCryptPasswordEncoder.encode(personne.getPassword()));

        servicePersonne.enregistrerPersonne(p);
        return "redirect:/user/profile";
    }

    public List offres(List<Offre> alloffre, Personne personne){
        List<List<Object>> list = new ArrayList<>();
        List<Offre> offres = new ArrayList<>();


        for (Offre offre:alloffre) {
            Boolean trouver = false;
            Offre offre1 = new Offre();
            if (offre.getAttribue() == null) {
                for (Poster poster:offre.getPostilerE()) {
                    if (poster.getEntreprise() == personne.getEntreprise()){
                        trouver = true;
                        break;
                    }

                }
                if (trouver){
                    offre1.setId(offre.getId());
                    offre1.setObjet(offre.getObjet());
                    offre1.setDatePublication(offre.getDateAttribution());
                    offre1.setDateFin(offre.getDateFin());
                    offre1.setDelai(offre.getDelai());
                    offre1.setMontant(offre.getMontant());
                    offre1.setStatus("OK");


                }
                else if (!trouver){
                    offre1.setId(offre.getId());
                    offre1.setObjet(offre.getObjet());
                    offre1.setDatePublication(offre.getDateAttribution());
                    offre1.setDateFin(offre.getDateFin());
                    offre1.setDelai(offre.getDelai());
                    offre1.setMontant(offre.getMontant());
                    offre1.setStatus("Pas encours");

                }
            }
            else{
                if (offre.getAttribue() == personne.getEntreprise()){
                    offre1.setId(offre.getId());
                    offre1.setObjet(offre.getObjet());
                    offre1.setDatePublication(offre.getDateAttribution());
                    offre1.setDateFin(offre.getDateFin());
                    offre1.setDelai(offre.getDelai());
                    offre1.setMontant(offre.getMontant());
                    offre1.setStatus("Success");

                }
                else{
                    offre1.setId(offre.getId());
                    offre1.setObjet(offre.getObjet());
                    offre1.setDatePublication(offre.getDateAttribution());
                    offre1.setDateFin(offre.getDateFin());
                    offre1.setDelai(offre.getDelai());
                    offre1.setMontant(offre.getMontant());
                    offre1.setStatus("Autre");

                }
            }

            offres.add(offre1);
        }
        return offres;
    }

    @GetMapping("/user/offres")
    public String offres(Model model){
        Personne personne = user();
        List<Offre> offreattribuer = serviceOffre.offreattribuer(personne.getEntreprise().getId());
        List<Offre> offrenoattribuer = serviceOffre.offrenoattribuer(personne.getEntreprise().getId());
        List<Offre> offreposter = serviceOffre.offreattribuer(personne.getEntreprise().getId());
        List<Offre> offrenoposter = serviceOffre.offreNoPoster(personne.getEntreprise().getId());
        List<Offre> alloffre = serviceOffre.alloffres();
        String page = "Offres";
        int light = 2;

       List<Offre> lesoffres=offres(alloffre,personne);

        for (Offre offre:offreattribuer) {
            long old = offre.getDateAttribution().getTime();
            long auj =new Date().getTime();


            offre.getDateAttribution().setMonth(offre.getDateAttribution().getMonth() + offre.getDelai());
            int difference_In_Time = (int) (offre.getDateAttribution().getTime() - old);
            int days_end = (int) (auj-old);


            long jourspasse = TimeUnit.MILLISECONDS.toDays(days_end);



            if(jourspasse >= offre.getDelai()){

                offre.setStatus("100");
            }
            else if(jourspasse < 0){
                offre.setStatus("0");
            }
            else {
                long percent =  ((jourspasse*100) / offre.getDelai());
                offre.setStatus(percent+"");
            }



        }



        model.addAttribute("personne", personne);
        model.addAttribute("offreattribuer", offreattribuer);
        model.addAttribute("offreposter", offreposter);
        model.addAttribute("offrenoposter", offrenoposter);
        model.addAttribute("offrenoattribuer", offrenoattribuer);
        model.addAttribute("alloffre", alloffre);
        model.addAttribute("page", page);
        model.addAttribute("lesoffres",lesoffres);
        model.addAttribute("light", light);

        return "user/interface/offres";
    }
    @GetMapping("/user/pdffile/{id}/offre.pdf")
    public void fileForm(@PathVariable("id") Integer id, Model m,HttpServletResponse r) {
      try{
          Offre offre = serviceOffre.get(id);
          serviceOffre.GeneratePdf(r,offre);
      }catch (OffreNotFoundException | IOException e){
          e.printStackTrace();
      }


    }


    @GetMapping("/user/offres/{id}")
    public String offre(@PathVariable("id") Integer id, Model model){
        try {
            Personne personne = user();
            Date aujourdhui = new Date();
            Boolean postiler = true;
            List<Offre> offreposter = serviceOffre.offrePoster(personne.getEntreprise().getId());
            for (Offre o:offreposter) {

                if (o.getId() == id){
                    postiler = false;

                }
            }

            Offre offre = serviceOffre.get(id);

            if (offre.getDateFin().before(aujourdhui)){
                postiler = false;


            }
            int light = 2;
            model.addAttribute("light", light);
            String page = "Offre : "+offre.getObjet();
            model.addAttribute("offre",offre);
            model.addAttribute("personne",personne);
            model.addAttribute("page",page);
            model.addAttribute("postiler",postiler);
        }catch (OffreNotFoundException e){

        }
        return "/user/interface/offre";
    }
    @GetMapping("/user/offre/postiler/{id}")
    public String postiler(@PathVariable("id") Integer id, Model model){
        try {
            Personne personne = user();

            Offre offre = serviceOffre.get(id);
            String page = "Postiler";
            int light = 2;
            model.addAttribute("light", light);
            model.addAttribute("offre",offre);
            model.addAttribute("personne",personne);
            model.addAttribute("page",page);
        }catch (OffreNotFoundException e){

        }
        return "/user/interface/postiler";
    }

    @PostMapping("/user/offre/postOffer")
    public String postOffer(@RequestParam("file") MultipartFile file,@RequestParam("description") String description,@RequestParam("id") Integer id)  {
        try{
            Offre offre = serviceOffre.get(id);
            Personne personne = user();
//            Optional<Entreprise> entreprise = entrepriseRepository.findById(1);
            Entreprise entreprise = user().getEntreprise();
           servicePoster.savePoster(file,description,offre,entreprise);

            Activiter a=new Activiter();
            a.setActiviter("Postiler dans l'offre : "+offre.getObjet());
            a.setUser(personne.getNom()+" "+personne.getPrenom());
            a.setView(false);
            a.setDate(new Date());


            serviceActiviter.enregistrerActiviter(a);
        }
        catch (Exception | OffreNotFoundException e){

        }

        return "redirect:/user/offres/"+id;
    }


    @GetMapping("/user/pdf")
    void generatePdf(HttpServletResponse r) throws IOException {
        serviceOffre.GeneratePdf(r,new Offre());
    }


}
