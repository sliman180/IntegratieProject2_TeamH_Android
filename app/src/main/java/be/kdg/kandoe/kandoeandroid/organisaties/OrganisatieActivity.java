package be.kdg.kandoe.kandoeandroid.organisaties;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.api.OrganisatieAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Authorization;
import be.kdg.kandoe.kandoeandroid.helpers.adaptermodels.HoofdthemaModel;
import be.kdg.kandoe.kandoeandroid.helpers.adaptermodels.OrganisatieModel;
import be.kdg.kandoe.kandoeandroid.pojo.Hoofdthema;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class OrganisatieActivity extends AppCompatActivity {

    private String id;
    private Activity mActivity;
    private TextView textViewAantal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_organisatie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if(toolbar != null){
            toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        textViewAantal = (TextView) findViewById(R.id.organisatie_activity_header);

        Intent intent = getIntent();
        id = intent.getStringExtra("organisatieId");
        setTitle(intent.getStringExtra("organisatieTitle"));

        getData();
    }


    public void getData(){

        Retrofit retrofit = Authorization.authorize(mActivity);
        OrganisatieAPI organisatieAPI = retrofit.create(OrganisatieAPI.class);
        Call<List<Hoofdthema>> call = organisatieAPI.getHoofthemasFromOrganisaties(id);

        call.enqueue(new Callback<List<Hoofdthema>>() {
            @Override
            public void onResponse(Response<List<Hoofdthema>> response, Retrofit retrofit) {
                if(response.body().size() == 0){
                    TextView textView = (TextView) findViewById(R.id.organisatie_no_hoofdthema);
                    textView.setVisibility(View.VISIBLE);
                }else {
                    createList(response);
                    Toast.makeText(getBaseContext(),"succes",Toast.LENGTH_SHORT).show();}

            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getBaseContext(),"failure",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createList(Response<List<Hoofdthema>> response){
        ListView listview = (ListView) findViewById(R.id.listview_hoofdthemas);

        final ArrayList<HoofdthemaModel> list = new ArrayList<>();
        final ArrayList<Hoofdthema> list2 = new ArrayList<>();

        for (int i = 0; i < response.body().size(); ++i) {
            HoofdthemaModel model = new HoofdthemaModel(String.valueOf(i+1),response.body().get(i).getNaam()
                    ,response.body().get(i).getBeschrijving(),response.body().get(i).getOrganisatie().getNaam(),
                    0);
            list.add(model);
            list2.add(response.body().get(i));
        }

        ArrayAdapter<HoofdthemaModel> adapter = null;

        if (mActivity != null) {
            adapter = new HoofdthemaAdapter(mActivity.getBaseContext(),
                    R.layout.item_list_hoofdthema, list);
        }

        if (listview != null) {
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {

                }

            });
        }

        String textAantal = "Hoofdthema's van : " + String.valueOf(response.body().get(0).getOrganisatie().getNaam());
        textViewAantal.setText(textAantal);
    }

    private class HoofdthemaAdapter extends ArrayAdapter<HoofdthemaModel> {
        private Context context;
        private ArrayList<HoofdthemaModel> modelsArrayList;


        public HoofdthemaAdapter(Context context,int textViewResourceId, ArrayList<HoofdthemaModel> modelsArrayList) {

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
                rowView = inflater.inflate(R.layout.item_list_hoofdthema, parent, false);

                // 3. Get icon,title & counter views from the rowView
                View view = (View) rowView.findViewById(R.id.backLine);
                TextView counterView = (TextView) rowView.findViewById(R.id.item_counter);
                final TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
                final TextView beschrijvingView = (TextView) rowView.findViewById(R.id.hoofdthema_beschrijving);
                final TextView organisatieView = (TextView) rowView.findViewById(R.id.hoofdthema_organisatie_beschrijving);

                // 4. Set the text for textView
                view.setVisibility(View.GONE);
                counterView.setText(modelsArrayList.get(position).getCounter());
                titleView.setText(modelsArrayList.get(position).getTitle());
                beschrijvingView.setText("Beschrijving : "+modelsArrayList.get(position).getBeschrijving());
                organisatieView.setText("Organisatie : "+modelsArrayList.get(position).getOrganisatie());
            }
            // 5. retrn rowView
            return rowView;
        }
    }

}
