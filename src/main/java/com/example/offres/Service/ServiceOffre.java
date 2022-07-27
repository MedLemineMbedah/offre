package com.example.offres.Service;

import com.example.offres.Exception.OffreNotFoundException;
import com.example.offres.Model.Admin;
import com.example.offres.Model.Offre;
import com.example.offres.Repository.AdminRepository;
import com.example.offres.Repository.OffreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceOffre {
    @Autowired
    private OffreRepository offreRepository;

    public void saveOffre(Offre offre){
        this.offreRepository.save(offre);
    }


    public List<Offre> offreattribuer(Integer id){

        return offreRepository.findOffreAttribuer(id);
    }
    public List<Offre> offrenoattribuer(Integer id){

        return offreRepository.findOffreNoAttribuer(id);
    }
    public List<Offre> offrePoster(Integer id){

        return offreRepository.findOffrePoster(id);
    }
    public List<Offre> offreNoPoster(Integer id){

        return offreRepository.findOffreNoPoster(id);
    }
    public List<Offre> alloffres(){

        return offreRepository.findAll();
    }

    public List<Offre> offreTerminer(){
        Date date = new Date();
        return offreRepository.offreTerminer(date);
    }
    public List<Offre> offreAccorder(){
        return offreRepository.offreAttribuer();
    }

    public List<Offre> offreencours(){
         Date date = new Date();
        return offreRepository.offreEncours(date);
    }

    public Offre get(Integer id) throws OffreNotFoundException {
        Optional<Offre> resultat = offreRepository.findById(id);
        if (resultat.isPresent()){
              return resultat.get();
        }
        throw new OffreNotFoundException("Erreur de trouver l'offre");

    }

    public void GeneratePdf(HttpServletResponse response,Offre offre) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);




        Paragraph title = new Paragraph(offre.getObjet(), fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontInfo = FontFactory.getFont(FontFactory.HELVETICA);
        fontInfo.setSize(13);

        Paragraph dateD = new Paragraph("date Publication: "+affichDate(offre.getDatePublication()), fontInfo);
        dateD.setAlignment(Paragraph.ALIGN_LEFT);

        Paragraph dateF = new Paragraph("date Fin: "+affichDate(offre.getDateFin()), fontInfo);
        dateF.setAlignment(Paragraph.ALIGN_LEFT);

        Paragraph delai = new Paragraph("Delai: "+offre.getDelai(), fontInfo);
        delai.setAlignment(Paragraph.ALIGN_LEFT);

        Paragraph montant = new Paragraph("Motant: "+offre.getMontant(), fontInfo);
        montant.setAlignment(Paragraph.ALIGN_LEFT);


        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(12);

        Paragraph description = new Paragraph(offre.getDescription(), fontParagraph);
        description.setAlignment(Paragraph.ALIGN_LEFT);
        description.setSpacingAfter(50);
        description.setSpacingBefore(5);



        Paragraph editeur = new Paragraph("Editeur: "+offre.getEditeur().getNom()+" "+offre.getEditeur().getPrenom() , fontInfo);
        editeur.setAlignment(Paragraph.ALIGN_RIGHT);

        document.add(title);
        document.add(dateD);
        document.add(dateF);
        document.add(delai);

        document.add(description);
        document.add(editeur);

        document.close();
    }

    String affichDate(Date date){
        return (date.getDay() /10 < 1  ? "0"+date.getDay() : date.getDay())+ "-"+
                (date.getMonth() /10 < 1  ? "0"+date.getMonth() : date.getMonth())+"-"+
                (date.getYear()+1900);
    }


}
