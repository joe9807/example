package joe.example.service;

import joe.example.client.TransactionsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class TransactionsService {
    @Autowired
    private TransactionsClient client;

    @Transactional
    public String method1(){
        System.out.println("method1: Current transaction name: "+ TransactionSynchronizationManager.getCurrentTransactionName());
        String result = method2();
        client.client();
        return result;
    }

    public String method2(){
        System.out.println("method2: Current transaction name: "+ TransactionSynchronizationManager.getCurrentTransactionName());
        return "method2";
    }
}
