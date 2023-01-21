package joe.example.client;

import joe.example.annotation.JoeAnnotation;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;

@Component
@Data
public class TransactionsClient {

    @JoeAnnotation
    private String value;

    @PostConstruct
    public void init(){
        System.out.println(value);
    }

    @Transactional
    public String client(){
        System.out.println("client: Current transaction name: "+ TransactionSynchronizationManager.getCurrentTransactionName());
        if (true) throw new RuntimeException("Test Exception");
        return "client";
    }
}
