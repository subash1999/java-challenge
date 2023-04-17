package jp.co.axa.apidemo.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import jp.co.axa.apidemo.validator.AtLeastOneNotNullValidator;;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOneNotNullValidator.class)
public @interface AtLeastOneNotNull {
    String message() default "At least one of the fields must be not null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


