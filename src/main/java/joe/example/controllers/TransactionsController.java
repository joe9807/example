package joe.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import joe.example.service.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {
    @Autowired
    private TransactionsService transactionsService;

    @Operation(summary = "Perform Transaction")
    @GetMapping("run")
    public String transaction(){
        return transactionsService.method1();
    }
}
