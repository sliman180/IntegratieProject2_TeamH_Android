package be.kdg.kandoe.kandoeandroid.api;

import retrofit.Call;
import retrofit.http.POST;
import retrofit.http.Path;

public interface DeelnameAPI {

    @POST("/api/deelnames/{id}")
    Call<Void> doeDeelname(@Path("id") String Id);



}
