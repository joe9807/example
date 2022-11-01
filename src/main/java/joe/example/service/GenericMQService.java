package joe.example.service;

import joe.example.entity.Example;
import joe.example.repository.ExampleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GenericMQService {

    @Autowired
    protected ExampleRepository repository;

    public List<Example> findAll(){
        return repository.findAll();
    }

    public Example saveExample(Example example) {
        return repository.save(example);
    }

    public void deleteAll(){
        repository.deleteAll();
    }
}
