package joe.example.service;

import joe.example.client.TransactionsClient;
import joe.example.entity.Transactions;
import joe.example.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@Transactional
public class TransactionsService {
    @Autowired
    private TransactionsClient client;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ApplicationContext applicationContext;

    public String method1(){
        String out = "method1: Current transaction name: "+ TransactionSynchronizationManager.getCurrentTransactionName();
        System.out.println(out);
        transactionRepository.save(Transactions.builder().value(LocalDateTime.now().toString()+"_"+out).build());
        String result = method2();
        try {
            client.client();
        } catch (Exception e) {
            System.out.println("Test Exception");
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String method2(){
        System.out.println("method2: Current transaction name: "+ TransactionSynchronizationManager.getCurrentTransactionName());
        return "method2";
    }

    public String getValue(){
        return client.getValue();
    }

    public Flux<String> getBeanDefinitionNames(){
        return Flux.just(String.join("\n", applicationContext.getBeanDefinitionNames()));
    }
}
