package joe.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import joe.example.entity.Example;
import joe.example.service.RabbitMQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/rabbitmq")
@RestController
public class RabbitMQController {

    @Autowired
    private RabbitMQService rabbitMQService;

    @Operation(summary = "Send Message")
    @PostMapping("/send")
    public String send(){
        return rabbitMQService.send();
    }

    @Operation(summary = "Receive Message")
    @GetMapping("/receive")
    public String receive() {
        return rabbitMQService.receiveMessage();
    }

    @Operation(summary = "List all Messages")
    @GetMapping("/list")
    public List<Example> list() {
        return rabbitMQService.findAll();
    }

    @PostMapping("/callback")
    public Example callback(@RequestBody Example example) {
        rabbitMQService.saveExample(example);
        return example;
    }
}
