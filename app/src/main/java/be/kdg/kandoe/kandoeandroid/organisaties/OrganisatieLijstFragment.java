package be.kdg.kandoe.kandoeandroid.organisaties;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
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
import be.kdg.kandoe.kandoeandroid.api.OrganisatieAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Autorisatie;
import be.kdg.kandoe.kandoeandroid.helpers.adaptermodels.OrganisatieModel;
import be.kdg.kandoe.kandoeandroid.helpers.SharedPreferencesMethods;
import be.kdg.kandoe.kandoeandroid.pojo.response.Gebruiker;
import be.kdg.kandoe.kandoeandroid.pojo.response.Organisatie;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class OrganisatieLijstFragment extends Fragment {

    private Activity mActivity;

    private Intent intent;

    private TextView textViewAantal;

    private View v;

    private ArrayList<Organisatie> unchangedList;

    private Gebruiker gebruiker;


    public OrganisatieLijstFragment() {
        // Required empty public constructor
    }

    public static OrganisatieLijstFragment newInstance() {
        return new OrganisatieLijstFragment();
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
        intent = new Intent(mActivity.getBaseContext(), OrganisatieActivity.class);
        getData();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_organisatie_lijst, container, false);
        textViewAantal = (TextView) v.findViewById(R.id.organisatie_header);
        return v;
    }

    private void getData(){
        be.kdg.kandoe.kandoeandroid.api.OrganisatieAPI organisatieAPI =
                Autorisatie.authorize(getActivity()).create(OrganisatieAPI.class);

        Call<List<Organisatie>> call = organisatieAPI.getOrganisaties(String.valueOf(gebruiker.getId()));
        call.enqueue(new Callback<List<Organisatie>>() {
            @Override
            public void onResponse(Response<List<Organisatie>> response, Retrofit retrofit) {
                if(response.body().size() == 0){
                    TextView textView = (TextView) v.findViewById(R.id.no_organisatie);
                    textView.setVisibility(View.VISIBLE);
                }else {
                    createList(response);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mActivity.getBaseContext(), "failure",
                        Toast.LENGTH_SHORT).show();
                Log.d("failure", t.getMessage());
            }
        });
    }

    private void createList(Response<List<Organisatie>> response){
        unchangedList.clear();
        ListView listview = null;
        if (getView() != null)
            listview = (ListView) getView().findViewById(R.id.listview_organisaties);

        final ArrayList<OrganisatieModel> list = new ArrayList<>();
        final ArrayList<Organisatie> list2 = new ArrayList<>();

        for (int i = 0; i < response.body().size(); ++i) {
            OrganisatieModel model = new OrganisatieModel(String.valueOf(i+1),response.body().get(i).getNaam(),
                    response.body().get(i).getBeschrijving(),R.drawable.ic_turned_in);
            list.add(model);
            list2.add(response.body().get(i));
            unchangedList.add(response.body().get(i));
        }

        ArrayAdapter<OrganisatieModel> adapter = null;

        if (mActivity != null) {
            adapter = new OrganisatieAdapter(mActivity.getBaseContext(),
                    R.layout.item_list_organisatie, list);
        }

        if (listview != null) {
            listview.setAdapter(adapter);
        }

        String textAantal = "Aantal : " + String.valueOf(list2.size());
        textViewAantal.setText(textAantal);
    }

    private class OrganisatieAdapter extends ArrayAdapter<OrganisatieModel> {

        private final Context context;
        private final ArrayList<OrganisatieModel> modelsArrayList;

        public OrganisatieAdapter(Context context,int textViewResourceId, ArrayList<OrganisatieModel> modelsArrayList) {

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
                rowView = inflater.inflate(R.layout.item_list_organisatie, parent, false);

                // 3. Get icon,title & counter views from the rowView
                final TextView counterView = (TextView) rowView.findViewById(R.id.item_counter);
                final TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
                final TextView beschrijvingView = (TextView) rowView.findViewById(R.id.organisatie_beschrijving);
                final ImageView clickImgView = (ImageView) rowView.findViewById(R.id.click_icon);
                clickImgView.setColorFilter(null);
                ImageView mImageView = clickImgView;

                // 4. Set the text for textView

                counterView.setText(modelsArrayList.get(position).getCounter());
                clickImgView.setImageResource(modelsArrayList.get(position).getClickIcon());
                String titleText = modelsArrayList.get(position).getTitle();
                titleView.setText(titleText);
                String beschrijvingText = "Beschrijving : " + modelsArrayList.get(position).getBeschrijving();
                beschrijvingView.setText(beschrijvingText);

                final int positionId = position;
                clickImgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent.putExtra("organisatieId", String.valueOf(unchangedList.get(positionId).getId()));
                        intent.putExtra("organisatieTitle", String.valueOf(unchangedList.get(positionId).getNaam()));
                        startActivity(intent);
                    }
                });
            }

            // 5. retrn rowView
            return rowView;
        }
    }

}
