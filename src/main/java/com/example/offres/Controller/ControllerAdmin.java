package com.example.offres.Controller;

import com.example.offres.Environement;
import com.example.offres.Exception.OffreNotFoundException;
import com.example.offres.Model.*;
import com.example.offres.Repository.AdminRepository;
import com.example.offres.Repository.PersonneRepository;
import com.example.offres.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Controller
public class ControllerAdmin {

    @Autowired
    private ServiceAdmin serviceAdmin;
    @Autowired
    private PersonneRepository personneRepository;
    @Autowired
    private ServiceOffre serviceOffre;
    @Autowired
    private ServicePersonne servicePersonne;
    @Autowired
    private ServiceEntreprise serviceEntreprise;
    @Autowired
    private PersonneRepository persoRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ServicePoster servicePoster;

    @Autowired
    private ServiceActiviter serviceActiviter;


    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public Admin user() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        Admin admin = adminRepository.findUser(user).get(0);
        return admin;
    }

    public List<Activiter> activiter() {
        List<Activiter> activiter = serviceActiviter.activiterdesc();
        return activiter;
    }

    public int sizeActiviter(List<Activiter> activiter) {
        int size = 0;
        for (Activiter ac : activiter) {
            if (!ac.getView()){
                size ++;
            }
        }
        return size;
    }

    @GetMapping("/admin")
    public String adminHomePage(Model model) {
        model.addAttribute("listAdmin", serviceAdmin.getAlladmins());
        return "index";
    }


    @RequestMapping("/admin/login")
    public ModelAndView login() {
        return new ModelAndView("admin/login");
    }

    @GetMapping("/admin/ajoutadminForm")
    public String ajoutadminForm(Model model) {
        Admin admin = new Admin();
        model.addAttribute("admin", admin);
        return "new_admin";
    }

    @GetMapping("/admin/home")
    public String adminHome(Model model) {
        Admin admin = new Admin();

        int offreencours = serviceOffre.offreencours().size();
        int participations = servicePoster.allopster().size();
        int entreprises = serviceEntreprise.getAllEntreprise().size();
        int personnes = servicePersonne.getAllPersonnes().size();
        List<Activiter> activiter = activiter();
        int size = sizeActiviter(activiter);
        model.addAttribute("activiter", activiter);
        model.addAttribute("size", size);
        model.addAttribute("offreencours", offreencours);
        model.addAttribute("participations", participations);
        model.addAttribute("entreprises", entreprises);
        model.addAttribute("personnes", personnes);

        model.addAttribute("admin", admin);

        return "admin/interface/home";
    }

    @PostMapping("/saveAdmin")
    public String saveAdmin(@ModelAttribute("admin") Admin admin) {
        serviceAdmin.saveAdmin(admin);
        return "redirect:/admin";
    }


    @GetMapping("/admin/updateadminForm/{id}")
    public String updateadminForm(@PathVariable(value = "id") Integer id, Model model) {
        // get Les Admin Apartir de service
        Admin admin = serviceAdmin.getAdminById(id);
        List<Activiter> activiter = activiter();
        int size = sizeActiviter(activiter);
        model.addAttribute("activiter", activiter);
        model.addAttribute("size", size);
        model.addAttribute("admin", admin);
        return "update_admin";
    }

    @GetMapping("/admin/deleteAdmin/{id}")
    public String deleteAdmin(@PathVariable(value = "id") Integer id) {
        this.serviceAdmin.deleteAdmin(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/ajoutOffreForm")
    public String ajoutOffreForm(Model model) {
        Admin admin = serviceAdmin.getAdminById(1);
        Offre offre = new Offre();
        offre.setEditeur(admin);

        List<Activiter> activiter = activiter();
        int size = sizeActiviter(activiter);
        model.addAttribute("offre", offre);
        model.addAttribute("activiter", activiter);
        model.addAttribute("size", size);
        return "new_offre";
    }

    @GetMapping("/admin/login")
    public String loginForm(Model model) {
        Admin admin = new Admin();

        model.addAttribute("admin", admin);
        return "admin/login";
    }

    @PostMapping("/admin/saveOffre")
    public String saveOffre(@ModelAttribute("offre") Offre offre) throws OffreNotFoundException {
        String etat = "Est Creer avec success";

        offre.setDatePublication(new Date());

        Admin admin = user();
        offre.setEditeur(admin);
        serviceOffre.saveOffre(offre);


        Activiter a = new Activiter();
        a.setActiviter("Publication d'un offre d'objet " + offre.getObjet());
        a.setUser(admin.getNom());


        Activiter activiter = new Activiter(admin.getNom(), "Publication d'un offre d'objet " + offre.getObjet(), new Date());

        serviceActiviter.enregistrerActiviter(activiter);

        return "redirect:/admin/offres?creation";

    }

    @PostMapping("/admin/offre/modifier")
    public String modifierOffre(Offre offre) throws OffreNotFoundException {
        Offre o = serviceOffre.get(offre.getId());
        o.setObjet(offre.getObjet());
        o.setDelai(offre.getDelai());
        o.setDescription(offre.getDescription());
        if (offre.getDateFin() != null) {
            o.setDateFin(offre.getDateFin());
        }
        o.setMontant(offre.getMontant());
        Admin admin = user();
        serviceOffre.saveOffre(o);


        Activiter a = new Activiter();
        a.setActiviter("Modifier l'offre : " + offre.getObjet());
        a.setUser(admin.getNom());
        a.setDate(new Date());


        serviceActiviter.enregistrerActiviter(a);

        return "redirect:/admin/offres?modifier";

    }

    @GetMapping("admin/offres/details/{id}")
    public String offredetails(@PathVariable("id") Integer id, Model model) throws OffreNotFoundException {

            Admin admin = user();
            Offre offre = serviceOffre.get(id);
            String page_head = "Offre";
            String page = "Detail Offre : " + offre.getObjet();
            String path = Environement.staticPath;
            List<Activiter> activiter = activiter();
            int size = sizeActiviter(activiter);
            model.addAttribute("activiter", activiter);
            model.addAttribute("size", size);
            model.addAttribute("offre", offre);
            model.addAttribute("admin", admin);
            model.addAttribute("page", page);
            model.addAttribute("page_head", page_head);
            model.addAttribute("path", path);

        return "/admin/interface/activites/detail";
    }

    @GetMapping("/admin/offres/{id}")
    public String offre(@PathVariable("id") Integer id, Model model) {
        try {
            Admin admin = user();
            Offre offre = serviceOffre.get(id);
            String page_head = "Offres";
            String page = "Offre : " + offre.getObjet();
            model.addAttribute("offre", offre);
            model.addAttribute("admin", admin);
            model.addAttribute("page", page);
            model.addAttribute("page_head", page_head);

        } catch (OffreNotFoundException e) {

        }
        List<Activiter> activiter = activiter();
        int size = sizeActiviter(activiter);
        model.addAttribute("activiter", activiter);
        model.addAttribute("size", size);

        return "/admin/interface/activites/offre";
    }


    @GetMapping("/admin/ajoutUSerForm")
    public String ajoutPerAndEnForm(Model model) {

        Personne personne = new Personne();
        model.addAttribute("personne", personne);
        Entreprise entreprise = new Entreprise();



        List<Activiter> activiter = activiter();
        int size = sizeActiviter(activiter);
        model.addAttribute("activiter", activiter);
        model.addAttribute("size", size);
        model.addAttribute("entreprise", entreprise);

        return "user/interface/new_user";
    }


    @GetMapping("/admin/list")
    public String litserEntrepriseAndPerso(Model model) {

        List<Entreprise> entrepriseList = serviceEntreprise.getAllEntreprise();
        model.addAttribute("entrepriseList", entrepriseList);



        Entreprise entreprise = new Entreprise();
        Personne personne = new Personne();
        List<Activiter> activiter = activiter();
        int size = sizeActiviter(activiter);
        model.addAttribute("activiter", activiter);
        model.addAttribute("size", size);
        model.addAttribute("entreprise", entreprise);
        model.addAttribute("personne", personne);

        return "admin/interface/list_Entreprise_Personne";
    }

    @PostMapping("/admin/savePA")
    public String save(@ModelAttribute("personne") Personne personne, @ModelAttribute("entreprise") Entreprise entreprise) {


        personne.setPassword(bCryptPasswordEncoder.encode(personne.getPassword()));
        servicePersonne.enregistrerPersonne(personne);
        Entreprise e = serviceEntreprise.enregistrerEntreprise(entreprise);

        Personne p = persoRepository.findPerso(personne.getEmail()).get(0);


        if (p != null) {

            e.setPersonne(p);
            serviceEntreprise.enregistrerEntreprise(e);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Admin admin = adminRepository.findUser(currentPrincipalName).get(0);
        Activiter activiter = new Activiter(admin.getNom(), "la creation d'entreprise de " + p.getNom(), new Date());

        serviceActiviter.enregistrerActiviter(activiter);

        return "redirect:/admin/list";
    }


    @GetMapping("/admin/updateEntrepriseForm/{id}")
    public String updateAdminEntreprise(@PathVariable(value = "id") Integer id, Model model) {
        // get Les Admin Apartir de service
        Entreprise entreprise = serviceEntreprise.getEntrepriseById(id);
        Personne personne = servicePersonne.finPersonne(entreprise.getPersonne().getEmail());

        List<Activiter> activiter = activiter();
        int size = sizeActiviter(activiter);
        model.addAttribute("activiter", activiter);
        model.addAttribute("size", size);
        model.addAttribute("entreprise", entreprise);
        model.addAttribute("personne", personne);
        return "admin/interface/update_entreprise";
    }


    @GetMapping("/admin/deleteAdminEntreprise/{id}")
    public String deleteAdminEntreprise(@PathVariable(value = "id") Integer id) {
        this.serviceEntreprise.deleteEntreprise(id);
        return "redirect:/admin/list";
    }


    @PostMapping("/admin/updatePA")
    public String updateEntreprise(@ModelAttribute("entreprise") Entreprise entreprise, @ModelAttribute("personne") Personne personne) {


        if (personneRepository.getById(personne.getId()).getPassword() != bCryptPasswordEncoder.encode(personne.getPassword()) && personne.getPassword() != "")
        {
            personne.setPassword(bCryptPasswordEncoder.encode(personne.getPassword()));
        }
        else{
            personne.setPassword(personneRepository.getById(personne.getId()).getPassword());
        }
        servicePersonne.enregistrerPersonne(personne);
        entreprise.setPersonne(personne);
        serviceEntreprise.enregistrerEntreprise(entreprise);

        Admin admin = user();
        Activiter activiter = new Activiter(admin.getNom(), "une modification d'entreprise de " + personne.getNom() + "et leur information ", new Date());

        serviceActiviter.enregistrerActiviter(activiter);

        return "redirect:/admin/list";
    }

    @GetMapping("/admin/profile")
    public String profileUser(Model model) {

        List<Activiter> activiter = activiter();
        int size = sizeActiviter(activiter);
        Admin admin = user();
        model.addAttribute("admin", admin);
        model.addAttribute("activiter", activiter);
        model.addAttribute("size", size);

        return "admin/interface/profile";
    }

    @PostMapping("/admin/update")
    public String updateAdmin(@ModelAttribute("admin") Admin admin) {


        Admin a = new Admin();
        a.setId(admin.getId());
        a.setPrenom(admin.getPrenom());
        a.setNom(admin.getNom());
        a.setEmail(admin.getEmail());
        Admin ad = user();
        if (ad.getPassword() != bCryptPasswordEncoder.encode(admin.getPassword()) && admin.getPassword() != ""){
            a.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
        }

        serviceAdmin.saveAdmin(a);

        return "redirect:/admin/profile";
    }


    @GetMapping("/admin/offres")
    public String litserOffre(Model model) {
        List<Offre> OffreList = serviceOffre.alloffres();
        Offre offre = new Offre();
        List<Activiter> activiter = activiter();
        int size = sizeActiviter(activiter);
        model.addAttribute("activiter", activiter);
        model.addAttribute("size", size);
        String page_head = "Activités";
        String page = "Offres";
        model.addAttribute("offreList", OffreList);
        model.addAttribute("offre", offre);
        model.addAttribute("page", page);
        model.addAttribute("page_head", page_head);

        return "admin/interface/activites/offres";
    }

    @GetMapping("/admin/offres/encours")
    public String offresEncours(Model model) {
        List<Offre> OffreList = serviceOffre.offreencours();
        Offre offre = new Offre();
        List<Activiter> activiter = activiter();
        int size = sizeActiviter(activiter);
        model.addAttribute("activiter", activiter);
        model.addAttribute("size", size);
        String page_head = "Activités";
        String page = "Offres En cours";
        model.addAttribute("size", size);
        model.addAttribute("activiter", activiter);
        model.addAttribute("offreList", OffreList);
        model.addAttribute("offre", offre);
        model.addAttribute("page", page);
        model.addAttribute("page_head", page_head);

        return "admin/interface/activites/offresencours";
    }

    @GetMapping("/admin/pdffile/{id}/offre.pdf")
    public void fileForm(@PathVariable("id") Integer id, Model m, HttpServletResponse r) {
        try {
            Offre offre = serviceOffre.get(id);
            serviceOffre.GeneratePdf(r, offre);
        } catch (OffreNotFoundException | IOException e) {
            e.printStackTrace();
        }


    }


    @GetMapping("/admin/ajoutOffreForm1")
    public String ajoutOffreForm1(Model model) {


        Offre offre = new Offre();
        model.addAttribute("offre", offre);
        Personne personne = new Personne();



        List<Activiter> activiter = activiter();
        int size = sizeActiviter(activiter);
        model.addAttribute("personne", personne);
        model.addAttribute("activiter", activiter);
        model.addAttribute("size", size);


        return "admin/interface/offreForm";
    }


    @GetMapping("/admin/toutesNotifications")
    public String litseNotification(Model model) {

        List<Activiter> activiter = activiter();
        int size = sizeActiviter(activiter);
        model.addAttribute("activiter", activiter);
        model.addAttribute("size", size);
        model.addAttribute("size", size);
        model.addAttribute("activiter", activiter);


        return "admin/interface/list_Notification";
    }

    @GetMapping("/admin/ajoutAdminForm")
    public String ajoutAdminForm(Model model) {

        Admin admin = new Admin();

        List<Activiter> activiter = activiter();
        int size = sizeActiviter(activiter);
        model.addAttribute("activiter", activiter);
        model.addAttribute("size", size);
        model.addAttribute("admin", admin);


        return "admin/interface/admin-register";
    }


    @PostMapping("/admin/saveAdmin")
    public String saveAdminD(@ModelAttribute("admin") Admin admin) {


        admin.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
        serviceAdmin.enregistrerAdmin(admin);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Admin admin1 = adminRepository.findUser(currentPrincipalName).get(0);
        Activiter activiter = new Activiter(admin1.getNom(), "la creation du compte de " + admin.getNom(), new Date());

        serviceActiviter.enregistrerActiviter(activiter);

        return "redirect:/admin/home";
    }


    @GetMapping("/admin/updateActiviter/{id}")
    public String updateActiviter(@PathVariable("id") Integer id, Model model) {

        Activiter activiter1 = serviceActiviter.getActiviteById(id);
        activiter1.setView(true);
        serviceActiviter.enregistrerActiviter(activiter1);

        model.addAttribute("activiter1", activiter1);
        List<Activiter> activiter = activiter();
        int size = sizeActiviter(activiter);
        model.addAttribute("activiter", activiter);
        model.addAttribute("size", size);

        return "admin/interface/notificationInfo";

    }



    @GetMapping("/admin/offres/accorder")
    public String offreAccorder(Model model){
        List<Offre> OffreList = serviceOffre.offreAccorder();
        Offre o = new Offre();
        List<Activiter> activiter = activiter();
        int size = sizeActiviter(activiter);


        for (Offre offre:OffreList) {
            long old = offre.getDateAttribution().getTime();
            long auj =new Date().getTime();


            offre.getDateAttribution().setMonth(offre.getDateAttribution().getMonth() + offre.getDelai());
            int difference_In_Time = (int) (offre.getDateAttribution().getTime() - old);
            long days_end =  auj-old;


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


        String page_head = "Activités";
        String page = "Offres Attribuer";
        model.addAttribute("activiter", activiter);
        model.addAttribute("size", size);
        model.addAttribute("offreList", OffreList);
        model.addAttribute("offre", o);
        model.addAttribute("page", page);
        model.addAttribute("page_head", page_head);
        return "admin/interface/activites/offreattribuer";

    }
    @GetMapping("/admin/offres/terminer")
    public String litserOffreTerminer(Model model) {
        List<Offre> OffreList = serviceOffre.offreTerminer();
        Offre offre = new Offre();
        List<Activiter> activiter = activiter();
        int size = sizeActiviter(activiter);

        String page_head = "Activités";
        String page = "Offres";
        model.addAttribute("activiter", activiter);
        model.addAttribute("size", size);
        model.addAttribute("offreList", OffreList);
        model.addAttribute("offre", offre);
        model.addAttribute("page", page);
        model.addAttribute("page_head", page_head);

        return "admin/interface/activites/offresTerminer";
    }


    @GetMapping("/admin/offres/terminer/{id}")
    public String personnePostiler(@PathVariable("id") Integer id, Model model) throws OffreNotFoundException {


        Admin admin = user();
        Offre offre = serviceOffre.get(id);
        List<Poster> ListPosterOffre = servicePoster.allOffrePoster(id);
        String page_head = "Offres";
        String page = "Offre : " + offre.getObjet();

        List<Activiter> activiter = activiter();
        int size = sizeActiviter(activiter);
        model.addAttribute("activiter", activiter);
        model.addAttribute("size", size);



        model.addAttribute("offre", offre);
        model.addAttribute("admin", admin);
        model.addAttribute("page", page);
        model.addAttribute("page_head", page_head);
        model.addAttribute("ListPosterOffre", ListPosterOffre);


        return "/admin/interface/activites/PersonnePostiler";
    }


    @GetMapping("/admin/offres/attribue/{id1}/{id2}")
    public String attribuerOffre(@PathVariable("id1") Integer id1, @PathVariable("id2") Integer id2, Model model) throws OffreNotFoundException {


        Admin admin = user();

        Entreprise entreprise = serviceEntreprise.getEntrepriseById(id1);
        Offre offre1 = serviceOffre.get(id2);
        offre1.setDateAttribution(new Date());
        offre1.setAttribue(entreprise);
        offre1.setEditeur(admin);
        serviceOffre.saveOffre(offre1);
        String page_head = "Offres";
        String page = "Offre : " + offre1.getObjet();
        List<Activiter> activiter = activiter();
        int size = sizeActiviter(activiter);
        model.addAttribute("activiter", activiter);
        model.addAttribute("size", size);


        model.addAttribute("offre", offre1);
        model.addAttribute("admin", admin);
        model.addAttribute("page", page);
        model.addAttribute("page_head", page_head);


        return "redirect:/admin/offres/terminer";
    }

    @RequestMapping("/admin/offre/file/{id}")
    @ResponseBody
    public void show(@PathVariable("id") Integer id, HttpServletResponse response) {

        Poster poster = servicePoster.get(id);

        String path = Environement.staticPath;
        String fileName = poster.getFile();
        String url = path + "\\" + poster.getOffre().getId() + "\\" + poster.getEntreprise().getPersonne().getId() + "\\" + poster.getFile();
        if (fileName.indexOf(".doc") > -1) response.setContentType("application/msword");
        if (fileName.indexOf(".docx") > -1) response.setContentType("application/msword");
        if (fileName.indexOf(".xls") > -1) response.setContentType("application/vnd.ms-excel");
        if (fileName.indexOf(".csv") > -1) response.setContentType("application/vnd.ms-excel");
        if (fileName.indexOf(".ppt") > -1) response.setContentType("application/ppt");
        if (fileName.indexOf(".pdf") > -1) response.setContentType("application/pdf");
        if (fileName.indexOf(".zip") > -1) response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setHeader("Content-Transfer-Encoding", "binary");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            FileInputStream fis = new FileInputStream(url);
            int len;
            byte[] buf = new byte[1024];
            while ((len = fis.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }
            bos.close();
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
