package com.quark.equipocinco.topictwisterbackend.util.logger;

import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class Errors {
	private static final String LOG_PATH = "ErrorLog.txt";

    private  BufferedWriter bufferedWriter;

    public Errors() {
        openLog();
    }

    private void openLog(){
        File log = new File(LOG_PATH);
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(log));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logError(String message){
        try{
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
