package joe.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import joe.example.client.FeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/feign")
@RestController
public class FeignController {
    @Autowired
    private FeignClient feignClient;

    @Operation(summary = "Send Message from Feign Client")
    @Parameter(name="number", example="10")
    @GetMapping("/sendNumber")
    public String send(int number) {
        return feignClient.sendNumber(number);
    }
}
