package com.quark.equipocinco.topictwisterbackend.util.logger;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class GenerateHash {

    public long generateAleatorio(){
        Random random = new Random();
        return Math.round(random.nextFloat() * Math.pow(10,12));
    }

}
