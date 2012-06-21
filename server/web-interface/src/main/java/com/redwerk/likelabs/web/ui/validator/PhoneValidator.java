package com.redwerk.likelabs.web.ui.validator;

import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

public class PhoneValidator implements Validator<String> {
	
	@Override
	public boolean isValid(String param) {
        return (StringUtils.isNotEmpty(param) && StringUtils.isNumeric(param));
	}

}

