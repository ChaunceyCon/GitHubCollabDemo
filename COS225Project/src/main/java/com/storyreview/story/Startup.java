package com.storyreview.story;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.storyreview.*;
import com.storyreview.mlp.TFIDF;

public class Startup {
    

    public static void main(String[] args) {
        //create the TFIDF object to effeciently get the necessary MLP data during processing
        TFIDF storyProcessor = new TFIDF();
        //fill final.txt by processing raw.txt with Preprocesser
        Preprocessor.keepLongest("src/resources/raw.txt","src/resources/short.txt");
        Preprocessor.processFile("src/resources/short.txt","src/resources/final.txt",storyProcessor);
        
        try {
            // Get connection string from file and create DB Handler
            Scanner csScanner = new Scanner(new File("src/resources/conString.txt"));
            String conString = csScanner.nextLine();
            DBHandler dbHandler = new DBHandler(conString,"stories");
            csScanner.close();

            // Upload data from final.txt
            dbHandler.uploadFromFile("src/resources/final.txt");
            
            // Close the connection
            dbHandler.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("Error finding the connection string file!");
        }
    }
}
