package be.kdg.kandoe.kandoeandroid.hoofdthema;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import java.util.HashMap;
import java.util.List;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.api.HoofdthemaAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Authorization;
import be.kdg.kandoe.kandoeandroid.helpers.adaptermodels.SubthemaModel;
import be.kdg.kandoe.kandoeandroid.pojo.Subthema;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class HoofdthemaActivity extends AppCompatActivity {

    private String id;
    private Activity mActivity;
    private TextView textViewAantal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_hoofdthema);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textViewAantal = (TextView) findViewById(R.id.hoofdthema_activity_header);

        Intent intent = getIntent();
        id = intent.getStringExtra("hoofdthemaId");
        setTitle(intent.getStringExtra("hoofdthemaTitle"));

        getData();
    }


    public void getData(){

        Retrofit retrofit = Authorization.authorize(mActivity);
        HoofdthemaAPI hoofdthemaAPI = retrofit.create(HoofdthemaAPI.class);
        Call<List<Subthema>> call = hoofdthemaAPI.getSubthemasFromHoofdthema(id);

        call.enqueue(new Callback<List<Subthema>>() {
            @Override
            public void onResponse(Response<List<Subthema>> response, Retrofit retrofit) {
                createList(response);
                Toast.makeText(getBaseContext(),"succes",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getBaseContext(),"failure",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createList(Response<List<Subthema>> response){
        ListView listview = (ListView) findViewById(R.id.listview_subthemas);

        final ArrayList<SubthemaModel> list = new ArrayList<>();
        final ArrayList<Subthema> list2 = new ArrayList<>();

        for (int i = 0; i < response.body().size(); ++i) {
            SubthemaModel model = new SubthemaModel(R.drawable.ic_style,response.body().get(i).getNaam()
                    ,response.body().get(i).getBeschrijving(),response.body().get(i).getHoofdthema().getOrganisatie().getNaam(),
                    response.body().get(i).getHoofdthema().getBeschrijving());
            list.add(model);
            list2.add(response.body().get(i));
        }

        ArrayAdapter<SubthemaModel> adapter = null;

        if (mActivity != null) {
            adapter = new SubthemaAdapter(mActivity.getBaseContext(),
                    R.layout.subthema_lijst_item, list);
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

        String textAantal = "Subthema's van : " + String.valueOf(response.body().get(0).getHoofdthema().getNaam());
        textViewAantal.setText(textAantal);
    }

    private class SubthemaAdapter extends ArrayAdapter<SubthemaModel> {
        private Context context;
        private ArrayList<SubthemaModel> modelsArrayList;


        public SubthemaAdapter(Context context,int textViewResourceId, ArrayList<SubthemaModel> modelsArrayList) {

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
                rowView = inflater.inflate(R.layout.subthema_lijst_item, parent, false);

                // 3. Get icon,title & counter views from the rowView
                ImageView imgView = (ImageView) rowView.findViewById(R.id.item_icon);
                TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
                TextView beschrijvingView = (TextView) rowView.findViewById(R.id.subthema_beschrijving);
                TextView organisatieView = (TextView) rowView.findViewById(R.id.subthema_organisatie_beschrijving);
                TextView hoofdthemaView = (TextView) rowView.findViewById(R.id.subthema_hoofdthema_beschrijving);

                // 4. Set the text for textView
                imgView.setImageResource(modelsArrayList.get(position).getIcon());
                titleView.setText(modelsArrayList.get(position).getTitle());
                beschrijvingView.setText("Beschrijving : "+modelsArrayList.get(position).getBeschrijving());
                organisatieView.setText("Organisatie : "+modelsArrayList.get(position).getOrganisatie());
                hoofdthemaView.setText("Hoofdthema : "+modelsArrayList.get(position).getHoofdthema());

            }
            // 5. retrn rowView
            return rowView;
        }
    }

}
