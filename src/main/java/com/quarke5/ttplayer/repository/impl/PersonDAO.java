package com.quarke5.ttplayer.repository.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.quarke5.ttplayer.model.Applicant;
import com.quarke5.ttplayer.model.Person;
import com.quarke5.ttplayer.model.Publisher;
import com.quarke5.ttplayer.model.User;
import com.quarke5.ttplayer.repository.DAOS;
import com.quarke5.ttplayer.util.Errors;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class PersonDAO implements DAOS<Person> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonDAO.class);
    private static final int FIRST = 0;

    @Autowired
    Firestore firestore;
    @Autowired
    Errors errors;
    private Firestore db;

    public PersonDAO(Firestore db) {
        this.db = db;
        this.db = FirestoreClient.getFirestore();
    }

    @Override
    public void update(Person person) {
        try {
            ApiFuture<WriteResult> collectionsApiFuture = db.collection(getCollectionDataBaseFirebase().getId())
                    .document(String.valueOf(person.getId())).set(person);
            String result = collectionsApiFuture.get().getUpdateTime().toString();
        }catch (ExecutionException | InterruptedException e){
            LOGGER.info(e.getMessage());
            errors.logError(e.getMessage());
        }
    }

    @Override
    public Person findById(String id) {
        try {
            DocumentReference documentReference = db.collection(getCollectionDataBaseFirebase().getId()).document(id);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();
            if(document.exists()) return document.toObject(Person.class);
        }catch (ExecutionException | InterruptedException e){
            LOGGER.info(e.getMessage());
        }
        return null;
    }

    @Override
    public WriteResult create(Person person) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> collectionsApiFuture = db.collection(getCollectionDataBaseFirebase().getId())
                .document(String.valueOf(person.getId())).set(person);
        if(collectionsApiFuture.isDone()) return collectionsApiFuture.get();
        return null;
    }

    @Override
    public List<Person> getAllEntities() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<Person> list = new ArrayList<>();
        ApiFuture<QuerySnapshot> query = getCollectionDataBaseFirebase().get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        for (QueryDocumentSnapshot doc : documents) {
            Person object = doc.toObject(Person.class);
            PropertyUtils.setProperty(object, "id", doc.getId());
            list.add(object);
        }
        return list;
    }

    @Override
    public Person getEntity(String identification) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = getCollectionDataBaseFirebase().whereEqualTo("identification", identification).get();
        QueryDocumentSnapshot document = future.get().getDocuments().get(FIRST);
        return document.toObject(Person.class);
    }

    public Person findByUser(User user) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = getCollectionDataBaseFirebase().whereEqualTo("user", user).get();
        QueryDocumentSnapshot document = future.get().getDocuments().get(FIRST);
        return document.toObject(Person.class);
    }

    public Person findByUsername(String username) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = getCollectionDataBaseFirebase().whereEqualTo("username", username).get();
        QueryDocumentSnapshot document = future.get().getDocuments().get(FIRST);
        return document.toObject(Person.class);
    }

    private CollectionReference getCollectionDataBaseFirebase() {
        return firestore.collection("person");
    }

}
