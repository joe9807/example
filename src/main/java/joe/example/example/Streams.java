package joe.example.example;

import java.util.Arrays;
import java.util.List;

public class Streams {
    public static void main (String[] args){
        List<String> letters = Arrays.asList("a", "b", "c", "d", "e");
        for (int i=0;i<100;i++) {
            String result = letters
                    .parallelStream()
                    .reduce("", (partialString, element) -> partialString + element);
            System.out.println(result);
        }
    }
}
