package com.example.offres.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

//tableau Offre
@Entity
@Table(name = "offre")
// table offer
public class Offre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String objet;
    @Column(nullable = false)
    private double montant;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datePublication;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateFin;

    @Column(nullable = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateAttribution;

    @Column(nullable = false)
    private int delai;

    @Column(nullable = false)
    private String description;


    @JsonIgnore
    @OneToMany(mappedBy="offre")
    private Set<Poster> postilerE;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn (name="editeur", referencedColumnName="id")
    private Admin editeur;


    @ManyToOne
    @JoinColumn(name="attribue")
    private Entreprise attribue;


    private String status;

    public Offre() {
    }

    public Offre(String objet, double montant, Date datePublication, Date dateFin, Date dateAttribution, int delai, String description, Set<Poster> postilerE, Admin editeur, Entreprise attribue) {
        this.objet = objet;
        this.montant = montant;
        this.datePublication = datePublication;
        this.dateFin = dateFin;
        this.dateAttribution = dateAttribution;
        this.delai = delai;
        this.description = description;
        this.postilerE = postilerE;
        this.editeur = editeur;
        this.attribue = attribue;
    }


    public Offre(String objet, double montant, Date datePublication, int delai, String description,Date dateFin) {
        this.objet = objet;
        this.montant = montant;
        this.datePublication = datePublication;
        this.delai = delai;
        this.description = description;
        this.dateFin= dateFin;
    }
    public Integer getId() {
        return id;
    }

    public Offre(Integer id, String objet, double montant, Date datePublication, Date dateFin, Date dateAttribution, int delai, String status) {
        this.id = id;
        this.objet = objet;
        this.montant = montant;
        this.datePublication = datePublication;
        this.dateFin = dateFin;
        this.dateAttribution = dateAttribution;
        this.delai = delai;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Date getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Date datePublication) {
        this.datePublication = datePublication;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Date getDateAttribution() {
        return dateAttribution;
    }

    public void setDateAttribution(Date dateAttribution) {
        this.dateAttribution = dateAttribution;
    }

    public int getDelai() {
        return delai;
    }

    public void setDelai(int delai) {
        this.delai = delai;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Poster> getPostilerE() {
        return postilerE;
    }

    public void setPostilerE(Set<Poster> postilerE) {
        this.postilerE = postilerE;
    }

    public Admin getEditeur() {
        return editeur;
    }

    public void setEditeur(Admin editeur) {
        this.editeur = editeur;
    }

    public Entreprise getAttribue() {
        return attribue;
    }

    public void setAttribue(Entreprise attribue) {
        this.attribue = attribue;
    }

    @Override
    public String toString() {
        return "Offre{" +
                "id=" + id +
                ", objet='" + objet + '\'' +
                ", montant=" + montant +
                ", datePublication=" + datePublication +
                ", dateFin=" + dateFin +
                ", dateAttribution=" + dateAttribution +
                ", delai=" + delai +
                ", description='" + description + '\'' +
                ", postilerE=" + postilerE +
                ", editeur=" + editeur +
                ", attribue=" + attribue +
                '}';
    }


}
