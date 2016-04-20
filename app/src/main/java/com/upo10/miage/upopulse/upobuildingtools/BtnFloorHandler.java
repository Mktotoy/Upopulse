package com.upo10.miage.upopulse.upobuildingtools;


import com.upo10.miage.upopulse.upobuildings.Building;

/**
 * Created by siaydin on 04/04/2015.
 */
public interface BtnFloorHandler {
    /***
     * Ajoute sur l'écran les boutons pour changer d'étage
     * @return le batiment est reconnus et donc affichage des boutons, sinon null dans le cas contraire
     */
    public Building setFloorBtnByBuilding();

    /***
     * Enlève les boutons d'étages
     * @return true si la camera n'est pas centré sur un batiment et donc les boutons seront enlevés
     */
    public boolean unSetFloorBtn();

}
