package be.kdg.kandoe.kandoeandroid.api;

import java.util.List;

import be.kdg.kandoe.kandoeandroid.pojo.Organisatie;
import be.kdg.kandoe.kandoeandroid.pojo.Subthema;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;


public interface OrganisatieAPI {
    @GET("/api/organisaties/my")
    Call<List<Organisatie>> getOrganisaties();


}
