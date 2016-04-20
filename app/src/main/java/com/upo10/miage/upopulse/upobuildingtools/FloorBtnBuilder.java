package com.upo10.miage.upopulse.upobuildingtools;

import android.content.Context;

import com.google.android.gms.maps.model.CameraPosition;
import com.upo10.miage.upopulse.upobuildings.Building;
import com.upo10.miage.upopulse.upobuildings.BuildingImpl;

import java.util.List;

/**
 * Created by siaydin on 04/04/2015.
 */
public interface FloorBtnBuilder {

    public List<FloorButton> getFloorBtn(CameraPosition c,Context ctt,List<BuildingImpl> lstBatiments);
    public Building getCurrentBuilding();
}
