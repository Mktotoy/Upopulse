package com.upo10.miage.upopulse.upologin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.upo10.miage.upopulse.R;
import com.upo10.miage.upopulse.upoevent.DisplayEvents;
import com.upo10.miage.upopulse.upopulse.HomePageActivity;
import com.upo10.miage.upopulse.upopulse.MyAdapter;
import com.upo10.miage.upopulse.upouser.User;
import com.upo10.miage.upopulse.upowbs.RequestWebService;

/**
 * Created by Sonatines on 24/05/2015.
 */
public class UpdateAccount extends ActionBarActivity  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    // TOOLBAR ATTRIBUTES \\
    //First We Declare Titles And Icons For Our Navigation Drawer List View
    //This Icons And Titles Are holded in an Array as you can see
    String TITLES[];
    int ICONS[];
    User user = User.getInstance();

    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view



    String NAME = user.getPrenom() + " " + user.getNom();
    String EMAIL = user.getMail();
    int PROFILE = R.drawable.profil_default;

    private Toolbar toolbar;                              // Declaring the Toolbar Object

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle

    // Variables localisation de l'utilisateur
    private GoogleApiClient mGoogleApiClient;
    public final String TAG = HomePageActivity.class.getSimpleName();
    String lat, lon;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        /* Assigning the toolbar object ot the view
    and setting the the Action bar to our toolbar
     */
        if(user.getAdm()<1)
        {
            TITLES = new String[]{"Accueil", "Evenements", "Partager ma localisation", "Compte"};
            ICONS = new int[]{R.drawable.ic_home, R.drawable.ic_events, R.drawable.ic_share_location, R.drawable.ic_action_person};
        }
        else
        {
            TITLES = new String[]{"Accueil", "Evenements", "Partager ma localisation", "Compte", "Administrateur"};
            ICONS = new int[]{R.drawable.ic_home, R.drawable.ic_events, R.drawable.ic_share_location, R.drawable.ic_action_accounts, R.drawable.ic_action_important};
        }

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager

        // Instanciation localisation utilisateur
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Gesture listener to detect simple tap on navigation drawer to do smth
        final GestureDetector mGestureDetector = new GestureDetector(UpdateAccount.this, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        // Gestions des taps sur le navigation drawer
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

                View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());
                if(child!=null && mGestureDetector.onTouchEvent(motionEvent)){

                    // TODO Gerer les redirections vers les bonnes pages au lieu des toasts

                    if (recyclerView.getChildPosition(child) == 1)
                    {
                        Intent intent = new Intent(UpdateAccount.this, HomePageActivity.class);
                        startActivity(intent);
                    }

                    if(recyclerView.getChildPosition(child) == 2)
                    {
                        Intent intent = new Intent(UpdateAccount.this, DisplayEvents.class);
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

                    if(recyclerView.getChildPosition(child) == 4)
                    {
                        Intent intent = new Intent(UpdateAccount.this, LoginActivity.class);
                        startActivity(intent);
                    }

                    if(recyclerView.getChildPosition(child) == 5)
                    {
                        Intent intent = new Intent(UpdateAccount.this, AdminActivity.class);
                        startActivity(intent);
                    }

                    Drawer.closeDrawers();

                    //Toast.makeText(UpdateAccount.this, "The Item Clicked is: " + TITLES[recyclerView.getChildPosition(child) - 1], Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer){

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
        mDrawerToggle.syncState();

        /***************      Remplir valeur champs    ************************/
        User u = User.getInstance();

        EditText name = (EditText) findViewById(R.id.user_name);
        EditText firstname = (EditText) findViewById(R.id.user_firstname);
        EditText email = (EditText) findViewById(R.id.user_email);
        EditText description = (EditText) findViewById(R.id.user_description);
        EditText telephone = (EditText) findViewById(R.id.user_telephone);

        name.setText(u.getNom());
        firstname.setText(u.getPrenom());
        email.setText(u.getMail());
        description.setText(u.getDescription());
        telephone.setText(u.getTelephone());



        /***************      Listener Button     ************************/
        Button create = (Button) findViewById(R.id.user_create);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                EditText name = (EditText) findViewById(R.id.user_name);
                EditText firstname = (EditText) findViewById(R.id.user_firstname);
                EditText email = (EditText) findViewById(R.id.user_email);
                EditText description = (EditText) findViewById(R.id.user_description);
                EditText pwd = (EditText) findViewById(R.id.user_password);
                EditText pwd1 = (EditText) findViewById(R.id.user_password1);

                if(name.getText().toString().equals("") == true || firstname.getText().toString().equals("") == true || email.getText().toString().equals("") == true)
                {
                    String chaine = "Veuillez saisir : ";

                    if(name.getText().toString().equals("") == true) chaine = chaine + "Nom";
                    if(firstname.getText().toString().equals("") == true) chaine = chaine + "  Prénom";
                    if(email.getText().toString().equals("") == true) chaine = chaine + "  Email";


                    TextView infopass = (TextView) findViewById(R.id.infopass);
                    infopass.setText(chaine);
                }
                else
                {
                    if (pwd.getText().toString().equals(pwd1.getText().toString()) == false)
                    {
                        TextView infopass = (TextView) findViewById(R.id.infopass);
                        infopass.setText("Vos mots de passe sont différents");
                    } else {
                        new RequestItemsServiceTask().execute();
                    }
                }
            }
        });
    }

    // TOOLBAR METHODS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
        MenuItem itemAdd = menu.findItem(R.id.action_add_event);
        itemAdd.setVisible(false);
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

    public void createDialogPartageLoc(final String code){

        TextView titreDialog, contentDialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateAccount.this);

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
        LayoutInflater inflater = UpdateAccount.this.getLayoutInflater();
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

    //Permet de lancer la requete pour envoyer les lat/lon au serveur et ensuite récupérer le code de partage de l'utilisateur
    class UserLocationRequest  extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog = new ProgressDialog(UpdateAccount.this);
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

    class RequestItemsServiceTask extends AsyncTask<Void, Void, Void>
    {
        private ProgressDialog dialog = new ProgressDialog(UpdateAccount.this);
        RequestWebService r = new RequestWebService();
        EditText name = (EditText) findViewById(R.id.user_name);
        EditText firstname = (EditText) findViewById(R.id.user_firstname);
        EditText email = (EditText) findViewById(R.id.user_email);
        EditText pwd = (EditText) findViewById(R.id.user_password);
        EditText description = (EditText) findViewById(R.id.user_description);
        EditText telephone = (EditText) findViewById(R.id.user_telephone);


        String error;
        User u = User.getInstance();

        @Override
        protected void onPreExecute() {
            // TODO i18n
            dialog.setMessage("Please wait..");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... unused) {
            // The ItemService would contain the method showed
            // in the previous paragraph
            try
            {
                error = r.updateUser(name.getText().toString(), firstname.getText().toString(), email.getText().toString(), description.getText().toString(), pwd.getText().toString(), telephone.getText().toString(), u.getToken());
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
            {
                dialog.dismiss();
            }

            if(error == null || error.equals("false") || (error.equals("") == false && error.equals("ok") == false)) //Aucun User trouv�
            {
                TextView infopass = (TextView) findViewById(R.id.infopass);
                infopass.setText("Erreur : " + error);
            }
            else
            {
                TextView infopass = (TextView) findViewById(R.id.infopass);
                u.setTelephone(telephone.getText().toString());
                u.setMail(email.getText().toString());
                u.setNom(name.getText().toString());
                u.setPrenom(firstname.getText().toString());
                u.setDescription(description.getText().toString());
                infopass.setText("Votre compte a bien été modifié");
            }
        }
    }
}
