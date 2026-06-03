package com.example.stoneocean.Util;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("NotContainsValidator 自定义验证器测试")
class NotContainsValidatorTest {

    private NotContainsValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new NotContainsValidator();
        // RETURNS_DEEP_STUBS 自动处理嵌套方法调用链
        // 如 context.buildConstraintViolationWithTemplate(...).addConstraintViolation()
        context = mock(ConstraintValidatorContext.class, Answers.RETURNS_DEEP_STUBS);
    }

    @Nested
    @DisplayName("initialize() 初始化测试")
    class InitializeTests {

        @Test
        @DisplayName("仅使用 value 属性时，设置单个禁止字符串")
        void initializeWithSingleValue() {
            NotContains annotation = createNotContainsAnnotation("bad", new String[]{});
            validator.initialize(annotation);

            assertFalse(validator.isValid("this is bad", context));
        }

        @Test
        @DisplayName("使用 values 数组时，设置多个禁止字符串")
        void initializeWithValuesArray() {
            NotContains annotation = createNotContainsAnnotation("", new String[]{"-", "|", "~"});
            validator.initialize(annotation);

            assertFalse(validator.isValid("hello-world", context));
            assertFalse(validator.isValid("hello|world", context));
            assertFalse(validator.isValid("hello~world", context));
        }

        @Test
        @DisplayName("values 为空数组时，使用 value 作为禁止字符串")
        void initializeWithEmptyValues() {
            NotContains annotation = createNotContainsAnnotation("forbidden", new String[]{});
            validator.initialize(annotation);

            assertFalse(validator.isValid("this is forbidden text", context));
        }
    }

    @Nested
    @DisplayName("isValid() 验证测试")
    class IsValidTests {

        @BeforeEach
        void initWithDashAndPipe() {
            NotContains annotation = createNotContainsAnnotation("", new String[]{"-", "|"});
            validator.initialize(annotation);
        }

        @Test
        @DisplayName("null 值返回 true（允许 null）")
        void isValidNull() {
            assertTrue(validator.isValid(null, context));
        }

        @Test
        @DisplayName("空字符串返回 true（允许空字符串）")
        void isValidEmpty() {
            assertTrue(validator.isValid("", context));
        }

        @Test
        @DisplayName("不含禁止字符串时返回 true")
        void isValidCleanString() {
            assertTrue(validator.isValid("cleanString", context));
            assertTrue(validator.isValid("hello world", context));
            assertTrue(validator.isValid("正常文本", context));
        }

        @Test
        @DisplayName("包含单个禁止字符 '-' 时返回 false")
        void isValidContainsDash() {
            assertFalse(validator.isValid("hello-world", context));
        }

        @Test
        @DisplayName("包含单个禁止字符 '|' 时返回 false")
        void isValidContainsPipe() {
            assertFalse(validator.isValid("hello|world", context));
        }

        @Test
        @DisplayName("同时包含多个禁止字符时返回 false")
        void isValidContainsMultiple() {
            assertFalse(validator.isValid("hello-|world", context));
        }

        @Test
        @DisplayName("禁止字符出现在开头时返回 false")
        void isValidForbiddenAtStart() {
            assertFalse(validator.isValid("-start", context));
        }

        @Test
        @DisplayName("禁止字符出现在结尾时返回 false")
        void isValidForbiddenAtEnd() {
            assertFalse(validator.isValid("end-", context));
        }
    }

    @Nested
    @DisplayName("User 实体注解场景模拟")
    class UserAnnotationScenario {

        @BeforeEach
        void initWithUserForbiddenChars() {
            NotContains annotation = createNotContainsAnnotation("", new String[]{"-", "|", "~"});
            validator.initialize(annotation);
        }

        @Test
        @DisplayName("合法账号名返回 true")
        void validAccountName() {
            assertTrue(validator.isValid("normalAccount", context));
            assertTrue(validator.isValid("account_123", context));
            assertTrue(validator.isValid("用户名", context));
        }

        @Test
        @DisplayName("账号包含 '-' 返回 false")
        void accountWithDash() {
            assertFalse(validator.isValid("bad-account", context));
        }

        @Test
        @DisplayName("账号包含 '|' 返回 false")
        void accountWithPipe() {
            assertFalse(validator.isValid("bad|account", context));
        }

        @Test
        @DisplayName("账号包含 '~' 返回 false")
        void accountWithTilde() {
            assertFalse(validator.isValid("bad~account", context));
        }
    }

    /**
     * 创建 NotContains 注解的模拟实例
     */
    private NotContains createNotContainsAnnotation(String value, String[] values) {
        return new NotContains() {
            @Override
            public String message() {
                return "不能包含非法字符: ${value}";
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class<? extends Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public String value() {
                return value;
            }

            @Override
            public String[] values() {
                return values;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return NotContains.class;
            }
        };
    }
}