package be.kdg.kandoe.kandoeandroid.api;


import java.util.List;

import be.kdg.kandoe.kandoeandroid.pojo.Cirkelsessie;
import be.kdg.kandoe.kandoeandroid.pojo.request.KaartRequest;
import be.kdg.kandoe.kandoeandroid.pojo.response.Kaart;
import be.kdg.kandoe.kandoeandroid.pojo.Spelkaart;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface CirkelsessieAPI {

    @GET("/api/cirkelsessies")
    Call<List<Cirkelsessie>> getCirkelsessies();

    @GET("/api/cirkelsessies/gesloten")
    Call<List<Cirkelsessie>> getCirkelsessiesGesloten();

    @GET("/api/cirkelsessies/gepland")
    Call<List<Cirkelsessie>> getCirkelsessiesOpen();

    @GET("/api/cirkelsessies/beindigd")
    Call<List<Cirkelsessie>> getCirkelsessiesEnded();

    @GET("/api/cirkelsessies/{id}")
    Call<Cirkelsessie> getCirkelsessie(@Path("id") String id);

    @GET("/api/cirkelsessies/{id}/spelkaarten")
    Call<List<Spelkaart>> getSpelkaarten(@Path("id") String id);

    @POST("/api/cirkelsessies/{id}/spelkaarten")
    Call<Kaart> createSpelKaart(@Path("id") String id, @Body KaartRequest kaart);
}
