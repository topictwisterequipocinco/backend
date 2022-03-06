package com.quark.equipocinco.topictwisterbackend.validator;

import com.quark.equipocinco.topictwisterbackend.model.Player;

public abstract class AbstractValidator extends Exception {
    protected static final String REGEX_NAMES = "^([A-Za-zñÑ0-9])+g+$";
    protected static final String REGEX_TEXT = "^[a-zA-ZñÑ]$";
    protected static final String REGEX_DATE = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
    protected static final String REGEX_EMAIL = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    public abstract boolean validPlayer(Player player) throws Exception;

}
