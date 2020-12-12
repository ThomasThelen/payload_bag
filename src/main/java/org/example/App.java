package org.example;


import org.dataone.speedbagit.SpeedBagIt;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipOutputStream;

public class App
{
    public static void main( String[] args ) throws IOException, NoSuchAlgorithmException, InterruptedException {
        // Create a new SpeedBag object that's using BagIt 1.0 and MD-5 as the checksum algorithm
        SpeedBagIt bag = new SpeedBagIt(1.0, "MD5");

        // Loop over the 'unbagged_data/' directory and add each file to the SpeedBagIt object
        File dataDirectory = new File("./unbagged_data/");
        File[] directoryListing = dataDirectory.listFiles();
        if (directoryListing != null) {
            for (File dataFile : directoryListing) {
                // Let SpeedBagIt know where the file should be written to in the bag, specifying the
                // path *with* the filename.
                String bagPath = "data/"+dataFile.getName();
                System.out.println(bagPath);
                bag.addFile(new FileInputStream(dataFile), bagPath, MessageDigest.getInstance("MD5"), false);
            }
        }

        // Specify where the BagIt fill will go on disk
        Path bagPath = Paths.get("./bagged_data.zip");
        Path bagFilePath = Files.createFile(bagPath);
        // Create a stream for SpeedBagIt to write to
        FileOutputStream fos = new FileOutputStream(bagFilePath.toString());
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(fos));
        // Stream the bag
        bag.stream(out);

    }
}
