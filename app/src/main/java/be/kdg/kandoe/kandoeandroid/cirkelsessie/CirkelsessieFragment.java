package be.kdg.kandoe.kandoeandroid.cirkelsessie;


import android.app.Fragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.api.CirkelsessieAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Authorization;
import be.kdg.kandoe.kandoeandroid.pojo.Cirkelsessie;
import be.kdg.kandoe.kandoeandroid.pojo.Spelkaart;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class CirkelsessieFragment extends Fragment {
    private View v;
    private String cirkelsessieId;
    private LinearLayout linearLayout;
    private Handler handler;

    private TextView aantalKaartenTextView;

    private final static long REFRESH_TIME = 2000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.horizontal_scroll_cirkelsessie, null);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cirkelsessieId = ((CirkelsessieActivity) getActivity()).getCirkelsessieId();

        handler = new Handler();

        linearLayout = (LinearLayout) v.findViewById(R.id.spelKaartLayout);

        aantalKaartenTextView = (TextView) getActivity().findViewById(R.id.aantalKaarten);

        getData();

    }

   private final Runnable callRunnable= new Runnable() {
        @Override
        public void run()
        {
            getData();
            //Do task
            handler.postDelayed(callRunnable, REFRESH_TIME);
        }
    };

    public void getData(){
        Retrofit retrofit = Authorization.authorize(getActivity());
        CirkelsessieAPI cirkelsessieAPI = retrofit.create(CirkelsessieAPI.class);
        Call<Cirkelsessie> call = cirkelsessieAPI.getCirkelsessie(cirkelsessieId);

        call.enqueue(new Callback<Cirkelsessie>() {

            @Override
            public void onResponse(Response<Cirkelsessie> response, Retrofit retrofit) {
                if (response.body() !=null) {
                    if (linearLayout.getChildCount() != 0) {
                        linearLayout.removeAllViews();
                    }
                    createTextViews(response);
                }

//                Toast.makeText(getActivity().getBaseContext(), "data received",
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), t.getMessage(),
                        Toast.LENGTH_LONG).show();
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


        int i = 0;
        for (final Spelkaart spelkaart : spelkaarts) {

            if(spelkaart.getPositie() == 0){

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

                String aantalKaartenText = "Aantal kaarten: " + String.valueOf(linearLayout.getChildCount());
                aantalKaartenTextView.setText(aantalKaartenText);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textView.setBackgroundColor(Color.LTGRAY);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Verplaats kaart?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((CirkelsessieActivity) getActivity()).changeCardPosition(spelkaart);
                            linearLayout.removeView(textView);
                        }
                    });
                    builder.setNegativeButton("Annuleer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            textView.setBackground(getResources().getDrawable(R.drawable.back));
                        }
                    });

                    builder.show();
                }
            });

            i++;

        }else {
                String aantalKaartenText = "Aantal kaarten: 0";
                aantalKaartenTextView.setText(aantalKaartenText);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(callRunnable);

    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(callRunnable, 2000);
    }
}