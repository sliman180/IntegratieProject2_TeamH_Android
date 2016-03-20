package be.kdg.kandoe.kandoeandroid.cirkelsessie;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.api.CirkelsessieAPI;
import be.kdg.kandoe.kandoeandroid.api.DeelnameAPI;
import be.kdg.kandoe.kandoeandroid.api.SpelkaartAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Autorisatie;
import be.kdg.kandoe.kandoeandroid.helpers.adaptermodels.Parent;
import be.kdg.kandoe.kandoeandroid.helpers.SharedPreferencesMethods;
import be.kdg.kandoe.kandoeandroid.kaart.KaartActivity;
import be.kdg.kandoe.kandoeandroid.pojo.response.Cirkelsessie;
import be.kdg.kandoe.kandoeandroid.pojo.response.Deelname;
import be.kdg.kandoe.kandoeandroid.pojo.response.Gebruiker;
import be.kdg.kandoe.kandoeandroid.pojo.request.KaartRequest;
import be.kdg.kandoe.kandoeandroid.pojo.response.Kaart;
import be.kdg.kandoe.kandoeandroid.pojo.response.Spelkaart;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Stelt de cirkel voor en alle kaarten die er worden aangemaakt
 */
@SuppressWarnings("ALL")
public class CirkelsessieActivity extends AppCompatActivity {

    private CirkelsessieListAdapter cirkelsessieListAdapter;
    private String cirkelsessieId;
    private int maxAantalCirkels;
    private Activity mActivity;
    private ExpandableListView elv;
    private CirkelsessieFragment fragmentCirkelsessie;
    private DeelnameFragment deelnameFragment;
    private Handler handler;
    private Gebruiker gebruiker;
    private boolean beurt;
    private final ArrayList<Spelkaart> spelkaarten = new ArrayList<>();
    private String status;
    private long datum;
    private final static long REFRESH_TIME = 2000;
    private Button buttonVoegKaart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_cirkelsessie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActivity = this;
        beurt = false;


        TabHost host = (TabHost)findViewById(R.id.tabHost);
        if(host !=null)
            host.setup();
        //Tab 1
        TabHost.TabSpec spec;
        if (host != null) {
            spec = host.newTabSpec(getString(R.string.deelnemers));
            spec.setContent(R.id.tab1);
            spec.setIndicator(getString(R.string.deelnemers));
            host.addTab(spec);
            //Tab 2
            spec = host.newTabSpec(getString(R.string.session));
            spec.setContent(R.id.tab2);
            spec.setIndicator(getString(R.string.session));
            host.addTab(spec);

            //Tab 3
            spec = host.newTabSpec(getString(R.string.chat));
            spec.setContent(R.id.tab3);
            spec.setIndicator(getString(R.string.chat));
            host.addTab(spec);
        }

        cirkelsessieId = extras.getString("cirkelsessieId");
        setTitle(extras.getString("cirkelsessieTitle"));
        status = extras.getString("status");
        datum = extras.getLong("datum");

        if(toolbar != null){
            toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        elv = (ExpandableListView) findViewById(R.id.card_list);
        cirkelsessieListAdapter = new CirkelsessieListAdapter(getBaseContext());
        elv.setAdapter(cirkelsessieListAdapter);

        fragmentCirkelsessie = (CirkelsessieFragment) getFragmentManager().findFragmentById(R.id.cirkelsessie_fragment);
        deelnameFragment = (DeelnameFragment) getFragmentManager().findFragmentById(R.id.deelname_fragment);
        buttonVoegKaart = (Button) findViewById(R.id.buttonAddKaart);
        handler = new Handler();

        String json = SharedPreferencesMethods.getFromSharedPreferences(mActivity, mActivity.getString(R.string.gebruiker));
        Gson gson = new Gson();
        gebruiker = gson.fromJson(json, Gebruiker.class);


    }

    public String getStatus() {
        return status;
    }

    public Gebruiker getGebruiker() {
        return gebruiker;
    }

    public boolean isBeurt() {
        return beurt;
    }

