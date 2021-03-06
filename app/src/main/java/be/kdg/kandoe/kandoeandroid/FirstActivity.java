package be.kdg.kandoe.kandoeandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import be.kdg.kandoe.kandoeandroid.authorization.AanmeldActivity;
import be.kdg.kandoe.kandoeandroid.authorization.RegistratieActivity;

public class FirstActivity extends AppCompatActivity {
    private AppCompatActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        mActivity = this;

        //region Scale circle image
        ImageView circle = (ImageView) findViewById(R.id.circel_afbeelding);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        ViewGroup.LayoutParams params = circle.getLayoutParams();
        params.height = width / 2;
        circle.setLayoutParams(params);
        //endregion

        //region Buttons
        Button login = (Button) findViewById(R.id.aanmeld_button);
        Button register = (Button) findViewById(R.id.registreer_button);

        if (login != null) {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mActivity, AanmeldActivity.class));

                }
            });
        }

        if (register != null) {
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mActivity, RegistratieActivity.class));
                }
            });
        }
        //endregion
    }
}
