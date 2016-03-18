package be.kdg.kandoe.kandoeandroid.api;


import java.util.List;

import be.kdg.kandoe.kandoeandroid.pojo.Hoofdthema;
import be.kdg.kandoe.kandoeandroid.pojo.Subthema;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

public interface HoofdthemaAPI {
    @GET("/api/gebruikers/{id}/hoofdthemas")
    Call<List<Hoofdthema>> getHoofdthemas(@Path("id") String id);

    @GET("/api/hoofdthemas/{id}/subthemas")
    Call<List<Subthema>> getSubthemasFromHoofdthema(@Path("id") String id);
}