    private void checkBeurt(){
        Retrofit retrofit = Autorisatie.authorize(mActivity);
        DeelnameAPI deelnameAPI = retrofit.create(DeelnameAPI.class);
        Call<List<Deelname>> call = deelnameAPI.getDeelnamesVanCirkelsessie(cirkelsessieId);
        call.enqueue(new Callback<List<Deelname>>() {
            @Override
            public void onResponse(Response<List<Deelname>> response, Retrofit retrofit) {
                List<Deelname> deelnames = response.body();
                for (int x = 0; x < deelnames.size(); x++) {
                    if (deelnames.get(x).getGebruiker().getId() == gebruiker.getId()) {
                        beurt = deelnames.get(x).isAanDeBeurt();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void voegKaartToe(View v) {
        final Dialog newCardDialog = new Dialog(this);
        newCardDialog.setContentView(R.layout.dialog_custom);
        newCardDialog.setTitle(R.string.vul_kaart_tekst);

        final EditText newCardInput = (EditText) newCardDialog.findViewById(R.id.dialogtext);
        Button okButton = (Button) newCardDialog.findViewById(R.id.dialogButtonOK);
        Button annuleerButton = (Button) newCardDialog.findViewById(R.id.dialogButtonAnnuleer);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = Autorisatie.authorize(mActivity);
                final CirkelsessieAPI cirkelsessieAPI = retrofit.create(CirkelsessieAPI.class);
                KaartRequest kaart = new KaartRequest(newCardInput.getText().toString(), newCardInput.getText().toString(), false);
                kaart.setGebruiker(gebruiker.getId());
                Call<Kaart> call = cirkelsessieAPI.createSpelKaart(cirkelsessieId, kaart);

                call.enqueue(new Callback<Kaart>() {
                    @Override
                    public void onResponse(Response<Kaart> response, Retrofit retrofit) {
                        fragmentCirkelsessie.getData();
                        newCardDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(mActivity.getBaseContext(), R.string.kaart_niet_aangemaakt,
                                Toast.LENGTH_SHORT).show();
                        newCardDialog.dismiss();
                    }
                });
            }
        });
        annuleerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCardDialog.cancel();
            }
        });
        newCardDialog.show();
    }

    public String getCirkelsessieId() {
        return cirkelsessieId;
    }

