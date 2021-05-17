package base;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tvmazeschedule.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //Captures all events, needed at the BaseActivity level to prevent Subscriber class errors
    @Subscribe
    public void onEvent(Object event) {
    }
}
