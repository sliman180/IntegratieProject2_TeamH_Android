package be.kdg.kandoe.kandoeandroid.login.api;


import java.util.List;

import be.kdg.kandoe.kandoeandroid.login.pojo.Kaart;
import be.kdg.kandoe.kandoeandroid.login.pojo.Spelkaart;
import retrofit.Call;
import retrofit.http.GET;

public interface KaartenAPI {

    @GET("/api/kaarten")
    Call<List<Kaart>> getKaarten();


}
