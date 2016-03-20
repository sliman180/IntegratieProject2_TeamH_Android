package be.kdg.kandoe.kandoeandroid.authorization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.api.AuthAPI;
import be.kdg.kandoe.kandoeandroid.pojo.request.RegistratieRequest;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RegistratieActivity extends AppCompatActivity implements Validator.ValidationListener{

    @NotEmpty(message = "Minstens 6 characters")
    private TextView mUsername;
    @Password(min = 8, scheme = Password.Scheme.ALPHA_NUMERIC,message = "Minstens 8 characters")
    private TextView mPassword;
    @Password(min = 8, scheme = Password.Scheme.ALPHA_NUMERIC,message = "Minstens 8 characters")
    private TextView mPassword2;
    @Email(message = "Vul een geldige email")
    private TextView mEmail;

    private final static String REGISTER = "register";

    private Activity mActivity;

    private AuthAPI authAPI;

    private String password;
    private String password2;
    private String username;
    private String email;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mActivity = this;
        validator = new Validator(this);
        validator.setValidationListener(this);

        // region TextViews
        mUsername = (TextView) findViewById(R.id.gebruikersnaam);
        mPassword = (TextView) findViewById(R.id.wachtwoord);
        mPassword2 = (TextView) findViewById(R.id.wachtwoord2);
        mEmail = (TextView) findViewById(R.id.email);
        //endregion

        Button registerButton = (Button) findViewById(R.id.registreer_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
         password = mPassword.getText().toString();
         password2 = mPassword2.getText().toString();
         username = mUsername.getText().toString();
         email = mEmail.getText().toString();
        if (!password.equals(password2)) {
            Toast.makeText(mActivity, R.string.password_mismatch, Toast.LENGTH_LONG).show();
            return;
        }
        authAPI = Autorisatie.authorize(this).create(AuthAPI.class);
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        Call<Void> call = authAPI.register(new RegistratieRequest(username, password, password,email));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Response<Void> response, Retrofit retrofit) {
                Toast.makeText(mActivity, R.string.register_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mActivity, AanmeldActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mActivity, R.string.register_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getBaseContext());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
