package main;

import gui.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import utils.PreferencesLoader;

/**
 * Checksum Calculator's main class
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
public class Main extends Application {

    public static String applicationVersion = "0.1 beta";
    private Controller mainFXMLController;

    @Override
    public void start(Stage primaryStage) throws Exception {

        PreferencesLoader.checkFolders();

        Parent root = FXMLLoader.load(getClass().getResource("/gui/ChecksumLayout.fxml"));
        primaryStage.setScene(new Scene(root, 390, 455));

        Font.loadFont(primaryStage.getScene().getClass().getResourceAsStream("file:src/resources/fonts/Roboto-Light.ttf"), 10);
        primaryStage.getIcons().add(new Image("resources/icon.png"));
        primaryStage.setTitle("Checksum Calculator " + applicationVersion);
        mainFXMLController = Controller.mainFXMLController;
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.toFront();


        // Save application setting on exit
        primaryStage.setOnCloseRequest(event -> mainFXMLController.onExit());
    }

    public static void main(String[] args) { launch(args); }
}
