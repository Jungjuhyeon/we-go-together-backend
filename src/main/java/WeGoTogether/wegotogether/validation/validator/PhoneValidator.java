package WeGoTogether.wegotogether.validation.validator;

import WeGoTogether.wegotogether.validation.annotation.ExistPhone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$");
        Matcher matcher = pattern.matcher(value);
        System.out.println(matcher.matches());
        return matcher.matches();
    }
}