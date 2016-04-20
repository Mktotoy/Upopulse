package com.upo10.miage.upopulse.upobuildings;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by siaydin on 08/05/2015.
 */
public interface Room {
    public String getNomRoom();
    public void setNomRoom(String n);
    public String getInfoRoom();
    public void setInfoRoom(String n);
    public Marker getMarkerRoom();
    public void setMarkerRoom(Marker m);
    public float getLatRoom();
    public void setLatRoom(float l);
    public float getLngRoom();
    public void setLngRoom(float l);
    public LatLng getCenter();
    public FloorImpl getEtage();
    public void setEtage(FloorImpl fl);
    public int getIdRoom();
    public void setIdRoom(int i);

}
