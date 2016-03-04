package be.kdg.kandoe.kandoeandroid.login.cirkelsessie;


import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.login.api.KaartenAPI;
import be.kdg.kandoe.kandoeandroid.login.api.SpelkaartenAPI;
import be.kdg.kandoe.kandoeandroid.login.pojo.Kaart;
import be.kdg.kandoe.kandoeandroid.login.pojo.Spelkaart;
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

        SpelkaartenAPI spelkaartenAPI = retrofit.create(SpelkaartenAPI.class);

        Call<List<Spelkaart>> call = spelkaartenAPI.getSpelkaarten();

        call.enqueue(new Callback<List<Spelkaart>>() {


            @Override
            public void onResponse(Response<List<Spelkaart>> response, Retrofit retrofit) {
                if(response !=null){
               // createTextViews(response,null);
                    ArrayList<Spelkaart> spelkaarts = new ArrayList<Spelkaart>();
                    for(int i = 0; i < 10; i++)
                    {
                        spelkaarts.add(new Spelkaart(i, new Kaart(i,"kaart" + i, "dummy",true), 0));
                    }
                    createTextViews(null,spelkaarts);
                }

                Toast.makeText(getActivity().getBaseContext(), "data received",
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), "failure",
                        Toast.LENGTH_SHORT).show();
               ArrayList<Spelkaart> spelkaarts = new ArrayList<Spelkaart>();
               for(int i = 0; i < 10; i++)
               {
                   spelkaarts.add(new Spelkaart(i, new Kaart(i,"kaart" + i, "dummy",true), 0));
               }
                createTextViews(null,spelkaarts);
            }

        });
    }


    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public void createTextViews(Response<List<Spelkaart>> response, ArrayList<Spelkaart> spelkaarts){

        List<Spelkaart> spelkaarten = new ArrayList<>();
        if(spelkaarts != null){
            spelkaarten.addAll(spelkaarts);
        }else {
           // kaarten.addAll(response.body());
        }
        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.spelKaartLayout);
        int i = 0;
        for (final Spelkaart spelkaart : spelkaarten) {

            final TextView textView = new TextView(getActivity());
            textView.setText(spelkaart.getKaart().getTekst());
            linearLayout.addView(textView);

            textView.setId(i);

            int padding = dpToPx(15);

            textView.setPadding(padding, padding, padding, padding);
            final LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            llp.setMarginEnd(dpToPx(5));
            textView.setLayoutParams(llp);
            textView.setBackground(getResources().getDrawable(R.drawable.back));
            textView.getLayoutParams();

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // custom dialog
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.custom_dialog);
                    dialog.setTitle("Verplaats kaart?");

                    Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                    Button annuleerButton = (Button) dialog.findViewById(R.id.dialogButtonAnnuleer);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((CirkelsessieActivity) getActivity()).setPosition(spelkaart);

                            dialog.dismiss();
                            textView.setVisibility(View.GONE);
                        }
                    });

                    annuleerButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }
            });

            i++;

        }
    }
}