package be.kdg.kandoe.kandoeandroid.login.api;


import java.util.List;

import be.kdg.kandoe.kandoeandroid.login.pojo.Cirkelsessie;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

public interface CirkelsessieAPI {

    @GET("/api/cirkelsessies")
    Call<List<Cirkelsessie>> getCirkelsessies();

    @GET("/api/cirkelsessies/{id}")
    Call<Cirkelsessie> getCirkelsessie(@Path("id") String id);
}
