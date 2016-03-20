package be.kdg.kandoe.kandoeandroid.api;


import java.util.List;

import be.kdg.kandoe.kandoeandroid.pojo.response.Bericht;
import be.kdg.kandoe.kandoeandroid.pojo.response.Cirkelsessie;
import be.kdg.kandoe.kandoeandroid.pojo.request.BerichtRequest;
import be.kdg.kandoe.kandoeandroid.pojo.request.KaartRequest;
import be.kdg.kandoe.kandoeandroid.pojo.response.Kaart;
import be.kdg.kandoe.kandoeandroid.pojo.response.Spelkaart;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 *  interface om de cirkelsessie web api aan te spreken
 */
public interface CirkelsessieAPI {

    @GET("/api/cirkelsessies/gesloten")
    Call<List<Cirkelsessie>> getCirkelsessiesGesloten();

    @GET("/api/cirkelsessies/gepland")
    Call<List<Cirkelsessie>> getCirkelsessiesGepland();

    @GET("/api/cirkelsessies/beindigd")
    Call<List<Cirkelsessie>> getCirkelsessiesEnded();

    @GET("/api/cirkelsessies/actief")
    Call<List<Cirkelsessie>> getCirkelsessiesStarted();

    @GET("/api/cirkelsessies/{id}")
    Call<Cirkelsessie> getCirkelsessie(@Path("id") String id);

    @GET("/api/cirkelsessies/{id}/spelkaarten")
    Call<List<Spelkaart>> getSpelkaarten(@Path("id") String id);

    @POST("/api/cirkelsessies/{id}/spelkaarten")
    Call<Kaart> createSpelKaart(@Path("id") String id, @Body KaartRequest kaart);

    @POST("/api/cirkelsessies/{id}/berichten")
    Call<Void> addBericht(@Path("id") String id, @Body BerichtRequest bericht);

    @GET("/api/cirkelsessies/{id}/berichten")
    Call<List<Bericht>> getBerichten(@Path("id") String id);
}
