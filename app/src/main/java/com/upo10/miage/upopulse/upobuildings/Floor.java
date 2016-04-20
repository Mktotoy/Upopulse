package com.upo10.miage.upopulse.upobuildings;

import com.google.android.gms.maps.model.LatLng;

import java.util.Iterator;

/**
 * Created by siaydin on 05/04/2015.
 */
public interface Floor {

    public int getLvl();
    public void setLvl(int lvl);

    public Building getBuilding();
    public void setBuilding(BuildingImpl b);

    public String getNomFloor();
    public void setNomFloor(String n);

    public Integer getIdImageOverlay();
    public void setIdImageOverlay(Integer id);

    public String getInfoFloor();
    public void setInfoFloor(String info);

    public float getLngImage();
    public void setLngImage(float f);

    public float getLargImage();
    public void setLargImage(float f);

    public double getLngCenter();
    public void setLngCenter(double p);

    public double getLatCenter();
    public void setLatCenter(double p);

    public LatLng getCenter();
    public void setCenter(LatLng latlng);

    public Iterator<RoomImpl> getRoomIterator();

    public void initializer();

    public int getIdFloor();
}
