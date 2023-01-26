package joe.example.client;

import joe.example.entity.Example;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@org.springframework.cloud.openfeign.FeignClient(name="kafka", url="${feign.client.url}", path="/kafka")
public interface FeignClient {
    @PostMapping("/sendNumber")
    List<Example> sendNumberAnotherName(@RequestParam(value="number") int number);
}
