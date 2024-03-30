package joe.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Example {
    @Id
    private Long id;

    private Long value;

    @Enumerated(EnumType.STRING)
    private ExampleState state;

    @Transient
    private String callbackUrl;

    @JsonIgnore
    public ExampleKey getKey(String tag){
        return new ExampleKey(value /10, tag);
    }
}
