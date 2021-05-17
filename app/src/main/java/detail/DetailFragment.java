package detail;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tvmazeschedule.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import base.BaseFragment;
import eventbus.ResponseDataEvent;
import model.Rating;
import model.Schedule;
import model.ScheduleResponse;
import model.Show;

public class DetailFragment extends BaseFragment {
    private static final String TAG = DetailFragment.class.getSimpleName();

    private Show show;
    private Schedule schedule;
    private ScheduleResponse scheduleResponse;
    private String title, summary, imageUrl, time;
    private ImageView showIv, backArrow, ratingIv;
    private TextView titleTv, timeTv, ratingTv;
    private ExpandableTextView expTv;
    private RecyclerView daysRv;
    private List<String> days = new ArrayList<>();
    private ScheduleDaysAdapter daysAdapter;
    private RecyclerView.LayoutManager daysRvLayoutManager;
    private LinearLayoutManager horizontalLayout;
    private NavController navController;
    public View rootView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            show = (Show) bundle.getSerializable("detail");
//            Log.d(TAG, "bundle passed to DetailFragment: " + show);
//        } else {
//            Log.d(TAG, "bundle was null");
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        rootView = view;

        expTv = rootView.findViewById(R.id.expand_text_view);
        titleTv = rootView.findViewById(R.id.title);
        showIv = rootView.findViewById(R.id.image_view);
        backArrow = rootView.findViewById(R.id.back_arrow);
        timeTv = rootView.findViewById(R.id.time_tv);
        ratingIv = rootView.findViewById(R.id.rating_iv);
        ratingTv = rootView.findViewById(R.id.rating_tv);
        daysRv = rootView.findViewById(R.id.schedule_days_rv);

        backArrow.setOnClickListener(backClickListener);
    }

    private final View.OnClickListener backClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(ResponseDataEvent event) {
        Log.d(TAG, "ScheduleResponseDataEvent received");
        scheduleResponse = event.getResponse();
        show = event.getShow();
        days = show.getSchedule().getDays();
        Log.d(TAG, "Days: " + days);

        daysRvLayoutManager = new LinearLayoutManager(getActivity());
        daysRv.setLayoutManager(daysRvLayoutManager);
        daysAdapter = new ScheduleDaysAdapter(days);
        horizontalLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        daysRv.setLayoutManager(horizontalLayout);
        daysRv.setAdapter(daysAdapter);

        populateViews();

        EventBus.getDefault().removeStickyEvent(event);
    }

    private void populateViews() {
        imageUrl = show.getImage().getOriginal();
        summary = show.getSummary();

        time = show.getSchedule().getTime();
        timeTv.setText(convertTime(time));

        String averageRating =  String.format(Locale.ENGLISH, "%.1f", show.getRating().getAverage());
        ratingTv.setText(averageRating);

        Spanned summaryParsed = Html.fromHtml(Html.fromHtml(summary).toString());
        title = show.getName();
        expTv.setText(summaryParsed);
        titleTv.setText(title);

        Glide.with(getActivity())
                .load(imageUrl)
                .apply(new RequestOptions().override(315, 443)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder))
                .into(showIv);
    }

    private String convertTime(String timeStr) {
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm", Locale.ENGLISH);
            final Date dateObj = sdf.parse(timeStr);
            Log.d(TAG, dateObj.toString());
            return new SimpleDateFormat("K:mm a", Locale.ENGLISH).format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getScreenDimensions() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        Glide.with(getActivity())
                .load(imageUrl)
                .apply(new RequestOptions().override(width, height)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder))
                .centerCrop()
                .into(showIv);
    }
}
