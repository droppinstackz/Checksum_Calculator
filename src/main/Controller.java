package main;// JavaFX Imports
import javafx.fxml.FXML;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

//import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

// Checksum getHash() imports
import static main.getHash.getHashString;

/**
 * Controller for the Checksum Calculator GUI
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
public class Controller implements Initializable {

    @FXML private TextField inputTextField;

    @FXML private ComboBox algorithmType;
    @FXML private MenuButton inputType;
    @FXML private Button openButton;
    @FXML private Button generateButton;

    @FXML private Button copyButton;
    @FXML private Button pasteButton;
    @FXML private Button clearButton;
    @FXML private CheckBox compareToCheckbox;

    @FXML private Label firstChecksum;
    @FXML private Label secondChecksum;


    private static String CSS_BACKGROUND_GREEN = "-fx-background-color: #E2FDE3";
    private static String CSS_BACKGROUND_RED = "-fx-background-color: #FFE4E4";
    private static String CSS_BACKGROUND_WHITE = "-fx-background-color: #FFFFFF";

//    private static String ALGORITHM_SELECTION = "MD5";

    // application will have to remember the algorithm selection, input type selection, and checkbox selection
    // compare checksums when generate or paste button is pushed
    // do not compare when field is empty or when compareTo is disabled
    // add a 'Copied' label (in red) to the right of the copy button after it is pushed
    // text from handleOpenButtonAction is not cleared when 'Text' entry is selected

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        // Initialize the algorithmType ChoiceBox
        algorithmType.setItems(FXCollections.observableArrayList());
        algorithmType.getItems().add(0, "MD5");
        algorithmType.setValue("MD5");
        algorithmType.getItems().add(1, "SHA-1");
        algorithmType.getItems().add(2, "SHA-256");
        algorithmType.getItems().add(3, "SHA-384");
        algorithmType.getItems().add(4, "SHA-512");


        // Initialize the inputType ChoiceBox
//        inputType.getItems().addAll(new MenuItem("Text"), new MenuItem("File"));
        inputType.setText("Text");

//        inputType.setItems(FXCollections.observableArrayList());
//        inputType.getItems().add(0, "Text");
//        inputType.setValue("Text");
//        inputType.getItems().add(1, "File");

        // Set the checksum result fields blank and enable & disable the appropriate elements
        firstChecksum.setText("");
        secondChecksum.setText("");
        inputTextField.setDisable(false);
        openButton.setDisable(true);
        compareToCheckbox.setSelected(false);
        pasteButton.setDisable(true);
        clearButton.setDisable(true);
        secondChecksum.setDisable(true);

        inputTextField.requestFocus();
    }

    @FXML protected void handleEnterKey() {
        // If 'Enter' key is pressed while the inputTextField is focused, automatically generate the checksum
        handleGenerateButtonAction();
    }

    @FXML protected void handleTextInputTypeSelection() {
        inputType.setText("Text");
        inputTextField.setDisable(false);
        openButton.setDisable(true);
    }

    @FXML protected void handleFileInputTypeSelection() {
        inputType.setText("File");
        inputTextField.setDisable(true);
        openButton.setDisable(false);
    }

    @FXML protected void handleOpenButtonAction() {
//        new java.awt.FileDialog((java.awt.Frame) null).show();
        // need to handle open dialog
        // need to find out how to create ellipses in middle of file path
//        inputTextField.setText("C:\\Users\\imaginary\\file\\path\\to\\nowhere\\with\\no\\end\\in\\sight.txt");
    }

    @FXML protected void handleGenerateButtonAction() {
        // Prevent the user from flooding the checksum generator function by locking critical buttons
        generateButton.setDisable(true);
        algorithmType.setDisable(true);
        copyButton.setDisable(true);
        inputType.setDisable(true);

        // The wait text will remain displayed while the checksum is being generated
        firstChecksum.setText("                             Generating...\n" +
                              "                    (This may take a while)");

        if(inputType.getText().equals("Text")) {
            firstChecksum.setText(getHashString(algorithmType.getValue().toString().toLowerCase(), inputTextField.getText()));

            // Place cursor focus back to the inputTextfield
            inputTextField.requestFocus();

        } else if (inputType.getText().equals("File")) {
//            firstChecksum.setText(getHashString(algorithmType.getValue().toString().toLowerCase(), inputTextField.getText()));

        }

        // Unlock critical buttons
        generateButton.setDisable(false);
        algorithmType.setDisable(false);
        copyButton.setDisable(false);
        inputType.setDisable(false);

        if(compareToCheckbox.isSelected()) { compareChecksums(); }
    }

    @FXML protected void handleCopyButtonAction() {
        // Copy the checksum text into the user's system clipboard if it is not empty
        if(!firstChecksum.getText().equals("")){
            StringSelection checksumSelection = new StringSelection(firstChecksum.getText());
            Clipboard clipboardCopy = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboardCopy.setContents(checksumSelection, checksumSelection);
            // print text: '(Copied to clipboard)'
        }
    }

    @FXML protected void handleCompareToSelect() {
        // If the compareTo checkbox is checked, enable the clear button, paste button, and second
        // checksum field. If it is not checked, disable the items.
        if(compareToCheckbox.isSelected()) {
            clearButton.setDisable(false);
            secondChecksum.setDisable(false);
            pasteButton.setDisable(false);

            // enable colors
            compareChecksums();
        } else {
            pasteButton.setDisable(true);
            clearButton.setDisable(true);
            secondChecksum.setDisable(true);

            // disable colors
            clearHighlights();
        }
    }

    @FXML protected void handlePasteButtonAction() {
        pasteButton.setDisable(true);
        try {
            // Retrieve contents from the clipboard
            Clipboard clipboardPaste = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable validContents = clipboardPaste.getContents(null);

            // If the contents are not null and are of type string
            if((validContents != null) && validContents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                // Try to retrieve the string contents
                String clipboardContents = "";
                try {
                    clipboardContents = (String)validContents.getTransferData(DataFlavor.stringFlavor);
                } catch (UnsupportedFlavorException e) {
                    secondChecksum.setText("Error: string DataFlavor types are not supported.");
                    secondChecksum.setStyle(CSS_BACKGROUND_RED);
                    e.printStackTrace();
                } catch (IOException e) {
                    secondChecksum.setText("Error: could not read the clipboard. It may be empty.");
                    secondChecksum.setStyle(CSS_BACKGROUND_RED);
                    e.printStackTrace();
                }

                // If the length is longer than the longest checksum result, print error message
                if (clipboardContents.length() > 128) {
                    secondChecksum.setText("String pasted is too long (Max length of 128 characters). " +
                            "Please paste in a valid checksum result.");
                    secondChecksum.setStyle(CSS_BACKGROUND_RED);
                } else {
                    secondChecksum.setText(clipboardContents);
                    compareChecksums();
                }
            }
        } catch (Exception e) {
            secondChecksum.setText("The clipboard could not be accessed. Another application may currently be accessing it.");
            secondChecksum.setStyle(CSS_BACKGROUND_RED);
//            e.printStackTrace();
        }
        pasteButton.setDisable(false);
    }

    @FXML protected void handleClearButtonAction() {
        // Set the text in the second checksum field to an empty string if it is not already empty
        if(!secondChecksum.getText().equals("")) {
            secondChecksum.setText("");
            clearHighlights();
        }
    }

    /**
     * Private method that compares the first and second checksums. If they are the same,
     * they are highlighted green, but if they are different, set the background to red.
     */
    private void compareChecksums() {
        if (!secondChecksum.getText().equals("") && secondChecksum.getText().equals(firstChecksum.getText())) {
            // set color to green
            // the checksums are the same & there is non-null text to compare
            firstChecksum.setStyle(CSS_BACKGROUND_GREEN);
            secondChecksum.setStyle(CSS_BACKGROUND_GREEN);

        } else if(!secondChecksum.getText().equals("")) {
            // set color to red
            // the checksums are not the same & there is non-null text to compare
            firstChecksum.setStyle(CSS_BACKGROUND_RED);
            secondChecksum.setStyle(CSS_BACKGROUND_RED);
        }
    }

    /**
     * Private method that removes any highlighting on both checksum results
     */
    private void clearHighlights() {
        firstChecksum.setStyle(CSS_BACKGROUND_WHITE);
        secondChecksum.setStyle(CSS_BACKGROUND_WHITE);
    }

}
