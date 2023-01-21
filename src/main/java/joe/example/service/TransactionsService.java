package joe.example.service;

import joe.example.client.TransactionsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionsService {
    @Autowired
    private TransactionsClient client;

    @Autowired
    private ApplicationContext applicationContext;

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

    public String getBeanDefinitionNames(){
        return String.join("\n", applicationContext.getBeanDefinitionNames());
    }
}
