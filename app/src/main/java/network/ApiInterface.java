package network;

import java.util.ArrayList;

import model.ScheduleResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("schedule")
    Call <ArrayList<ScheduleResponse>> getSchedule(
            @Query("country") String country,
            @Query("date") String date
    );

    @GET("search/shows")
    Call <ArrayList<ScheduleResponse>> searchShows(
            @Query("q") String query
    );

}
