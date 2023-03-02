package com.quarke5.ttplayer.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface Creators<T> {
    ResponseEntity<?> create(@RequestBody @Valid T entity) throws Exception;
}
