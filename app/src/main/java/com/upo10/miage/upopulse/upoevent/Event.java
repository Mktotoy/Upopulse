package com.upo10.miage.upopulse.upoevent;

import android.os.Parcel;
import android.os.Parcelable;

import com.upo10.miage.upopulse.upobuildings.FloorImpl;
import com.upo10.miage.upopulse.upobuildings.RoomImpl;
import com.upo10.miage.upopulse.upouser.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by emmjavay on 06/05/2015.
 */
public class Event
{

    private int id;
    private String nom;
    private Date dateDebut;
    private Date dateFin;
    private String description;
    private String image;

    private RoomImpl emplacement;
    private User userResp;

    public Event(int id,String nom,Date dateDebut,Date dateFin, String description, RoomImpl emplacement, User userResp,String image){
        this.id = id;
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
        this.emplacement = emplacement;
        this.userResp = userResp;
        this.image = image;
    }

    public int getId() { return id; }
    public String getNom() {
        return nom;
    }
    public Date getDateDebut() {
        return dateDebut;
    }
    public Date getDateFin(String prenom) {
        return dateFin;
    }
    public String getDescription() {
        return description;
    }
    public User getUserResp(){ return userResp; }
    public RoomImpl getEmplacement() { return emplacement; }

    public void setId(int id){ this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setDateDebut (Date dateDebut) { this.dateDebut = dateDebut; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
    public void setDescription(String description) { this.description = description; }
    public void setUserResp(User userResp) { this.userResp = userResp; }
    public void setEmplacement(RoomImpl emplacement) { this.emplacement = emplacement;}


    public String getDateDebutFormated() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return df.format(this.dateDebut);
    }

    public String getDateFinFormated() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return df.format(this.dateFin);
    }
}
