package com.example.offres.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


@Entity
@Table(name = "poster")
public class Poster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn (name="offre")
    private Offre offre;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn (name="entreprise")
    private Entreprise entreprise;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String file;

    public Poster() {
    }

    public Poster(Integer id, Offre offre, Entreprise entreprise, String description, String file) {
        this.id = id;
        this.offre = offre;
        this.entreprise = entreprise;
        this.description = description;
        this.file = file;
    }

    public Poster(Offre offre, Entreprise entreprise, String description, String file) {
        this.offre = offre;
        this.entreprise = entreprise;
        this.description = description;
        this.file = file;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Offre getOffre() {
        return offre;
    }

    public void setOffre(Offre offre) {
        this.offre = offre;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "Postiler{" +
                "id=" + id +
                ", offre=" + offre +
                ", entreprise=" + entreprise +
                ", description='" + description + '\'' +
                ", file='" + file + '\'' +
                '}';
    }
}
