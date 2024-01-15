package WeGoTogether.wegotogether.validation.validator;

import WeGoTogether.wegotogether.validation.annotation.ExistPhone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class PhoneValidator implements ConstraintValidator<ExistPhone, String> {
    @Override
    public void initialize(ExistPhone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 전화번호의 유효성을 여기에서 검증합니다.
        if (value == null) {
            return false; // null은 유효하지 않음
        }

        // 전화번호는 "01*-****-****" 형식이어야 함
        return value.matches("01[016789]-\\d{4}-\\d{4}");
    }
}
