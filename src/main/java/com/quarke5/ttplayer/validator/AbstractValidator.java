package com.quarke5.ttplayer.validator;

import com.quarke5.ttplayer.dto.request.ForgotDTO;
import com.quarke5.ttplayer.dto.request.LoginNicknameDTO;
import com.quarke5.ttplayer.exception.*;
import com.quarke5.ttplayer.model.*;

public abstract class AbstractValidator extends Exception {
    protected static final String REGEX_NAMES = "^([A-Za-zñÑ0-9])+g+$";
    protected static final String REGEX_TEXT = "^[a-zA-ZñÑ]$";
    protected static final String REGEX_DATE = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
    protected static final String REGEX_EMAIL = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    public boolean validPlayer(Player player) throws PlayerException { return false; }

    public abstract boolean validateLoginNickname(Player responseDTO, LoginNicknameDTO loginNicknameDTO);

    public boolean validPerson(Person per) throws PersonException {return false;}
    public boolean validApplicant(Applicant applicant) throws PersonException {return false;}
    public boolean validPublisher(Publisher publisher) throws PersonException {return false;}
    public boolean validJobOffer(JobOffer jobOffer) throws JobOfferException {return false;}
    public boolean validCategory(Category category) throws CategoryException {return false;}
    public boolean isValidForgot(User user, ForgotDTO forgotDTO) throws UserException {return false;};
}
