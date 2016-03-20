package be.kdg.kandoe.kandoeandroid.deelnames;


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

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.api.DeelnameAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Autorisatie;
import be.kdg.kandoe.kandoeandroid.cirkelsessie.CirkelsessieActivity;
import be.kdg.kandoe.kandoeandroid.helpers.adaptermodels.DeelnameModel;
import be.kdg.kandoe.kandoeandroid.helpers.SharedPreferencesMethods;
import be.kdg.kandoe.kandoeandroid.pojo.response.Deelname;
import be.kdg.kandoe.kandoeandroid.pojo.response.Gebruiker;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeelnameLijstFragment extends Fragment {



    private Activity mActivity;

    private Intent intent;

    private TextView textViewAantal;

    private View viewToChange;
    private int tempPosition = 0;
    private Gebruiker gebruiker;

    private View v;

    public DeelnameLijstFragment() {
        // Required empty public constructor
    }

    public static DeelnameLijstFragment newInstance() {
        return new DeelnameLijstFragment();
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
        intent = new Intent(mActivity.getBaseContext(), CirkelsessieActivity.class);
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_deelname_lijst, container, false);
        textViewAantal = (TextView) v.findViewById(R.id.deelname_header);
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
        DeelnameAPI deelnameAPI =
                Autorisatie.authorize(getActivity()).create(DeelnameAPI.class);
        Call<List<Deelname>> call = deelnameAPI.getDeelnamesGebruiker(String.valueOf(gebruiker.getId()));
        call.enqueue(new Callback<List<Deelname>>() {
            @Override
            public void onResponse(Response<List<Deelname>> response, Retrofit retrofit) {
                if(response.body().size() == 0){
                    TextView textView = (TextView) v.findViewById(R.id.no_deelnames);
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

    public void createList(Response<List<Deelname>> response){
        ListView listview = null;
        if (getView() != null)
            listview = (ListView) getView().findViewById(R.id.listview_deelnames);

        final ArrayList<DeelnameModel> list = new ArrayList<>();
        final ArrayList<Deelname> list2 = new ArrayList<>();
        SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        for (int i = 0; i < response.body().size(); ++i) {
            Date date = new Date(response.body().get(i).getDatum());
            DeelnameModel model = new DeelnameModel(R.drawable.ic_arrow_right,"",ft.format(date),String.valueOf(i + 1));
            list.add(model);
            list2.add(response.body().get(i));
        }

        ArrayAdapter<DeelnameModel> adapter = null;

        if (mActivity != null) {
            adapter = new DeelnameAdapter(mActivity.getBaseContext(),
                    R.layout.item_list_deelname, list);
        }

        if (listview != null) {
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    intent.putExtra("cirkelsessieId", "1");
                    intent.putExtra("cirkelsessieTitle", "title");
                    startActivity(intent);
                    viewToChange = view;
                    tempPosition = position;
                    viewToChange.setBackgroundColor(Color.LTGRAY);
                }

            });
        }

        String textAantal = "Aantal : " + String.valueOf(list2.size());
        textViewAantal.setText(textAantal);
    }

    private class DeelnameAdapter extends ArrayAdapter<DeelnameModel> {

        private Context context;
        private ArrayList<DeelnameModel> modelsArrayList;
        HashMap<String, Integer> mIdMap = new HashMap<>();

        public DeelnameAdapter(Context context,int textViewResourceId, ArrayList<DeelnameModel> modelsArrayList) {

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
                rowView = inflater.inflate(R.layout.item_list_deelname, parent, false);

                // 3. Get icon,title & counter views from the rowView
                ImageView imgView = (ImageView) rowView.findViewById(R.id.item_icon);
                TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
                TextView dateView = (TextView) rowView.findViewById(R.id.date);
                TextView counterView = (TextView) rowView.findViewById(R.id.item_counter);

                // 4. Set the text for textView
                imgView.setImageResource(modelsArrayList.get(position).getIcon());
                String titleText = "Cirkelsessie : " + modelsArrayList.get(position).getTitle();
                titleView.setText(titleText);
                String dateText = "Deelgenomen op : " + modelsArrayList.get(position).getDate();
                dateView.setText(dateText);
                counterView.setText(modelsArrayList.get(position).getCounter());

            }

            // 5. retrn rowView
            return rowView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(viewToChange != null)
            if ((tempPosition % 2 == 1)) {
                viewToChange.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else {
                viewToChange.setBackgroundColor(Color.WHITE);
            }
        getData();
    }
}
