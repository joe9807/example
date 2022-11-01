package joe.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import joe.example.entity.Example;
import joe.example.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/kafka")
@RestController
public class KafkaController {
    @Autowired
    private KafkaService kafkaService;

    @Operation(summary = "Send Message")
    @PostMapping("/send")
    public String send(){
        return kafkaService.sendMessage();
    }

    @Operation(summary = "List all Messages")
    @GetMapping("/list")
    public List<Example> list() {
        return kafkaService.findAll();
    }

    @Operation(summary = "Delete All Messages")
    @GetMapping("/delete")
    public void deleteAll() {
        kafkaService.deleteAll();
    }

    @PostMapping("/callback")
    public Example callback(@RequestBody Example example) {
        return kafkaService.saveExample(example);
    }
}
