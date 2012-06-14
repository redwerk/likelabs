package com.redwerk.likelabs.web.ui.utils;

import java.beans.PropertyEditorSupport;

/**
 *
 */
public class EnumEditor extends PropertyEditorSupport {

    private Class clazz;

    /**
     *
     * @param clazz
     */
    public EnumEditor(Class clazz) {
        this.clazz = clazz;
    }

    /**
     *
     * @return
     */
    @Override
    public String getAsText() {
        return super.getAsText();
    }

    /**
     *
     * @param text
     * @throws IllegalArgumentException
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {

        setValue(Enum.valueOf(clazz, text));
    }
}
