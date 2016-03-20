package be.kdg.kandoe.kandoeandroid.profiel;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Min;

import java.util.List;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.api.AuthAPI;
import be.kdg.kandoe.kandoeandroid.api.GebruikerAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Autorisatie;
import be.kdg.kandoe.kandoeandroid.pojo.response.Gebruiker;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class ProfielFragment extends Fragment implements Validator.ValidationListener{

    private Gebruiker gebruiker;

    @NotEmpty
    private EditText gebruikersnaam;
    @Email(message = "Vul een geldige email")
    private EditText mail;
    @NotEmpty
    private EditText telefoon;
    @NotEmpty
    private EditText voornaam;
    @NotEmpty
    private EditText familienaam;

    @Password(min = 8, scheme = Password.Scheme.ALPHA_NUMERIC)
    private EditText wachtwoord;

    private Validator validator;

    private GebruikerAPI gebruikerAPI;


    public ProfielFragment() {
        // Required empty public constructor
    }


    public static ProfielFragment newInstance() {
        return new ProfielFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profiel, container, false);
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
                gebruikerAPI = retrofit.create(GebruikerAPI.class);
                String gebruikersnaamS = gebruikersnaam.getText().toString();
                String telefoonS = telefoon.getText().toString();
                String familienaamS = familienaam.getText().toString();
                String voornaamS = voornaam.getText().toString();
                String mailS = mail.getText().toString();
                if(!wachtwoord.getText().toString().equals("")){
                    String wachtwoordS = wachtwoord.getText().toString();
                    gebruiker.setWachtwoord(wachtwoordS);
                }
                gebruiker.setGebruikersnaam(gebruikersnaamS);
                gebruiker.setEmail(mailS);
                gebruiker.setVoornaam(voornaamS);
                gebruiker.setFamilienaam(familienaamS);
                gebruiker.setTelefoon(telefoonS);
                validator.validate();
            }
        });
        return v;
    }

    private void getData(){
        Retrofit retrofit = Autorisatie.authorize(getActivity());
        AuthAPI authAPI = retrofit.create(AuthAPI.class);
        Call<Gebruiker> call = authAPI.getGebruiker();
        call.enqueue(new Callback<Gebruiker>() {
            @Override
            public void onResponse(Response<Gebruiker> response, Retrofit retrofit){
                if(response != null){
                    gebruiker = response.body();
                    gebruikersnaam.setText(response.body().getGebruikersnaam());
                    mail.setText(response.body().getEmail());
                    telefoon.setText(response.body().getTelefoon());
                    voornaam.setText(response.body().getVoornaam());
                    familienaam.setText(response.body().getFamilienaam());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity().getBaseContext(),"failure",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        Call<Void> call = gebruikerAPI.updateGegevens(String.valueOf(gebruiker.getId()),gebruiker);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Response<Void> response, Retrofit retrofit) {
                Toast.makeText(getActivity().getBaseContext(), "succes gesaved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), "niet gesaved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity().getBaseContext());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getActivity().getBaseContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
