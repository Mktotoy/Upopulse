package com.upo10.miage.upopulse.upowbs;

import com.google.android.gms.maps.model.LatLng;
import com.upo10.miage.upopulse.upobuildings.RoomImpl;
import com.upo10.miage.upopulse.upoevent.Event;
import com.upo10.miage.upopulse.upopulse.HomePageActivity;
import com.upo10.miage.upopulse.upouser.User;
import com.upo10.miage.upopulse.upouser.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nelmojahid on 06/05/2015.
 */
public class RequestWebService
{
    private static final String url = "http://upo-pulse.esy.es/upopulse.php/";


    public void connectUser(String email, String pwd) throws IOException, JSONException
    {

        WebService w = new WebService();

        try
        {
            JSONObject json = w.requestWebService(url + "users/" + email + "/" + pwd);
            User u = User.getInstance();

            u.setId(json.getInt("utilisateur_id"));
            u.setNom(json.getString("utilisateur_nom"));
            u.setPrenom(json.getString("utilisateur_prenom"));
            u.setMail(json.getString("utilisateur_mail"));
            u.setDescription(json.getString("utilisateur_description"));
            u.setTelephone(json.getString("utilisateur_telephone"));
            u.setAdm(json.getInt("utilisateur_administrateur"));
            u.setToken(json.getString("utilisateur_token"));

        } catch (JSONException e) {
            // manage exceptions
        }
    }

    public String createUser(String name, String firstname, String email, String pwd, String description)
    {
        WebService w = new WebService();

        String parametre = "nom=" + name + "&prenom=" + firstname + "&mail=" + email + "&password=" + pwd + "&description=" + description;

        return w.postRequest(url + "users", parametre);
    }

    public String updateUser(String name, String firstname, String email, String description, String pwd, String telephone, String token)
    {
        WebService w = new WebService();

        String parametre = "nom=" + name + "&prenom=" + firstname + "&mail=" + email + "&password=" + pwd + "&description=" + description + "&telephone=" + telephone + "&token=" + token;

        return w.putRequest(url + "users", parametre);
    }

    public List<Users> getAllUsers()
    {
        WebService w = new WebService();
        List<Users> u = new LinkedList<>();

        try
        {
            JSONObject json = w.requestWebService(url + "users");

            JSONArray items = json.getJSONArray("users");

            for (int i = 0; i < items.length(); i++)
            {
                JSONObject obj = items.getJSONObject(i);
                if(obj.getInt("utilisateur_administrateur")<1) u.add(new Users(obj.getString("utilisateur_nom"),obj.getString("utilisateur_prenom"), obj.getString("utilisateur_mail"), obj.getInt("utilisateur_administrateur")));
            }
            return u;

        } catch (JSONException e) {
            // manage exceptions
        }

        return u;
    }

    public String changeDroitUsers(String mail, String token, int droit)
    {
        WebService w = new WebService();

        return w.putRequest(url + "users/habilitation/" + mail, "habilitation=" + droit + "&token=" + token);
    }

    public String createPartageLocationCode(String latitude, String longitude)
    {
        WebService w = new WebService();
        String parametre = "latitude=" + latitude + "&longitude=" + longitude;

        return w.postRequest(url + "partage", parametre);
    }

    public LatLng getPartageLocationCode(String query){

        WebService w = new WebService();
        LatLng newLatLngUser = null;
        Double lat,lng;

        try
        {
            JSONObject json = w.requestWebService(url + "partage/" + query);

            if(json.getString("partage_id").equals(query)){
                lat = Double.parseDouble(json.getString("partage_latitude"));
                lng = Double.parseDouble(json.getString("partage_longitude"));

                newLatLngUser = new LatLng(lat,lng);
            }
        } catch (JSONException e) {
            // manage exceptions
        }

        return newLatLngUser;

    }

    public ArrayList<Event> getAllEvents() throws JSONException, ParseException, SQLException {
        WebService w = new WebService();
        JSONObject json = w.requestWebService(url + "evenements");
        JSONArray lstJsonEvents = json.getJSONArray("evenements");
        ArrayList<Event> lstEvents = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        for (int i = 0; i < lstJsonEvents.length(); i++) {
            JSONObject c = lstJsonEvents.getJSONObject(i);

            int idEvent = Integer.parseInt(c.getString("evenement_id"));
            Date dateDebut = formatter.parse(c.getString("evenement_date_debut"));
            Date dateFin = formatter.parse(c.getString("evenement_date_fin"));

            User creaEvent = new User();
            creaEvent.setNom(c.getString("utilisateur_nom"));
            creaEvent.setPrenom(c.getString("utilisateur_nom"));
            creaEvent.setMail(c.getString("utilisateur_mail"));
            creaEvent.setDescription(c.getString("utilisateur_description"));

            int idLieu = Integer.parseInt(c.getString("lieu_id"));
            RoomImpl lieu = HomePageActivity.DBHELPER.getRoomImplDao().queryForId(idLieu);

            Event e = new Event(
                    idEvent,
                    c.getString("evenement_nom"),
                    dateDebut,
                    dateFin,
                    c.getString("evenement_description"),
                    lieu,
                    creaEvent,
                    c.getString("evenement_image")
            );
            lstEvents.add(e);
        }
        return lstEvents;
    }
}
