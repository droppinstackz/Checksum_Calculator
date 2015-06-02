package actions;

import gui.Controller;
import javafx.scene.control.Label;

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

public class CopiedStatusRunner implements Runnable {

    Label copiedLabel;
//    private Controller mainFXMLController;

    public CopiedStatusRunner (Label copiedLabel) {
        this.copiedLabel = copiedLabel;
    }

    @Override
    public void run() {
        try {
//            copiedLabel.setText("(Copied)");
//            mainFXMLController.copiedLabel.setStyle("-fx-background-color: #FFE4E4");
            copiedLabel.setText("(Copied)");
            Thread.sleep(3000);
            copiedLabel.setText("");

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}