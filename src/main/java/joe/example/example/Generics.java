package joe.example.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Generics <T0, T1 extends Number, T2, T3>{
    private final T0 zeroValue;

    Generics(T0 zeroValue){
        this.zeroValue = zeroValue;
    }

    public T0 getZeroValue(){
        return zeroValue;
    }

    public <T> void setValue(T value){

    }

    public static void main(String[] args){
        //test commit master
        Generics<String, Integer, Long, Object> generic = new Generics<>("zero value");
        String zeroValue = generic.getZeroValue();
        generic.setValue("");
        Generics<List<String>, Double, Map<String, String>, java.util.Queue<String>> generic1 = new Generics<>(new ArrayList<>());
        List<String> zeroValue1 = generic1.getZeroValue();
        System.out.println(zeroValue);
        System.out.println(zeroValue1);
    }
}
