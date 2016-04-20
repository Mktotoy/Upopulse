package com.upo10.miage.upopulse.upobuildings;

import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Iterator;

/**
 * Created by siaydin on 05/04/2015.
 */
@DatabaseTable(tableName = "FLOOR")
public class FloorImpl implements Floor{
    @DatabaseField
    private int lvl;

    @DatabaseField(generatedId = true)
    private int idFloor;

    public FloorImpl(int lvl, BuildingImpl batiment, String nomFloor, String infoFloor, Integer idImageOverlay,float lng,float larg,LatLng centre) {
        this.lvl = lvl;
        this.batiment = batiment;
        this.nomFloor = nomFloor;
        this.infoFloor = infoFloor;
        IdImageOverlay = idImageOverlay;
        this.lng = lng;
        this.larg = larg;
        this.center = centre;
        this.latCenter = this.center.latitude;
        this.lngCenter = this.center.longitude;
    }

    FloorImpl(){

    }

    @DatabaseField(foreign = true)
    private BuildingImpl batiment;

    @Override
    public Iterator<RoomImpl> getRoomIterator() {
        return this.salles.iterator();
    }

    @DatabaseField
    private String nomFloor;

    @DatabaseField
    private Integer IdImageOverlay;

    @DatabaseField
    private String infoFloor;

    @DatabaseField
    private float lng;

    @DatabaseField
    private float larg;

    @DatabaseField
    private double latCenter;

    @DatabaseField
    private double lngCenter;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<RoomImpl> salles;

    private LatLng center;

    @Override
    public int getLvl() {
        return this.lvl;
    }

    @Override
    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    @Override
    public Building getBuilding() {
        return this.batiment;
    }

    @Override
    public void setBuilding(BuildingImpl b) {
        this.batiment = b;
    }

    @Override
    public String getNomFloor() {
        return this.nomFloor;
    }

    @Override
    public void setNomFloor(String n) {
        this.nomFloor = n;
    }

    @Override
    public Integer getIdImageOverlay() {
        return this.IdImageOverlay;
    }

    @Override
    public void setIdImageOverlay(Integer id) {
        this.IdImageOverlay = id;
    }

    @Override
    public String getInfoFloor() {
        return this.infoFloor;
    }

    @Override
    public void setInfoFloor(String info) {
        this.infoFloor = info;
    }

    @Override
    public float getLngImage() {
        return lng;
    }

    @Override
    public void setLngImage(float f) {
        this.lng = f;
    }

    @Override
    public float getLargImage() {
        return larg;
    }

    @Override
    public void setLargImage(float f) {
        this.larg = f;
    }

    public double getLatCenter() {
        return latCenter;
    }

    public void setLatCenter(double latCenter) {
        this.latCenter = latCenter;
    }

    public double getLngCenter() {
        return lngCenter;
    }

    public void setLngCenter(double lngCenter) {
        this.lngCenter = lngCenter;
    }

    public LatLng getCenter() {
        return center;
    }

    public void setCenter(LatLng center) {
        this.center = center;
    }

    public int getIdFloor(){return this.idFloor;}


    @Override
    public void initializer() {
        this.center = new LatLng(this.latCenter,this.lngCenter);
    }
}
