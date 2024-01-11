package WeGoTogether.wegotogether.validation.annotation;

import WeGoTogether.wegotogether.validation.validator.PhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
@Documented
public @interface Phone {

    String message() default "***-****-**** 형식으로 작성해주세요.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
