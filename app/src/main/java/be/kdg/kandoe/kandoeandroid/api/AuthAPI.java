package be.kdg.kandoe.kandoeandroid.api;

import be.kdg.kandoe.kandoeandroid.pojo.response.Credentials;
import be.kdg.kandoe.kandoeandroid.pojo.response.Gebruiker;
import be.kdg.kandoe.kandoeandroid.pojo.response.Token;
import be.kdg.kandoe.kandoeandroid.pojo.request.RegistratieRequest;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 *  interface om de auth web api aan te spreken
 */
public interface AuthAPI {

    @GET("/auth/profile")
    Call<Gebruiker> getGebruiker();

    @POST("auth/login")
    Call<Token> login(@Body Credentials credentials);

    @POST("auth/register")
    Call<Void> register(@Body RegistratieRequest registratieRequest);
}
