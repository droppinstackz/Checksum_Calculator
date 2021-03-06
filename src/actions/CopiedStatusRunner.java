package actions;

import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * A thread that temporarily displays, then hides a label for 2.5 seconds
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

public class CopiedStatusRunner implements Runnable {

    Label timedLabel;

    public CopiedStatusRunner (Label timedLabel) {
        this.timedLabel = timedLabel;
    }

    @Override
    public void run() {
        try {
            Platform.runLater(() -> timedLabel.setVisible(true));
            Thread.sleep(2500);
            Platform.runLater(() -> timedLabel.setVisible(false));

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
