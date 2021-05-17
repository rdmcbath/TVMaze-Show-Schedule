package detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvmazeschedule.R;

import java.util.List;

public class ScheduleDaysAdapter extends RecyclerView.Adapter<ScheduleDaysAdapter.RecyclerViewHolder> {

    private final List<String> days;

    public ScheduleDaysAdapter(List<String> horizontalList) {
        this.days = horizontalList;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.day_tv);
        }
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_days_schedule, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleDaysAdapter.RecyclerViewHolder holder, int position) {
        holder.textView.setText(days.get(position));
    }

    @Override
    public int getItemCount() {
        if (days != null) {
            return days.size();
        } else {
            return 0;
        }
    }
}
