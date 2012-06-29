package com.redwerk.likelabs.web.ui.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.ui.ModelMap;

/**
 *
 */
public class JsonResponseBuilder {

    private static final String SUCCESS_KEY = "success";
    private static final String DATA_KEY = "data";
    private static final String MESSAGES_KEY = "messages";
    private static final String ERROR_MESSAGE_KEY = "error";
    private static final String DEFAULT_ERROR_MESSAGE = "Internal server error";

    private boolean success = true;
    private Object data;
    private List<String> messages = new ArrayList<String>();
    private ModelMap response = new ModelMap();
    

    public void setNotSuccess() {
        setNotSuccess(DEFAULT_ERROR_MESSAGE);
    }

    public void setNotSuccess(String errorMessage) {
        this.success = false;
        setErrorMessage(errorMessage);
    }

    public void setData(List data) {
        this.data = data;
    }

    public void setData(Map<? extends String,? extends Object> data) {
        this.data = data;
    }

    public void setErrorMessage(Object error) {
        if (error instanceof String) {
            this.response.put(ERROR_MESSAGE_KEY, error);
            return;
        }
        if (error instanceof Exception) {
            this.response.put(ERROR_MESSAGE_KEY, ((Exception)error).getMessage());
            return;
        }
        this.response.put(ERROR_MESSAGE_KEY, error.toString());
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public void addMessages(List<String> messages) {
        this.messages.addAll(messages);
    }

    public boolean addCustomFieldData(String key, Object customData) {
        if (SUCCESS_KEY.equals(key) || DATA_KEY.equals(key) || MESSAGES_KEY.equals(key) || ERROR_MESSAGE_KEY.equals(key)) {
            return false;
        }
        this.response.put(key, customData);
        return true;
    }

    public ModelMap getModelResponse() {
        this.response.put(DATA_KEY, (data != null ) ? data : "");
        this.response.put(MESSAGES_KEY, this.messages);
        this.response.put(SUCCESS_KEY, this.success);
        return response;
    }
}
