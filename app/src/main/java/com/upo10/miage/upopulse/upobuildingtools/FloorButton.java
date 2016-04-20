package com.upo10.miage.upopulse.upobuildingtools;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.upo10.miage.upopulse.upobuildings.Floor;
import com.upo10.miage.upopulse.upobuildings.FloorImpl;
import com.upo10.miage.upopulse.upobuildings.Room;
import com.upo10.miage.upopulse.upobuildings.RoomImpl;
import com.upo10.miage.upopulse.upopulse.HomePageActivity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by siaydin on 01/04/2015.
 */
public class FloorButton extends Button implements View.OnClickListener {


    private LatLng floor;
    private float longueur;
    private float largeur;
    private int floorName;
    private Floor currentFloor;
    private boolean markerOnMap = false;

    public FloorButton(LatLng floor, float longueur, float largeur, int floorName, Context context, Floor fl) {
        super(context);
        this.initializer(floor, longueur, largeur, floorName, fl);
    }

    public LatLng getFloor() {
        return floor;
    }

    public void setFloor(LatLng floor) {
        this.floor = floor;
    }

    public float getLongueur() {
        return longueur;
    }

    public void setLongueur(float longueur) {
        this.longueur = longueur;
    }

    public float getLargeur() {
        return largeur;
    }

    public void setLargeur(float largeur) {
        this.largeur = largeur;
    }

    public int getFloorName() {
        return floorName;
    }

    public void setFloorName(int floorName) {
        this.floorName = floorName;
    }

    /**
     * Initialise tous les attributs d'un bouton
     *
     * @param floor     Centre de l'image du plan de l'étage
     * @param longueur  longueur de l'image
     * @param largeur   largeur de l'image
     * @param floorName l'id de l'image
     */
    public void initializer(LatLng floor, float longueur, float largeur, int floorName, Floor fl) {
        this.floor = floor;
        this.longueur = longueur;
        this.largeur = largeur;
        this.floorName = floorName;
        setOnClickListener(this);
        this.currentFloor = fl;
    }

    @Override
    public void onClick(View v) {
        HomePageActivity.mMap.getFocusedBuilding();
        HomePageActivity.buildFloor(HomePageActivity.mMap, this.floor, this.longueur, this.largeur, this.floorName);
        HomePageActivity.setCurrentFloor((FloorImpl) this.currentFloor);

        //Gestion des markers: affichage des markes pour l'étage courrant.
        if(this.currentFloor != null) {
            //on récupère l'iterator des salles
            Iterator<RoomImpl> itSalle = this.currentFloor.getRoomIterator();
            Room r;
            if(markerOnMap)
                while (itSalle.hasNext()) {
                    //pour chaque salle on itère et on ajoute un marker à la map
                    r = itSalle.next();
                    HashMap<String,Object> datas = new HashMap<>();
                    datas.put("pos",r.getCenter());
                    datas.put("title",r.getNomRoom());
                    HomePageActivity.addMarker(datas);
                }
        }
    }

}
