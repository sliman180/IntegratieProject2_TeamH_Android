package be.kdg.kandoe.kandoeandroid.api;

import java.util.List;

import be.kdg.kandoe.kandoeandroid.pojo.response.Gebruiker;
import be.kdg.kandoe.kandoeandroid.pojo.response.Subthema;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 *  interface om de gebruiker web api aan te spreken
 */
public interface GebruikerAPI {

    @PUT("/api/gebruikers/{id}")
    Call<Void> updateGegevens(@Path("id") String id, @Body Gebruiker gebruiker);

    @GET("/api/gebruikers/{id}/subthemas")
    Call<List<Subthema>> getSubThemas(@Path("id") String id);

}
