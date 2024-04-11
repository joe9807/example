package joe.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import joe.example.entity.Example;
import joe.example.service.MQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RequestMapping("/activemq")
@RestController
@ConditionalOnProperty(value = "activemq.enabled", havingValue = "true")
public class ActiveMQController {
    @Autowired
    @Qualifier("activeMQServiceImpl")
    private MQService exampleService;

    @Operation(summary = "Send Message")
    @GetMapping("/send")
    public Mono<Example> send() {
        return exampleService.sendMessage();
    }

    @Operation(summary = "Receive Message")
    @GetMapping("/receive")
    public String receive() {
        return exampleService.receiveMessage();
    }

    @Operation(summary = "List all Messages")
    @GetMapping("/list")
    public Flux<Example> list() {
        return exampleService.findAll().delayElements(Duration.ofSeconds(2));
    }

    @Operation(summary = "Delete All Messages")
    @DeleteMapping("/delete")
    public void deleteAll() {
        exampleService.deleteAll();
    }

    @PostMapping("/callback")
    public Mono<Example> callback(@RequestBody Example example) {
        return exampleService.saveExample(example);
    }
}