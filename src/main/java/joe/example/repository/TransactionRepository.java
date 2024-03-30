package joe.example.repository;

import joe.example.entity.Transactions;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends R2dbcRepository<Transactions, Long> {
}
