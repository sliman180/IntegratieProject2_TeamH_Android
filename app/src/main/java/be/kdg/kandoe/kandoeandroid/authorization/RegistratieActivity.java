package be.kdg.kandoe.kandoeandroid.authorization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.api.AuthAPI;
import be.kdg.kandoe.kandoeandroid.pojo.request.RegistratieRequest;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RegistratieActivity extends AppCompatActivity {
    private TextView mUsername;
    private TextView mPassword;
    private TextView mPassword2;
    private TextView mEmail;

    private final static String REGISTER = "register";

    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mActivity = this;

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
        String password = mPassword.getText().toString();
        String password2 = mPassword2.getText().toString();
        String username = mUsername.getText().toString();
        String email = mEmail.getText().toString();
        if (!password.equals(password2)) {
            Toast.makeText(mActivity, R.string.password_mismatch, Toast.LENGTH_LONG).show();
            return;
        }
        AuthAPI authAPI = Autorisatie.authorize(this).create(AuthAPI.class);
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
}
