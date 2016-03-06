package be.kdg.kandoe.kandoeandroid.login.cirkelsessie;


import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.login.api.AuthAPI;
import be.kdg.kandoe.kandoeandroid.login.api.CirkelsessieAPI;
import be.kdg.kandoe.kandoeandroid.login.api.KaartenAPI;
import be.kdg.kandoe.kandoeandroid.login.api.SpelkaartenAPI;
import be.kdg.kandoe.kandoeandroid.login.pojo.Cirkelsessie;
import be.kdg.kandoe.kandoeandroid.login.pojo.Credentials;
import be.kdg.kandoe.kandoeandroid.login.pojo.Kaart;
import be.kdg.kandoe.kandoeandroid.login.pojo.Spelkaart;
import be.kdg.kandoe.kandoeandroid.login.pojo.Token;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class CirkelsessieFragment extends Fragment {

    View v;
    private String token;
    private String cirkelsessieId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.test, null);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        token = ((CirkelsessieActivity) getActivity()).getToken();

        cirkelsessieId = ((CirkelsessieActivity) getActivity()).getCirkelsessieId();


        getData();
    }


    public void getData(){


        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build();
                return chain.proceed(newRequest);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://teamh-spring.herokuapp.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        CirkelsessieAPI cirkelsessieAPI = retrofit.create(CirkelsessieAPI.class);

        Call<Cirkelsessie> call = cirkelsessieAPI.getCirkelsessie(cirkelsessieId);

        call.enqueue(new Callback<Cirkelsessie>() {


            @Override
            public void onResponse(Response<Cirkelsessie> response, Retrofit retrofit) {
                if(response.body() !=null){
                createTextViews(response);

                }

                Toast.makeText(getActivity().getBaseContext(), "data received",
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), "failure",
                        Toast.LENGTH_SHORT).show();

            }

        });
    }


    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public void createTextViews(Response<Cirkelsessie> response){

        List<Spelkaart> spelkaarts = new ArrayList<>();

        spelkaarts.addAll(response.body().getSpelkaarten());

        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.spelKaartLayout);
        int i = 0;
        for (final Spelkaart spelkaart : spelkaarts) {

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