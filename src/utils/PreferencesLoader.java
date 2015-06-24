package utils;

import actions.GetHash;
import main.Main;

import java.io.*;
import java.util.Properties;

/**
 * This class allows the program to store and retrieve previous user settings
 *
 * This file is part of Checksum Calculator.
 *
 * Copyright (C) 2015  droppinstackz
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
public class PreferencesLoader {

    public static Properties prop = new Properties();
    public static InputStream input = null;
    public static OutputStream output = null;

    // Saves to: (user home folder)/.checksumCalc/ccalc_v1.0/ccalc.properties

    private static String propertiesPath = System.getProperty("user.home") + File.separator + ".checksumCalc" +
            File.separator + "ccalc_v" + Main.applicationVersion;
    private static String propertiesFile = "ccalc.properties";

    /**
     * A method that checks that the properties folder and properties file are properly created. If they do not
     * exist, it attempts to create them.
     */
    public static void checkFolders() {

        File folderPath = new File(propertiesPath); // Try to find or make the folder path
        if(!folderPath.exists()) {
            try {
                folderPath.mkdirs();
            } catch (Exception e) {
                System.out.println("The properties file path could not be loaded or created");
            }
        }

        File file = new File(propertiesPath + File.separator + propertiesFile); // Try to find or make the
        if(!file.exists()) {                                                    // properties file
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("The properties file could not be loaded or created");
            }
        }

        try { // Load the input stream to read the data
            input = new FileInputStream(propertiesPath + File.separator + propertiesFile);
            prop.load(input);
        } catch (FileNotFoundException e) {
            System.out.println("The properties file could not be opened");
        } catch (IOException e) {
            System.out.println("Could not read the properties file");
        }
    }

    /**
     * Close the input stream
     */
    public static void closeInputStream() {
        if(input != null) {
            try {
                input.close();
            } catch (IOException e) {
                System.out.println("Could not close the input file stream");
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the user preferences
     *
     * @param algorithmType A string representing the algorithm type selection
     * @param inputType A string representing the input type selection
     * @param isChecked A boolean representing the state of the CompareTo checkbox
     */
    public static void savePreferences(String algorithmType, String inputType, boolean isChecked) {

        closeInputStream();

        try { // Load the output stream to write the data
            output = new FileOutputStream(propertiesPath + File.separator + propertiesFile);

            setAlgorithmType(algorithmType);
            setInputType(inputType);
            setCompareToChecked(isChecked);

            prop.store(output, null);

        } catch (FileNotFoundException e) {
            System.out.println("The properties file could not be opened");
        } catch (IOException e) {
            System.out.println("Could not write to the properties file");
        }

        if(output != null) { // Close the output stream
            try {
                output.close();
            } catch (IOException e) {
                System.out.println("Could not close the output file stream");
                e.printStackTrace();
            }
        }
    }


    // #### Getters #### //

    // Get the algorithm type
    public static String getAlgorithmType() {
        String value = prop.getProperty("algorithmType");
        if((value != null) && GetHash.algorithmIsValid(value)) {
            return value;
        } else {
            return "MD5";
        }
    }
    // Get the user-selected input type
    public static String getInputType() {
        String value = prop.getProperty("inputType");
        if((value != null) && (value.equals("Text") || value.equals("File"))) {
            return value;
        } else {
            return "File";
        }
    }
    // Get the CompareTo checkbox state
    public static boolean getCompareToChecked() {
        String value = prop.getProperty("compareToChecked");
        if(value != null) {
            return value.equals("true");
        } else {
            return false;
        }
    }


    // #### Setters #### //

    // Set the algorithm type
    private static void setAlgorithmType(String algorithmType) {
        if(GetHash.algorithmIsValid(algorithmType)) {
            prop.setProperty("algorithmType", algorithmType);
        } else {
            prop.setProperty("algorithmType", "MD5");
        }
    }
    // Set the user-selected input type
    private static void setInputType(String inputType) {
        if(inputType.equals("Text") || inputType.equals("File")) {
            prop.setProperty("inputType", inputType);
        } else {
            prop.setProperty("inputType", "File");
        }
    }
    // Set the CompareTo checkbox state
    private static void setCompareToChecked(boolean isChecked) {
        if(isChecked) {
            prop.setProperty("compareToChecked", "true");
        } else {
            prop.setProperty("compareToChecked", "false");
        }
    }
}
