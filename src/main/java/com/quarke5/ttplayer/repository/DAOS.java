package com.quarke5.ttplayer.repository;

import com.google.cloud.firestore.WriteResult;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface DAOS<T> {
    void update(T entity);

    T findById(String id);

    WriteResult create(T entity) throws ExecutionException, InterruptedException;

    List<T> getAllEntities() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    T getEntity(String username) throws ExecutionException, InterruptedException;

}
