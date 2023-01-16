package joe.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import joe.example.entity.Example;
import joe.example.service.MQService;
import joe.example.service.impl.RabbitMQServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/rabbitmq")
@RestController
@ConditionalOnProperty(value = "rabbitmq.enabled", havingValue = "true")
public class RabbitMQController {
    @Autowired
    @Qualifier("rabbitMQServiceImpl")
    private RabbitMQServiceImpl rabbitMQService;

    @Operation(summary = "Send Message")
    @PostMapping("/send")
    public Example send(){
        return rabbitMQService.sendMessage();
    }

    @Operation(summary = "Receive Message")
    @Parameter(name="dlq", example="true")
    @GetMapping("/receive")
    public String receive(boolean dlq) {
        return rabbitMQService.receiveMessage(dlq);
    }

    @Operation(summary = "List all Messages")
    @GetMapping("/list")
    public List<Example> list() {
        return rabbitMQService.findAll();
    }

    @Operation(summary = "Delete All Messages")
    @DeleteMapping("/delete")
    public void deleteAll() {
        rabbitMQService.deleteAll();
    }

    @PostMapping("/callback")
    public Example callback(@RequestBody Example example) {
        return rabbitMQService.saveExample(example);
    }
}
