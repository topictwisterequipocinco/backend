package com.quark.equipocinco.topictwisterbackend.repository;

import com.quark.equipocinco.topictwisterbackend.dto.request.LoginDTO;
import com.quark.equipocinco.topictwisterbackend.model.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PlayerDAO {

    void update(Player newPlayer);

    Player getPlayerByID(String id);

    void create(Player player);

    List<Player> getAllEntities() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    Player getPlayer(LoginDTO loginDTO) throws ExecutionException, InterruptedException;
}
