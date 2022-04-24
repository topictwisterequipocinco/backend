package com.quarke5.ttplayer.util.error;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class Errors {
    private static final String LOG_PATH = "ErrorLog.txt";

    private BufferedWriter bufferedWriter;

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
