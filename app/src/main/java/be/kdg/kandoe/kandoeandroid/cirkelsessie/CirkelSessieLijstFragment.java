package be.kdg.kandoe.kandoeandroid.cirkelsessie;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.api.CirkelsessieAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Autorisatie;
import be.kdg.kandoe.kandoeandroid.helpers.adaptermodels.CirkelsessieModel;
import be.kdg.kandoe.kandoeandroid.pojo.response.Cirkelsessie;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CirkelSessieLijstFragment extends Fragment {

    private Intent intent;
    private Activity mActivity;
    private ListView listView;
    private View viewToChange;
    private int tempPosition = 0;
    private LinearLayout linlaHeaderProgress;
    private TextView textViewAantal;
    private Button buttonOpen;
    private Button buttonGesloten;
    private Button buttonEnd;
    private Button buttonGestart;
    private CirkelsessieAdapter adapter = null;
    private final ArrayList<CirkelsessieModel> list = new ArrayList<>();
    private final ArrayList<Cirkelsessie> list2 = new ArrayList<>();
    private final SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy HH:mm");


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            mActivity = (Activity) context;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_cirkelsessie_lijst, null);
        listView = (ListView) v.findViewById(R.id.listview);
        textViewAantal = (TextView) v.findViewById(R.id.cirkelsessie_header);
        listView.setSelector(R.drawable.my_selector);
        listView.setVisibility(View.GONE);
        linlaHeaderProgress = (LinearLayout) v.findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.VISIBLE);

        buttonGestart = (Button) v.findViewById(R.id.buttonGestart);
        buttonOpen = (Button) v.findViewById(R.id.buttonOpen);
        buttonGesloten = (Button) v.findViewById(R.id.buttonGesloten);
        buttonEnd = (Button) v.findViewById(R.id.buttonEnd);

        buttonGestart.setBackgroundResource(R.color.colorPrimary);
        buttonEnd.setBackgroundResource(R.color.colorPrimary);
        buttonGesloten.setBackgroundResource(R.color.colorPrimary);
        buttonOpen.setBackgroundResource(R.color.colorPrimary);


        buttonInit();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null)
            mActivity = getActivity();
        intent = new Intent(mActivity.getBaseContext(), CirkelsessieActivity.class);
        createAdapter();
        getData();

    }

    private void buttonInit(){
        buttonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        buttonGesloten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CirkelsessieAPI cirkelsessieAPI =
                        Autorisatie.authorize(getActivity()).create(CirkelsessieAPI.class);
                Call<List<Cirkelsessie>> call = cirkelsessieAPI.getCirkelsessiesGesloten();
                call.enqueue(new Callback<List<Cirkelsessie>>() {
                    @Override
                    public void onResponse(Response<List<Cirkelsessie>> response, Retrofit retrofit) {
                        addData(response);
                        buttonGesloten.setBackgroundResource(R.color.colorAccent);
                        buttonOpen.setBackgroundResource(R.color.colorPrimary);
                        buttonEnd.setBackgroundResource(R.color.colorPrimary);
                        buttonGestart.setBackgroundResource(R.color.colorPrimary);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(mActivity.getBaseContext(), "failure",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        buttonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CirkelsessieAPI cirkelsessieAPI =
                        Autorisatie.authorize(getActivity()).create(CirkelsessieAPI.class);
                Call<List<Cirkelsessie>> call = cirkelsessieAPI.getCirkelsessiesEnded();
                call.enqueue(new Callback<List<Cirkelsessie>>() {
                    @Override
                    public void onResponse(Response<List<Cirkelsessie>> response, Retrofit retrofit) {
                        addData(response);
                        buttonEnd.setBackgroundResource(R.color.colorAccent);
                        buttonOpen.setBackgroundResource(R.color.colorPrimary);
                        buttonGesloten.setBackgroundResource(R.color.colorPrimary);
                        buttonGestart.setBackgroundResource(R.color.colorPrimary);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(mActivity.getBaseContext(), "failure",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        buttonGestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CirkelsessieAPI cirkelsessieAPI =
                        Autorisatie.authorize(getActivity()).create(CirkelsessieAPI.class);
                Call<List<Cirkelsessie>> call = cirkelsessieAPI.getCirkelsessiesStarted();
                call.enqueue(new Callback<List<Cirkelsessie>>() {
                    @Override
                    public void onResponse(Response<List<Cirkelsessie>> response, Retrofit retrofit) {
                        addData(response);
                        buttonGestart.setBackgroundResource(R.color.colorAccent);
                        buttonEnd.setBackgroundResource(R.color.colorPrimary);
                        buttonOpen.setBackgroundResource(R.color.colorPrimary);
                        buttonGesloten.setBackgroundResource(R.color.colorPrimary);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(mActivity.getBaseContext(), "failure",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void getData(){
        CirkelsessieAPI cirkelsessieAPI =
                Autorisatie.authorize(getActivity()).create(CirkelsessieAPI.class);
        Call<List<Cirkelsessie>> call = cirkelsessieAPI.getCirkelsessiesGepland();
        call.enqueue(new Callback<List<Cirkelsessie>>() {
            @Override
            public void onResponse(Response<List<Cirkelsessie>> response, Retrofit retrofit) {
                addData(response);

                buttonOpen.setBackgroundResource(R.color.colorAccent);
                buttonGesloten.setBackgroundResource(R.color.colorPrimary);
                buttonEnd.setBackgroundResource(R.color.colorPrimary);
                buttonGestart.setBackgroundResource(R.color.colorPrimary);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mActivity.getBaseContext(), "failure",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addData(Response<List<Cirkelsessie>> response){
        list.clear();
        list2.clear();
        for (int i = 0; i < response.body().size(); ++i) {
            CirkelsessieModel cirkelsessieModel = new CirkelsessieModel(R.drawable.ic_arrow_right
                    ,response.body().get(i).getNaam()
                    ,response.body().get(i).getGebruiker().getGebruikersnaam()
                    ,String.valueOf(i + 1)
                    ,response.body().get(i).getStartDatum()
                    ,response.body().get(i).getSubthema()
                    );

            list.add(cirkelsessieModel);
            list2.add(response.body().get(i));
        }
        if(adapter !=null){
            adapter.notifyDataSetChanged();
            String textAantal = "Aantal : " + String.valueOf(list2.size());
            textViewAantal.setText(textAantal);
        }
    }
    private void createAdapter(){
        ListView listview = null;
        if (getView() != null)
            listview = (ListView) getView().findViewById(R.id.listview);

        if (mActivity != null && adapter ==null) {
            adapter = new CirkelsessieAdapter(mActivity.getBaseContext(),
                    R.layout.item_list_cirkelsessie, list);
        }

        if (listview != null) {
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    intent.putExtra("cirkelsessieId", String.valueOf(list2.get(position).getId()));
                    intent.putExtra("cirkelsessieTitle", String.valueOf(list2.get(position).getNaam()));
                    Date date = new Date(list2.get(position).getStartDatum());
                    Date dateNow = new Date();
                    if (dateNow.after(date)) {
                        if (list2.get(position).getStatus().equals("BEEINDIGD")) {
                            intent.putExtra("status", list2.get(position).getStatus());
                            intent.putExtra("datum", list2.get(position).getStartDatum());
                        } else {
                            intent.putExtra("status", "GESTART");
                            intent.putExtra("datum", list2.get(position).getStartDatum());
                        }
                    } else {
                        intent.putExtra("status", list2.get(position).getStatus());
                    }
                    startActivity(intent);
                    viewToChange = view;
                    tempPosition = position;
                    viewToChange.setBackgroundResource(R.color.lightBlue);
                }

            });
        }

        linlaHeaderProgress.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);


    }

    @Override
    public void onResume() {
        super.onResume();
        if(viewToChange != null)
        if ((tempPosition % 2 == 1)) {
            viewToChange.setBackgroundResource(R.color.colorPrimary);
        } else {
            viewToChange.setBackgroundColor(Color.WHITE);
        }
        buttonEnd.setBackgroundResource(R.color.colorPrimary);
        buttonGesloten.setBackgroundResource(R.color.colorPrimary);
        getData();

    }

    private class CirkelsessieAdapter extends ArrayAdapter<CirkelsessieModel> {
        private final Context context;
        private final ArrayList<CirkelsessieModel> modelsArrayList;

        public CirkelsessieAdapter(Context context,int textViewResourceId, ArrayList<CirkelsessieModel> modelsArrayList) {
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
                rowView = inflater.inflate(R.layout.item_list_cirkelsessie, parent, false);

                // 3. Get icon,title & counter views from the rowView
                RelativeLayout relativeLayout = (RelativeLayout) rowView.findViewById(R.id.backgroundLayout);
                View view = rowView.findViewById(R.id.frontLine);
                ImageView imgView = (ImageView) rowView.findViewById(R.id.item_icon);
                TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
                TextView organisatorView = (TextView) rowView.findViewById(R.id.item_organisator);
                TextView counterView = (TextView) rowView.findViewById(R.id.item_counter);
                TextView datumView = (TextView) rowView.findViewById(R.id.item_datum);
                TextView subthemaView = (TextView) rowView.findViewById(R.id.item_subthema);

                // 4. Set the text for textView
                imgView.setImageResource(modelsArrayList.get(position).getIcon());
                titleView.setText(modelsArrayList.get(position).getTitle());
                organisatorView.setText(context.getString(R.string.organisator) + modelsArrayList.get(position).getOrganisator());
                counterView.setText(modelsArrayList.get(position).getCounter());
                Date date = new Date(modelsArrayList.get(position).getDatum());
                datumView.setText(context.getString(R.string.startdatum) + String.valueOf(ft.format(date)));
                if(modelsArrayList.get(position).getSubthema() != null){
                subthemaView.setText(context.getString(R.string.subthema)+ ":" + modelsArrayList.get(position).getSubthema().getNaam());
                }else {
                    subthemaView.setVisibility(View.GONE);
                }

                if (position % 2 == 1) {
                    rowView.setBackgroundResource(R.color.colorPrimary);
                    titleView.setTextColor(Color.WHITE);
                    counterView.setTextColor(Color.WHITE);
                    view.setBackgroundColor(Color.WHITE);
                    imgView.setImageResource(R.drawable.ic_arrow_right_bright);
                    relativeLayout.setBackgroundResource(R.drawable.background_list_item_alt);
                    organisatorView.setTextColor(Color.WHITE);
                    subthemaView.setTextColor(Color.WHITE);
                    datumView.setTextColor(Color.WHITE);
                } else {
                    rowView.setBackgroundColor(Color.WHITE);
                }
            }
            // 5. retrn rowView
            return rowView;
        }
    }
}
