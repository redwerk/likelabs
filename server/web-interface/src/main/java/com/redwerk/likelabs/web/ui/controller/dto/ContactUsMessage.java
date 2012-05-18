package com.redwerk.likelabs.web.ui.controller.dto;

public class ContactUsMessage {

        private String name;
        private String email;
        private String summary;
        private String message;

        public String getEmail() {
            return email;
        }

        public String getMessage() {
            return message;
        }

        public String getName() {
            return name;
        }

        public String getSummary() {
            return summary;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }
    }
