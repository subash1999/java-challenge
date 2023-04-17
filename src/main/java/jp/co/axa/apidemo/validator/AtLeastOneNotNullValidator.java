package jp.co.axa.apidemo.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import jp.co.axa.apidemo.annotations.AtLeastOneNotNull;

public class AtLeastOneNotNullValidator implements ConstraintValidator<AtLeastOneNotNull, Object> {

	private String message;

    @Override
    public void initialize(AtLeastOneNotNull constraintAnnotation) {
        message = constraintAnnotation.message();
    }
	 /**
     * {@inheritDoc}
     * <p>
     * Checks if one of the not null value is provided in the DTO
     * </p>
     * 
     */
    @Override   
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        boolean isAtLeastOneNotNull = false;
        for (java.lang.reflect.Field field : value.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.get(value) != null) {
                    isAtLeastOneNotNull = true;
                    break;
                }
            } catch (IllegalAccessException e) {
                // ignore exception
            }
        }

        if (!isAtLeastOneNotNull) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(this.message)
                    .addConstraintViolation();
        }

        return isAtLeastOneNotNull;
    }
}

