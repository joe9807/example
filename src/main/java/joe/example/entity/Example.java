package joe.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Example {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int value;

    @Enumerated(EnumType.STRING)
    private ExampleState state;

    @Transient
    private String callbackUrl;

    @JsonIgnore
    public ExampleKey getKey(String tag){
        return new ExampleKey(value/10, tag);
    }
}
