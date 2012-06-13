package com.redwerk.likelabs.web.ui.utils;

import java.beans.PropertyEditorSupport;

public class EnumEditor extends PropertyEditorSupport {

        private Class clazz;

        public EnumEditor(Class clazz) {
            this.clazz = clazz;
        }

        @Override
        public String getAsText() {
            return super.getAsText();
        }

        @Override
        public void setAsText(String text) throws IllegalArgumentException {

            setValue(Enum.valueOf(clazz, text));
        }
}
