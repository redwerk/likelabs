package com.redwerk.likelabs.web.ui.validator;

import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

public class PhoneValidator implements Validator<String> {
	
	private Pattern pattern;
	
	public PhoneValidator() {
		
	}
	
	@Override
	public boolean isValid(String param) {
            return StringUtils.isNumeric(param) && StringUtils.isNotEmpty(param);
	}
}

