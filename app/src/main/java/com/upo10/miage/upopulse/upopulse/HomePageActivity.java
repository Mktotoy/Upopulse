package com.upo10.miage.upopulse.upopulse;
/**
 * Created by Dimitri on 12/04/2015.
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.upo10.miage.upopulse.R;
import com.upo10.miage.upopulse.upobuildings.Building;
import com.upo10.miage.upopulse.upobuildings.BuildingImpl;
import com.upo10.miage.upopulse.upobuildings.FloorImpl;
import com.upo10.miage.upopulse.upobuildingtools.BtnFloorHandler;
import com.upo10.miage.upopulse.upobuildingtools.BtnFloorHandlerImpl;
import com.upo10.miage.upopulse.upodatabase.DatabaseHelper;
import com.upo10.miage.upopulse.upoevent.DisplayEvents;
import com.upo10.miage.upopulse.upologin.AdminActivity;
import com.upo10.miage.upopulse.upologin.LoginActivity;
import com.upo10.miage.upopulse.upologin.UpdateAccount;
import com.upo10.miage.upopulse.uposearch.SearchResultsActivity;
import com.upo10.miage.upopulse.upouser.User;
import com.upo10.miage.upopulse.upowbs.RequestWebService;

import org.w3c.dom.Document;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.akexorcist.gdaplibrary.GoogleDirection;
import de.hdodenhof.circleimageview.CircleImageView;


public class HomePageActivity extends ActionBarActivity
        implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {

    // TOOLBAR ATTRIBUTES \\
    //First We Declare Titles And Icons For Our Navigation Drawer List View
    //This Icons And Titles Are holded in an Array as you can see

    // MAPS ATTRIBUTE \\
    public static GoogleMap mMap;
    public static DatabaseHelper DBHELPER = null;

    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view
    static GroundOverlay imageOverlay;
    private static int imageOverlayID;
    String TITLES[];
    int ICONS[];
    String NAME = "";
    String EMAIL = "";
    int PROFILE = R.drawable.profil_default;
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle

    // Variables localisation de l'utilisateur
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = HomePageActivity.class.getSimpleName();
    String lat, lon;

    //Recherche code partage de localisation
    String rechercheTexte = null;
    private static boolean queryPassed = false;

    //private TextView mCameraTextView;
    boolean dejaFocus = false;
    private Toolbar toolbar;                              // Declaring the Toolbar Object
    private BuildingImpl batG = null;
    private List<BuildingImpl> batiments;
    private BuildingImpl batCourant = null;
    private static FloorImpl etageCourant = null;
    public static LatLng center = new LatLng(48.903046, 2.215644);
    private static Marker markerRoomSelected;

    //Tracer d'itinéraire
    private LatLng pointA;
    private LatLng pointB;
    private Marker markerA;
    private Marker markerB;
    private List<Polyline> listItinerary = new ArrayList<>();
    private List<Marker> listMarker = new ArrayList<>();
    private Marker userMarker = null;
    private Polyline itinerary;
    GoogleDirection gd;
    Document mDoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapsInitializer.initialize(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page);
    /* Assigning the toolbar object ot the view
    and setting the the Action bar to our toolbar
     */
		try {
            User user = User.getInstance();

            if (user != null && user.getNom().equals("") == false) {
                NAME = user.getPrenom() + " " + user.getNom();
                EMAIL = user.getMail();
                if(user.getAdm()<1) {
                    TITLES = new String[]{"Accueil", "Evenements", "Partager ma localisation", "Compte"};
                    ICONS = new int[]{R.drawable.ic_home, R.drawable.ic_events, R.drawable.ic_share_location, R.drawable.ic_action_person};
                }
                else
                {
                    TITLES = new String[]{"Accueil", "Evenements", "Partager ma localisation", "Compte", "Administrateur"};
                    ICONS = new int[]{R.drawable.ic_home, R.drawable.ic_events, R.drawable.ic_share_location, R.drawable.ic_action_accounts, R.drawable.ic_action_important};
                }

            } else {
                TITLES = new String[]{"Accueil", "Evenements", "Partager ma localisation", "Connexion"};
                ICONS = new int[]{R.drawable.ic_home, R.drawable.ic_events, R.drawable.ic_share_location, R.drawable.ic_action_accounts};
            }
        }
        catch(Exception e)
        {
            TITLES = new String[]{"Accueil", "Evenements", "Partager ma localisation", "Connexion"};
            ICONS = new int[]{R.drawable.ic_home, R.drawable.ic_events, R.drawable.ic_share_location, R.drawable.ic_action_accounts};
        }
		
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        //if(u.getNom().equals("") == false && u.getNom() != null) NAME = u.getPrenom() + " " + u.getNom();

        mAdapter = new MyAdapter(TITLES, ICONS, NAME, EMAIL, PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager

        // Gesture listener to detect simple tap on navigation drawer to do smth
        final GestureDetector mGestureDetector = new GestureDetector(HomePageActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        // Gestions des taps sur le navigation drawer
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                    // TODO Gerer les redirections vers les bonnes pages au lieu des toasts

                    if(recyclerView.getChildPosition(child) == 0)
                    {
                        try
                        {
                            User u = User.getInstance();
                            if(u != null && u.getNom().equals("") == false)
                            {
                                TextView link = (TextView) findViewById(R.id.name);
                                TextView link1 = (TextView) findViewById(R.id.email);
                                CircleImageView img = (CircleImageView) findViewById(R.id.circleView);

                                link.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(HomePageActivity.this, UpdateAccount.class);
                                        startActivity(intent);
                                    }
                                });


                                link1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(HomePageActivity.this, UpdateAccount.class);
                                        startActivity(intent);
                                    }
                                });

                                img.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(HomePageActivity.this, UpdateAccount.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                        catch(Exception e){}
                    }

                    if (recyclerView.getChildPosition(child) == 1)
                    {
                      /*  Intent intent = new Intent(HomePageActivity.this, HomePageActivity.class);
                        startActivity(intent);*/
                        Drawer.closeDrawers();
                    }

                    if(recyclerView.getChildPosition(child) == 2)
                    {
                        Intent intent = new Intent(HomePageActivity.this, DisplayEvents.class);
                        startActivity(intent);
                    }

                    if(recyclerView.getChildPosition(child) == 4)
                    {
                        Intent intent;

                        if(TITLES[3].equals("Connexion") == true)
                        {
                            intent = new Intent(HomePageActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }

                        if(TITLES[3].equals("Compte") == true)
                        {
                            intent = new Intent(HomePageActivity.this, UpdateAccount.class);
                            startActivity(intent);
                        }
                    }
					
					if(recyclerView.getChildPosition(child) == 5)
                    {
                        Intent intent;
                        intent = new Intent(HomePageActivity.this, AdminActivity.class);
                        startActivity(intent);
                    }

                    // Partage de localisation
                    if (recyclerView.getChildPosition(child) == 3) {
                        Location userLocation;

                        userLocation  = getUserLastLoc();

                        if(userLocation != null){
                            lat = Double.toString(userLocation.getLatitude());
                            lon = Double.toString(userLocation.getLongitude());
                            new UserLocationRequest().execute();
                        }else{
                            createDialogPartageLoc("erreur localisation");
                        }
                    }

                  /*  Drawer.closeDrawers();
                    Toast.makeText(HomePageActivity.this, "The Item Clicked is: " + TITLES[recyclerView.getChildPosition(child) - 1]+ " "+ recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                    */
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }

        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

        // Instanciation localisation utilisateur
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // CODE SIMON MAPS
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //mCameraTextView=(TextView) findViewById(R.id.camera_text);

        //appelle le databasehelper
        DBHELPER = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        //On récupère tous les batiments de la base de données
        if (DBHELPER != null) {
            if(SearchResultsActivity.roomSelected == null) {
                batiments = DBHELPER.getRuntimeBuildingImplDao().queryForAll();
                //Par défault, on n'affiche que le batiment G. Seuls les plans d'un seul batiment seront affichés à la fois.
                try {
                    batCourant = DBHELPER.getBuildingImplDao().queryForId("Bat.G");
                    batCourant.initializer();
                    //batG.initializer();
                    center = batCourant.getCenter();
                    etageCourant = (FloorImpl) batCourant.getFloor(0);
                    if(etageCourant != null)
                        etageCourant.initializer();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            //Lorsque l'utilisateur a recheché une salle
            if(SearchResultsActivity.roomSelected != null){
                this.etageCourant = SearchResultsActivity.roomSelected.getEtage();
                this.etageCourant.initializer();
                LatLng ptnSalle = new LatLng(SearchResultsActivity.roomSelected.getLatRoom(), SearchResultsActivity.roomSelected.getLngRoom());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ptnSalle, 38));
                center = ptnSalle;
                    //mMap.clear();
            }
        }

        //Tracer d'itinéraire
        gd = new GoogleDirection(this);
        gd.setOnDirectionResponseListener(new GoogleDirection.OnDirectionResponseListener() {
            public void onResponse(String status, Document doc, GoogleDirection gd) {
                mDoc = doc;
                itinerary = mMap.addPolyline(gd.getPolyline(doc, 3, Color.RED));
                if(itinerary != null)
                    listItinerary.add(itinerary);
            }
        });

    }

    // TOOLBAR METHODS
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);

        MenuItem itemAdd = menu.findItem(R.id.action_add_event);
        itemAdd.setVisible(false);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =(SearchView) menu.findItem(R.id.action_search).getActionView();

        // ajout d'un listener sur la searchview pour traiter les différents types de requete
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() == 8 && query.substring(0, 2).equals("PL")) { // il s'agit bien d'un code de partage de loc
                    Log.i(TAG, "code partage de loc recherché " + query);
                    rechercheTexte = query;
                    queryPassed = false;

                    // on interroge le serveur pour récupérer la latitude et la longitude du code de partage
                    new getPLLatLonRequest().execute();

                    // Permet de reset la vue recherche après affiché le point
                    SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                    searchView.setIconified(true);


                    return true;
                }
                return false;
            }
        });

        ComponentName cn = new ComponentName(this, SearchResultsActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_bar) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void createDialogGeoLocItinerary(LatLng pa,LatLng pb){
        TextView titreDialog, contentDialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this);
                // Ajout des boutons adequat
        if(pb == null && itinerary == null)
            builder.setPositiveButton(R.string.itinerary, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                            pointB = new LatLng(getUserLastLoc().getLatitude(),getUserLastLoc().getLongitude());
                            traceItinerary(pointA, pointB);
                    }

            });
        builder.setNegativeButton(R.string.cancelItinerary, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // ne rien faire
                cleanItinerary();
            }
        });
        if(itinerary == null)
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cleanItinerary();
                }
            });

        // Get the layout inflater
        LayoutInflater inflater = HomePageActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_geoloc, null);

        // on recupere les textviews de la view du dialog
        titreDialog = (TextView) view.findViewById(R.id.Title_geoloc);
        contentDialog = (TextView) view.findViewById(R.id.Texte_geoloc);

        builder.setView(view);
        AlertDialog dialog = builder.create();

        titreDialog.setText(R.string.itinerayrDialogTitle);
        if(pb == null && itinerary == null)
            contentDialog.setText(R.string.itineraryDialogGeolocDrawContent);
        else
            contentDialog.setText(R.string.itineraryDialogDeleteItinerayTraceContent);

        dialog.show();
    }

    public void createDialogNoGeoLocItinerary(LatLng pa,LatLng pb){
        TextView titreDialog, contentDialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this);
        builder.setPositiveButton(R.string.itinerary, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (itinerary == null)
                    traceItinerary(pointA, pointB);
            }

            });
        builder.setNegativeButton(R.string.cancelItinerary, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // ne rien faire
                cleanItinerary();
            }
        });
        if(itinerary == null)
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cleanItinerary();
            }
        });

        // Get the layout inflater
        LayoutInflater inflater = HomePageActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_itinerary_nogeoloc, null);

        // on recupere les textviews de la view du dialog
        titreDialog = (TextView) view.findViewById(R.id.Title_nogeoloc);
        contentDialog = (TextView) view.findViewById(R.id.Texte_nogeoloc);

        builder.setView(view);
        AlertDialog dialog = builder.create();

        titreDialog.setText(R.string.itinerayrDialogTitle);

        contentDialog.setText(R.string.itineraryDialogNoGeoLocDrawContent);

        dialog.show();
    }

    public void createDialogPartageLoc(final String code){

        TextView titreDialog, contentDialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this);

        // Ajout des boutons adequat
        if(!code.equals("erreur serveur") && !code.equals("erreur localisation") && !code.equals("TimeOut")){
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            }).setNegativeButton(R.string.sms, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // open the sms app
                    String uri = "smsto:";
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
                    intent.putExtra("sms_body", "Voici mon code de localisation "+code);
                    intent.putExtra("compose_mode", true);
                    startActivity(intent);
                }
            });
        }else{
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
        }


        // Get the layout inflater
        LayoutInflater inflater = HomePageActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_partage_loc, null);

        // on recupere les textviews de la view du dialog
        titreDialog = (TextView) view.findViewById(R.id.Title_partage_loc);
        contentDialog = (TextView) view.findViewById(R.id.Texte_partage_loc);

        builder.setView(view);
        AlertDialog dialog = builder.create();

        titreDialog.setText("Partage de localisation");

        if(!code.equals("false")){
            contentDialog.setText("Votre code à partager :\n"+code);
        }else if(code.equals("erreur localisation")){
            contentDialog.setText("Erreur localisation\nVeuillez activer vos données ou GPS.");
        }else if(code.equals("erreur serveur") || code.equals("TimeOut")){
            contentDialog.setText("Erreur serveur.\n\"Veuillez réessayer plus tard.");
        }

        dialog.show();
    }


    // METHODES CARTE ET GESTION DES BATIMENTS
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if(SearchResultsActivity.roomSelected == null) {
            Double latitude = cameraPosition.target.latitude;
            Double longitude = cameraPosition.target.longitude;
            Building buildingFound;
            BtnFloorHandler gestionnaireFloorBtns = new BtnFloorHandlerImpl(cameraPosition, (LinearLayout) (findViewById(R.id.linLayoutBtnFloor)), this, this.batiments);
            buildingFound = gestionnaireFloorBtns.setFloorBtnByBuilding();
            if (buildingFound == null) {
                gestionnaireFloorBtns.unSetFloorBtn();
                etageCourant = null;
            } else {
                buildingFound.initializer();

                //batCourant = (BuildingImpl) buildingFound;
                //center = batCourant.getCenter();
                if ((!buildingFound.getCenter().equals(center)) || (etageCourant != null && etageCourant.getIdImageOverlay() != imageOverlayID)) {
                    if(!buildingFound.getCenter().equals(center))
                        removeMarker(markerRoomSelected);
                    center = buildingFound.getCenter();
                    batCourant = (BuildingImpl) buildingFound;
                    buildFloor(mMap, buildingFound.getFloor(0).getCenter(), buildingFound.getFloor(0).getLngImage(), buildingFound.getFloor(0).getLargImage(), buildingFound.getFloor(0).getIdImageOverlay());

                }

            }
        }
        else{
            //Si l'utilisateur avait recherché une salle:
            if(SearchResultsActivity.roomSelected != null) {
                etageCourant.initializer();
                imageOverlayID = 0;
                buildFloor(mMap, etageCourant.getCenter(), etageCourant.getLngImage(), etageCourant.getLargImage(), etageCourant.getIdImageOverlay());
                markerRoomSelected = addMarker(SearchResultsActivity.roomSelected.getNomRoom(), center);
                center = batCourant.getCenter();
                SearchResultsActivity.roomSelected = null;
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap map) {
        MapsInitializer.initialize(this);
        //clean itinerary
        cleanItinerary();
        mMap = map;
        if(batCourant == null && SearchResultsActivity.roomSelected != null) {
            try {
                batCourant = DBHELPER.getBuildingImplDao().queryForId(SearchResultsActivity.roomSelected.getEtage().getBuilding().getNomBuilding());
                batCourant.initializer();
                batiments = DBHELPER.getBuildingImplDao().queryForAll();
                center = SearchResultsActivity.roomSelected.getCenter();
                imageOverlayID = etageCourant.getIdImageOverlay();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(center)
                .zoom(18)
                .bearing(-39)
                .tilt(30)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.setMyLocationEnabled(true);
        map.setOnCameraChangeListener(this);

        //Tracer d'itineraire
        mMap.setOnMarkerClickListener(HomePageActivity.this);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                //lstLatLngs.add(point);
                if(connectionavailable()) {
                    if (pointA == null) {
                        pointA = point;
                        markerA = mMap.addMarker(new MarkerOptions().position(point));
                        listMarker.add(markerA);
                    } else if (pointB == null ) {
                        pointB = point;
                        markerB = mMap.addMarker(new MarkerOptions().position(point));
                        listMarker.add(markerB);
                    }
                    if (pointA != null && pointB != null) {
                        //dialog oui/non + tracer itinéraire
                        if (itinerary == null && connectionavailable())
                            createDialogNoGeoLocItinerary(pointA, pointB);
                    }
                }
                else
                    Toast.makeText(HomePageActivity.this,R.string.noConnexionForItinerary,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void buildFloor(GoogleMap map, LatLng floor, float longueur, float largeur, int floorName) {
        mMap = map;
        if((floorName != HomePageActivity.imageOverlayID) || (imageOverlay != null && imageOverlayID == floorName && !imageOverlay.isVisible())){
            imageOverlayID = floorName;
            if (imageOverlay != null)
                imageOverlay.remove();
            GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromResource(floorName))
                    .anchor(0, 1)
                    .bearing(-39.5f)
                    .transparency(0.5f)
                    .position(floor, longueur, largeur);
            imageOverlay = mMap.addGroundOverlay(newarkMap);
        }
    }

    public static void setCurrentFloor(FloorImpl fl) {
        etageCourant = fl;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (DBHELPER != null) {
            OpenHelperManager.releaseHelper();
            DBHELPER = null;
        }
    }

    public static void addMarker(HashMap<String,Object> datas){
        mMap.addMarker(new MarkerOptions()
                .title((String) datas.get("title"))
                .position((LatLng) datas.get("pos"))
                .draggable(false));
    }

    public static Marker addMarker(String title,LatLng pos) {
        return mMap.addMarker(new MarkerOptions()
                .title(title)
                .position(pos)
                .draggable(false));
    }

    public static void clearMap(){
        mMap.clear();
    }

    // Localisation utilisateur methods
    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.i(TAG, "Location services connected.");

        if (location == null) {
            Log.i(TAG, "Location null.");
        }
        else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
    }


    private Location getUserLastLoc(){
        Location location;

        if(mGoogleApiClient.isConnected()){
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            //mGoogleApiClient.disconnect();
        }else{
            mGoogleApiClient.connect();
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
          //  mGoogleApiClient.disconnect();
    }
    return location;
    }

    public void traceItinerary(LatLng a,LatLng b){
        try {
            if(connectionavailable()) {
                gd.setLogging(true);
                gd.request(a, b, GoogleDirection.MODE_WALKING);
            }
            else
                Toast.makeText(HomePageActivity.this,"No connection for itinerary request.",Toast.LENGTH_SHORT);
        }catch(Exception e){
            Log.i(TAG,e.toString());
            Toast.makeText(HomePageActivity.this,"No connection for itinerary.",Toast.LENGTH_SHORT).show();
        }
    }

    public void removeMarker(Marker m){
        if(m != null){
            m.remove();
            m = null;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //Si l'utilisateur clique sur un des markers qu'il a rajouté il clear la map
        if(marker.getPosition().equals(pointA) || marker.getPosition().equals(pointB)){
            if(connectionavailable()) {
                if (marker.getPosition().equals((pointA)) && connectionavailable() && getUserLastLoc() != null) {
                    createDialogGeoLocItinerary(pointA, null);
                } else if (pointB != null && pointA != null) {
                    for (Marker m : listMarker) {
                        if (!m.getPosition().equals(pointA)) {
                            createDialogGeoLocItinerary(pointA, m.getPosition());
                        }
                    }
                }
                else if(getUserLastLoc() == null){
                    Toast.makeText(this,R.string.noGeoloc,Toast.LENGTH_SHORT).show();
                }
            }
            else
                Toast.makeText(this,R.string.noConnexionForItinerary,Toast.LENGTH_SHORT).show();

        }
        return false;
    }

    public boolean connectionavailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void cleanItinerary(){
        pointA = null;
        pointB = null;
        removeMarker(markerB);
        removeMarker(markerA);
        if(itinerary != null) {
            itinerary.remove();
            itinerary = null;
        }
        for(Polyline pl : listItinerary){
            pl.remove();
        }
        for(Marker m : listMarker){
            m.remove();
        }
        listMarker.clear();
        listItinerary.clear();
    }


    //Permet de lancer la requete pour envoyer les lat/lon au serveur et ensuite récupérer le code de partage de l'utilisateur
    class UserLocationRequest  extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog = new ProgressDialog(HomePageActivity.this);
        String error;
        RequestWebService r = new RequestWebService();

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait..");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... unused) {
            // The ItemService would contain the method showed
            // in the previous paragraph
            try
            {

                error = r.createPartageLocationCode(lat, lon);
            }
            catch (Throwable e)
            {
                // handle exceptions
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            if (dialog.isShowing())
                dialog.dismiss();


            if(error == null || error.equals("false")) //Aucun code de partage trouvé
            {
                createDialogPartageLoc("erreur serveur");
            }
            else{
                // on partage le code de localisation
                createDialogPartageLoc(error);
            }
        }
    }

    //Permet de lancer la requete pour récupérer la latitude et la longitude du code de partage de localisation
    class getPLLatLonRequest  extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog = new ProgressDialog(HomePageActivity.this);
        LatLng error;
        RequestWebService r = new RequestWebService();

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait..");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... unused) {
            // The ItemService would contain the method showed
            // in the previous paragraph
            try
            {

                error = r.getPartageLocationCode(rechercheTexte);
            }
            catch (Throwable e)
            {
                // handle exceptions
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            if (dialog.isShowing())
                dialog.dismiss();

            Log.i("onpost location ", "location "+error);


            if(error == null || error.equals("false")) //Aucun code de partage trouvé
            {
                //creer dialog d'erreur comme quoi le code n'existe pas

                AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this);
                builder.setMessage("Code introuvable")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create();

                builder.show();
            }
            else{
                queryPassed = true;
                // on centre sur la map et on ajoute un marqueur grace à la latitue et longitude
                LatLng locTrouvee = error;

                CameraUpdate center=CameraUpdateFactory.newLatLng(locTrouvee);
                mMap.moveCamera(center);
                addMarker("Position de votre ami",locTrouvee);
            }
        }
    }

}