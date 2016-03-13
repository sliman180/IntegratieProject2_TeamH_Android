package be.kdg.kandoe.kandoeandroid.organisaties;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
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
import be.kdg.kandoe.kandoeandroid.helpers.Model;
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

        return inflater.inflate(R.layout.fragment_organisatie_lijst, container, false);
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
            }
        });
    }

    public void createList(Response<List<Organisatie>> response){
        ListView listview = null;
        if (getView() != null)
            listview = (ListView) getView().findViewById(R.id.listview_organisaties);

        final ArrayList<Model> list = new ArrayList<>();
        final ArrayList<Organisatie> list2 = new ArrayList<>();

        for (int i = 0; i < response.body().size(); ++i) {
            Model model = new Model(R.drawable.ic_arrow_right,response.body().get(i).getNaam(),String.valueOf(i+1));
            list.add(model);
            list2.add(response.body().get(i));
        }

        ArrayAdapter<Model> adapter = null;

        if (mActivity != null) {
            adapter = new StableArrayAdapter(mActivity.getBaseContext(),
                    R.layout.cirkelsessie_lijst_item, list);
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
    }

    private class StableArrayAdapter extends ArrayAdapter<Model> {

        private Context context;
        private ArrayList<Model> modelsArrayList;
        HashMap<String, Integer> mIdMap = new HashMap<>();



        public StableArrayAdapter(Context context,int textViewResourceId, ArrayList<Model> modelsArrayList) {

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
                rowView = inflater.inflate(R.layout.cirkelsessie_lijst_item, parent, false);

                // 3. Get icon,title & counter views from the rowView
                ImageView imgView = (ImageView) rowView.findViewById(R.id.item_icon);
                TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
                TextView counterView = (TextView) rowView.findViewById(R.id.item_counter);

                // 4. Set the text for textView
                imgView.setImageResource(modelsArrayList.get(position).getIcon());
                titleView.setText(modelsArrayList.get(position).getTitle());
                counterView.setText(modelsArrayList.get(position).getCounter());

                if (position % 2 == 1) {
                    rowView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    titleView.setTextColor(Color.WHITE);
                    counterView.setTextColor(Color.WHITE);
                    imgView.setImageResource(R.drawable.ic_arrow_right_bright);

                } else {
                    rowView.setBackgroundColor(Color.WHITE);
                }
            }


            // 5. retrn rowView
            return rowView;
        }
    }

}
