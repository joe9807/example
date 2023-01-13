package joe.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import joe.example.client.FeignClient;
import joe.example.entity.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/feign")
@RestController
public class FeignController {
    @Autowired
    private FeignClient feignClient;

    @Operation(summary = "Send Message from Feign Client")
    @Parameter(name="number", example="10")
    @GetMapping("/sendNumber")
    public List<Example> sendNumber(int number) {
        return feignClient.sendNumber(number);
    }
}
