package joe.example.bpp;

import joe.example.annotation.JoeAnnotation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.stream.IntStream;

public class JoeBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        for (Field field: bean.getClass().getDeclaredFields()){
            if (field.isAnnotationPresent(JoeAnnotation.class)) {
                generateValue(bean, field);
            }
        }

        return bean;
    }

    private void generateValue(Object bean, Field field){
        int random = new Random().nextInt(30);
        final StringBuilder result = new StringBuilder();
        for (int i=0;i<random;i++) {
            Random r = new Random();
            char c = (char)(r.nextInt(26) + 'a');
            IntStream.range(0, new Random().nextInt(5)).forEach(unused -> result.append(c));
        }

        try {
            field.setAccessible(true);
            field.set(bean, result.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().getName().contains("TransactionsClient")){
            System.out.println("sdfdg");
        }
        for (Field field: bean.getClass().getDeclaredFields()){
            if (field.isAnnotationPresent(JoeAnnotation.class)) {
                try {
                    field.setAccessible(true);
                    String value = (String) field.get(bean);
                    field.set(bean, getCompressedValue(value));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        return bean;
    }

    private String getCompressedValue(String value){
        int count = 0;
        char prevChar = 0;
        final StringBuilder result = new StringBuilder();
        for (char ch:value.toCharArray()){
            if (ch != prevChar) {
                if (count != 0) {
                    result.append(count);
                }

                result.append(ch);

                count = 0;
            }

            prevChar = ch;
            count++;
        }

        result.append(count);
        return result.toString();
    }
}
