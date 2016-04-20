package com.upo10.miage.upopulse.upobuildingtools;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.model.CameraPosition;
import com.upo10.miage.upopulse.upobuildings.Building;
import com.upo10.miage.upopulse.upobuildings.BuildingImpl;
import com.upo10.miage.upopulse.upobuildings.FloorImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siaydin on 04/04/2015.
 */
public class FloorBtnBuilderImpl implements FloorBtnBuilder {

    private Building currentBuilding;

    @Override
    public List<FloorButton> getFloorBtn(CameraPosition c, Context ctt, List<BuildingImpl> lstBatiments) {
        List<FloorButton> myBtns = new ArrayList<>();
        //on sélectionne tous les batiments de la base de données
        List<BuildingImpl> listBatiments = lstBatiments;
        //Pour chaque batiment, on teste s'il est au centre de la caméra et s'il l'est on instancie ses boutons
        FloorButton fl = null;
        FloorImpl etage = null;
        for (BuildingImpl bat : listBatiments) {
            if (bat.isOnCenter(c)) {
                currentBuilding = bat;
                //le batiment est au centre de la caméra
                //Pour chaque etage on, on initialise ses attributs et on crée un bouton qu'on ajoute à la liste myBtns
                for (int i = 0; i < bat.getNbFloor(); i++) {
                    etage = (FloorImpl) bat.getFloor(i);
                    etage.initializer();
                    fl = new FloorButton(etage.getCenter(), etage.getLngImage(), etage.getLargImage(), bat.getFloorOverlayID(i), ctt, etage);
                    fl.setText(etage.getNomFloor());
                    myBtns.add(fl);
                }
                //on affiche un message à l'écran pour indiquer le batiment détecté
                //Toast.makeText(ctt,bat.getInfoBuilding(),Toast.LENGTH_SHORT).show();

            }
        }
        return myBtns;
    }

    @Override
    public Building getCurrentBuilding() {
        return currentBuilding;
    }
}
