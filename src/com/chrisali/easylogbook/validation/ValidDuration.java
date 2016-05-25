package com.chrisali.easylogbook.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = com.chrisali.easylogbook.validation.ValidDurationImplementation.class)
public @interface ValidDuration {

	String message() default "Please enter a duration from 0.0 to 24.0 hours";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	float min() default 0.0f;
	
	float max() default 24.0f;
}
