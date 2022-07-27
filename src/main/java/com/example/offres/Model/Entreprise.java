package com.example.offres.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
// Tableau Entreprise

@Entity
@Table(name = "entreprise")
public class Entreprise {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name="denomination")
    private String denomination;

    @Column(nullable = false,unique = false, name="immatriculation")
    private String immatriculation;

    @JsonIgnore
    @OneToMany(mappedBy="entreprise")
    private Set<Poster> postilerO;

    @JsonIgnore
    @OneToMany(mappedBy="attribue")
    private Set<Offre> offre;


    // Relation One to One avec Personne
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "gerant",
            joinColumns =
                    { @JoinColumn(name = "entreprise", referencedColumnName = "id") },
            inverseJoinColumns =
                    { @JoinColumn(name = "personne", referencedColumnName = "id") })
    private Personne personne;


    public Entreprise() {
    }

    public Entreprise(Integer id, String denomination, String immatriculation, Personne personne) {
        this.id = id;
        this.denomination = denomination;
        this.immatriculation = immatriculation;
        this.personne = personne;
    }

    public Entreprise(String denomination, String immatriculation, Personne personne) {
        this.denomination = denomination;
        this.immatriculation = immatriculation;
        this.personne = personne;
    }

    public Entreprise(String denomination, String immatriculation) {
        this.denomination = denomination;
        this.immatriculation = immatriculation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public Set<Poster> getPostilerO() {
        return postilerO;
    }

    public void setPostilerO(Set<Poster> postilerO) {
        this.postilerO = postilerO;
    }

    public Set<Offre> getOffre() {
        return offre;
    }

    public void setOffre(Set<Offre> offre) {
        this.offre = offre;
    }
}
