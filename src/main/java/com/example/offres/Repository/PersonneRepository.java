package com.example.offres.Repository;

import com.example.offres.Model.Admin;
import com.example.offres.Model.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonneRepository extends JpaRepository<Personne, Integer> {

@Query("SELECT u FROM Personne u  WHERE u.email = :email ")
public List<Personne> findPerso(@Param("email") String email);

}
