package be.kdg.kandoe.kandoeandroid.login.api;


import java.util.List;

import be.kdg.kandoe.kandoeandroid.login.pojo.Spelkaart;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface SpelkaartenAPI {

    @POST("/api/spelkaarten/verschuif/{id}")
    Call<Spelkaart> verschuif(@Path("id") String id);
}
