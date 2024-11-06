package pl.rstepniewski.demo.config.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@Slf4j
public class CacheConditionService {

    @Value("${cache.nbp.update-window.start-hour}")
    private int updateWindowStartHour;

    @Value("${cache.nbp.update-window.start-minute}")
    private int updateWindowStartMinute;

    @Value("${cache.nbp.update-window.end-hour}")
    private int updateWindowEndHour;

    @Value("${cache.nbp.update-window.end-minute}")
    private int updateWindowEndMinute;

    public boolean isWithinUpdateWindow() {
        LocalTime now = LocalTime.now();
        LocalTime windowStart = LocalTime.of(updateWindowStartHour, updateWindowStartMinute);
        LocalTime windowEnd = LocalTime.of(updateWindowEndHour, updateWindowEndMinute);

        boolean withinWindow = now.isAfter(windowStart) && now.isBefore(windowEnd);
        if (withinWindow) {
            log.debug("Current time {} is within NBP update window ({} - {})",
                    now, windowStart, windowEnd);
        }
        return withinWindow;
    }

    public boolean shouldUseCache() {
        return !isWithinUpdateWindow();
    }
}