package be.kdg.kandoe.kandoeandroid.subthema;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import be.kdg.kandoe.kandoeandroid.authorization.Authorization;
import be.kdg.kandoe.kandoeandroid.helpers.Model;
import be.kdg.kandoe.kandoeandroid.helpers.SharedPreferencesMethods;
import be.kdg.kandoe.kandoeandroid.api.*;
import be.kdg.kandoe.kandoeandroid.pojo.Organisatie;
import be.kdg.kandoe.kandoeandroid.pojo.Subthema;
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

    private String token;

    private Activity mActivity;
    private Intent intent;

    private TextView textViewAantal;

    private View v;

    public SubthemaLijstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SubthemaLijstFragment.
     */
    public static SubthemaLijstFragment newInstance() {
        return new SubthemaLijstFragment();
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
        v = inflater.inflate(R.layout.fragment_subthemalijst, container, false);
        textViewAantal = (TextView) v.findViewById(R.id.subthema_header);
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
       SubthemaAPI subthemaAPI =
                Authorization.authorize(getActivity()).create(SubthemaAPI.class);

        Call<List<Subthema>> call = subthemaAPI.getSubThemas();
        call.enqueue(new Callback<List<Subthema>>() {
            @Override
            public void onResponse(Response<List<Subthema>> response, Retrofit retrofit) {
                createList(response);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mActivity.getBaseContext(), "failure",
                        Toast.LENGTH_SHORT).show();
                Log.d("failure",t.getMessage());
            }
        });
    }

    public void createList(Response<List<Subthema>> response){
        ListView listview = null;
        if (getView() != null)
            listview = (ListView) getView().findViewById(R.id.listview_subthemas);

        final ArrayList<Model> list = new ArrayList<>();
        final ArrayList<Subthema> list2 = new ArrayList<>();

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

        String textAantal = "Aantal : " + String.valueOf(list2.size());
        textViewAantal.setText(textAantal);
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
