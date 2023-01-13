package joe.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import joe.example.entity.Example;
import joe.example.service.MQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/activemq")
@RestController
public class ActiveMQController {
    @Autowired
    @Qualifier("activeMQServiceImpl")
    private MQService exampleService;

    @Operation(summary = "Send Message")
    @GetMapping("/send")
    public Example send() {
        return exampleService.sendMessage();
    }

    @Operation(summary = "Receive Message")
    @GetMapping("/receive")
    public String receive() {
        return exampleService.receiveMessage();
    }

    @Operation(summary = "List all Messages")
    @GetMapping("/list")
    public List<Example> list() {
        return exampleService.findAll();
    }

    @Operation(summary = "Delete All Messages")
    @DeleteMapping("/delete")
    public void deleteAll() {
        exampleService.deleteAll();
    }

    @PostMapping("/callback")
    public Example callback(@RequestBody Example example) {
        return exampleService.saveExample(example);
    }
}