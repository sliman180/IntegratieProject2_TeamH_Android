package be.kdg.kandoe.kandoeandroid.authorization;

import android.app.Activity;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.helpers.SharedPreferencesMethods;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class Autorisatie {

    public static Retrofit authorize(Activity activity){

        final String token = SharedPreferencesMethods.getFromSharedPreferences(activity, activity.getString(R.string.token));
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request newRequest =
                        chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + token).build();
                return chain.proceed(newRequest);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                //10.0.3.2:8080 for localhost
                //http://teamh-spring.herokuapp.com
                .baseUrl(activity.getString(R.string.sourceURL))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit;
    }
}
