package be.kdg.kandoe.kandoeandroid;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
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
import be.kdg.kandoe.kandoeandroid.api.GebruikerAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Authorization;
import be.kdg.kandoe.kandoeandroid.cirkelsessie.CirkelSessieLijstFragment;
import be.kdg.kandoe.kandoeandroid.deelnames.DeelnameLijstFragment;
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
    private Drawer result;
    private LinkedHashMap<String, Object> menuMap;
    private String[] mMenuItems;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        mMenuItems = getResources().getStringArray(R.array.menu_item_array);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //if you want to update the items at a later time it is recommended to keep it in a variable
        SecondaryDrawerItem item1 = new SecondaryDrawerItem().withName("Home").withIcon(GoogleMaterial.Icon.gmd_home);
        SecondaryDrawerItem profiel = new SecondaryDrawerItem().withName("Mijn profiel").withIcon(GoogleMaterial.Icon.gmd_account_circle);
        SecondaryDrawerItem subthemas = new SecondaryDrawerItem().withName("Mijn subthemas").withIcon(GoogleMaterial.Icon.gmd_style);
        SecondaryDrawerItem cirkelsessies = new SecondaryDrawerItem().withName("Cirkelsessies").withIcon(MaterialDesignIconic.Icon.gmi_dot_circle_alt);
        SecondaryDrawerItem mijnCirkelsessies = new SecondaryDrawerItem().withName("Mijn cirkelsessies").withIcon(GoogleMaterial.Icon.gmd_folder_shared);
        SecondaryDrawerItem mijnDeelnames = new SecondaryDrawerItem().withName("Mijn deelnames").withIcon(GoogleMaterial.Icon.gmd_playlist_add_check);
        SecondaryDrawerItem mijnOrganisaties = new SecondaryDrawerItem().withName("Mijn organisaties").withIcon(GoogleMaterial.Icon.gmd_account_balance);
        SecondaryDrawerItem instellingen = new SecondaryDrawerItem().withName("Instellingen").withIcon(GoogleMaterial.Icon.gmd_settings);
        SecondaryDrawerItem help = new SecondaryDrawerItem().withName("Help").withIcon(GoogleMaterial.Icon.gmd_help);

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.dark_)
                .addProfiles(
                        new ProfileDrawerItem().withName("Sliman Said").withEmail("sliman258@gmail.com")
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
        //create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        profiel,
                        new DividerDrawerItem(),
                        cirkelsessies,
                        new DividerDrawerItem(),
                        mijnDeelnames,
                        mijnOrganisaties,
                        mijnCirkelsessies,
                        subthemas,
                        new DividerDrawerItem(),
                        instellingen,
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
        menuMap = new LinkedHashMap<>();
        menuMap.put("Home", null);
        menuMap.put("Mijn profiel", ProfielFragment.newInstance());
        menuMap.put("Divider", null);
        menuMap.put("Cirkelsessies", new CirkelSessieLijstFragment());
        menuMap.put("Divider", null);
        menuMap.put("Mijn deelnames", DeelnameLijstFragment.newInstance());
        menuMap.put("Mijn organisaties", OrganisatieLijstFragment.newInstance());
        menuMap.put("Mijn cirkelsessies", null);
        menuMap.put("Mijn subthemas", SubthemaLijstFragment.newInstance());
        menuMap.put("Divider", null);
        menuMap.put("Instellingen",null);
        menuMap.put("Afmelden", LOG_OUT_METHOD);

        // On start the first screen should be open
        getFragmentManager().beginTransaction().replace(R.id.content_frame,
                (Fragment) menuMap.get(mMenuItems[4])).commit();
        setTitle(mMenuItems[4]);
        result.setSelection(cirkelsessies);
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
    @Override
    public void setTitle(CharSequence title) {
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    }

    private void logOut() {
        SharedPreferencesMethods.saveInSharedPreferences(this, getString(R.string.token), "");
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
