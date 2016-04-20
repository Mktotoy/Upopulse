package com.upo10.miage.upopulse.upobuildings;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

/**
 * Created by siaydin on 05/04/2015.
 */
public interface Building {

    /***
     * Indique si le batiment se trouve au centre de l'écran
     * @param cam Une instance de CameraPosition
     * @return true si centré sur la carte false sinon
     */
    public boolean isOnCenter(CameraPosition cam);

    /***
     * Indique le nombre d'étage dans le batîment
     * @return le nombre d'étage dans le batîment
     */
    public int getNbFloor();

    /***
     * Renvoi l'id de l'image de l'étage
     * @param floorLVL l'étage: 0,1,2,3,4,5,6,etc...
     * @return l'id de l'image de l'étage
     */
    public Integer getFloorOverlayID(int floorLVL);

    public String getNomBuilding();
    public void setNomBuilding(String n);

    public List<Floor> getLstFloor();
    public void setLstFloor(List<Floor> lst);

    public Floor getFloor(int lvl);
    public Floor setFloor(Floor fl);

    public String getInfoBuilding();
    public String setInfoBuilding(String infos);

    public LatLngBounds getLatLngBoundsBuilding();
    public void setLatLngBoundsBuilding(LatLngBounds latlng);

    public LatLng getNorthEast();
    public void setNorthEast(LatLng p);

    public double getLatNorthEast();
    public void getLatNorthEast(double p);

    public double getLngNorthEast();
    public void getLngNorthEast(double p);

    public LatLng getSouthWest();
    public void setSouthWest(LatLng p);

    public double getLatSouthWest();
    public void setLatSouthWest(double p);

    public double getLngSouthWest();
    public void setLngSouthWest(double p);

    public double getLngCenter();
    public void setLngCenter(double p);

    public double getLatCenter();
    public void setLatCenter(double p);

    public LatLng getCenter();
    public void setCenter(LatLng latlng);

    public void initializer();
}
