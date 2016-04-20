package com.upo10.miage.upopulse.uposearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.upo10.miage.upopulse.R;
import com.upo10.miage.upopulse.upobuildings.RoomImpl;
import com.upo10.miage.upopulse.upopulse.HomePageActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SearchResultsActivity extends ActionBarActivity {


    private static String TAG = "SearchResultsActivity";
    public static RoomImpl roomSelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        final ListView lstv = (ListView) findViewById(R.id.listViewSearchResults);
        final Context ctxt = this;

        //Recupere les resultats de la recherche pour les salles:
        List<RoomImpl> lstResults =  searchRooms(getIntent());
        //Instanciation de l'adapter
        final SearchRoomListAdapter searchAdapter = new SearchRoomListAdapter(this,R.layout.row_search_results,lstResults);
        lstv.setAdapter(searchAdapter);

        TextView txtSearchResults = (TextView) findViewById(R.id.txtViewSearchResults);
        txtSearchResults.setText("Resultats recherche: "+lstResults.size()+" element(s)");

        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Si l'utilisateur clique sur une salle:
                if(searchAdapter.getSalle(i) instanceof RoomImpl) {
                    RoomImpl salle = (RoomImpl) searchAdapter.getSalle(i);
                    roomSelected = salle;
                    salle.getEtage().initializer();
                    try {
                        roomSelected = HomePageActivity.DBHELPER.getRoomImplDao().queryForId(salle.getIdRoom());
                        roomSelected.setEtage(HomePageActivity.DBHELPER.getFloorImplDao().queryForId(roomSelected.getEtage().getIdFloor()));
                        roomSelected.getEtage().initializer();
                        roomSelected.getEtage().setBuilding(HomePageActivity.DBHELPER.getBuildingImplDao().queryForId(roomSelected.getEtage().getBuilding().getNomBuilding()));
                        roomSelected.getEtage().getBuilding().initializer();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Erreur recuperation de la salle lors du click");
                    }
                    Toast.makeText(ctxt, salle.getNomRoom(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ctxt, HomePageActivity.class));
                }
            }
        });

        //handleIntent(getIntent());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /***
     * Recherche des salles
     * @param intent un itent provenant de HomePageActivity
     * @return Une liste de salles
     */
    private List<RoomImpl> searchRooms(Intent intent) {
        List<RoomImpl> lstSalles = new LinkedList<>();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i(TAG,"SearchQuery = "+query);
            Dao<RoomImpl, Integer> roomDAO = null;
            try {
                roomDAO = HomePageActivity.DBHELPER.getRoomImplDao();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            QueryBuilder<RoomImpl,Integer> requeteBuilder = roomDAO.queryBuilder();
            try {
                //On fait un like dans le champs nomRoom de la table RoomImpl
                lstSalles = (List<RoomImpl>) requeteBuilder.where().like("nomRoom", "%" + query.trim() + "%").query();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //logs nombre de resultats:
        Log.i(TAG,"Nombre de salles trouver: "+lstSalles.size());
        return lstSalles;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        searchRooms(intent);
    }
}
