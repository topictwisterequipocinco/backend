package com.quarke5.ttplayer.service.interfaces;

public interface Urls {
    String baseLocalhost = "http://localhost:8082";
    String baseHeroku = "https://bolsadetrabajo.herokuapp.com/";
    String URL_PERSON = baseHeroku + "person/";
    String URL_APPLICANT = baseHeroku + "applicant/";
    String URL_PUBLISHER = baseHeroku + "publisher/";

    //String URL_PERSON_CREATE_AND_UPDATE = "http://localhost:8082/person/";
    //String URL_APPLICANT_CREATE_AND_UPDATE = "http://localhost:8082/applicant/";
    //String URL_PUBLISHER_CREATE_AND_UPDATE = "http://localhost:8082/publisher/";
}
