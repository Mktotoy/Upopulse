package com.upo10.miage.upopulse.uposearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.upo10.miage.upopulse.R;
import com.upo10.miage.upopulse.upobuildings.BuildingImpl;
import com.upo10.miage.upopulse.upobuildings.FloorImpl;
import com.upo10.miage.upopulse.upobuildings.RoomImpl;
import com.upo10.miage.upopulse.upopulse.HomePageActivity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by siaydin on 23/05/2015.
 */

/***
 * Adpater pour rechercher les salles
 */
public class SearchRoomListAdapter extends ArrayAdapter<Object>{


    private HashMap<Integer, Object> mIdMap = new HashMap<Integer, Object>();
    //Liste d'objet contenant les resultats de la recherche
    private List<Object> lstResQuery = new LinkedList<>();
    private final Context context;

    public SearchRoomListAdapter(Context context, int resource, List<Object> objects) {
        super(context, resource, objects);
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(i,objects.get(i));
        }
        this.context = context;
        lstResQuery = objects;
    }

    public SearchRoomListAdapter(SearchResultsActivity context, int row_search_results, List<RoomImpl> lstResults) {
        super(context, row_search_results, new LinkedList<Object>(lstResults));
        lstResQuery.addAll(lstResults);
        for (int i = 0; i < lstResQuery.size(); ++i) {
            mIdMap.put(i,lstResQuery.get(i));
        }
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_search_results, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        if(lstResQuery.get(position) instanceof RoomImpl) {
            RoomImpl salle = ((RoomImpl) lstResQuery.get(position));
            textView.setText(((RoomImpl) lstResQuery.get(position)).getNomRoom());
            TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
            salle.getEtage().initializer();
            ////////////////////////////////
            //Rajout 25/05/2015
            //////////////////////////////
            FloorImpl fl = null;
            try {
                fl = HomePageActivity.DBHELPER.getFloorImplDao().queryForId(salle.getEtage().getIdFloor());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            BuildingImpl bat = null;
            if(fl != null){
                try {
                    bat = HomePageActivity.DBHELPER.getBuildingImplDao().queryForId(fl.getBuilding().getNomBuilding());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(fl != null && bat != null)
                textView2.setText(bat.getNomBuilding()+" etage "+fl.getNomFloor());
            //////////////////////////////////////////////////////////////

        }
        //TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
        //textView.setText(lstSalles.get(position).getInfoRoom());
        return rowView;
    }

    @Override
    public long getItemId(int position) {
        Object item = getItem(position);
        //a adapter pour les events
        return item instanceof RoomImpl ? ((RoomImpl)item).getIdRoom() : null;
    }

    /***
     * Retourne un objet de type RoomImpl ou autre
     * @param position la position dans la lstview
     * @return un objet de type RoomImpl ou autre
     */
    public Object getSalle(int position){
        return getItem(position);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
