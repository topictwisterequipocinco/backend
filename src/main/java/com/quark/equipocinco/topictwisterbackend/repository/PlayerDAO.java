package com.quark.equipocinco.topictwisterbackend.repository;

import com.quark.equipocinco.topictwisterbackend.model.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PlayerDAO {

    void update(Player newPlayer) throws ExecutionException, InterruptedException;

    Player getPlayerByID(String id) throws ExecutionException, InterruptedException;

    void create(Player player);

    List<Player> getAllEntities() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

}
