package com.example.stoneocean.Util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotContainsValidator implements ConstraintValidator<NotContains, String> {

    private String[] forbiddenStrings;

    @Override
    public void initialize(NotContains constraintAnnotation) {
        String value = constraintAnnotation.value();
        String[] values = constraintAnnotation.values();

        if (values.length == 0) {
            this.forbiddenStrings = new String[]{value};
        } else {
            this.forbiddenStrings = values;
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // 允许 null 或空字符串（可结合 @NotBlank 控制）
        }

        for (String forbidden : forbiddenStrings) {
            if (value.contains(forbidden)) {
                // 手动设置错误信息，支持 ${value}
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                "不能包含: " + forbidden)
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
