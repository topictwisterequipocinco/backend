package com.quarke5.ttplayer.validator;

import com.quarke5.ttplayer.dto.request.LoginNicknameDTO;
import com.quarke5.ttplayer.exception.PlayerException;
import com.quarke5.ttplayer.model.Player;

public abstract class AbstractValidator extends Exception {
    protected static final String REGEX_NAMES = "^([A-Za-zñÑ0-9])+g+$";
    protected static final String REGEX_TEXT = "^[a-zA-ZñÑ]$";
    protected static final String REGEX_DATE = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
    protected static final String REGEX_EMAIL = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    public boolean validPlayer(Player player) throws PlayerException { return false; }

    public abstract boolean validateLoginNickname(Player responseDTO, LoginNicknameDTO loginNicknameDTO);
}
