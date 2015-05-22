package main;

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
public class getHash {

    private static final int MAX_STRING_LENGTH = 10000;

    /**
     * Generates a hash of an input string and returns the hash as a hex string
     *
     * @param hashType The type of hashing algorithm. Options are: "md5", "sha-1", "sha-256", "sha-384", or "sha-512"
     * @param input The string to hash
     * @return The hashed result as a hex string
     */
    public static String getHashString(String hashType, String input) {

        if (checkLength(input) != 0) {
            String errorTooLong = "Input string is " + checkLength(input) + " character";

            // Add an 's' if oversized by more than 1
            if (checkLength(input) > 1){
                errorTooLong += "s";
            }
            return errorTooLong + " too long. Max length is " + MAX_STRING_LENGTH + " characters.";
        }

        if(hashType.equals("md5")) {
            return md5Hex(input);
        } else if(hashType.equals("sha-1")) {
            return sha1Hex(input);
        } else if(hashType.equals("sha-256")) {
            return sha256Hex(input);
        } else if(hashType.equals("sha-384")) {
            return sha384Hex(input);
        } else if(hashType.equals("sha-512")) {
            return sha512Hex(input);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Generates a hash of a file and returns the hash as a hex string
     *
     * @param hashType The type of hashing algorithm. Options are: "md5", "sha1", "sha256", "sha384", or "sha512"
     * @param inputFile The file to hash
     * @return The hashed result as a hex string
     */
    public static String getHashFile(String hashType, InputStream inputFile) {

        try {
            if(hashType.equals("md5")) {
                return md5Hex(inputFile);
            } else if(hashType.equals("sha-1")) {
                return sha1Hex(inputFile);
            } else if(hashType.equals("sha-256")) {
                return sha256Hex(inputFile);
            } else if(hashType.equals("sha-384")) {
                return sha384Hex(inputFile);
            } else if(hashType.equals("sha-512")) {
                return sha512Hex(inputFile);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IOException e) {
            return "IO error";
        }
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

    private static boolean checkSize(String input) { // need to change input

        return false;
    }
}
