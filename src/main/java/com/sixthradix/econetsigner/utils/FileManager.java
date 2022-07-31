package com.sixthradix.econetsigner.utils;


import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class FileManager {
    private final static Logger logger = Logger.getLogger(FileManager.class.getName());


    public synchronized void writeToTextFile(List<String> data, String filePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for(String line: data){
            bw.write(line);
            bw.newLine();
        }
        bw.close();
    }

    public List<String> readTextFile(String filepath, boolean delete) throws FileNotFoundException{
        return readTextFile(new File(filepath), delete);
    }

    public List<String> readTextFile(File file, boolean delete) throws FileNotFoundException {
        /*Reads file Line by Line and return List of String and deletes the file after*/
        FileReader fileReader = new FileReader(file);
        BufferedReader bufReader = new BufferedReader(fileReader);
        List<String> data = new ArrayList<>();
        while (true) {
            try {
                String line = null;
                if ((line = bufReader.readLine()) != null) {
                    if (!line.isEmpty()) {
                            data.add(line);
                    }
                } else {
                    break;
                }
            } catch (Exception e) {
                logger.severe(e.getMessage());
            }
        }
        //close readers and delete input file(consume)
        try {
            bufReader.close();
            fileReader.close();

            if(delete && !Files.deleteIfExists(file.toPath())){
                logger.severe("Could not delete file: ".concat(file.getAbsolutePath()));
            }
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
        return data;
    }

    public String readText(String filepath) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public JSONObject readJSONFile(String filepath) throws FileNotFoundException {
        return readJSONFile(new File(filepath));
    }

    public JSONObject readJSONFile(File file) throws FileNotFoundException {
        return new JSONObject(new FileReader(file));
    }
}
