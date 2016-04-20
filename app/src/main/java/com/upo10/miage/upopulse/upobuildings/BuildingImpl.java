package com.upo10.miage.upopulse.upobuildings;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Iterator;
import java.util.List;

/**
 * Created by siaydin on 05/04/2015.
 */
@DatabaseTable(tableName = "BUILDING")
public class BuildingImpl implements Building {

    @DatabaseField(id = true)
    private String nomBuilding;


    private List<Floor> lstFloor;


    private LatLng northEast;

    private LatLng southWest;
    @DatabaseField
    private String infoBuilding;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<FloorImpl> etages;

    private LatLngBounds latLngBoundsBuilding;

    @DatabaseField
    private double latNorthEast;
    @DatabaseField
    private double lngNorthEast;

    @DatabaseField
    private double latSouthWest;
    @DatabaseField
    private double lngSouthWest;

    @DatabaseField
    private double latCenter;

    @DatabaseField
    private double lngCenter;

    private LatLng center;

    private int nbFloor;

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


    public BuildingImpl(String nomBuilding, LatLng northEast, LatLng southWest, String infoBuilding,LatLng center) {
        this.nomBuilding = nomBuilding;
        this.northEast = northEast;
        this.southWest = southWest;
        this.infoBuilding = infoBuilding;
        this.northEast = northEast;
        this.southWest = southWest;
        this.latNorthEast = this.northEast.latitude;
        this.lngNorthEast = this.northEast.longitude;
        this.latSouthWest = this.southWest.latitude;
        this.lngSouthWest = this.southWest.longitude;
        this.latLngBoundsBuilding = new LatLngBounds(southWest,northEast);
        this.center = center;
        this.latCenter = center.latitude;
        this.lngCenter = center.longitude;
    }

    BuildingImpl(){

    }

    @Override
    public void initializer() {
        this.northEast = new LatLng(this.latNorthEast,this.lngNorthEast);
        this.southWest = new LatLng(this.latSouthWest,this.lngSouthWest);
        this.latLngBoundsBuilding = new LatLngBounds(southWest,northEast);
        this.center = new LatLng(this.latCenter,this.lngCenter);
    }

    /**
     * Indique si le batiment se trouve au centre de l'écran
     *
     * @param cam Une instance de CameraPosition
     * @return true si centré sur la carte false sinon
     */
    @Override
    public boolean isOnCenter(CameraPosition cam) {
        /*if(southWest == null || northEast == null){
            southWest = new LatLng(latSouthWest,lngSouthWest);
            northEast = new LatLng(latNorthEast,lngNorthEast);
        }
        if(latLngBoundsBuilding == null){
            this.latLngBoundsBuilding = new LatLngBounds(southWest,northEast);
        }
        if(center == null){
            this.center = new LatLng(this.latCenter,this.lngCenter);
        }*/

        this.initializer();
        if(cam.target.latitude < northEast.latitude && cam.target.latitude > southWest.latitude && cam.target.longitude > southWest.longitude && cam.target.longitude < northEast.longitude)
            return true;
        else
            return false;
        /*if(latLngBoundsBuilding.contains(cam.target))
            return true;
        else
            return false;*/
    }

    /**
     * Indique le nombre d'étage dans le batîment
     *
     * @return le nombre d'étage dans le batîment
     */
    @Override
    public int getNbFloor() {
        return this.etages.size();
    }

    /**
     * Renvoi l'id de l'image de l'étage
     *
     * @param floorLVL l'étage: 0,1,2,3,4,5,6,etc...
     * @return l'id de l'image de l'étage
     */
    @Override
    public Integer getFloorOverlayID(int floorLVL) {
        return this.getFloor(floorLVL).getIdImageOverlay();
    }

    @Override
    public String getNomBuilding() {
        return nomBuilding;
    }

    @Override
    public void setNomBuilding(String n) {
        nomBuilding = n;
    }

    @Override
    public List<Floor> getLstFloor() {
        return this.lstFloor;
    }

    @Override
    public void setLstFloor(List<Floor> lst) {
        this.lstFloor = lst;
    }

    @Override
    public Floor getFloor(int lvl) {
        Iterator<FloorImpl> it = etages.iterator();
        Floor fl = null;
        for(int i = 0;it.hasNext();i++){
            fl = it.next();
            if(i == lvl && fl.getLvl() == i) {
                fl.initializer();
                return fl;
            }
        }
        return null;
    }

    @Override
    public Floor setFloor(Floor fl) {
        return null;
    }

    @Override
    public String getInfoBuilding() {
        return infoBuilding;
    }

    @Override
    public String setInfoBuilding(String infos) {
        return infoBuilding = infos;
    }

    @Override
    public LatLngBounds getLatLngBoundsBuilding() {
        return latLngBoundsBuilding;
    }

    @Override
    public void setLatLngBoundsBuilding(LatLngBounds latlng) {
        latLngBoundsBuilding = latlng;
    }

    @Override
    public LatLng getNorthEast() {
        return northEast;
    }

    @Override
    public void setNorthEast(LatLng p) {
        northEast = p;
    }

    @Override
    public double getLatNorthEast() {
        return latNorthEast;
    }

    @Override
    public void getLatNorthEast(double p) {
        latNorthEast = p;
    }

    @Override
    public double getLngNorthEast() {
        return lngNorthEast;
    }

    @Override
    public void getLngNorthEast(double p) {
        lngNorthEast = p;
    }

    @Override
    public LatLng getSouthWest() {
        return southWest;
    }

    @Override
    public void setSouthWest(LatLng p) {
        southWest = p;
    }

    @Override
    public double getLatSouthWest() {
        return latSouthWest;
    }

    @Override
    public void setLatSouthWest(double p) {
        latSouthWest = p;
    }

    @Override
    public double getLngSouthWest() {
        return lngSouthWest;
    }

    @Override
    public void setLngSouthWest(double p) {
        lngSouthWest = p;
    }
}
