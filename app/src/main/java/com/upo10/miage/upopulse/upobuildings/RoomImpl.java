package com.upo10.miage.upopulse.upobuildings;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by siaydin on 08/05/2015.
 */
@DatabaseTable(tableName = "ROOM")
public class RoomImpl implements Room{
    @DatabaseField
    private float latRoom;
    @DatabaseField
    private float lngRoom;
    private Marker markerRoom;
    @DatabaseField
    private String nomRoom;
    @DatabaseField
    private String infoRoom;

    public RoomImpl() {
    }

    @DatabaseField(generatedId = true)
    private int idRoom;

    @Override
    public int getIdRoom() {
        return idRoom;
    }

    @Override
    public void setIdRoom(int idRoom) {
        this.idRoom = idRoom;
    }

    @DatabaseField(foreign = true)

    private FloorImpl etage;

    @Override
    public String getInfoRoom() {
        return infoRoom;
    }

    @Override
    public LatLng getCenter() {
        return new LatLng(this.latRoom,this.lngRoom);
    }

    @Override
    public void setInfoRoom(String infoRoom) {
        this.infoRoom = infoRoom;
    }

    @Override
    public float getLatRoom() {
        return latRoom;
    }

    public void setLatRoom(float latRoom) {
        this.latRoom = latRoom;
    }

    @Override
    public float getLngRoom() {
        return lngRoom;
    }

    public void setLngRoom(float lngRoom) {
        this.lngRoom = lngRoom;
    }

    @Override
    public Marker getMarkerRoom() {
        return markerRoom;
    }

    @Override
    public void setMarkerRoom(Marker markerRoom) {
        this.markerRoom = markerRoom;
    }


    @Override
    public void setNomRoom(String nomRoom) {
        this.nomRoom = nomRoom;
    }

    public RoomImpl(String nomRoom, float lngRoom, float latRoom, String infoRoom,FloorImpl fl) {
        this.nomRoom = nomRoom;
        this.lngRoom = lngRoom;
        this.latRoom = latRoom;
        this.infoRoom = infoRoom;
        this.etage = fl;
    }

    @Override
    public FloorImpl getEtage() {

        return etage;
    }

    public void setEtage(FloorImpl etage) {
        this.etage = etage;
    }

    @Override
    public String getNomRoom() {
        return nomRoom;
    }
}
