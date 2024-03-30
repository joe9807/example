package joe.example.client;

import jakarta.annotation.PostConstruct;
import joe.example.annotation.JoeAnnotation;
import joe.example.entity.Transactions;
import joe.example.repository.TransactionRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;

@Component
@Data
@RequiredArgsConstructor
public class TransactionsClient {

    private final TransactionRepository transactionRepository;

    @JoeAnnotation
    private String value;

    @PostConstruct
    public void init(){
        System.out.println(value);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String client(){
        String out = "client: Current transaction name: "+ TransactionSynchronizationManager.getCurrentTransactionName();
        System.out.println(out);
        transactionRepository.save(Transactions.builder().value(LocalDateTime.now().toString()+"_"+out).build());
        if (true) throw new RuntimeException("Test Exception");
        return "client";
    }
}
