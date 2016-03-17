package be.kdg.kandoe.kandoeandroid.api;


import java.util.List;

import be.kdg.kandoe.kandoeandroid.pojo.response.Kaart;
import retrofit.Call;
import retrofit.http.GET;

public interface KaartenAPI {

    @GET("/api/kaarten")
    Call<List<Kaart>> getKaarten();

}
