package be.kdg.kandoe.kandoeandroid.login.api;


import java.util.List;

import be.kdg.kandoe.kandoeandroid.login.pojo.Cirkelsessie;
import retrofit.Call;
import retrofit.http.GET;

public interface CirkelsessieAPI {

    @GET("/api/cirkelsessies")
    Call<List<Cirkelsessie>> getCirkelsessies();
}
