package be.kdg.kandoe.kandoeandroid;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.LinkedHashMap;

import be.kdg.kandoe.kandoeandroid.api.GebruikerAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Authorization;
import be.kdg.kandoe.kandoeandroid.cirkelsessie.CirkelSessieLijstFragment;
import be.kdg.kandoe.kandoeandroid.helpers.SharedPreferencesMethods;
import be.kdg.kandoe.kandoeandroid.organisaties.OrganisatieLijstFragment;
import be.kdg.kandoe.kandoeandroid.pojo.Gebruiker;
import be.kdg.kandoe.kandoeandroid.profiel.ProfielFragment;
import be.kdg.kandoe.kandoeandroid.subthema.SubthemaLijstFragment;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_OUT_METHOD = "log_out";
    private String[] mMenuItems;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinkedHashMap<String, Object> menuMap;
    private String token;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = this;
        mMenuItems = getResources().getStringArray(R.array.menu_item_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                (Toolbar) findViewById(R.id.toolbar),
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
//                getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        else if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, mMenuItems));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());



        token = SharedPreferencesMethods.getFromSharedPreferences(this, getString(R.string.token));

        menuMap = new LinkedHashMap<>();
        menuMap.put("Mijn profiel", ProfielFragment.newInstance());
        menuMap.put("Mijn subthemas", SubthemaLijstFragment.newInstance());
        menuMap.put("Cirkelsessies", new CirkelSessieLijstFragment());
        menuMap.put("Mijn organisaties", OrganisatieLijstFragment.newInstance());
        menuMap.put("Afmelden", LOG_OUT_METHOD);

        // On start the first screen should be open
        getFragmentManager().beginTransaction().replace(R.id.content_frame,
                (Fragment) menuMap.get(mMenuItems[0])).commit();
        setTitle(mMenuItems[0]);
        createSharedUserObject();

    }

    private void createSharedUserObject(){
        Retrofit retrofit = Authorization.authorize(mActivity);
        GebruikerAPI gebruikerAPI = retrofit.create(GebruikerAPI.class);
        Call<Gebruiker> call = gebruikerAPI.getGebruiker();
        call.enqueue(new Callback<Gebruiker>() {
            @Override
            public void onResponse(Response<Gebruiker> response, Retrofit retrofit) {
                Gebruiker gebruiker = response.body();
                SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(gebruiker);
                prefsEditor.putString("Gebruiker", json);
                prefsEditor.apply();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getBaseContext(),"failure gebruiker",Toast.LENGTH_SHORT).show();
            }
        });



    }
    private void logOut() {
        SharedPreferencesMethods.saveInSharedPreferences(this, getString(R.string.token), "");
        Intent intent = new Intent(this, FirstActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectedItem(position);
        }
    }

    private void selectedItem(int position) {
        if ((menuMap.get(mMenuItems[position])) instanceof Fragment) {
            Fragment fragment = (Fragment) menuMap.get(mMenuItems[position]);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            mDrawerList.setItemChecked(position, true);
            setTitle(mMenuItems[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
        else if ((menuMap.get(mMenuItems[position])).equals(LOG_OUT_METHOD)){
            finish();
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
            logOut();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
