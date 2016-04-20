package com.upo10.miage.upopulse.upobuildingtools;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.CameraPosition;
import com.upo10.miage.upopulse.upobuildings.Building;
import com.upo10.miage.upopulse.upobuildings.BuildingImpl;

import java.util.List;

/**
 * Created by siaydin on 04/04/2015.
 */
public class BtnFloorHandlerImpl implements BtnFloorHandler {

    CameraPosition camPos;
    LinearLayout btnContainer;
    LinearLayout containerOfBtnContainer;
    Context c;
    List<BuildingImpl> listBatiment;

    public BtnFloorHandlerImpl(CameraPosition camPos, LinearLayout containerOfBtnContainer,Context ctxt,List<BuildingImpl> lstBatiments) {
        this.camPos = camPos;
        this.btnContainer = new LinearLayout(ctxt);
        this.containerOfBtnContainer = containerOfBtnContainer;
        this.c = ctxt;
        this.listBatiment = lstBatiments;
    }

    /**
     * Ajoute sur l'écran les boutons pour changer d'étage
     * @return true si le batiment est reconnus et donc affichage des boutons, false dans le cas contraire
     */
    @Override
    public Building setFloorBtnByBuilding() {
        unSetFloorBtn();//on enlève les boutons
        Building batimen = null;
        //taille boutons
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) this.c.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int taille = 77;
        Point size = new Point();
        display.getSize(size);
        //Taille
        int height = size.x;
        int width = size.y;
        //
        FloorBtnBuilder flBtnBuilder = new FloorBtnBuilderImpl();//on appelle notre constructeur de boutons
        List<FloorButton> myBtns = flBtnBuilder.getFloorBtn(this.camPos,this.c,this.listBatiment);//on récu^ère des boutons si un batiment est détecté
        if(myBtns.size()>0 && width > 0)
            taille = (width-(width*39/100))/myBtns.size();
        //Toast.makeText(c,"Taille bouton: "+taille+" widht:"+width+"   height: "+height,Toast.LENGTH_SHORT).show();

        if(myBtns != null && !myBtns.isEmpty()) {
            this.btnContainer = new LinearLayout(c);
            for (FloorButton fl : myBtns) {//on ajoute les boutons à l'écran
                LinearLayout linLay = new LinearLayout(c);
                linLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                fl.setLayoutParams (new LinearLayout.LayoutParams(taille, LinearLayout.LayoutParams.WRAP_CONTENT));
                linLay.addView(fl);
                //linLay.setBackgroundColor(Color.parseColor("#F2ffffff"));
                btnContainer.addView(linLay);
            }

            containerOfBtnContainer.addView(btnContainer);
           // btnContainer.setBackgroundColor(Color.parseColor("#FF5722"));
            //on force les linearlayou contenant les boutons à se redessiner.
            btnContainer.invalidate();
            containerOfBtnContainer.setWillNotDraw(false);
           // containerOfBtnContainer.setBackgroundColor(Color.parseColor("#FF5722"));
            containerOfBtnContainer.invalidate();
            return flBtnBuilder.getCurrentBuilding();
        }
        return null;
    }

    /**
     * Enlève les boutons d'étages
     *
     * @return true si la camera n'est pas centré sur un batiment et donc les boutons seront enlevés
     */
    @Override
    public boolean unSetFloorBtn() {
        //on enlève tous les boutons du layout qui les contients
        for(int i = containerOfBtnContainer.getChildCount();i>=0;i--){
            containerOfBtnContainer.removeView(containerOfBtnContainer.getChildAt(i));
        }
        containerOfBtnContainer.invalidate();//on force la view à se redéssiner
        return false;
    }

    public CameraPosition getCamPos() {
        return camPos;
    }

    public void setCamPos(CameraPosition camPos) {
        this.camPos = camPos;
    }

    public LinearLayout getBtnContainer() {
        return btnContainer;
    }

    public void setBtnContainer(LinearLayout btnContainer) {
        this.btnContainer = btnContainer;
    }

    public LinearLayout getContainerOfBtnContainer() {
        return containerOfBtnContainer;
    }

    public void setContainerOfBtnContainer(LinearLayout containerOfBtnContainer) {
        this.containerOfBtnContainer = containerOfBtnContainer;
    }

    public Context getC() {
        return c;
    }

    public void setC(Context c) {
        this.c = c;
    }
}
