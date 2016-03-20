package be.kdg.kandoe.kandoeandroid;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import java.util.LinkedHashMap;

import be.kdg.kandoe.kandoeandroid.cirkelsessie.CirkelSessieLijstFragment;
import be.kdg.kandoe.kandoeandroid.deelnames.DeelnameLijstFragment;
import be.kdg.kandoe.kandoeandroid.help.HelpFragment;
import be.kdg.kandoe.kandoeandroid.helpers.SharedPreferencesMethods;
import be.kdg.kandoe.kandoeandroid.hoofdthema.HoofdThemaLijstFragment;
import be.kdg.kandoe.kandoeandroid.organisaties.OrganisatieLijstFragment;
import be.kdg.kandoe.kandoeandroid.pojo.response.Gebruiker;
import be.kdg.kandoe.kandoeandroid.profiel.ProfielFragment;
import be.kdg.kandoe.kandoeandroid.subthema.SubthemaLijstFragment;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_OUT_METHOD = "log_out";
    private Drawer result;
    private LinkedHashMap<String, Object> menuMap;
    private String[] mMenuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Activity mActivity = this;
        Gson gson = new Gson();
        String json = SharedPreferencesMethods.getFromSharedPreferences(mActivity,getString(R.string.gebruiker));

        Gebruiker gebruiker = gson.fromJson(json, Gebruiker.class);
        mMenuItems = getResources().getStringArray(R.array.menu_item_array);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //if you want to update the items at a later time it is recommended to keep it in a variable
        SecondaryDrawerItem profiel = new SecondaryDrawerItem().withName(R.string.mijn_profiel).withIcon(GoogleMaterial.Icon.gmd_account_circle);
        SecondaryDrawerItem subthemas = new SecondaryDrawerItem().withName(R.string.mijn_subthemas).withIcon(GoogleMaterial.Icon.gmd_style);
        SecondaryDrawerItem cirkelsessies = new SecondaryDrawerItem().withName(R.string.cirkelsessies).withIcon(MaterialDesignIconic.Icon.gmi_dot_circle_alt);
        SecondaryDrawerItem hoofdthemas = new SecondaryDrawerItem().withName(R.string.mijn_hoofdthemas).withIcon(GoogleMaterial.Icon.gmd_turned_in);
        SecondaryDrawerItem mijnDeelnames = new SecondaryDrawerItem().withName(R.string.mijn_deelnames).withIcon(GoogleMaterial.Icon.gmd_playlist_add_check);
        SecondaryDrawerItem mijnOrganisaties = new SecondaryDrawerItem().withName(R.string.mijn_organisaties).withIcon(GoogleMaterial.Icon.gmd_account_balance);
        SecondaryDrawerItem help = new SecondaryDrawerItem().withName(R.string.help).withIcon(GoogleMaterial.Icon.gmd_help);

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.dark_)
                .addProfiles(
                        new ProfileDrawerItem().withName(gebruiker.getGebruikersnaam()).withEmail(gebruiker.getGebruikersnaam() + "@kandoe.be")
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
        //create the drawer and remember the `Drawer` result object
        assert toolbar != null;
        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .addDrawerItems(
                        profiel,
                        new DividerDrawerItem(),
                        cirkelsessies,
                        new DividerDrawerItem(),
                        mijnDeelnames,
                        mijnOrganisaties,
                        hoofdthemas,
                        subthemas,
                        new DividerDrawerItem(),
                        help
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                       // Toast.makeText(getBaseContext(),String.valueOf(position),Toast.LENGTH_SHORT).show();
                        selectedItem(position);

                        return true;
                    }
                })
                .build();
        result.addStickyFooterItem(new SecondaryDrawerItem().withName("Afmelden").withIcon(GoogleMaterial.Icon.gmd_power_settings_new));

        result.getDrawerLayout().setId(R.id.drawer);

        menuMap = new LinkedHashMap<>();
        menuMap.put(getString(R.string.mijn_profiel), ProfielFragment.newInstance());
        menuMap.put(getString(R.string.divider), null);
        menuMap.put(getString(R.string.cirkelsessies), new CirkelSessieLijstFragment());
        menuMap.put(getString(R.string.divider), null);
        menuMap.put(getString(R.string.mijn_deelnames), DeelnameLijstFragment.newInstance());
        menuMap.put(getString(R.string.mijn_organisaties), OrganisatieLijstFragment.newInstance());
        menuMap.put(getString(R.string.mijn_hoofdthemas), HoofdThemaLijstFragment.newInstance());
        menuMap.put(getString(R.string.mijn_subthemas), SubthemaLijstFragment.newInstance());
        menuMap.put(getString(R.string.divider), null);
        menuMap.put(getString(R.string.help), HelpFragment.newInstance());
        menuMap.put(getString(R.string.afmelden), LOG_OUT_METHOD);

        // On start the first screen should be open
        getFragmentManager().beginTransaction().replace(R.id.content_frame,
                (Fragment) menuMap.get(mMenuItems[3])).commit();
        setTitle(mMenuItems[3]);
        result.setSelection(cirkelsessies);
    }

    @Override
    public void setTitle(CharSequence title) {
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    }

    public void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }
    private void logOut() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();
        for (String mMenuItem : mMenuItems) {
            if((menuMap.get(mMenuItem)) instanceof Fragment){
            getFragmentManager().beginTransaction().remove((Fragment) menuMap.get(mMenuItem)).commit();
            }
        }
        freeMemory();
        finish();
        Intent intent = new Intent(this, FirstActivity.class);
        startActivity(intent);
    }

    private void selectedItem(int position) {
        if (position == -1){
            finish();
            result.closeDrawer();
            logOut();
        }else if ((menuMap.get(mMenuItems[position])) instanceof Fragment) {
            Fragment fragment = (Fragment) menuMap.get(mMenuItems[position]);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
            setTitle(mMenuItems[position]);
            result.closeDrawer();
        }
    }

}
