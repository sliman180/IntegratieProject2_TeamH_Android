package be.kdg.kandoe.kandoeandroid.cirkelsessie;


import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.api.DeelnameAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Authorization;
import be.kdg.kandoe.kandoeandroid.pojo.Deelname;
import be.kdg.kandoe.kandoeandroid.pojo.Gebruiker;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeelnameFragment extends Fragment {

    private View v;
    private Button buttonDeelname;
    private String cirkelsessieId;
    private Gebruiker gebruiker;
    private boolean isDeelnemer;
    private Button buttonVoegKaart;
    private Button buttonInfo;
    private String status;
    private TextView beurtTextview;
    private ChatFragment chatFragment;

    public DeelnameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_deelname, container, false);
        buttonDeelname = (Button) v.findViewById(R.id.buttonDeelname);
        buttonInfo = (Button) v.findViewById(R.id.buttonInfo);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isDeelnemer = false;
        cirkelsessieId = ((CirkelsessieActivity) getActivity()).getCirkelsessieId();
        gebruiker = ((CirkelsessieActivity) getActivity()).getGebruiker();
        status = ((CirkelsessieActivity) getActivity()).getStatus();
        buttonVoegKaart = (Button) getActivity().findViewById(R.id.buttonAddKaart);
        beurtTextview = (TextView) getActivity().findViewById(R.id.isBeurt);
        chatFragment = (ChatFragment) getFragmentManager().findFragmentById(R.id.chat_fragment);
        onClickDeelnemen();
        checkStatus();
    }

    public void checkStatus(){
        switch (status) {
            case "GESLOTEN":
                buttonInfo.setText(R.string.sessie_gesloten);
                buttonInfo.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                buttonInfo.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_red_500));
                buttonInfo.setEnabled(false);
                buttonDeelname.setText(R.string.geen_deelname);
                buttonDeelname.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                buttonDeelname.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_red_500));
                buttonDeelname.setEnabled(false);
                beurtTextview.setVisibility(View.GONE);
                checkIsDeelnemer();
                checkIsNotDeelnemer();
                break;
            case "OPEN":
                buttonInfo.setText(R.string.sessie_open);
                buttonInfo.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                buttonInfo.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_green_500));
                buttonInfo.setEnabled(false);
                checkIsDeelnemer();
                checkIsNotDeelnemer();
                beurtTextview.setVisibility(View.GONE);
                buttonVoegKaart.setEnabled(false);
                chatFragment.getChatButton().setEnabled(false);
                buttonVoegKaart.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                chatFragment.getChatButton().getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                break;
            case "GESTART":
                buttonInfo.setText(R.string.sessie_gestart);
                buttonInfo.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                buttonInfo.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_green_500));
                buttonInfo.setEnabled(false);
                buttonDeelname.setText(R.string.geen_deelname);
                buttonDeelname.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                buttonDeelname.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_red_500));
                buttonDeelname.setEnabled(false);
                checkIsDeelnemer();
                checkIsNotDeelnemer();
                break;
            case "BEEINDIGD":
                buttonInfo.setText(R.string.sessie_beindigd);
                buttonInfo.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                buttonInfo.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_black_1000));
                buttonInfo.setEnabled(false);
                buttonDeelname.setText(R.string.geen_deelname);
                buttonDeelname.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                buttonDeelname.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_red_500));
                buttonDeelname.setEnabled(false);
                isDeelnemer = false;
                buttonVoegKaart.setEnabled(false);
                buttonVoegKaart.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                beurtTextview.setVisibility(View.GONE);
                checkIsDeelnemer();
                break;

        }
    }
    public void checkIsNotDeelnemer(){
        if (!isDeelnemer) {
            buttonVoegKaart.setEnabled(false);
            buttonVoegKaart.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void checkIsDeelnemer(){
        Retrofit retrofit = Authorization.authorize(getActivity());
        DeelnameAPI deelnameAPI = retrofit.create(DeelnameAPI.class);
        Call<List<Deelname>> call = deelnameAPI.getDeelnamesVanCirkelsessie(cirkelsessieId);
        call.enqueue(new Callback<List<Deelname>>() {
            @Override
            public void onResponse(Response<List<Deelname>> response, Retrofit retrofit) {
                List<Deelname> deelnames = response.body();
                for (int i = 0; i < deelnames.size(); i++) {
                    if (deelnames.get(i).getCirkelsessie().getId() == Integer.parseInt(cirkelsessieId)) {
                        if (deelnames.get(i).getGebruiker().getId() == gebruiker.getId()) {
                            buttonDeelname.setText(R.string.deelgenomen);
                            buttonDeelname.setTextColor(Color.WHITE);
                            buttonDeelname.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                            buttonDeelname.setEnabled(false);
                            isDeelnemer = true;
                            if(status.equals("GESTART") && isDeelnemer){
                            buttonVoegKaart.setEnabled(true);
                            buttonVoegKaart.getBackground().setColorFilter(null);
                            }
                        }
                    }
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Log.d("checkIsDeelnemer:",t.getMessage());
            }
        });
    }

    public void onClickDeelnemen(){
        buttonDeelname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.deelnemen_y_n);
                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Retrofit retrofit = Authorization.authorize(getActivity());
                        DeelnameAPI deelnameAPI = retrofit.create(DeelnameAPI.class);
                        Call<Void> call = deelnameAPI.doeDeelname(cirkelsessieId);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Response<Void> response, Retrofit retrofit) {
                                buttonVoegKaart.setEnabled(true);
                                buttonVoegKaart.getBackground().setColorFilter(null);
                                buttonDeelname.setText(R.string.deelgenomen);
                                buttonDeelname.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
                                buttonDeelname.setEnabled(false);
                                isDeelnemer = true;
                            }
                            @Override
                            public void onFailure(Throwable t) {
                                Log.d("onCliclDeelnemen:", t.getMessage());

                            }
                        });

                    }
                });
                builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    public boolean isDeelnemer() {
        return isDeelnemer;
    }
}
