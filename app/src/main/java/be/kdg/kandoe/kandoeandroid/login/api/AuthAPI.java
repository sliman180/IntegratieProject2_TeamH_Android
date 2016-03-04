package be.kdg.kandoe.kandoeandroid.login.api;

import be.kdg.kandoe.kandoeandroid.login.pojo.Credentials;
import be.kdg.kandoe.kandoeandroid.login.pojo.Token;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

public interface AuthAPI {
    @POST("auth/login")
    Call<Token> login(@Body Credentials credentials);
}
