package joe.example.client;

import org.springframework.web.bind.annotation.PostMapping;

@org.springframework.cloud.openfeign.FeignClient(name="kafka", url="${feign.client.url}", path="/kafka")
public interface FeignClient {
    @PostMapping("/send")
    public String sendFeign();
}
