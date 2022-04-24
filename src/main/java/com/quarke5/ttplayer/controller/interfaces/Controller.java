package com.quarke5.ttplayer.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

public interface Controller<T> {

    ResponseEntity<?> get(@PathVariable("id") String id);

    ResponseEntity<?> update(@PathVariable("data") String data);

    ResponseEntity<?> create(@RequestBody @Valid T entity) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    ResponseEntity<?> getAll() throws Exception;

}
