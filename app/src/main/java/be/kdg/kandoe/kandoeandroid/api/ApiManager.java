package be.kdg.kandoe.kandoeandroid.api;


import android.app.Activity;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.List;

import be.kdg.kandoe.kandoeandroid.MainActivity;
import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.helpers.SharedPreferencesMethods;
import be.kdg.kandoe.kandoeandroid.pojo.Deelname;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public class ApiManager {

    public interface DeelnameAPI {

        @POST("/api/deelnames/{id}")
        Call<Void> doeDeelname(@Path("id") String Id);

        @GET("/api/gebruikers/deelnames")
        Call<List<Deelname>> getDeelnames();

    }

    public static void addInterceptor(Activity activity){
        final String token = SharedPreferencesMethods.getFromSharedPreferences(activity, activity.getString(R.string.token));
        client.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Interceptor.Chain chain) throws
                    IOException {
                Request newRequest =
                        chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + token).build();
                return chain.proceed(newRequest);
            }
        });
    }
    private static final OkHttpClient client = new OkHttpClient();

    private static final Retrofit RETROFIT = new Retrofit.Builder()
            //10.0.3.2 voor localhost
            .baseUrl("http://teamh-spring.herokuapp.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final DeelnameAPI DEELNAME_API = RETROFIT.create(DeelnameAPI.class);

    public static DeelnameAPI getDeelnameApiService() {
        return DEELNAME_API;
    }
}
