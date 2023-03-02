package com.quarke5.ttplayer.repository.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.quarke5.ttplayer.model.Applicant;
import com.quarke5.ttplayer.model.JobOffer;
import com.quarke5.ttplayer.model.User;
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

public class JobOfferDAO implements DAOS<JobOffer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobOfferDAO.class);
    private static final int FIRST = 0;

    @Autowired
    Firestore firestore;
    @Autowired
    Errors errors;
    private Firestore db;

    public JobOfferDAO(Firestore db) {
        this.db = db;
        this.db = FirestoreClient.getFirestore();
    }

    @Override
    public void update(JobOffer jobOffer) {
        try {
            ApiFuture<WriteResult> collectionsApiFuture = db.collection(getCollectionDataBaseFirebase().getId())
                    .document(String.valueOf(jobOffer.getId())).set(jobOffer);
            String result = collectionsApiFuture.get().getUpdateTime().toString();
        }catch (ExecutionException | InterruptedException e){
            LOGGER.info(e.getMessage());
            errors.logError(e.getMessage());
        }
    }

    @Override
    public JobOffer findById(String id) {
        try {
            DocumentReference documentReference = db.collection(getCollectionDataBaseFirebase().getId()).document(id);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();
            if(document.exists()) return document.toObject(JobOffer.class);
        }catch (ExecutionException | InterruptedException e){
            LOGGER.info(e.getMessage());
        }
        return null;
    }

    @Override
    public WriteResult create(JobOffer jobOffer) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> collectionsApiFuture = db.collection(getCollectionDataBaseFirebase().getId())
                .document(String.valueOf(jobOffer.getId())).set(jobOffer);
        if(collectionsApiFuture.isDone()) return collectionsApiFuture.get();
        return null;
    }

    @Override
    public List<JobOffer> getAllEntities() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<JobOffer> list = new ArrayList<>();
        ApiFuture<QuerySnapshot> query = getCollectionDataBaseFirebase().get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        for (QueryDocumentSnapshot doc : documents) {
            JobOffer object = doc.toObject(JobOffer.class);
            PropertyUtils.setProperty(object, "id", doc.getId());
            list.add(object);
        }
        return list;
    }

    @Override
    public JobOffer getEntity(String title) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = getCollectionDataBaseFirebase().whereEqualTo("title", title).get();
        QueryDocumentSnapshot document = future.get().getDocuments().get(FIRST);
        return document.toObject(JobOffer.class);
    }

    private CollectionReference getCollectionDataBaseFirebase() {
        return firestore.collection("joboffer");
    }

    public List<JobOffer> findAllByState(String state){
        return null;
    }
}
