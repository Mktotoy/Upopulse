package com.upo10.miage.upopulse.upouser;

import android.graphics.drawable.Drawable;

/**
 * Created by Sonatines on 26/05/2015.
 */
public class Users
{
    private String nom, prenom, mail;
    private int adm, id;
    Drawable picture;

    public Users(String nom, String prenom, String mail, int adm) {
        this.nom = nom;
        this.prenom = prenom;
        this.adm = adm;
        this.mail = mail;
    }



    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getAdm() {
        return adm;
    }

    public void setAdm(int adm) {
        this.adm = adm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Drawable getPicture() {
        return picture;
    }

    public void setPicture(Drawable picture) {
        this.picture = picture;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
