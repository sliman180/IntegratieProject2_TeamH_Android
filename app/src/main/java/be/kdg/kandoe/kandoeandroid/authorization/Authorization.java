package be.kdg.kandoe.kandoeandroid.authorization;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class Authorization {
    public static Retrofit authorize(final String token){
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build();
                return chain.proceed(newRequest);
            }
        });

        return new Retrofit.Builder()
                .baseUrl("http://teamh-spring.herokuapp.com").client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
