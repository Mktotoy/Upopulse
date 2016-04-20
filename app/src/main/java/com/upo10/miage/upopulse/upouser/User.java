package com.upo10.miage.upopulse.upouser;

/**
 * Created by nelmojahid on 06/05/2015.
 */
public class User
{
    private static User instance;
    private String nom, prenom, mail, telephone, description, token;
    private int adm, id;

    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getAdm() { return adm; }

    public void setAdm(int adm) {
        this.adm = adm;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }


    public static synchronized User getInstance(){
        if(instance==null){
            instance=new User();
        }
        return instance;
    }
}
