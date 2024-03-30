package joe.example.repository;

import joe.example.entity.Example;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepository extends R2dbcRepository<Example, Long> {
}
