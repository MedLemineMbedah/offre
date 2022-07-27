package com.example.offres.Model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "activiter")
public class Activiter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String user;

    @Column
    private Boolean view;

    @Column(nullable = false)
    private String activiter;


    @Column(nullable = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    public Activiter() {
    }

    public Activiter(String user, String activiter, Date date) {
        this.user = user;
        this.activiter = activiter;
        this.date = date;
        this.view=false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getActiviter() {
        return activiter;
    }

    public void setActiviter(String activiter) {
        this.activiter = activiter;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }
}
