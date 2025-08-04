package com.example.stoneocean;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.function.*;
public class temp {
}

class LambdaExample {

    // 接受一个 Consumer<String>：输入 String，无返回值
    public static void executeConsumer(Consumer<String> consumer, String input) {
        consumer.accept(input);
    }

    // 接受一个 Function<String, Integer>：输入 String，返回 Integer
    public static Integer executeFunction(Function<String, Integer> func, String input) {
        return func.apply(input);
    }

    // 接受一个 Predicate<Integer>：输入 Integer，返回 boolean
    public static boolean testCondition(Predicate<Integer> predicate, Integer value) {
        return predicate.test(value);
    }

    public static void main(String[] args) {

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String result = encoder.encode("123456");
        System.out.println(result);

        encoder.matches("123456", result);
    }
}