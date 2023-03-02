package com.quarke5.ttplayer.util;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GenerateHash {

    public long generateAleatorio(){
        Random random = new Random();
        return Math.round(random.nextFloat() * Math.pow(10,12));
    }

}
