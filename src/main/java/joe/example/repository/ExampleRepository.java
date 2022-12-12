package joe.example.repository;

import joe.example.entity.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {

    List<Example> findAll();

    Example save(Example example);

    void deleteAll();
}
