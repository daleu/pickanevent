package com.pes12.pickanevent.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationDrawer extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EventsFragment.OnFragmentInteractionListener,
        TimelineFragment.OnFragmentInteractionListener,
        GruposFragment.OnFragmentInteractionListener,
        AmistadesFragment.OnFragmentInteractionListener {

    UsuarioEntity actual;
    String idActual;
    CircleImageView nav_user;
    Bitmap imageAux;
    String controlActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        actual = getUsuarioActual();
        idActual = getAuth().getCurrentUser().getUid();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Timeline");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);

        //Carregar dades al navigation drawer

        View hView =  navigationView.getHeaderView(0);
        nav_user = (CircleImageView) hView.findViewById(R.id.profile_image);
        Picasso.with(this).load(getAuth().getCurrentUser().getPhotoUrl()).into(nav_user);

        TextView nom = (TextView)hView.findViewById(R.id.name);
        Log.e("username",actual.getNickname());
        nom.setText(actual.getNickname());

        if(actual.getCm()){
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_slideshow).setVisible(false);
            nav_Menu.findItem(R.id.nav_share).setVisible(false);
        }

        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = TimelineFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, fragment).commit();

    }

    protected void onResume(){
        Log.e("resume","resume");
        super.onResume();
        if(controlActivity!=null) {
            finish();
            startActivity(getIntent());
        }
        else controlActivity = "1";
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            getSupportActionBar().setTitle("Timeline");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_fragment, new TimelineFragment());
            ft.commit();
        } else if (id == R.id.nav_gallery) {
            getSupportActionBar().setTitle("Mis Grupos");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_fragment, new GruposFragment());
            ft.commit();
        } else if (id == R.id.nav_slideshow) {
            getSupportActionBar().setTitle("Mis Amistades");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_fragment, new AmistadesFragment());
            ft.commit();
        } else if (id == R.id.nav_manage) {
            getSupportActionBar().setTitle("Mis Eventos");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_fragment, new EventsFragment());
            ft.commit();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public String getUsuariActual(){
        return idActual;
    }
}
