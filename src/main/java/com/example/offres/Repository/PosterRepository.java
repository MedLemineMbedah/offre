package com.example.offres.Repository;


import com.example.offres.Model.Offre;
import com.example.offres.Model.Personne;
import com.example.offres.Model.Poster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PosterRepository extends JpaRepository<com.example.offres.Model.Poster, Integer> {

    @Query("select o from Offre o WHERE o.dateFin >= :date")
    public List<Personne> offreEncours(@Param("date") Date date);

    @Query("select o from Poster o WHERE o.offre.id = :id")
    public List<Poster> offrePoster(@Param("id") Integer id);



}
