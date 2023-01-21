package joe.example.service;

import joe.example.client.TransactionsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@Transactional
public class TransactionsService {
    @Autowired
    private TransactionsClient client;

    public String method1(){
        System.out.println("method1: Current transaction name: "+ TransactionSynchronizationManager.getCurrentTransactionName());
        String result = method2();
        try {
            client.client();
        } catch (Exception e) {
            System.out.println("Test Exception");
        }
        return result;
    }

    public String method2(){
        System.out.println("method2: Current transaction name: "+ TransactionSynchronizationManager.getCurrentTransactionName());
        return "method2";
    }

    public String getValue(){
        return client.getValue();
    }
}
