package be.kdg.kandoe.kandoeandroid.cirkelsessie;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.api.DeelnameAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Autorisatie;
import be.kdg.kandoe.kandoeandroid.helpers.adaptermodels.DeelnemersModel;
import be.kdg.kandoe.kandoeandroid.pojo.response.Deelname;
import be.kdg.kandoe.kandoeandroid.pojo.response.Gebruiker;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class DeelnameFragment extends Fragment {

    private Button buttonDeelname;
    private String cirkelsessieId;
    private Gebruiker gebruiker;
    private boolean isDeelnemer;
    private Button buttonVoegKaart;
    private Button buttonInfo;
    private String status;
    private TextView beurtTextview;
    private ChatFragment chatFragment;
    private Activity mActivity;
    private int maxAantalDeelnemers = 0;
    private DeelnemersAdapter adapter = null;
    private Handler handler;

    private ArrayList<DeelnemersModel> list = new ArrayList<>();

    public DeelnameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_deelname, container, false);
        buttonDeelname = (Button) v.findViewById(R.id.buttonDeelname);
        buttonInfo = (Button) v.findViewById(R.id.buttonInfo);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        isDeelnemer = false;
        handler = new Handler();
        cirkelsessieId = ((CirkelsessieActivity) getActivity()).getCirkelsessieId();
        gebruiker = ((CirkelsessieActivity) getActivity()).getGebruiker();
        status = ((CirkelsessieActivity) getActivity()).getStatus();
        buttonVoegKaart = (Button) getActivity().findViewById(R.id.buttonAddKaart);
        beurtTextview = (TextView) getActivity().findViewById(R.id.isBeurt);
        chatFragment = (ChatFragment) getFragmentManager().findFragmentById(R.id.chat_fragment);
        onClickDeelnemen();
        checkStatus();
        createAdapter();
    }

    public void checkStatus(){
        switch (status) {
            case "GESLOTEN":
                buttonInfo.setText(R.string.sessie_gesloten);
                buttonInfo.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                buttonInfo.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_red_500));
                buttonInfo.setEnabled(false);
                buttonDeelname.setText(R.string.geen_deelname);
                buttonDeelname.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                buttonDeelname.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_red_500));
                buttonDeelname.setEnabled(false);
                beurtTextview.setVisibility(View.GONE);
                checkIsDeelnemer();
                checkIsNotDeelnemer();
                break;
            case "OPEN":
                buttonInfo.setText(R.string.sessie_open);
                buttonInfo.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                buttonInfo.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_green_500));
                buttonInfo.setEnabled(false);
                checkIsDeelnemer();
                checkIsNotDeelnemer();
                beurtTextview.setVisibility(View.GONE);
                buttonVoegKaart.setEnabled(false);
                chatFragment.getChatButton().setEnabled(false);
                buttonVoegKaart.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                chatFragment.getChatButton().getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                break;
            case "GESTART":
                buttonInfo.setText(R.string.sessie_gestart);
                buttonInfo.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                buttonInfo.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_green_500));
                buttonInfo.setEnabled(false);
                buttonDeelname.setText(R.string.geen_deelname);
                buttonDeelname.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                buttonDeelname.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_red_500));
                buttonDeelname.setEnabled(false);
                checkIsDeelnemer();
                checkIsNotDeelnemer();
                break;
            case "BEEINDIGD":
                buttonInfo.setText(R.string.sessie_beindigd);
                buttonInfo.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                buttonInfo.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_black_1000));
                buttonInfo.setEnabled(false);
                buttonDeelname.setText(R.string.geen_deelname);
                buttonDeelname.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                buttonDeelname.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_red_500));
                buttonDeelname.setEnabled(false);
                isDeelnemer = false;
                buttonVoegKaart.setEnabled(false);
                buttonVoegKaart.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                beurtTextview.setVisibility(View.GONE);
                checkIsDeelnemer();
                break;

        }
    }

    public void checkIsNotDeelnemer(){
        if (!isDeelnemer) {
            buttonVoegKaart.setEnabled(false);
            buttonVoegKaart.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void checkIsDeelnemer(){
        Retrofit retrofit = Autorisatie.authorize(getActivity());
        DeelnameAPI deelnameAPI = retrofit.create(DeelnameAPI.class);
        Call<List<Deelname>> call = deelnameAPI.getDeelnamesVanCirkelsessie(cirkelsessieId);
        call.enqueue(new Callback<List<Deelname>>() {
            @Override
            public void onResponse(Response<List<Deelname>> response, Retrofit retrofit) {
                List<Deelname> deelnames = response.body();
                for (int i = 0; i < deelnames.size(); i++) {
                    if (deelnames.get(i).getCirkelsessie().getId() == Integer.parseInt(cirkelsessieId)) {
                        if (deelnames.get(i).getGebruiker().getId() == gebruiker.getId()) {
                            buttonDeelname.setText(R.string.deelgenomen);
                            buttonDeelname.setTextColor(Color.WHITE);
                            buttonDeelname.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                            buttonDeelname.setEnabled(false);
                            isDeelnemer = true;
                            if (status.equals("GESTART") && isDeelnemer) {
                                buttonVoegKaart.setEnabled(true);
                                buttonVoegKaart.getBackground().setColorFilter(null);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("checkIsDeelnemer:", t.getMessage());
            }
        });
    }

    public void onClickDeelnemen(){
        buttonDeelname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.deelnemen_y_n);
                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Retrofit retrofit = Autorisatie.authorize(getActivity());
                        DeelnameAPI deelnameAPI = retrofit.create(DeelnameAPI.class);
                        Call<Void> call = deelnameAPI.doeDeelname(cirkelsessieId);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Response<Void> response, Retrofit retrofit) {
                                buttonDeelname.setText(R.string.deelgenomen);
                                buttonDeelname.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                                buttonDeelname.setEnabled(false);
                                isDeelnemer = true;
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Log.d("onClickDeelnemen:", t.getMessage());

                            }
                        });

                    }
                });
                builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    public void getData(){
        DeelnameAPI deelnameAPI =
                Autorisatie.authorize(getActivity()).create(DeelnameAPI.class);
        Call<List<Deelname>> call = deelnameAPI.getDeelnamesVanCirkelsessie(cirkelsessieId);
        call.enqueue(new Callback<List<Deelname>>() {
            @Override
            public void onResponse(Response<List<Deelname>> response, Retrofit retrofit) {
                addData(response);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mActivity.getBaseContext(), "failure",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createAdapter(){
        ListView listview = null;
        if (getView() != null)
            listview = (ListView) getView().findViewById(R.id.listview_deelnemers);

        if (mActivity != null && adapter ==null) {
            adapter = new DeelnemersAdapter(mActivity.getBaseContext(),
                    R.layout.item_list_deelnemers, list);
        }

        if (listview != null) {
            listview.setAdapter(adapter);
        }
    }

    public void addData(Response<List<Deelname>> response){
        list.clear();
        SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        for (int i = 0; i < response.body().size(); ++i) {
            Date date = new Date(response.body().get(i).getDatum());
            DeelnemersModel model = new DeelnemersModel(R.drawable.ic_account_box
                    ,response.body().get(i).getGebruiker().getGebruikersnaam()
                    ,response.body().get(i).isAanDeBeurt()
                    ,ft.format(date)
                    ,String.valueOf(response.body().get(i).getAangemaakteKaarten()));
            list.add(model);
        }
        int aantalDeelnemers = response.body().size();
        if((maxAantalDeelnemers < aantalDeelnemers) && adapter !=null){
            adapter.notifyDataSetChanged();
            maxAantalDeelnemers = aantalDeelnemers;
        }
        if(adapter != null && status.equals("GESTART")){
        adapter.notifyDataSetChanged();
        }
    }


    private class DeelnemersAdapter extends ArrayAdapter<DeelnemersModel> {

        private Context context;
        private ArrayList<DeelnemersModel> modelsArrayList;

        public DeelnemersAdapter(Context context, int textViewResourceId, ArrayList<DeelnemersModel> modelsArrayList) {

            super(context, textViewResourceId, modelsArrayList);

            this.context = context;
            this.modelsArrayList = modelsArrayList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 1. Create inflater
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            // 2. Get rowView from inflater

            View rowView = null;
            if(!modelsArrayList.get(position).isGroupHeader()){
                rowView = inflater.inflate(R.layout.item_list_deelnemers, parent, false);

                // 3. Get icon,title & counter views from the rowView
                ImageView imgView = (ImageView) rowView.findViewById(R.id.item_icon);
                TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
                TextView dateView = (TextView) rowView.findViewById(R.id.date);
                TextView beurtView = (TextView) rowView.findViewById(R.id.beurt);
                TextView aantalKaarten = (TextView) rowView.findViewById(R.id.aantalKaarten);

                // 4. Set the text for textView
                imgView.setImageResource(modelsArrayList.get(position).getIcon());
                String titleText = modelsArrayList.get(position).getTitle();
                titleView.setText(titleText);
                String dateText = "Deelgenomen op : " + modelsArrayList.get(position).getDate();
                dateView.setText(dateText);
                if(status.equals("GESTART")){
                    if(modelsArrayList.get(position).getBeurt()){
                        beurtView.setText("Is aan de beurt");
                        beurtView.setTextColor(Color.GREEN);
                    }else {
                        beurtView.setText("Is niet aan de beurt");
                        beurtView.setTextColor(Color.RED);
                    }
                }else {
                    beurtView.setVisibility(View.GONE);
                }
                aantalKaarten.setText("Gemaakte kaarten: " + modelsArrayList.get(position).getAantalKaarten());

            }

            // 5. retrn rowView
            return rowView;
        }
    }

    public boolean isDeelnemer() {
        return isDeelnemer;
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(chatRunnable);

    }

    @Override
    public void onResume() {
        super.onResume();

        handler.postDelayed(chatRunnable, 1000);
    }

    private final Runnable chatRunnable = new Runnable() {
        @Override
        public void run()
        {
            getData();
            //Do task
            handler.postDelayed(chatRunnable, 1000);
        }
    };
}
