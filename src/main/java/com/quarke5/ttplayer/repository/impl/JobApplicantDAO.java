package com.quarke5.ttplayer.repository.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.quarke5.ttplayer.model.Applicant;
import com.quarke5.ttplayer.model.JobApplication;
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

public class JobApplicantDAO implements DAOS<JobApplication> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobApplicantDAO.class);
    private static final int FIRST = 0;

    @Autowired
    Firestore firestore;
    @Autowired
    Errors errors;
    private Firestore db;

    public JobApplicantDAO(Firestore db) {
        this.db = db;
        this.db = FirestoreClient.getFirestore();
    }

    @Override
    public void update(JobApplication jobApplication) {
        try {
            ApiFuture<WriteResult> collectionsApiFuture = db.collection(getCollectionDataBaseFirebase().getId())
                    .document(String.valueOf(jobApplication.getId())).set(jobApplication);
            String result = collectionsApiFuture.get().getUpdateTime().toString();
        }catch (ExecutionException | InterruptedException e){
            LOGGER.info(e.getMessage());
            errors.logError(e.getMessage());
        }
    }

    @Override
    public JobApplication findById(String id) {
        try {
            DocumentReference documentReference = db.collection(getCollectionDataBaseFirebase().getId()).document(id);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();
            if(document.exists()) return document.toObject(JobApplication.class);
        }catch (ExecutionException | InterruptedException e){
            LOGGER.info(e.getMessage());
        }
        return null;
    }

    @Override
    public WriteResult create(JobApplication jobApplication) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> collectionsApiFuture = db.collection(getCollectionDataBaseFirebase().getId())
                .document(String.valueOf(jobApplication.getId())).set(jobApplication);
        if(collectionsApiFuture.isDone()) return collectionsApiFuture.get();
        return null;
    }

    @Override
    public List<JobApplication> getAllEntities() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<JobApplication> list = new ArrayList<>();
        ApiFuture<QuerySnapshot> query = getCollectionDataBaseFirebase().get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        for (QueryDocumentSnapshot doc : documents) {
            JobApplication object = doc.toObject(JobApplication.class);
            PropertyUtils.setProperty(object, "id", doc.getId());
            list.add(object);
        }
        return list;
    }

    @Override
    public JobApplication getEntity(String identification) throws ExecutionException, InterruptedException {
        return null;
    }

    private CollectionReference getCollectionDataBaseFirebase() {
        return firestore.collection("jobapplicant");
    }

}
