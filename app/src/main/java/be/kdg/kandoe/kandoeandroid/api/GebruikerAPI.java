package be.kdg.kandoe.kandoeandroid.api;

import be.kdg.kandoe.kandoeandroid.pojo.Gebruiker;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;


public interface GebruikerAPI {

    @GET("api/gebruikers/[id]")
    Call<Gebruiker> getGebruiker(@Path("id") String id);


}
