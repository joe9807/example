package joe.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import joe.example.entity.Example;
import joe.example.service.MQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/kafka")
@RestController
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true")
public class KafkaController {
    @Autowired
    //qualified by name here
    private MQService kafkaServiceImpl;

    @Operation(summary = "Send Message")
    @PostMapping("/send")
    public Mono<Example> send(){
        return kafkaServiceImpl.sendMessage();
    }

    @Operation(summary = "Send Messages")
    @Parameter(name="number", example="10")
    @PostMapping("/sendNumber")
    public Flux<Example> sendNumber(int number){
        return kafkaServiceImpl.sendMessages(number);
    }

    @Operation(summary = "List all Messages")
    @GetMapping("/list")
    public Flux<Example> list() {
        return kafkaServiceImpl.findAll();
    }

    @Operation(summary = "Delete All Messages")
    @DeleteMapping("/delete")
    public Mono<Void> deleteAll() {
        return kafkaServiceImpl.deleteAll();
    }

    @PostMapping("/callback")
    public Mono<Example> callback(@RequestBody Example example) {
        return kafkaServiceImpl.saveExample(example);
    }
}
