package be.kdg.kandoe.kandoeandroid.login.cirkelsessie;


import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.login.api.KaartenAPI;
import be.kdg.kandoe.kandoeandroid.login.pojo.Kaart;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 *
 *
 */
public class CirkelsessieFragment extends Fragment {

    View v;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.test, null);


        getData();
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    public void getData(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://teamh-spring.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KaartenAPI kaartenAPI = retrofit.create(KaartenAPI.class);

        Call<List<Kaart>> call = kaartenAPI.getKaarten();
        call.enqueue(new Callback<List<Kaart>>() {


            @Override
            public void onResponse(Response<List<Kaart>> response, Retrofit retrofit) {
                createTextViews(response);

                Toast.makeText(getActivity().getBaseContext(), "data received",
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                System.out.println("failure");
            }

        });
    }


    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    public void createTextViews(Response<List<Kaart>> response){

        List<Kaart> kaarten = new ArrayList<>();
        kaarten.addAll(response.body());
        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.spelKaartLayout);
        int i = 0;
        for (final Kaart aTextArray : kaarten) {

            TextView textView = new TextView(getActivity());
            textView.setText(aTextArray.getTekst());
            linearLayout.addView(textView);

            textView.setId(i);

            int padding = dpToPx(15);

            textView.setPadding(padding, padding, padding, padding);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            llp.setMarginEnd(dpToPx(5));
            textView.setLayoutParams(llp);
            textView.setBackground(getResources().getDrawable(R.drawable.back));
            textView.getLayoutParams();

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity().getBaseContext(), "kaartId: "+String.valueOf(aTextArray.getId()),
                            Toast.LENGTH_SHORT).show();

                    ((CirkelsessieActivity)getActivity()).setTest(aTextArray.getTekst());
                }
            });

            i++;

        }
    }
}