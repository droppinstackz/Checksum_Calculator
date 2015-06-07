package actions;

import gui.Controller;

import static org.apache.commons.codec.digest.DigestUtils.*;

import java.io.*;

/**
 * Generates a hash of a string or a file using the Apache Commons Codec.
 *
 * Copyright (C) 2015 droppinstackz
 *
 * This file is part of Checksum Calculator.
 *
 * Checksum Calculator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Checksum Calculator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Checksum Calculator.  If not, see <http://www.gnu.org/licenses/>.
 */

public class GetHash implements Runnable {

    private static String ALGORITHM_TYPE = "MD5";
    private static String INPUT_TYPE = "File";
    private static String HEX_RESULT = "";
    private static boolean ABORTED = false;

    private static String USER_TEXT_INPUT = "";
    private static InputStream USER_FILE_INPUT = null;

    private Controller mainFXMLController;


    @Override
    public void run() {
        try {
            if (INPUT_TYPE.equals("Text")) {

                try {
                    switch (ALGORITHM_TYPE) {
                        case "MD5":
                            HEX_RESULT = md5Hex(USER_TEXT_INPUT);

                            break;
                        case "SHA-1":
                            HEX_RESULT = sha1Hex(USER_TEXT_INPUT);

                            break;
                        case "SHA-256":
                            HEX_RESULT = sha256Hex(USER_TEXT_INPUT);

                            break;
                        case "SHA-384":
                            HEX_RESULT = sha384Hex(USER_TEXT_INPUT);

                            break;
                        case "SHA-512":
                            HEX_RESULT = sha512Hex(USER_TEXT_INPUT);

                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    mainFXMLController.returnChecksum(HEX_RESULT, 0); // return the result

                } catch (Exception e) {
                    mainFXMLController.returnChecksum("Could not read the input text", 1);
                    e.printStackTrace();
                }

            } else if (INPUT_TYPE.equals("File")) {
                try {
                    switch (ALGORITHM_TYPE) {
                        case "MD5":
                            HEX_RESULT = md5Hex(USER_FILE_INPUT);

                            break;
                        case "SHA-1":
                            HEX_RESULT = sha1Hex(USER_FILE_INPUT);

                            break;
                        case "SHA-256":
                            HEX_RESULT = sha256Hex(USER_FILE_INPUT);

                            break;
                        case "SHA-384":
                            HEX_RESULT = sha384Hex(USER_FILE_INPUT);

                            break;
                        case "SHA-512":
                            HEX_RESULT = sha512Hex(USER_FILE_INPUT);

                            break;
                        default:
                            throw new IllegalArgumentException();
                    }

                    // Close file
                    try {
                        if (USER_FILE_INPUT != null) {
                            USER_FILE_INPUT.close();
                        }
                        mainFXMLController.returnChecksum(HEX_RESULT, 0);
                    } catch (IOException e) {
                        mainFXMLController.returnChecksum("The file stream could not be closed", 1);
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    if (ABORTED){ // If the user voluntarily aborted the thread
                        mainFXMLController.returnChecksum("                      Calculation stopped", 2);
                    } else {
                        mainFXMLController.returnChecksum("IO error", 1);
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            mainFXMLController.returnChecksum("The generator function encountered a fatal error.", 1);
            e.printStackTrace();
        }
    }

    /**
     * Stops the main running thread. Since the checksum methods are external blocking methods
     * using IO operations, which can't be interrupted, this method is a workaround to
     * conventional thread interrupts. It closes the file stream being read, which forces
     * an IO Exception that kills the checksum generation.
     */
    public void stop() {
        try {
            ABORTED = true; // Sets the ABORTED flag to true to let the thread know it was
                            // voluntarily aborted
            if (USER_FILE_INPUT != null) {
                USER_FILE_INPUT.close();
            }
        } catch (IOException e) {
            mainFXMLController.returnChecksum("The file stream could not be closed", 1);
            e.printStackTrace();
        }
    }

    /**
     * Constructor for generating a hash of a string and returning the resultant hash as a hex string
     *
     * @param hashType The type of hashing algorithm. Options are: "MD5", "SHA-1", "SHA-256", "SHA-384", or "SHA-512"
     * @param input The string to hash
     * @param inputFXMLController The controller class for the FXML gui
     */
    public GetHash(String hashType, String input, Controller inputFXMLController) {
        HEX_RESULT = ""; //clear
        ALGORITHM_TYPE = hashType;
        USER_TEXT_INPUT = input;
        INPUT_TYPE = "Text";

        mainFXMLController = inputFXMLController;
    }

    /**
     * Constructor for generating a hash of a file and returning the resultant hash as a hex string
     *
     * @param hashType The type of hashing algorithm. Options are: "MD5", "SHA-1", "SHA-256", "SHA-384", or "SHA-512"
     * @param inputFile The file to hash
     * @param inputFXMLController The controller class for the FXML gui
     */
    public GetHash(String hashType, InputStream inputFile, Controller inputFXMLController) {
        HEX_RESULT = ""; //clear
        ALGORITHM_TYPE = hashType;
        USER_FILE_INPUT = inputFile;
        INPUT_TYPE = "File";

        mainFXMLController = inputFXMLController;
    }

    /**
     * A method that determines whether a given hash algorithm can be generated by this class
     *
     * @param input A string. in uppercase, representing a hash algorithm
     * @return true if this class supports the algorithm, and false otherwise
     */
    public static boolean algorithmIsValid(String input) {
        return (input.equals("MD5") || input.equals("SHA-1") || input.equals("SHA-256") || input.equals("SHA-384") || input.equals("SHA-512"));
    }
}
