package com.example.offres;

import com.example.offres.Model.*;
import com.example.offres.Repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class OffresApplicationTests {

	@Test
	void contextLoads() {
	}
	@Autowired
	private AdminRepository repoAdmin;

	@Autowired
	private OffreRepository repoOffre;

	@Autowired
	private EntrepriseRepository entrepriseRepository;

	@Autowired
	private PersonneRepository personneRepository;

	@Autowired
	private PosterRepository posterRepository;


	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

 // Ajouter Administrateur Test
	@Test
	public void testAddNew(){
		Admin admin = new Admin();
		admin.setEmail("admin@gmail.com");
		admin.setNom("med");
		admin.setPrenom("lemine");
		admin.setPassword(bCryptPasswordEncoder.encode("password"));
		//repoAdmin.enregistrerAdmin(admin);


		Admin savedUser = repoAdmin.save(admin);
	}


	@Test
	public void testAddNewOffre(){


		Admin admin = repoAdmin.findAll().get(0);

		Offre offre = new Offre();


		offre.setEditeur(admin);
		offre.setDelai(13);
		offre.setMontant(8000000);

		offre.setDateAttribution(new Date());
		offre.setDateFin(new Date());
		offre.setDatePublication(new Date());

		offre.setObjet("Test 2");
		offre.setDescription("hello World test2");

//		offre.get
		repoOffre.save(offre);


	}

	// Suprimer les contenu de tous les table
	@Test
	public void testdelete(){
		repoAdmin.deleteAll();
		personneRepository.deleteAll();
		repoOffre.deleteAll();
		entrepriseRepository.deleteAll();

	}

	@Test
	public void poster(){
       Offre offre = repoOffre.findAll().get(0);
	   Personne p = personneRepository.findAll().get(0);
		Poster poster = new Poster();

	   poster.setDescription("Test 1");
	   poster.setFile("/static/file/1-1/file1.pdf");
	   poster.setOffre(offre);
	   poster.setEntreprise(p.getEntreprise());
	   posterRepository.save(poster);


	}


	public void attribuer(Offre o, Entreprise p){
		o.setAttribue(p);
		repoOffre.save(o);
	}


	// Tester les requettes
	@Test
	public void testfindOffre(){
		Personne p = personneRepository.findAll().get(0);


		List<Offre> offre = repoOffre.findOffrePoster(p.getEntreprise().getId());
		attribuer(offre.get(0),p.getEntreprise());
		List<Offre> offrea = repoOffre.findOffreAttribuer(p.getEntreprise().getId());


		for (Offre of:offrea
			 ) {
			System.out.println("Objectif :::: "+of.getObjet()+"ofre Admin"+of.getEditeur().getNom());
		}
	}

	@Test
	public void testdatebetween(){
		List<Offre> offreuser = repoOffre.findAll();
		for (Offre offre:offreuser) {
			long old = offre.getDateAttribution().getTime();
			long auj =new Date().getTime();


			offre.getDateAttribution().setMonth(offre.getDateAttribution().getMonth() + offre.getDelai());
			int difference_In_Time = (int) (offre.getDateAttribution().getTime() - old);
			int days_end = (int) (auj-old);

			long diffInjours = TimeUnit.MILLISECONDS.toDays(difference_In_Time);
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


	}

	@Test
	public void postiler(){
		Offre offre = repoOffre.findById(2).get();
		System.out.println("Size :::"+offre.getPostilerE().size());
		for (Poster p:offre.getPostilerE()) {
			System.out.println("user :"+p.getEntreprise().getPersonne().getNom()+" - Entreprsise : "+p.getEntreprise().getDenomination()+" - Description : "+p.getDescription());
		}
	}



  // Tester Relation entre Personne et Entreprise
	@Test
	public void relationPersonneEntreprise(){
		Entreprise e = new Entreprise();
		Personne p = new Personne();
		p.setNom("Salif test");
		p.setPrenom("Sow");
		p.setEmail("salif@mail.com");
		p.setPassword(bCryptPasswordEncoder.encode("password"));
		p.setAdresse("SOKOK1");
		p.setTelephone("22232332");
		personneRepository.save(p);



		e.setDenomination("Syskat");
		e.setImmatriculation("2123F27");

		entrepriseRepository.save(e);
        Personne test = personneRepository.findById(p.getId()).get();


		e.setPersonne(test);

		entrepriseRepository.save(e);

	}



	@Test
	public void getOffres(){
		ArrayList<List> list = new ArrayList<>();
		ArrayList<String> offres = new ArrayList<>();
		List<Offre> alloffre = repoOffre.findAll();
		Personne personne = personneRepository.findById(1).get();
		String page = "Offres";
		Boolean trouver = false;
		for (Offre offre:alloffre) {
			if (offre.getAttribue() == null) {
				for (Poster poster:offre.getPostilerE()) {
					if (poster.getEntreprise() == personne.getEntreprise()){
						trouver = true;
					}
					if (trouver){

						offres.add(0,offre.getId().toString());
						offres.add(1,offre.getObjet());
						offres.add(2,offre.getDateAttribution().toString());
						offres.add(3,offre.getDateFin().toString());
						offres.add(4,String.valueOf(offre.getDelai()));
						offres.add(5,String.valueOf(offre.getMontant()));
						offres.add(6,"Ok");
					}
					else{
						offres.add(0,offre.getId().toString());
						offres.add(1,offre.getObjet());
						offres.add(2,offre.getDateAttribution().toString());
						offres.add(3,offre.getDateFin().toString());
						offres.add(4,String.valueOf(offre.getDelai()));
						offres.add(5,String.valueOf(offre.getMontant()));
						offres.add(6,"Pas encours");
					}
				}
			}
			else{
				if (offre.getAttribue() == personne.getEntreprise()){
					offres.add(0,offre.getId().toString());
					offres.add(1,offre.getObjet());
					offres.add(2,offre.getDateAttribution().toString());
					offres.add(3,offre.getDateFin().toString());
					offres.add(4,String.valueOf(offre.getDelai()));
					offres.add(5,String.valueOf(offre.getMontant()));
					offres.add(6,"Success");
				}
				else{
					offres.add(0,offre.getId().toString());
					offres.add(1,offre.getObjet());
					offres.add(2,offre.getDateAttribution().toString());
					offres.add(3,offre.getDateFin().toString());
					offres.add(4,String.valueOf(offre.getDelai()));
					offres.add(5,String.valueOf(offre.getMontant()));
					offres.add(6,"Autre");
				}
			}

		}
		for (int i=0;i<offres.size();i++){
			int j=0;
			System.out.println(offres.get(i));
		}
	}





}



