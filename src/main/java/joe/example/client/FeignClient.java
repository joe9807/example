package joe.example.client;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.cloud.openfeign.FeignClient(name="kafka", url="${feign.client.url}", path="/kafka")
public interface FeignClient {
    @PostMapping("/sendNumber")
    String sendNumber(@RequestParam(value="number") int number);
}
