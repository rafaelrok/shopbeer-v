package br.com.rafaelvieira.shopbeer.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

import br.com.rafaelvieira.shopbeer.validation.AttributeConfirmation;
import org.apache.commons.beanutils.BeanUtils;

public class AttributeConfirmationValidator implements ConstraintValidator<AttributeConfirmation, Object> {

	private String attribute;
	private String attributeConfirmation;
	
	@Override
	public void initialize(AttributeConfirmation constraintAnnotation) {
		this.attribute = constraintAnnotation.attribute();
		this.attributeConfirmation = constraintAnnotation.attributeConfirmation();
	}

	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {
		boolean valid = false;
		try {
			Object valueAttribute = BeanUtils.getProperty(object, this.attribute);
			Object valueAttributeConfirmation = BeanUtils.getProperty(object, this.attributeConfirmation);
			
			valid = bothAreNull(valueAttribute, valueAttributeConfirmation) || bothAreEqual(valueAttribute, valueAttributeConfirmation);
		} catch (Exception e) {
			throw new RuntimeException("Error retrieving attribute values", e);
		}
		
		if (!valid) {
			context.disableDefaultConstraintViolation();
			String message = context.getDefaultConstraintMessageTemplate();
			ConstraintViolationBuilder violationBuilder = context.buildConstraintViolationWithTemplate(message);
			violationBuilder.addPropertyNode(attributeConfirmation).addConstraintViolation();
		}
		
		return valid;
	}

	private boolean bothAreEqual(Object valorAtributo, Object valorAtributoConfirmacao) {
		return valorAtributo != null && valorAtributo.equals(valorAtributoConfirmacao);
	}

	private boolean bothAreNull(Object valorAtributo, Object valorAtributoConfirmacao) {
		return valorAtributo == null && valorAtributoConfirmacao == null;
	}

}
