package com.storyreview.database;

import java.io.*;
import java.util.*;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DBHandler {

    MongoClient client;
    MongoDatabase database;
    MongoCollection<Document> storiesCollection;

    public DBHandler(String conString,String colName) {
        client = MongoClients.create(conString);
        database = client.getDatabase("project225");
        storiesCollection = database.getCollection(colName);
    }

    public void addStory(String line) {
        Scanner parser = new Scanner(line);
        parser.useDelimiter(";;");
        try {
            String title = parser.next();
            String story = parser.next();
            String labels = parser.next();
            Document targetStory = new Document();
            targetStory.append("title", title).append("story", story).append("labels", labels);
            storiesCollection.insertOne(targetStory);
        } finally {
            parser.close();
        }
    }

    public void uploadFromFile(String filePath) {
        System.out.println("Reading from file: " + filePath);  // Debug print to verify file path
        try {
            Scanner lineScanner = new Scanner(new File(filePath));
            String line;
            while (lineScanner.hasNextLine()) {
                // Add each line as a story to the database
                line=lineScanner.nextLine();
                addStory(line);
            }
            lineScanner.close();
            System.out.println("Successfully uploaded data from " + filePath);
        } catch (IOException e) {
            System.err.println("Error reading from file: " + filePath);
            e.printStackTrace();
        }
    }
    

    public void close() {
        if (client != null) {
            client.close();
        }
    }
}

