package com.example.stoneocean.Util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NotContainsValidator.class)
public @interface NotContains {

    String message() default "不能包含非法字符: ${value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // 要禁止的子字符串
    String value() default "";

    // 支持多个禁止的字符串（可选）
    String[] values() default {};
}