package com.quarke5.ttplayer.repository.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.quarke5.ttplayer.dto.request.PersonDTO;
import com.quarke5.ttplayer.model.Applicant;
import com.quarke5.ttplayer.model.Player;
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

public class ApplicantDAO implements DAOS<Applicant> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicantDAO.class);
    private static final int FIRST = 0;

    @Autowired
    Firestore firestore;
    @Autowired
    Errors errors;
    private Firestore db;

    public ApplicantDAO(Firestore db) {
        this.db = db;
        this.db = FirestoreClient.getFirestore();
    }

    @Override
    public void update(Applicant applicant) {
        try {
            ApiFuture<WriteResult> collectionsApiFuture = db.collection(getCollectionDataBaseFirebase().getId())
                    .document(String.valueOf(applicant.getId())).set(applicant);
            String result = collectionsApiFuture.get().getUpdateTime().toString();
        }catch (ExecutionException | InterruptedException e){
            LOGGER.info(e.getMessage());
            errors.logError(e.getMessage());
        }
    }

    @Override
    public Applicant findById(String id) {
        try {
            DocumentReference documentReference = db.collection(getCollectionDataBaseFirebase().getId()).document(id);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();
            if(document.exists()) return document.toObject(Applicant.class);
        }catch (ExecutionException | InterruptedException e){
            LOGGER.info(e.getMessage());
        }
        return null;
    }

    @Override
    public WriteResult create(Applicant applicant) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> collectionsApiFuture = db.collection(getCollectionDataBaseFirebase().getId())
                .document(String.valueOf(applicant.getId())).set(applicant);
        if(collectionsApiFuture.isDone()) return collectionsApiFuture.get();
        return null;
    }

    @Override
    public List<Applicant> getAllEntities() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<Applicant> list = new ArrayList<>();
        ApiFuture<QuerySnapshot> query = getCollectionDataBaseFirebase().get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        for (QueryDocumentSnapshot doc : documents) {
            Applicant object = doc.toObject(Applicant.class);
            PropertyUtils.setProperty(object, "id", doc.getId());
            list.add(object);
        }
        return list;
    }

    @Override
    public Applicant getEntity(String identification) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = getCollectionDataBaseFirebase().whereEqualTo("identification", identification).get();
        QueryDocumentSnapshot document = future.get().getDocuments().get(FIRST);
        return document.toObject(Applicant.class);
    }

    public Applicant findByUser(User user){
        return null;
    }

    private CollectionReference getCollectionDataBaseFirebase() {
        return firestore.collection("applicant");
    }
}
