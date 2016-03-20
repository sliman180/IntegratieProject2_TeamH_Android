package be.kdg.kandoe.kandoeandroid.cirkelsessie;


import android.app.Fragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
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
import be.kdg.kandoe.kandoeandroid.authorization.Autorisatie;
import be.kdg.kandoe.kandoeandroid.pojo.response.Spelkaart;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class CirkelsessieFragment extends Fragment {
    private View v;
    private String cirkelsessieId;
    private LinearLayout linearLayout;
    private Handler handler;

    private TextView beurtTextView;
    private boolean beurt;
    private String status;

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

        status = ((CirkelsessieActivity) getActivity()).getStatus();

        handler = new Handler();

        linearLayout = (LinearLayout) v.findViewById(R.id.spelKaartLayout);

        beurtTextView = (TextView) getActivity().findViewById(R.id.isBeurt);

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
        beurt = ((CirkelsessieActivity) getActivity()).isBeurt();
        Retrofit retrofit = Autorisatie.authorize(getActivity());
        CirkelsessieAPI cirkelsessieAPI = retrofit.create(CirkelsessieAPI.class);
        Call<List<Spelkaart>> call = cirkelsessieAPI.getSpelkaarten(cirkelsessieId);

        call.enqueue(new Callback<List<Spelkaart>>() {

            @Override
            public void onResponse(Response<List<Spelkaart>> response, Retrofit retrofit) {
                if (response.body() !=null) {
                    if (linearLayout.getChildCount() != 0) {
                        linearLayout.removeAllViews();
                    }
                    createTextViews(response);
                }
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

    public void createTextViews(Response<List<Spelkaart>> response){
        List<Spelkaart> spelkaarts = new ArrayList<>();

        spelkaarts.addAll(response.body());

        for (final Spelkaart spelkaart : spelkaarts) {

            if(spelkaart.getPositie() == 0){

            final TextView textView = new TextView(getActivity());
            textView.setText(spelkaart.getKaart().getTekst());
            linearLayout.addView(textView);

            int padding = dpToPx(15);

            textView.setPadding(padding, padding, padding, padding);
            final LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            llp.setMarginEnd(dpToPx(5));
            textView.setLayoutParams(llp);
            textView.setBackgroundResource(R.drawable.back);

                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(beurt && status.equals("GESTART")) {
                                textView.setBackgroundColor(Color.LTGRAY);
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Verplaats kaart?");
                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ((CirkelsessieActivity) getActivity()).changeCardPosition(spelkaart);
                                        linearLayout.removeView(textView);
                                    }
                                });
                                builder.setNegativeButton(R.string.annuleer, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        textView.setBackground(getResources().getDrawable(R.drawable.back));
                                    }
                                });

                            builder.show();
                            }else if(!beurt && status.equals("GESTART")){
                                Toast.makeText(getActivity().getBaseContext(),R.string.n_beurt,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity().getBaseContext(),"Cirkelsessie is niet gestart",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



        }
            if(beurt){
                beurtTextView.setText(R.string.beurt);
                beurtTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_green_600));

            }else {
            beurtTextView.setText(R.string.n_beurt);
                beurtTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_red_600));

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