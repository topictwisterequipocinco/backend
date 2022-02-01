package com.quark.equipocinco.topictwisterbackend.service.common;

import com.google.cloud.firestore.CollectionReference;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

public interface GenericService<I, O, T> {

    ResponseEntity<?> get(String data) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    ResponseEntity<?> update(String data, I entity);

    ResponseEntity<?> delete(String data);

    ResponseEntity<?> create(I entity) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    ResponseEntity<?> getAll();

    boolean verifyIsExists(String element) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    CollectionReference getCollectionDataBaseFirebase();

}
