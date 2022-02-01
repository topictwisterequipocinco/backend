package com.quark.equipocinco.topictwisterbackend.service.common;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class GenericServiceImpl<I, O, T> implements GenericService<I, O, T> {

    private static final Logger logger = LoggerFactory.getLogger(GenericServiceImpl.class);

    @Autowired Firestore firestore;
    @Autowired MessageSource messageSource;
    private Firestore db;
    public Class<O> clazz;
    public Class<T> clazzT;

    @SuppressWarnings("unchecked")
    public GenericServiceImpl(Firestore db) {
        this.db = db;
        this.db = FirestoreClient.getFirestore();
        this.clazz = ((Class<O>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
    }

    @Override
    public ResponseEntity<?> get(String data) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = db.collection(getCollectionDataBaseFirebase().getId()).document(data);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        O dto = null;

        try {
            if(document.exists()) {
                dto = document.toObject(clazz);
            }
            return ResponseEntity.status(HttpStatus.OK).body(dto + messageSource.getMessage("get.entity.success", null,null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("get.entity.failed", null,null));
        }
    }

    @Override
    public ResponseEntity<?> delete(String data) {
        try{
            ApiFuture<WriteResult> writeResult = db.collection(getCollectionDataBaseFirebase().getId()).document(data).delete();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(messageSource.getMessage("delete.entity.success", null,null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(messageSource.getMessage("delete.entity.failed", null,null));
        }
    }

    @Override
    public ResponseEntity<?> getAll() {
        List<O> list = new ArrayList<>();
        try{
            ApiFuture<QuerySnapshot> query = getCollectionDataBaseFirebase().get();
            List<QueryDocumentSnapshot> documents = query.get().getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                O object = doc.toObject(clazz);
                PropertyUtils.setProperty(object, "id", doc.getId());
                list.add(object);
            }
            return ResponseEntity.status(HttpStatus.OK).body(list);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    public O getNewEntityPersisted(String username) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = db.collection(getCollectionDataBaseFirebase().getId()).document(username);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        return document.exists() ? document.toObject(clazz) : null;
    }

}
