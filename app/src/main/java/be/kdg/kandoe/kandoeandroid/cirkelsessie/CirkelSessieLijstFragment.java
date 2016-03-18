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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.api.CirkelsessieAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Authorization;
import be.kdg.kandoe.kandoeandroid.helpers.adaptermodels.CirkelsessieModel;
import be.kdg.kandoe.kandoeandroid.pojo.Cirkelsessie;
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
    private CirkelsessieAdapter adapter = null;
    private ArrayList<CirkelsessieModel> list = new ArrayList<>();
    private ArrayList<Cirkelsessie> list2 = new ArrayList<>();

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

        buttonOpen = (Button) v.findViewById(R.id.buttonOpen);
        buttonGesloten = (Button) v.findViewById(R.id.buttonGesloten);
        buttonEnd = (Button) v.findViewById(R.id.buttonEnd);

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

    public void buttonInit(){
        buttonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CirkelsessieAPI cirkelsessieAPI =
                        Authorization.authorize(getActivity()).create(CirkelsessieAPI.class);
                Call<List<Cirkelsessie>> call = cirkelsessieAPI.getCirkelsessiesOpen();
                call.enqueue(new Callback<List<Cirkelsessie>>() {
                    @Override
                    public void onResponse(Response<List<Cirkelsessie>> response, Retrofit retrofit) {
                        addData(response);
                        buttonOpen.setBackgroundResource(R.color.colorAccent);
                        buttonGesloten.setBackgroundResource(R.color.colorPrimary);
                        buttonEnd.setBackgroundResource(R.color.colorPrimary);


                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(mActivity.getBaseContext(), "failure",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        buttonGesloten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CirkelsessieAPI cirkelsessieAPI =
                        Authorization.authorize(getActivity()).create(CirkelsessieAPI.class);
                Call<List<Cirkelsessie>> call = cirkelsessieAPI.getCirkelsessiesGesloten();
                call.enqueue(new Callback<List<Cirkelsessie>>() {
                    @Override
                    public void onResponse(Response<List<Cirkelsessie>> response, Retrofit retrofit) {
                        addData(response);
                        buttonGesloten.setBackgroundResource(R.color.colorAccent);
                        buttonOpen.setBackgroundResource(R.color.colorPrimary);
                        buttonEnd.setBackgroundResource(R.color.colorPrimary);
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
                        Authorization.authorize(getActivity()).create(CirkelsessieAPI.class);
                Call<List<Cirkelsessie>> call = cirkelsessieAPI.getCirkelsessiesEnded();
                call.enqueue(new Callback<List<Cirkelsessie>>() {
                    @Override
                    public void onResponse(Response<List<Cirkelsessie>> response, Retrofit retrofit) {
                        addData(response);
                        buttonEnd.setBackgroundResource(R.color.colorAccent);
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

    public void getData(){
        CirkelsessieAPI cirkelsessieAPI =
                Authorization.authorize(getActivity()).create(CirkelsessieAPI.class);
        Call<List<Cirkelsessie>> call = cirkelsessieAPI.getCirkelsessiesOpen();
        call.enqueue(new Callback<List<Cirkelsessie>>() {
            @Override
            public void onResponse(Response<List<Cirkelsessie>> response, Retrofit retrofit) {
                addData(response);
                buttonOpen.setBackgroundResource(R.color.colorAccent);
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
                    ,response.body().get(i).getNaam(),response.body().get(i).getGebruiker().getGebruikersnaam()
                    ,String.valueOf(i+1));

            list.add(cirkelsessieModel);
            list2.add(response.body().get(i));
        }
        if(adapter !=null){
            adapter.notifyDataSetChanged();
        }
    }
    public void createAdapter(){
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
                    startActivity(intent);
                    viewToChange = view;
                    tempPosition = position;
                    viewToChange.setBackgroundResource(R.color.lightBlue);
                }

            });
        }

        linlaHeaderProgress.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        String textAantal = "Aantal : " + String.valueOf(list2.size());
        textViewAantal.setText(textAantal);

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
        private Context context;
        private ArrayList<CirkelsessieModel> modelsArrayList;

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

                // 4. Set the text for textView
                imgView.setImageResource(modelsArrayList.get(position).getIcon());
                titleView.setText(modelsArrayList.get(position).getTitle());
                organisatorView.setText("Organisator: "+ modelsArrayList.get(position).getOrganisator());
                counterView.setText(modelsArrayList.get(position).getCounter());

                if (position % 2 == 1) {
                    rowView.setBackgroundResource(R.color.colorPrimary);
                    titleView.setTextColor(Color.WHITE);
                    counterView.setTextColor(Color.WHITE);
                    view.setBackgroundColor(Color.WHITE);
                    imgView.setImageResource(R.drawable.ic_arrow_right_bright);
                    relativeLayout.setBackgroundResource(R.drawable.background_list_item_alt);
                    organisatorView.setTextColor(Color.WHITE);
                } else {
                    rowView.setBackgroundColor(Color.WHITE);
                }
            }

            // 5. retrn rowView
            return rowView;
        }
    }
}
