package com.quarke5.ttplayer.service.crud;

import com.quarke5.ttplayer.exception.PersonException;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

public interface Writeable<T> {
    int ZERO = 0;

    ResponseEntity<?> update(Long id, T entity) throws PersonException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;
}
