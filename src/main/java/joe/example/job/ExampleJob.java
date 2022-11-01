package joe.example.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ExampleJob {
    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        System.out.println(new Date().toString());
    }
}
