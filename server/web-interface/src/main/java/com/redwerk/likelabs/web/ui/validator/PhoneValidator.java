package com.redwerk.likelabs.web.ui.validator;

import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

public class PhoneValidator implements Validator<String> {
	
	private Pattern pattern;
        private static final byte PHONE_PLUS_INDEX = 1;
	
	public PhoneValidator() {
		
	}
	
	@Override
	public boolean isValid(String param) {

            if (StringUtils.isBlank(param))
                return false;
            if (!param.startsWith("+"))
                return false;
            if (!StringUtils.isNumeric(param.substring(PHONE_PLUS_INDEX)))
                return false;
            return true;
	}
}

