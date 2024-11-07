package edu.book.socialnetwork.enums;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("activation-account");

    private final String templateName;

    EmailTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
