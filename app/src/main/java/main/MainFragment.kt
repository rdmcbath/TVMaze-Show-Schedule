package main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvmazeschedule.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import base.BaseFragment;
import detail.ScheduleDaysAdapter;
import model.ScheduleResponse;
import network.ApiClient;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends BaseFragment {
    private static final String TAG = MainFragment.class.getSimpleName();

    private ResponseAdapter responseAdapter;
    private List<ScheduleResponse> scheduleResponses = new ArrayList<>();;
    private List<String> days = new ArrayList<>();
    private RecyclerView showRecycler, daysRecycler;
    private NavController navController;
    private ImageView backArrow;
    private SearchView searchView;
    private View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        v = view;

        showRecycler = v.findViewById(R.id.scheduleRV);
        backArrow = v.findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(backClickListener);
        searchView = v.findViewById(R.id.search_view);

        initSearchView();
        fetchSchedule();
    }

    private final View.OnClickListener backClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };

    private void initSearchView() {
        ImageView magImage = (ImageView) searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        magImage.setVisibility(View.GONE);
        magImage.setImageDrawable(null);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ((MainActivity) getActivity()).hideKeyboard();
                if (TextUtils.isEmpty(query)) {
                    Toast.makeText(getActivity(), "No results", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    getShowsFromSearch(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        ImageView clearButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        clearButton.setColorFilter(ContextCompat.getColor(getActivity(), R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        clearButton.setOnClickListener(v -> {
            ((MainActivity) getActivity()).hideKeyboard();
            if(searchView.getQuery().length() == 0) {
                searchView.setIconified(true);
            } else {
                fetchSchedule();
                searchView.setQuery("", false);
            }
        });
    }

    public void fetchSchedule() {
        scheduleResponses.clear();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
        Date date = Calendar.getInstance().getTime();
        String strDate = dateFormat.format(date);

        ApiInterface service = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<ArrayList<ScheduleResponse>> call = service.getSchedule("US", strDate);

        call.enqueue(new Callback<ArrayList<ScheduleResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ScheduleResponse>> call, @NonNull Response<ArrayList<ScheduleResponse>> response) {
                scheduleResponses = response.body();
                Log.d(TAG, "response code: " + response.code() + "/ response.body: " + response.body().toString());
                responseAdapter = new ResponseAdapter(scheduleResponses, getActivity(), navController);
                LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                showRecycler.setLayoutManager(lm);
                showRecycler.setAdapter(responseAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<ScheduleResponse>> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

    public void getShowsFromSearch(String query) {
        scheduleResponses.clear();

        ApiInterface service = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<ArrayList<ScheduleResponse>> call = service.searchShows(query);

        call.enqueue(new Callback<ArrayList<ScheduleResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ScheduleResponse>> call, @NonNull Response<ArrayList<ScheduleResponse>> response) {
                scheduleResponses = response.body();
                Log.d(TAG, "response code: " + response.code() + "/ response.body: " + response.body());
                responseAdapter = new ResponseAdapter(scheduleResponses, getActivity(), navController);
                LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                showRecycler.setLayoutManager(lm);
                showRecycler.setAdapter(responseAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<ScheduleResponse>> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }
}
