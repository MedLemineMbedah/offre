package com.example.offres.Repository;

import com.example.offres.Model.Admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    @Query("SELECT u FROM Admin u  WHERE u.email = :username ")
    public List<Admin> findUser(@Param("username") String username);



}
