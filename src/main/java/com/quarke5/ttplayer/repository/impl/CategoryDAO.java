package com.quarke5.ttplayer.repository.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.quarke5.ttplayer.model.Category;
import com.quarke5.ttplayer.repository.DAOS;
import com.quarke5.ttplayer.util.Errors;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CategoryDAO implements DAOS<Category> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryDAO.class);
    private static final int FIRST = 0;

    @Autowired
    Firestore firestore;
    @Autowired
    Errors errors;
    private Firestore db;

    public CategoryDAO(Firestore db) {
        this.db = db;
        this.db = FirestoreClient.getFirestore();
    }

    @Override
    public void update(Category category) {
        try {
            ApiFuture<WriteResult> collectionsApiFuture = db.collection(getCollectionDataBaseFirebase().getId())
                    .document(String.valueOf(category.getId())).set(category);
            String result = collectionsApiFuture.get().getUpdateTime().toString();
        }catch (ExecutionException | InterruptedException e){
            LOGGER.info(e.getMessage());
            errors.logError(e.getMessage());
        }
    }

    @Override
    public Category findById(String id) {
        try {
            DocumentReference documentReference = db.collection(getCollectionDataBaseFirebase().getId()).document(id);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();
            if(document.exists()) return document.toObject(Category.class);
        }catch (ExecutionException | InterruptedException e){
            LOGGER.info(e.getMessage());
        }
        return null;
    }

    @Override
    public WriteResult create(Category category) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> collectionsApiFuture = db.collection(getCollectionDataBaseFirebase().getId())
                .document(String.valueOf(category.getId())).set(category);
        if(collectionsApiFuture.isDone()) return collectionsApiFuture.get();
        return null;
    }

    @Override
    public List<Category> getAllEntities() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<Category> list = new ArrayList<>();
        ApiFuture<QuerySnapshot> query = getCollectionDataBaseFirebase().get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        for (QueryDocumentSnapshot doc : documents) {
            Category object = doc.toObject(Category.class);
            PropertyUtils.setProperty(object, "id", doc.getId());
            list.add(object);
        }
        return list;
    }

    @Override
    public Category getEntity(String name) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = getCollectionDataBaseFirebase().whereEqualTo("name", name).get();
        QueryDocumentSnapshot document = future.get().getDocuments().get(FIRST);
        return document.toObject(Category.class);
    }

    private CollectionReference getCollectionDataBaseFirebase() {
        return firestore.collection("category");
    }
}
