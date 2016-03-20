package be.kdg.kandoe.kandoeandroid.kaart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.api.SpelkaartAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Autorisatie;
import be.kdg.kandoe.kandoeandroid.pojo.response.Spelkaart;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class KaartActivity extends AppCompatActivity {

    private String spelkaartId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if(toolbar != null){
            toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        spelkaartId = extras.getString("spelkaartId");
        setTitle(extras.getString("spelkaartTitle"));

        getData();

    }

    private void getData(){
        Retrofit retrofit = Autorisatie.authorize(this);
        SpelkaartAPI spelkaartAPI = retrofit.create(SpelkaartAPI.class);
        Call<Spelkaart> call = spelkaartAPI.getSpelkaart(spelkaartId);
        call.enqueue(new Callback<Spelkaart>() {
            @Override
            public void onResponse(Response<Spelkaart> response, Retrofit retrofit) {
                TextView title = (TextView) findViewById(R.id.spelkaart);
                TextView kaart = (TextView) findViewById(R.id.spelkaart_kaart);
                TextView gebruiker = (TextView) findViewById(R.id.spelkaart_gebruiker);
                TextView url = (TextView) findViewById(R.id.spelkaart_url);
                if (title != null) {
                    title.setText("Kaart titel: "+ response.body().getKaart().getTekst());
                }
                if (kaart != null) {
                    kaart.setText("Positie op cirkel: "+ String.valueOf(response.body().getPositie()));
                }
                if (gebruiker != null) {
                    gebruiker.setText("Gemaakt door: " + response.body().getKaart().getGebruiker().getGebruikersnaam());
                }
                if (url != null) {
                    url.setText("Image url: " + response.body().getKaart().getImageUrl());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