    public void changeCardPosition(Spelkaart spelkaart){
        Retrofit retrofit = Autorisatie.authorize(mActivity);
        SpelkaartAPI spelkaartAPI = retrofit.create(SpelkaartAPI.class);
        Call<Spelkaart> call = spelkaartAPI.verschuif(String.valueOf(spelkaart.getId()));
        call.enqueue(new Callback<Spelkaart>() {
            @Override
            public void onResponse(Response<Spelkaart> response, Retrofit retrofit) {
                Toast.makeText(mActivity.getBaseContext(), R.string.kaart_verplaatst,
                        Toast.LENGTH_SHORT).show();
                cirkelsessieListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mActivity.getBaseContext(), R.string.kaart_n_verplaatst,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Stelt een adapter voor die data ophaalt en toevoegt aan de sessie
     */
    private class CirkelsessieListAdapter extends BaseExpandableListAdapter {

        private final Context context;

        private final ArrayList<Parent> parentItems;

        public CirkelsessieListAdapter(Context context) {
            this.context = context;
            this.parentItems = new ArrayList<>();
            getCirkelData();
        }

        private void getSpelkaartenData(){
            Retrofit retrofit = Autorisatie.authorize(mActivity);
            CirkelsessieAPI cirkelsessieAPI = retrofit.create(CirkelsessieAPI.class);
            Call<List<Spelkaart>> call = cirkelsessieAPI.getSpelkaarten(cirkelsessieId);
            call.enqueue(new Callback<List<Spelkaart>>() {
                @Override
                public void onResponse(Response<List<Spelkaart>> response, Retrofit retrofit) {
                    parentItems.clear();
                    spelkaarten.clear();
                    spelkaarten.addAll(response.body());
                    for (int i = 1; i < maxAantalCirkels + 1; i++) {
                        final ArrayList<Spelkaart> tempSpelkaarten = new ArrayList<>();
                        final Parent parent = new Parent();
                        for (int j = 0; j < spelkaarten.size(); j++) {
                            Spelkaart spelkaart = spelkaarten.get(j);
                            if (spelkaart.getPositie() == i) {
                                tempSpelkaarten.add(spelkaart);
                            }
                        }
                        parent.setChildren(tempSpelkaarten);
                        parent.setCircleLength(maxAantalCirkels - i);
                        parentItems.add(parent);
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }

        private void getCirkelData(){
            Retrofit retrofit = Autorisatie.authorize(mActivity);
            CirkelsessieAPI cirkelsessieAPI = retrofit.create(CirkelsessieAPI.class);
            Call<Cirkelsessie> call = cirkelsessieAPI.getCirkelsessie(cirkelsessieId);
            call.enqueue(new Callback<Cirkelsessie>() {
                @Override
                public void onResponse(Response<Cirkelsessie> response, Retrofit retrofit) {
                    if (response.body() != null) {
                        maxAantalCirkels = response.body().getAantalCirkels();

                        Date date = new Date(datum);
                        Date dateNow = new Date();
                        if (dateNow.after(date) && response.body().getStatus().equals("OPEN")) {
                            status = "GESTART";
                        }else {
                            status = response.body().getStatus();
                        }

                    }
                }
                @Override
                public void onFailure(Throwable t) {
                    Log.d("failure", t.getMessage());
                }

            });
        }

        @Override
        public int getGroupCount() {
            return parentItems.size();
        }


        @Override
        public int getChildrenCount(int i) {
            return parentItems.get(i).getChildren().size();
        }

        @Override
        public Parent getGroup(int i) {
            return parentItems.get(i);
        }

        @Override
        public Spelkaart getChild(int i, int i1) {
            return parentItems.get(i).getChildren().get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.cirkel_header_list, viewGroup ,false);
                view.setBackgroundColor(Color.WHITE);
            }
                TextView textView = (TextView) view.findViewById(R.id.title_header);
                textView.setText(String.valueOf(getGroupCount() - i));
                ImageView imageView = (ImageView) view.findViewById(R.id.image_header);
                imageView.setBackgroundResource(R.drawable.cirkel);
             elv.setGroupIndicator(null);
             elv.expandGroup(i);
            return view;
        }

        @Override
        public View getChildView(final int groupPos, final int childPos, boolean b, View view, ViewGroup viewGroup) {
            final String childText = getChild(groupPos,childPos).getKaart().getTekst();

            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.cirkel_list_child, viewGroup,false);
            }

            final TextView txtListChild = (TextView) view
                    .findViewById(R.id.kaart);
            ImageView imageView = (ImageView) view.findViewById(R.id.item_icon_kaart);

            imageView.setImageResource(R.drawable.ic_picture_in_picture);
            txtListChild.setText(childText);
            txtListChild.setTextColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity.getBaseContext(), KaartActivity.class);
                    intent.putExtra("spelkaartId", String.valueOf(getChild(groupPos, childPos).getId()));
                    intent.putExtra("spelkaartTitle", getChild(groupPos, childPos).getKaart().getTekst());
                    startActivity(intent);
                }
            });

            final View finalView = view;
            if(deelnameFragment.isDeelnemer()){
            txtListChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(beurt && status.equals("GESTART")){
                    finalView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setTitle(R.string.verplaats_kaart_q);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finalView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.md_grey_400));
                            Spelkaart spelkaart = getChild(groupPos, childPos);
                            if (spelkaart.getPositie() == maxAantalCirkels) {
                                Toast.makeText(getBaseContext(), R.string.max_positie,
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Parent oldParent = getGroup(groupPos);
                                oldParent.getChildren().remove(spelkaart);
                                changeCardPosition(spelkaart);
                                notifyDataSetChanged();
                            }
                        }
                    });
                    builder.setNegativeButton(R.string.annuleer, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finalView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.md_grey_400));
                        }
                    });

                    builder.show();
                    }else if(!beurt && status.equals("GESTART")){
                        Toast.makeText(getBaseContext(),R.string.n_beurt,Toast.LENGTH_SHORT).show();
                    }else if(status.equals("BEEINDIGD")){
                        Toast.makeText(getBaseContext(),"Cirkelsessie is beëindigd",Toast.LENGTH_SHORT).show();
                        buttonVoegKaart.setEnabled(false);
                        buttonVoegKaart.getBackground().setColorFilter(ContextCompat.getColor(mActivity, R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);

                    }else {
                        Toast.makeText(getBaseContext(),"Cirkelsessie is niet gestart",Toast.LENGTH_SHORT).show();
                    }
                }
            });}

            return view;

        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

    }

    private final Runnable cirkelRunnable = new Runnable() {
        @Override
        public void run()
        {
            cirkelsessieListAdapter.getCirkelData();
            checkBeurt();
            cirkelsessieListAdapter.getSpelkaartenData();

            //Do task
            handler.postDelayed(cirkelRunnable, REFRESH_TIME);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(cirkelRunnable);

    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(cirkelRunnable, REFRESH_TIME);
    }

}
