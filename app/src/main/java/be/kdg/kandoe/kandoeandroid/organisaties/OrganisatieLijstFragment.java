package be.kdg.kandoe.kandoeandroid.organisaties;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
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
import be.kdg.kandoe.kandoeandroid.api.OrganisatieAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Authorization;
import be.kdg.kandoe.kandoeandroid.helpers.adaptermodels.OrganisatieModel;
import be.kdg.kandoe.kandoeandroid.helpers.SharedPreferencesMethods;
import be.kdg.kandoe.kandoeandroid.pojo.Organisatie;
import be.kdg.kandoe.kandoeandroid.subthema.SubthemaActivity;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class OrganisatieLijstFragment extends Fragment {


    private String token;

    private Activity mActivity;

    private Intent intent;

    private TextView textViewAantal;

    private View v;

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
            token = SharedPreferencesMethods.getFromSharedPreferences(mActivity, getString(R.string.token));
        }
        intent = new Intent(mActivity.getBaseContext(), SubthemaActivity.class);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void getData(){
        be.kdg.kandoe.kandoeandroid.api.OrganisatieAPI organisatieAPI =
                Authorization.authorize(getActivity()).create(OrganisatieAPI.class);

        Call<List<Organisatie>> call = organisatieAPI.getOrganisaties();
        call.enqueue(new Callback<List<Organisatie>>() {
            @Override
            public void onResponse(Response<List<Organisatie>> response, Retrofit retrofit) {
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

    public void createList(Response<List<Organisatie>> response){
        ListView listview = null;
        if (getView() != null)
            listview = (ListView) getView().findViewById(R.id.listview_organisaties);

        final ArrayList<OrganisatieModel> list = new ArrayList<>();
        final ArrayList<Organisatie> list2 = new ArrayList<>();

        for (int i = 0; i < response.body().size(); ++i) {
            OrganisatieModel model = new OrganisatieModel(R.drawable.ic_account_balance,response.body().get(i).getNaam(),
                    response.body().get(i).getBeschrijving());
            list.add(model);
            list2.add(response.body().get(i));
        }

        ArrayAdapter<OrganisatieModel> adapter = null;

        if (mActivity != null) {
            adapter = new OrganisatieAdapter(mActivity.getBaseContext(),
                    R.layout.organisatie_lijst_item, list);
        }

        if (listview != null) {
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
//                    intent.putExtra("subthemaId", String.valueOf(list2.get(position).getId()));
//                    intent.putExtra("token", token);
//                    startActivity(intent);
                }

            });
        }

        String textAantal = "Aantal : " + String.valueOf(list2.size());
        textViewAantal.setText(textAantal);
    }

    private class OrganisatieAdapter extends ArrayAdapter<OrganisatieModel> {

        private Context context;
        private ArrayList<OrganisatieModel> modelsArrayList;

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
                rowView = inflater.inflate(R.layout.organisatie_lijst_item, parent, false);

                // 3. Get icon,title & counter views from the rowView
                ImageView imgView = (ImageView) rowView.findViewById(R.id.item_icon);
                TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
                TextView beschrijvingView = (TextView) rowView.findViewById(R.id.organisatie_beschrijving);

                // 4. Set the text for textView

                imgView.setImageResource(modelsArrayList.get(position).getIcon());
                String titleText = modelsArrayList.get(position).getTitle();
                titleView.setText(titleText);
                String beschrijvingText = "Beschrijving : " + modelsArrayList.get(position).getBeschrijving();
                beschrijvingView.setText(beschrijvingText);

            }


            // 5. retrn rowView
            return rowView;
        }
    }

}
