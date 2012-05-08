package com.redwerk.likelabs.web.ui.validator;

import java.util.regex.Pattern;

public class EmailValidator implements Validator<String>{

        private static final Pattern PATTERN_EMAIL = Pattern.compile("^([!#-'*+\\-\\/-9=?A-Z^-~]+[.])*[!#-'*+\\-\\/-9=?A-Z^-~]+@(((?:(?:[\\da-zA-Z](?:[-\\da-zA-Z]{0,61}[\\da-zA-Z])?)\\.)+(?:[a-zA-Z](?:[-\\da-zA-Z]{0,6}[\\da-zA-Z])?)\\.?))$");

	public EmailValidator() {

	}

	@Override
	public boolean isValid(String email) {
       
            return PATTERN_EMAIL.matcher(email).find() ? true : false;
	}

}
