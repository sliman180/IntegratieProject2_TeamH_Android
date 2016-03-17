package be.kdg.kandoe.kandoeandroid.api;

import java.util.List;

import be.kdg.kandoe.kandoeandroid.pojo.Hoofdthema;
import be.kdg.kandoe.kandoeandroid.pojo.Organisatie;
import be.kdg.kandoe.kandoeandroid.pojo.Subthema;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;


public interface OrganisatieAPI {
    @GET("/api/gebruikers/{id}/organisaties")
    Call<List<Organisatie>> getOrganisaties(@Path("id") String id);

    @GET("/api/organisaties/{id}/hoofdthemas")
    Call<List<Hoofdthema>> getHoofthemasFromOrganisaties(@Path("id") String id);


}
