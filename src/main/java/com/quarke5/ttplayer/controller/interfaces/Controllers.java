package com.quarke5.ttplayer.controller.interfaces;

import com.quarke5.ttplayer.exception.PersonException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

public interface Controllers<T> {
    ResponseEntity<?> get(@PathVariable Long id);
    ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid T entity) throws PersonException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;
    ResponseEntity<?> delete(@PathVariable Long id);
    ResponseEntity<?> getAll() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;
}
