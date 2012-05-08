package com.redwerk.likelabs.web.ui.validator;

public interface Validator<T> {

    boolean isValid(T value);

}
