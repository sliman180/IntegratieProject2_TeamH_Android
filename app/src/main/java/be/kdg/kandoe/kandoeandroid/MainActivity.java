package be.kdg.kandoe.kandoeandroid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import java.util.LinkedHashMap;

import be.kdg.kandoe.kandoeandroid.cirkelsessie.CirkelSessieLijstFragment;
import be.kdg.kandoe.kandoeandroid.helpers.SharedPreferencesMethods;
import be.kdg.kandoe.kandoeandroid.organisaties.OrganisatieLijstFragment;
import be.kdg.kandoe.kandoeandroid.profiel.ProfielFragment;
import be.kdg.kandoe.kandoeandroid.subthema.SubthemaLijstFragment;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_OUT_METHOD = "log_out";
    private String[] mMenuItems;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinkedHashMap<String, Object> menuMap;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_linear);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });

        token = SharedPreferencesMethods.getFromSharedPreferences(this, getString(R.string.token));

        menuMap = new LinkedHashMap<>();
        menuMap.put("Mijn profiel", ProfielFragment.newInstance());
        menuMap.put("Subthemas", SubthemaLijstFragment.newInstance());
        menuMap.put("Cirkelsessies", new CirkelSessieLijstFragment());
        menuMap.put("Mijn organisaties", OrganisatieLijstFragment.newInstance());
        menuMap.put("Afmelden", LOG_OUT_METHOD);

        // On start the first screen should be open
        getFragmentManager().beginTransaction().replace(R.id.content_frame,
                (Fragment) menuMap.get(mMenuItems[0])).commit();
        setTitle(mMenuItems[0]);
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
