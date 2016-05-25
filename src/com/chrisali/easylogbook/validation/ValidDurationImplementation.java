package com.chrisali.easylogbook.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidDurationImplementation implements ConstraintValidator<ValidDuration, Float> {

	private float min;
	private float max;
	
	@Override
	public void initialize(ValidDuration constraintAnnotation) {
		min = constraintAnnotation.min();
		max = constraintAnnotation.max();
	}

	@Override
	public boolean isValid(Float duration, ConstraintValidatorContext context) {
		if (duration < min)
			return false;
		if (duration > max)
			return false;
		
		return true;
	}
}
