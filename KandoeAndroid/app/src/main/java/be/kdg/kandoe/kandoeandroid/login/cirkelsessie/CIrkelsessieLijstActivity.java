package be.kdg.kandoe.kandoeandroid.login.cirkelsessie;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.login.api.CirkelsessieAPI;
import be.kdg.kandoe.kandoeandroid.login.pojo.Cirkelsessie;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class CIrkelsessieLijstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cirkelsessie_lijst);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getData();


    }

    public void getData(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://teamh-spring.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CirkelsessieAPI cirkelsessieAPI = retrofit.create(CirkelsessieAPI.class);

        Call<List<Cirkelsessie>> call = cirkelsessieAPI.getCirkelsessies();
        call.enqueue(new Callback<List<Cirkelsessie>>() {


            @Override
            public void onResponse(Response<List<Cirkelsessie>> response, Retrofit retrofit) {
                createList(response);

                Toast.makeText(getBaseContext(), response.body().get(0).getNaam(),
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getBaseContext(), "failure",
                        Toast.LENGTH_SHORT).show();
                System.out.println("failure");
            }

        });
    }

    public void createList(Response<List<Cirkelsessie>> response){
        final ListView listview = (ListView) findViewById(R.id.listview);

        final ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < response.body().size(); ++i) {
            list.add(response.body().get(i).getNaam());
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }

        });
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
