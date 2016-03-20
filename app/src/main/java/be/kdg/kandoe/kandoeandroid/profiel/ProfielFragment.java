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
import be.kdg.kandoe.kandoeandroid.authorization.Authorization;
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
    private EditText editText;

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
//        intent = new Intent(mActivity.getBaseContext(), SubthemaActivity.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profiel, container, false);
        editText = (EditText) v.findViewById(R.id.gebruikersnaam);

        getData();

        Button button = (Button) v.findViewById(R.id.buttonSave);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = Authorization.authorize(getActivity());
                GebruikerAPI gebruikerAPI = retrofit.create(GebruikerAPI.class);
                String gebruikersnaam = editText.getText().toString();
                gebruiker.setGebruikersnaam(gebruikersnaam);
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
        Retrofit retrofit = Authorization.authorize(getActivity());
        AuthAPI authAPI = retrofit.create(AuthAPI.class);
        Call<Gebruiker> call = authAPI.getGebruiker();
        call.enqueue(new Callback<Gebruiker>() {
            @Override
            public void onResponse(Response<Gebruiker> response, Retrofit retrofit){
                if(response != null){
                gebruiker = response.body();
                editText.setText(response.body().getGebruikersnaam());}
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity().getBaseContext(),"failure",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
