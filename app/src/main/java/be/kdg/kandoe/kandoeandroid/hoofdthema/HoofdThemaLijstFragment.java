package be.kdg.kandoe.kandoeandroid.hoofdthema;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.api.HoofdthemaAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Authorization;
import be.kdg.kandoe.kandoeandroid.helpers.SharedPreferencesMethods;
import be.kdg.kandoe.kandoeandroid.helpers.adaptermodels.HoofdthemaModel;
import be.kdg.kandoe.kandoeandroid.pojo.Gebruiker;
import be.kdg.kandoe.kandoeandroid.pojo.Hoofdthema;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class HoofdThemaLijstFragment extends Fragment {



    private Activity mActivity;
    private Intent intent;

    private TextView textViewAantal;
    private ArrayList<Hoofdthema> unchangedList;
    private View v;
    private Gebruiker gebruiker;

    public HoofdThemaLijstFragment() {
        // Required empty public constructor
    }

    public static HoofdThemaLijstFragment newInstance() {
        return new HoofdThemaLijstFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            mActivity = getActivity();
            unchangedList = new ArrayList<>();
            String json = SharedPreferencesMethods.getFromSharedPreferences(mActivity, mActivity.getString(R.string.gebruiker));
            Gson gson = new Gson();
            gebruiker = gson.fromJson(json, Gebruiker.class);
        }
        intent = new Intent(mActivity.getBaseContext(), HoofdthemaActivity.class);
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_hoofd_thema_lijst, container, false);
        textViewAantal = (TextView) v.findViewById(R.id.hoofdthema_header);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void getData(){
        HoofdthemaAPI hoofdthemaAPI =
                Authorization.authorize(getActivity()).create(HoofdthemaAPI.class);

        Call<List<Hoofdthema>> call = hoofdthemaAPI.getHoofdthemas(String.valueOf(gebruiker.getId()));
        call.enqueue(new Callback<List<Hoofdthema>>() {
            @Override
            public void onResponse(Response<List<Hoofdthema>> response, Retrofit retrofit) {
                createList(response);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mActivity.getBaseContext(), "failure",
                        Toast.LENGTH_SHORT).show();
                Log.d("failure", t.getMessage());
            }
        });
    }

    public void createList(Response<List<Hoofdthema>> response){
        unchangedList.clear();
        ListView listview = null;
        if (getView() != null)
            listview = (ListView) getView().findViewById(R.id.listview_hoofdthemas);

        final ArrayList<HoofdthemaModel> list = new ArrayList<>();
        final ArrayList<Hoofdthema> list2 = new ArrayList<>();

        for (int i = 0; i < response.body().size(); ++i) {
            HoofdthemaModel model = new HoofdthemaModel(String.valueOf(i+1)
                    ,response.body().get(i).getNaam()
                    ,response.body().get(i).getBeschrijving()
                    ,response.body().get(i).getOrganisatie().getNaam()
                    ,R.drawable.ic_style);
            list.add(model);
            list2.add(response.body().get(i));
            unchangedList.add(response.body().get(i));
        }

        ArrayAdapter<HoofdthemaModel> adapter = null;

        if (mActivity != null) {
            adapter = new HoofdthemaAdapter(mActivity.getBaseContext(),
                    R.layout.item_list_hoofdthema, list);
        }

        if (listview != null) {
            listview.setAdapter(adapter);

        }

        String textAantal = "Aantal : " + String.valueOf(list2.size());
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
                TextView counterView = (TextView) rowView.findViewById(R.id.item_counter);
                final TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
                final TextView beschrijvingView = (TextView) rowView.findViewById(R.id.hoofdthema_beschrijving);
                final TextView organisatieView = (TextView) rowView.findViewById(R.id.hoofdthema_organisatie_beschrijving);
                final ImageView clickImgView = (ImageView) rowView.findViewById(R.id.click_icon);

                // 4. Set the text for textView
                counterView.setText(modelsArrayList.get(position).getCounter());
                titleView.setText(modelsArrayList.get(position).getTitle());
                beschrijvingView.setText("Beschrijving : " + modelsArrayList.get(position).getBeschrijving());
                organisatieView.setText("Organisatie : " + modelsArrayList.get(position).getOrganisatie());
                clickImgView.setImageResource(modelsArrayList.get(position).getClickIcon());

                final int positionId = position;
                clickImgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent.putExtra("hoofdthemaId", String.valueOf(unchangedList.get(positionId).getId()));
                        intent.putExtra("hoofdthemaTitle", String.valueOf(unchangedList.get(positionId).getNaam()));
                        startActivity(intent);
                    }
                });

            }

            // 5. return rowView
            return rowView;
        }
    }
}
