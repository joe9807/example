package joe.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import joe.example.entity.Example;
import joe.example.service.MQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    //qualified by name here
    private MQService kafkaServiceImpl;

    @Operation(summary = "Send Message")
    @PostMapping("/send")
    public Example send(){
        return kafkaServiceImpl.sendMessage();
    }

    @Operation(summary = "Send Messages")
    @Parameter(name="number", example="10")
    @PostMapping("/sendNumber")
    public List<Example> sendNumber(int number){
        return kafkaServiceImpl.sendMessages(number);
    }

    @Operation(summary = "List all Messages")
    @GetMapping("/list")
    public List<Example> list() {
        return kafkaServiceImpl.findAll();
    }

    @Operation(summary = "Delete All Messages")
    @DeleteMapping("/delete")
    public void deleteAll() {
        kafkaServiceImpl.deleteAll();
    }

    @PostMapping("/callback")
    public Example callback(@RequestBody Example example) {
        return kafkaServiceImpl.saveExample(example);
    }
}
