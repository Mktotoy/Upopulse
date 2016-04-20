package com.upo10.miage.upopulse.upodatabase;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.opencsv.CSVReader;
import com.upo10.miage.upopulse.upobuildings.BuildingImpl;
import com.upo10.miage.upopulse.upobuildings.FloorImpl;
import com.upo10.miage.upopulse.upobuildings.Room;
import com.upo10.miage.upopulse.upobuildings.RoomImpl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by siaydin on 05/04/2015.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "DataBaseHelper";
    private static final String DATABASE_NAME = "UPOPulse.sqlite";
    private static final int DATABASE_VERSION = 1;
    private Context ctxt;
    //objets DAO pour accéder à la base
    private Dao<BuildingImpl, String> buildingDAO = null;
    private RuntimeExceptionDao<BuildingImpl, String> runtimeBuildingDao = null;

    //objets DAO pour accéder à la base
    private Dao<FloorImpl, Integer> floorDAO = null;
    private RuntimeExceptionDao<FloorImpl, Integer> runtimeFloorDao = null;

    //objets DAO pour accéder à la base
    private Dao<RoomImpl, Integer> roomDAO = null;
    private RuntimeExceptionDao<RoomImpl, Integer> runtimeRoomDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctxt = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        Log.i(DatabaseHelper.class.getName(), "onCreate");

        try {
            /*TableUtils.dropTable(connectionSource, BuildingImpl.class, true);
            TableUtils.dropTable(connectionSource, FloorImpl.class, true);*/
            TableUtils.createTable(connectionSource, BuildingImpl.class);
            TableUtils.createTable(connectionSource, FloorImpl.class);
            TableUtils.createTable(connectionSource, RoomImpl.class);
        } catch (SQLException e) {
            Log.i(TAG, "creation table échouée");
            e.printStackTrace();
        }

        long millis = System.currentTimeMillis();

        //insertion des batiments
        insertBuildings("buildings.csv");
        //insertions des étages
        insertFloors("floors.csv");
        //insertions des salles
        insertRooms("rooms.csv");

        Log.i(DatabaseHelper.class.getName(), "created new entries in onCreate: " + millis);
    }

    public Dao<FloorImpl, Integer> getFloorImplDao() throws SQLException {
        if (floorDAO == null)
            floorDAO = getDao(FloorImpl.class);
        return floorDAO;
    }

    public RuntimeExceptionDao<FloorImpl, Integer> getRuntimeFloorImplDao() {
        if (runtimeFloorDao == null)
            runtimeFloorDao = getRuntimeExceptionDao(FloorImpl.class);
        return runtimeFloorDao;
    }

    public RuntimeExceptionDao<BuildingImpl, String> getRuntimeBuildingImplDao() {
        if (runtimeBuildingDao == null)
            runtimeBuildingDao = getRuntimeExceptionDao(BuildingImpl.class);
        return runtimeBuildingDao;
    }

    public Dao<BuildingImpl, String> getBuildingImplDao() throws SQLException {
        if (buildingDAO == null)
            buildingDAO = getDao(BuildingImpl.class);
        return buildingDAO;
    }

    public RuntimeExceptionDao<RoomImpl, Integer> getRuntimeRoomImplDao() {
        if (runtimeRoomDao == null)
            runtimeRoomDao = getRuntimeExceptionDao(RoomImpl.class);
        return runtimeRoomDao;
    }

    public Dao<RoomImpl, Integer> getRoomImplDao() throws SQLException {
        if (roomDAO == null)
            roomDAO = getDao(RoomImpl.class);
        return roomDAO;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, BuildingImpl.class, true);
            TableUtils.dropTable(connectionSource, FloorImpl.class, true);
            TableUtils.dropTable(connectionSource, RoomImpl.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        super.close();
        buildingDAO = null;
        runtimeBuildingDao = null;
        floorDAO = null;
        runtimeFloorDao = null;
        roomDAO = null;
        runtimeRoomDao = null;
    }

    /**
     * Insère les salles contenues dans les fichiers csv
     *
     * @param filename le nom du fichier csv sous la forme nomfichier.csv
     */
    public void insertRooms(String filename) {
        List<String[]> lstRooms = getAllRowsfromCSV(filename);
        ArrayList<RoomImpl> lstSalles = new ArrayList<>();
        RuntimeExceptionDao<FloorImpl, Integer> floorDAO = getRuntimeFloorImplDao();
        RuntimeExceptionDao<RoomImpl, Integer> roomDao = getRuntimeRoomImplDao();
        RuntimeExceptionDao<BuildingImpl, String> buildDao = getRuntimeBuildingImplDao();
        for (String[] row : lstRooms) {
            System.out.println(Arrays.toString(row));
            FloorImpl fl = (FloorImpl) buildDao.queryForId(row[8].trim()).getFloor(Integer.parseInt(row[9].trim()));
            lstSalles.add(new RoomImpl(row[1], Float.parseFloat(row[3].trim().split(",")[0]), Float.parseFloat(row[3].trim().split(",")[1]), row[2], fl));
        }
        for (Room r : lstSalles) {
            roomDao.createOrUpdate((RoomImpl) r);
        }
    }

    /**
     * Insere des étages contenus dans un fichier csv
     *
     * @param filename le nom du fichier sous la forme nomFichier.csv
     */
    public void insertFloors(String filename) {
        List<String[]> lstFloors = getAllRowsfromCSV(filename);
        ArrayList<FloorImpl> lstEtages = new ArrayList<>();
        RuntimeExceptionDao<FloorImpl, Integer> floorDAO = getRuntimeFloorImplDao();
        RuntimeExceptionDao<BuildingImpl, String> buildDao = getRuntimeBuildingImplDao();
        for (String[] ligne : lstFloors) {
            float larg = Float.parseFloat(ligne[6].trim());
            float lngr = Float.parseFloat(ligne[5].trim());
            LatLng center = new LatLng(Float.parseFloat(ligne[7].trim().split(",")[0]), Float.parseFloat(ligne[7].trim().split(",")[1]));
            int id = this.ctxt.getResources().getIdentifier(ligne[4].trim(), "drawable", this.ctxt.getPackageName());
            lstEtages.add(new FloorImpl(Integer.parseInt(ligne[0].trim()), buildDao.queryForId(ligne[1].trim()), ligne[2].trim(), ligne[3].trim(), id, lngr, larg, center));
        }
        for (FloorImpl fl : lstEtages) {
            floorDAO.createOrUpdate(fl);
        }
    }

    /**
     * Insertion des batiment contenus dans un fichier
     *
     * @param filename le nom du fichier sous la forme nomfichier.csv
     */
    public void insertBuildings(String filename) {
        List<String[]> lstBuildings = getAllRowsfromCSV(filename);
        ArrayList<BuildingImpl> lstBatiments = new ArrayList<>();
        RuntimeExceptionDao<BuildingImpl, String> buildDao = getRuntimeBuildingImplDao();
        for (String[] ligne : lstBuildings) {
            LatLng northEast = new LatLng(Float.parseFloat(ligne[1].trim().split(",")[0]), Float.parseFloat(ligne[1].trim().split(",")[1]));
            LatLng southWast = new LatLng(Float.parseFloat(ligne[2].trim().split(",")[0]), Float.parseFloat(ligne[2].trim().split(",")[1]));
            LatLng center = new LatLng(Float.parseFloat(ligne[4].trim().split(",")[0]), Float.parseFloat(ligne[4].trim().split(",")[1]));
            lstBatiments.add(new BuildingImpl(ligne[0].trim(), northEast, southWast, ligne[3].trim(), center));
        }
        for (BuildingImpl bat : lstBatiments) {
            buildDao.createOrUpdate(bat);
        }
    }

    /**
     * Lit un fichier CSV dans le dossier assets et renvoie toutes les lignes dans une liste
     *
     * @param filenamewithextension nomfichier.csv
     * @return une liste de tableau de String
     */
    private List<String[]> getAllRowsfromCSV(String filenamewithextension) {
        //insertion en lisant les fichier CSV BAT G ETAGE RDC
        AssetManager am = ctxt.getAssets();
        //on crée le reader de fichier csv
        CSVReader reader = null;
        try {
            reader = new CSVReader(new InputStreamReader(am.open(filenamewithextension)), ';', '"', 1);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Erreur ouverture fichier csv etage_rdc");
        }

        //on récupère toutes les lignes
        List<String[]> allRows = null;
        try {
            allRows = reader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Erreur lecture fichier csv etage_rdc");
        }
        try {
            reader.close();
        } catch (IOException e) {
            Log.i(TAG, "Erreur fermeture fichier " + filenamewithextension);
            e.printStackTrace();
        }
        return allRows;
    }
}
