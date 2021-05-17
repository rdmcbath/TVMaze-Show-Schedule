package eventbus;

import model.Schedule;
import model.ScheduleResponse;
import model.Show;

public class ResponseDataEvent {
    private final ScheduleResponse response;
    private Show show;
    private Schedule schedule;

    public ResponseDataEvent(ScheduleResponse response, Show show, Schedule schedule) {
        this.response = response;
        this.show = show;
        this.schedule = schedule;
    }

    public ScheduleResponse getResponse() {
        return response;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Show getShow() {
        return show;
    }
}
