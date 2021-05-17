package main;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tvmazeschedule.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

import eventbus.ResponseDataEvent;
import model.Schedule;
import model.ScheduleResponse;
import model.Show;

public class ResponseAdapter extends RecyclerView.Adapter<ResponseAdapter.RecyclerViewHolder> {
    private static final String TAG = ResponseAdapter.class.getSimpleName();

    private List<ScheduleResponse> scheduleResponses;
    private Context mContext;
    private NavController navController;
    String imageUrl = "";

    public ResponseAdapter(List<ScheduleResponse> showData, Context mContext, NavController navController) {
        this.scheduleResponses = showData;
        this.mContext = mContext;
        this.navController = navController;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        final ScheduleResponse scheduleResponse = scheduleResponses.get(position);
        final Show show = scheduleResponse.getShow();
        final Schedule schedule = scheduleResponse.getShow().getSchedule();

        if (scheduleResponse.getShow().getImage() != null) {
            imageUrl = scheduleResponse.getShow().getImage().getMedium();
        }

        if (scheduleResponse.getShow().getName() != null) {
            holder.showTitle.setText(scheduleResponses.get(position).getShow().getName());
        }

        if (scheduleResponses.get(position).getShow().getRuntime() != null) {
            holder.runTime.setText(String.format(Locale.ENGLISH, "%d minutes", scheduleResponses.get(position).getShow().getRuntime()));
        } else {
            holder.runTime.setText("");
        }

        if (scheduleResponses.get(position).getShow().getNetwork() != null) {
            String networkStr = scheduleResponses.get(position).getShow().getNetwork().getName();
            Spanned networkParsed = Html.fromHtml(Html.fromHtml(networkStr).toString());
            holder.showNetwork.setText(networkParsed);
        } else {
            holder.showNetwork.setText("");
        }

        Glide.with(mContext)
                .load(imageUrl)
                .apply(new RequestOptions().override(158, 220)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder))
                .into(holder.showImage);

        holder.showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new ResponseDataEvent(scheduleResponse, show, schedule));

                // TODO: other ways to do things
//                Fragment detailFragment = new DetailFragment();
//                Bundle data = new Bundle();
//                data.putSerializable("detail", show);
//                detailFragment.setArguments(data);
//                MainActivity activity = (MainActivity) v.getContext();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, detailFragment, "DetailFragment").addToBackStack(null).commit();

                navController = Navigation.findNavController(v);
                navController.navigate(R.id.detailFragment);

            }
        });
    }

    @Override
    public int getItemCount() {
        if (scheduleResponses != null) {
            return scheduleResponses.size();
        } else {
            return 0;
        }
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView showTitle, runTime, showNetwork;
        private ImageView showImage;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            showTitle = itemView.findViewById(R.id.showName);
            showImage = itemView.findViewById(R.id.showImage);
            showNetwork = itemView.findViewById(R.id.showNetwork);
            runTime = itemView.findViewById(R.id.runTime);
        }
    }
}
