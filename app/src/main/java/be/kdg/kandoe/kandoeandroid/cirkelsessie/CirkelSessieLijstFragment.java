package be.kdg.kandoe.kandoeandroid.cirkelsessie;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.kdg.kandoe.kandoeandroid.MainActivity;
import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.api.CirkelsessieAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Authorization;
import be.kdg.kandoe.kandoeandroid.pojo.Cirkelsessie;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CirkelSessieLijstFragment extends Fragment {

    private Intent intent;
    private Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            mActivity = (Activity) context;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cirkelsessie_lijst, null);
        super.onCreate(savedInstanceState);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null)
            mActivity = getActivity();
        intent = new Intent(mActivity.getBaseContext(), CirkelsessieActivity.class);

        getData();
    }

    public void getData(){
        CirkelsessieAPI cirkelsessieAPI =
                Authorization.authorize(getActivity()).create(CirkelsessieAPI.class);

        Call<List<Cirkelsessie>> call = cirkelsessieAPI.getCirkelsessies();
        call.enqueue(new Callback<List<Cirkelsessie>>() {

            @Override
            public void onResponse(Response<List<Cirkelsessie>> response, Retrofit retrofit) {
                createList(response);

                Toast.makeText(mActivity.getBaseContext(), "cirkelsessie received",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mActivity.getBaseContext(), "failure",
                        Toast.LENGTH_SHORT).show();
                Log.d("CirkelsessieFragment", t.getMessage());

            }

        });
    }

    public void createList(Response<List<Cirkelsessie>> response){
        ListView listview = null;
        if (getView() != null)
            listview = (ListView) getView().findViewById(R.id.listview);

        final ArrayList<String> list = new ArrayList<>();
        final ArrayList<Cirkelsessie> list2 = new ArrayList<>();

        for (int i = 0; i < response.body().size(); ++i) {
            list.add(response.body().get(i).getNaam());
            list2.add(response.body().get(i));
        }
        StableArrayAdapter adapter = null;

        if (mActivity != null) {
            adapter = new StableArrayAdapter(mActivity.getBaseContext(),
                    android.R.layout.simple_list_item_1, list);
        }

        if (listview != null) {
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    intent.putExtra("cirkelsessieId", String.valueOf(list2.get(position).getId()));
                    startActivity(intent);
                }

            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {

            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }

        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
