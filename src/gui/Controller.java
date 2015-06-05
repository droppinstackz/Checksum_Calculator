package gui;
import actions.CopiedStatusRunner;
import actions.GetHash;
import javafx.application.Platform;
import javafx.fxml.FXML;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    @FXML private ComboBox<Object> algorithmType;
    @FXML private MenuButton inputType;
    @FXML private Button openButton;
    @FXML private Button generateButton;
    @FXML private Button stopButton;

    @FXML private Button copyButton;
    @FXML private Button pasteButton;
    @FXML private Button clearButton;
    @FXML private CheckBox compareToCheckbox;

    @FXML private Label firstChecksum;
    @FXML private Label secondChecksum;
    @FXML public Label copiedLabel;

    @FXML private Label waitLine1;
    @FXML private Label waitLine2Size;
    @FXML private Label waitLine3;
    @FXML private ProgressIndicator progressIndicator;

    private File inputFile = null;
    private String userText = "";
    private static int FILE_SIZE_BYTES = 0;

    private static String CSS_BACKGROUND_GREEN = "-fx-background-color: #E2FDE3";
    private static String CSS_BACKGROUND_RED = "-fx-background-color: #FFE4E4";
    private static String CSS_BACKGROUND_WHITE = "-fx-background-color: #FFFFFF";

    // application will have to remember the algorithm selection, input type selection, and checkbox selection
    // add a 'Stop' button to stop the checksum calculation

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
        inputType.setText("Text");

        // Set the checksum result fields blank and enable & disable the appropriate elements
        firstChecksum.setText("");
        secondChecksum.setText("");

        inputTextField.setDisable(false);
        openButton.setDisable(true);
        copiedLabel.setVisible(false);
        inputTextField.requestFocus();

        // CompareTo options
        compareToCheckbox.setSelected(false);
        pasteButton.setDisable(true);
        clearButton.setDisable(true);
        secondChecksum.setDisable(true);

        setProgressMessage("disable");
    }

    @FXML
    protected void handleEnterKey() {
        // If 'Enter' key is pressed while the inputTextField is focused, automatically generate the checksum
        handleGenerateButtonAction();
    }

    @FXML
    protected void handleTextInputTypeSelection() {
        // When the user selects the "Text" option from the dropdown
        Platform.runLater(() -> {
            inputType.setText("Text");
            inputTextField.setText(userText);
            inputTextField.setDisable(false);
            openButton.setDisable(true);
            generateButton.setDisable(false);
            inputTextField.requestFocus();
        });
    }

    @FXML
    protected void handleFileInputTypeSelection() {
        // When the user selects the "File" option from the dropdown
        Platform.runLater(() -> {
            inputType.setText("File");
            userText = inputTextField.getText();
            inputTextField.setText("");
            inputTextField.setDisable(true);
            openButton.setDisable(false);
            generateButton.setDisable(true);
        });
    }

    @FXML
    protected void handleOpenButtonAction() {

        // Open the file chooser
        try {
            Stage stage = (Stage) generateButton.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select File");
            inputFile = fileChooser.showOpenDialog(stage);

            if (inputFile != null) {  // Display file path in inputTextField
                String filePath = inputFile.getAbsolutePath();
                int stringLength = filePath.length();

                // place ellipses in the middle of the file path if it is too long
                if (stringLength > 50) {
                    filePath = filePath.substring(0, 11) + "... " + filePath.substring(stringLength - 30);
                }
                inputTextField.setText(filePath);
                generateButton.setDisable(false);
                firstChecksum.setText("");
                clearHighlights();
            }
        } catch (Exception e) {
            displayError("The file could not be opened", firstChecksum);
        }
    }

    @FXML
    protected void handleGenerateButtonAction() {

        lockButtons();
        setProgressMessage("enable");

        // Starts the threads that will generate the checksums
        if (inputType.getText().equals("Text")) {
            inputTextField.setDisable(true);

            GetHash ght = new GetHash(algorithmType.getValue().toString().toLowerCase(), inputTextField.getText(), Controller.this);
            new Thread(ght).start();

        } else if (inputType.getText().equals("File") && (inputFile != null)) {
            InputStream fileStream;

            try {
                fileStream = new FileInputStream(inputFile);
                FILE_SIZE_BYTES = fileStream.available(); // Get the file size in bytes

                GetHash ght = new GetHash(algorithmType.getValue().toString().toLowerCase(), fileStream, Controller.this);
                new Thread(ght).start();

            } catch (FileNotFoundException e) {
                displayError("The file could not be found", firstChecksum);
                e.printStackTrace();

            } catch (IOException ioEX1) {
                displayError("The file could not be read", firstChecksum);
                ioEX1.printStackTrace();
            }
        }
    }

    @FXML
    protected void handleStopButtonAction() {
        //TODO code to kill checksum generator thread
    }

    @FXML
    protected void handleCopyButtonAction() {
        // Copy the checksum text into the user's system clipboard if it is not empty
        Platform.runLater(() -> {
            if (!firstChecksum.getText().equals("")) {
                StringSelection checksumSelection = new StringSelection(firstChecksum.getText());
                Clipboard clipboardCopy = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboardCopy.setContents(checksumSelection, checksumSelection);

                CopiedStatusRunner ctr = new CopiedStatusRunner(copiedLabel);
                new Thread(ctr).start();
            }
        });
    }

    @FXML
    protected void handleCompareToSelect() {
        // If the compareTo checkbox is checked, enable the clear button, paste button, and second
        // checksum field. If it is not checked, disable the items.
        Platform.runLater(() -> {
            if (compareToCheckbox.isSelected()) {
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
        });
    }

    @FXML
    protected void handlePasteButtonAction() {
        Platform.runLater(() -> {
            pasteButton.setDisable(true);
            try {
                // Retrieve contents from the clipboard
                Clipboard clipboardPaste = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable validContents = clipboardPaste.getContents(null);

                // If the contents are not null and are of type string
                if ((validContents != null) && validContents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    // Try to retrieve the string contents
                    String clipboardContents = "";
                    try {
                        clipboardContents = (String) validContents.getTransferData(DataFlavor.stringFlavor);
                    } catch (UnsupportedFlavorException e) {
                        displayError("Error: string DataFlavor types are not supported.", secondChecksum);
                        e.printStackTrace();
                    } catch (IOException e) {
                        displayError("Error: could not read the clipboard. It may be empty.", secondChecksum);
                        e.printStackTrace();
                    }

                    // If the length is longer than the longest checksum result, print error message
                    if (clipboardContents.length() > 128) {
                        displayError("String pasted is too long  (Max length of 128 characters). " +
                                "Please paste in a valid checksum result.", secondChecksum);
                    } else {
                        secondChecksum.setText(clipboardContents);
                        compareChecksums();
                    }
                }
            } catch (Exception e) {
                displayError("The clipboard could not be accessed. Another application may currently be accessing it.", secondChecksum);
            }
            pasteButton.setDisable(false);
        });
    }

    @FXML
    protected void handleClearButtonAction() {
        // Set the text in the second checksum field to an empty string if it is not already empty
        Platform.runLater(() -> {
            if (!secondChecksum.getText().equals("")) {
                secondChecksum.setText("");
                clearHighlights();
            }
        });
    }


    public void returnChecksum(String result, boolean failure) {
        Platform.runLater(() -> {
            setProgressMessage("disable");

            if (failure) {
                displayError(result, firstChecksum);

            } else {
                firstChecksum.setText(result);
                compareChecksums();
            }

            unlockButtons();

            // Place cursor focus back to the inputTextfield
            if (inputType.getText().equals("Text")) {
                inputTextField.setDisable(false);
                inputTextField.requestFocus();
            }
        });
    }

    /**
     * Private method that compares the first and second checksums. If they are the same,
     * they are highlighted green, but if they are different, set the background to red.
     */
    private void compareChecksums() {
        if ((!firstChecksum.getText().equals("")) && (!secondChecksum.getText().equals("")) && secondChecksum.getText().equals(firstChecksum.getText()) && compareToCheckbox.isSelected()) {
            // set color to green
            // the checksums are the same & there is non-null text to compare
            Platform.runLater(() -> {
                firstChecksum.setStyle(CSS_BACKGROUND_GREEN);
                secondChecksum.setStyle(CSS_BACKGROUND_GREEN);
            });

            } else if ((!firstChecksum.getText().equals("")) && (!secondChecksum.getText().equals("")) && compareToCheckbox.isSelected()) {
            // set color to red
            // the checksums are not the same & there is non-null text to compare
            Platform.runLater(() -> {
                firstChecksum.setStyle(CSS_BACKGROUND_RED);
                secondChecksum.setStyle(CSS_BACKGROUND_RED);
            });
        }
    }

    /**
     * Private method that removes any highlighting on both checksum results
     */
    private void clearHighlights() {
        Platform.runLater(() -> {
            firstChecksum.setStyle(CSS_BACKGROUND_WHITE);
            secondChecksum.setStyle(CSS_BACKGROUND_WHITE);
        });
    }

    /**
     * Prevent the user from flooding the checksum generator function by locking critical buttons
     */
    private void lockButtons() {
        Platform.runLater(() -> {
            algorithmType.setDisable(true);
            inputType.setDisable(true);
            generateButton.setDisable(true);
            copyButton.setDisable(true);
        });
    }

    /**
     * Unlock critical buttons
     */
    private void unlockButtons() {
        Platform.runLater(() -> {
            algorithmType.setDisable(false);
            inputType.setDisable(false);
            generateButton.setDisable(false);
            copyButton.setDisable(false);
        });
    }

    /**
     * Set the progress message in the first text-field
     *
     * @param action "enable" to enable message, and "disable" to disable message
     */
    private void setProgressMessage(String action) {
        Platform.runLater(() -> {
            if (action.equals("enable")) {
                generateButton.setVisible(false);

                stopButton.setVisible(true);

                progressIndicator.setDisable(false);
                progressIndicator.setVisible(true);
                progressIndicator.setProgress(-1);

                waitLine1.setVisible(true);
                waitLine2Size.setVisible(true);
                waitLine2Size.setText("");

                FILE_SIZE_BYTES = 0;
                if (inputType.getText().equals("File")) {
                    waitLine2Size.setText(FILE_SIZE_BYTES + " bytes");
                } else if (inputType.getText().equals("Text")) {
                    waitLine2Size.setText(inputTextField.getText().length() + " characters");
                }

                waitLine3.setVisible(true);

                firstChecksum.setText("");
                clearHighlights();

            } else if (action.equals("disable")) {
                generateButton.setVisible(true);

                stopButton.setDisable(true);
                stopButton.setVisible(false);

                progressIndicator.setDisable(true);
                progressIndicator.setVisible(false);
                progressIndicator.setProgress(0);

                waitLine1.setVisible(false);
                waitLine2Size.setVisible(false);
                waitLine3.setVisible(false);
            }
        });
    }

    /**
     * Displays an error in the given label and sets its highlight to red
     *
     * @param errorMessage An error message to display
     * @param displayLabel The label where the error message will be displayed
     */
    public void displayError(String errorMessage, Label displayLabel) {
        Platform.runLater(() -> {
            displayLabel.setText(errorMessage);
            displayLabel.setStyle(CSS_BACKGROUND_RED);
        });
    }
}
