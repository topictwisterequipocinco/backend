package com.quarke5.ttplayer.repository;

import com.google.cloud.firestore.WriteResult;
import com.quarke5.ttplayer.model.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PlayerDAO {

    void update(Player newPlayer);

    Player getPlayerByID(String id);

    WriteResult create(Player player) throws ExecutionException, InterruptedException;

    List<Player> getAllEntities() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    Player getPlayer(String username) throws ExecutionException, InterruptedException;
}
