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

    private static final int MAX_STRING_LENGTH = 1000000;
    private static String ALGORITHM_TYPE = "md5";
    private static String INPUT_TYPE = "Text";
    private static String HEX_RESULT = "";

    private static String USER_TEXT_INPUT = "";
    private static InputStream USER_FILE_INPUT = null;

    private Controller mainFXMLController;


    @Override
    public void run() {

        try {
            if (INPUT_TYPE.equals("Text")) {

                if (checkLength(USER_TEXT_INPUT) != 0) { // check that the length is not too long
                    String errorTooLong = "Input string is " + checkLength(USER_TEXT_INPUT) + " character";

                    // Add an 's' if exceeds the length by more than 1
                    if (checkLength(USER_TEXT_INPUT) > 1){
                        errorTooLong += "s";
                    }
                    mainFXMLController.returnChecksum(errorTooLong + " too long. Max length is " + MAX_STRING_LENGTH + " characters.", true);

                } else {

                    if (ALGORITHM_TYPE.equals("md5")) {
                        HEX_RESULT = md5Hex(USER_TEXT_INPUT);

                    } else if (ALGORITHM_TYPE.equals("sha-1")) {
                        HEX_RESULT = sha1Hex(USER_TEXT_INPUT);

                    } else if (ALGORITHM_TYPE.equals("sha-256")) {
                        HEX_RESULT = sha256Hex(USER_TEXT_INPUT);

                    } else if (ALGORITHM_TYPE.equals("sha-384")) {
                        HEX_RESULT = sha384Hex(USER_TEXT_INPUT);

                    } else if (ALGORITHM_TYPE.equals("sha-512")) {
                        HEX_RESULT = sha512Hex(USER_TEXT_INPUT);

                    } else {
                        throw new IllegalArgumentException();
                    }

                    mainFXMLController.returnChecksum(HEX_RESULT, false); // return the result
                }

            } else if(INPUT_TYPE.equals("File")) {
                try {
                    if(ALGORITHM_TYPE.equals("md5")) {
                        HEX_RESULT = md5Hex(USER_FILE_INPUT);

                    } else if(ALGORITHM_TYPE.equals("sha-1")) {
                        HEX_RESULT = sha1Hex(USER_FILE_INPUT);

                    } else if(ALGORITHM_TYPE.equals("sha-256")) {
                        HEX_RESULT = sha256Hex(USER_FILE_INPUT);

                    } else if(ALGORITHM_TYPE.equals("sha-384")) {
                        HEX_RESULT = sha384Hex(USER_FILE_INPUT);

                    } else if(ALGORITHM_TYPE.equals("sha-512")) {
                        HEX_RESULT = sha512Hex(USER_FILE_INPUT);

                    } else {
                        throw new IllegalArgumentException();
                    }

                    // Close file
                    try {
                        if(USER_FILE_INPUT != null) {
                            USER_FILE_INPUT.close();
                        }
                    } catch (IOException ioEX2) {
                        mainFXMLController.returnChecksum("The file stream could not be closed", true);
                        ioEX2.printStackTrace();
                    }

                } catch (IOException e) {
                    HEX_RESULT = "IO error";
                }
                mainFXMLController.returnChecksum(HEX_RESULT, false);
//                Platform.runLater(() -> mainFXMLController.returnChecksum(HEX_RESULT, false)); // return the result
            }

        } catch (Exception e) {
            mainFXMLController.returnChecksum("The generator function encountered a fatal error.", true);
            e.printStackTrace();
        }
    }

    /**
     * Constructor for generating a hash of a string and returning the resultant hash as a hex string
     *
     * @param hashType The type of hashing algorithm. Options are: "md5", "sha-1", "sha-256", "sha-384", or "sha-512"
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
     * @param hashType The type of hashing algorithm. Options are: "md5", "sha-1", "sha-256", "sha-384", or "sha-512"
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
     * Returns 0 if the input is less than the max length, or return the number of characters exceeding the
     * max length
     * @param input The user-entered string
     * @return 0 if the string is less than the max length, or if it is longer than the max length,
     *      the difference between the string length and max length
     */
    private static int checkLength(String input) {
        if (input.length() < MAX_STRING_LENGTH) {
            return 0;
        } else {
            return input.length() - MAX_STRING_LENGTH;
        }
    }

//    private static boolean checkSize(String input) { // need to change input
//
//        return false;
//    }


}
