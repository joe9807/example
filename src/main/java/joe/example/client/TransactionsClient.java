package joe.example.client;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class TransactionsClient {

    @Transactional
    public String client(){
        System.out.println("client: Current transaction name: "+ TransactionSynchronizationManager.getCurrentTransactionName());
        if (true) throw new RuntimeException("Test Exception");
        return "client";
    }
}
