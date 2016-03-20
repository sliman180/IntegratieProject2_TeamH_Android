package be.kdg.kandoe.kandoeandroid.profiel;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.api.AuthAPI;
import be.kdg.kandoe.kandoeandroid.api.GebruikerAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Autorisatie;
import be.kdg.kandoe.kandoeandroid.helpers.SharedPreferencesMethods;
import be.kdg.kandoe.kandoeandroid.pojo.response.Gebruiker;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class ProfielFragment extends Fragment {

    private String token;

    private Activity mActivity;

    private Gebruiker gebruiker;
    private Intent intent;

    private View v;
    private EditText gebruikersnaam;
    private EditText mail;
    private EditText telefoon;
    private EditText voornaam;
    private EditText familienaam;
    private EditText wachtwoord;


    public ProfielFragment() {
        // Required empty public constructor
    }


    public static ProfielFragment newInstance() {
        return new ProfielFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            mActivity = getActivity();
            token = SharedPreferencesMethods.getFromSharedPreferences(mActivity, getString(R.string.token));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profiel, container, false);
        gebruikersnaam = (EditText) v.findViewById(R.id.gebruikersnaam_ed);
        mail = (EditText) v.findViewById(R.id.email_ed);
        telefoon = (EditText) v.findViewById(R.id.telefoon_ed);
        voornaam = (EditText) v.findViewById(R.id.voornaam_ed);
        familienaam = (EditText) v.findViewById(R.id.familienaam_ed);
        wachtwoord = (EditText) v.findViewById(R.id.wachtwoord_ed);


        getData();

        Button button = (Button) v.findViewById(R.id.buttonSave);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = Autorisatie.authorize(getActivity());
                GebruikerAPI gebruikerAPI = retrofit.create(GebruikerAPI.class);
                String gebruikersnaamS = gebruikersnaam.getText().toString();
                gebruiker.setGebruikersnaam(gebruikersnaamS);
                gebruiker.setWachtwoord("admin");
                Call<Void> call = gebruikerAPI.updateGegevens(String.valueOf(gebruiker.getId()),gebruiker);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Response<Void> response, Retrofit retrofit) {
                        Toast.makeText(getActivity().getBaseContext(),"succes gesaved",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(getActivity().getBaseContext(),"niet gesaved",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return v;
    }

    public void getData(){
        Retrofit retrofit = Autorisatie.authorize(getActivity());
        AuthAPI authAPI = retrofit.create(AuthAPI.class);
        Call<Gebruiker> call = authAPI.getGebruiker();
        call.enqueue(new Callback<Gebruiker>() {
            @Override
            public void onResponse(Response<Gebruiker> response, Retrofit retrofit){
                if(response != null){
                gebruiker = response.body();
                gebruikersnaam.setText(response.body().getGebruikersnaam());}
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity().getBaseContext(),"failure",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
