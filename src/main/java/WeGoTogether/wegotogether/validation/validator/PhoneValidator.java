package WeGoTogether.wegotogether.validation.validator;

import WeGoTogether.wegotogether.validation.annotation.Phone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    //@Override
    //public void initialize(Phone phone) {
    //    ConstraintValidator.super.initialize(constraintAnnotation);
    //}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        String regex = "^01(?:0|1|[6-9])-(\\d{3}|\\d{4})-\\d{4}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
