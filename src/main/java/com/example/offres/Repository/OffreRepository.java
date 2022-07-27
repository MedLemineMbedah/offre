package com.example.offres.Repository;


import com.example.offres.Model.Admin;
import com.example.offres.Model.Offre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OffreRepository extends JpaRepository<Offre, Integer> {
 // Requette findOffrePoster par un utilisateur specifique
    @Query("SELECT o FROM Offre o WHERE o.id IN (SELECT p.offre.id FROM Poster p WHERE p.entreprise.id = :entreprise)")
    public List<Offre> findOffrePoster(@Param("entreprise") Integer entreprise);
    // Requette findOffreNoPoster par un utilisateur specifique
    @Query("SELECT o FROM Offre o WHERE o.id NOT IN (SELECT p.offre.id FROM Poster p WHERE p.entreprise.id = :entreprise)")
    public List<Offre> findOffreNoPoster(@Param("entreprise") Integer entreprise);
    // Requette findOffreAttribuer par un utilisateur Specifique
    @Query("select o from Offre o where o.attribue.id= :entreprise and o.attribue.id != 'NULL'")
    public List<Offre> findOffreAttribuer(@Param("entreprise") Integer entreprise);
    // Requette Offre Attribuer
    @Query("select o from Offre o where o.attribue.id != 'NULL'")
    public List<Offre> offreAttribuer();
    // Requette findOffreNoAttribuer pou utilisateures specifique
    @Query("select o from Offre o,Poster p where o.attribue.id != :entreprise and o.attribue.id != 'Null' and p.offre.id=o.id and p.entreprise.id != :entreprise and o.id IN (SELECT p.offre.id FROM Poster p WHERE p.entreprise.id = :entreprise)")
    public List<Offre> findOffreNoAttribuer(@Param("entreprise") Integer entreprise);
    @Query("select o from Offre o WHERE o.dateFin >= :date")
    public List<Offre> offreEncours(@Param("date") Date date);

   @Query("select o from Offre o WHERE o.dateFin < :date and o.dateAttribution IS NULL")
   public List<Offre> offreTerminer(@Param("date") Date date);







}
