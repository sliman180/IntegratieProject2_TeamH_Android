package be.kdg.kandoe.kandoeandroid.subthema;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.authorization.Autorisatie;
import be.kdg.kandoe.kandoeandroid.helpers.SharedPreferencesMethods;
import be.kdg.kandoe.kandoeandroid.api.*;
import be.kdg.kandoe.kandoeandroid.helpers.adaptermodels.SubthemaModel;
import be.kdg.kandoe.kandoeandroid.pojo.response.Gebruiker;
import be.kdg.kandoe.kandoeandroid.pojo.response.Subthema;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SubthemaLijstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubthemaLijstFragment extends Fragment {

    private Activity mActivity;

    private TextView textViewAantal;

    private View v;

    private Gebruiker gebruiker;

    public SubthemaLijstFragment() {
        // Required empty public constructor
    }

    public static SubthemaLijstFragment newInstance() {
        return new SubthemaLijstFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            mActivity = getActivity();
            String json = SharedPreferencesMethods.getFromSharedPreferences(mActivity, mActivity.getString(R.string.gebruiker));
            Gson gson = new Gson();
            gebruiker = gson.fromJson(json, Gebruiker.class);
        }
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_subthemalijst, container, false);
        textViewAantal = (TextView) v.findViewById(R.id.subthema_header);
        return v;
    }

    private void getData(){
       GebruikerAPI gebruikerAPI =
                Autorisatie.authorize(getActivity()).create(GebruikerAPI.class);

        Call<List<Subthema>> call = gebruikerAPI.getSubThemas(String.valueOf(gebruiker.getId()));
        call.enqueue(new Callback<List<Subthema>>() {
            @Override
            public void onResponse(Response<List<Subthema>> response, Retrofit retrofit) {
                if(response.body().size() == 0){
                    TextView textView = (TextView) v.findViewById(R.id.no_subthemas);
                    textView.setVisibility(View.VISIBLE);
                }else {
                    createList(response);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mActivity.getBaseContext(), R.string.failure,
                        Toast.LENGTH_SHORT).show();
                Log.d("failure",t.getMessage());
            }
        });
    }

    private void createList(Response<List<Subthema>> response){
        ListView listview = null;
        if (getView() != null)
            listview = (ListView) getView().findViewById(R.id.listview_subthemas);

        final ArrayList<SubthemaModel> list = new ArrayList<>();
        final ArrayList<Subthema> list2 = new ArrayList<>();

        for (int i = 0; i < response.body().size(); ++i) {
            SubthemaModel model = new SubthemaModel(String.valueOf(i+1),response.body().get(i).getNaam()
                    ,response.body().get(i).getBeschrijving(),response.body().get(i).getHoofdthema().getOrganisatie().getNaam(),
                    response.body().get(i).getHoofdthema().getNaam());
//            SubthemaModel model = new SubthemaModel();
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

        String textAantal = "Aantal : " + String.valueOf(list2.size());
        textViewAantal.setText(textAantal);
    }

    private class SubthemaAdapter extends ArrayAdapter<SubthemaModel> {

        private final Context context;
        private final ArrayList<SubthemaModel> modelsArrayList;
        HashMap<String, Integer> mIdMap = new HashMap<>();



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
                TextView counterView = (TextView) rowView.findViewById(R.id.item_counter);
                TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
                TextView beschrijvingView = (TextView) rowView.findViewById(R.id.subthema_beschrijving);
                TextView organisatieView = (TextView) rowView.findViewById(R.id.subthema_organisatie_beschrijving);
                TextView hoofdthemaView = (TextView) rowView.findViewById(R.id.subthema_hoofdthema_beschrijving);

                // 4. Set the text for textView
                counterView.setText(modelsArrayList.get(position).getCounter());
                titleView.setText(modelsArrayList.get(position).getTitle());
                beschrijvingView.setText(context.getString(R.string.beschrijving)+modelsArrayList.get(position).getBeschrijving());
                organisatieView.setText(context.getString(R.string.organisatie)+modelsArrayList.get(position).getOrganisatie());
                hoofdthemaView.setText(context.getString(R.string.hoofdthema)+modelsArrayList.get(position).getHoofdthema());

            }


            // 5. retrn rowView
            return rowView;
        }
    }

}
