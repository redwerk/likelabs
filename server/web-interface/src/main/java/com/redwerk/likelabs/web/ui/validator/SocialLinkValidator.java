package com.redwerk.likelabs.web.ui.validator;

import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;


public class SocialLinkValidator implements Validator<String>{

    private static final Pattern PATTERN_EMAIL = Pattern.compile("(?:http://vk.com|^((?:(?:http|ftp)s?://)(?:.*)?\\.facebook.com)).");

    @Override
    public boolean isValid(String param) {
        if (StringUtils.isBlank(param))
            return false;
        if (!PATTERN_EMAIL.matcher(param).find())
            return false;
        return  true;
    }

}
