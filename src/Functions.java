import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Functions {

    public static void showScene(Stage primaryStage, Scene baseScene) {

        primaryStage.setTitle("");
        primaryStage.setScene(baseScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Choose a file from the PC
     *
     * @param primaryStage the component that the FileChooser will hook on
     * @return the selected file
     */
    public static File chooseFileFromPc(Stage primaryStage) {

        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(primaryStage);

    }

    /**
     * Check whether the extension of the specified file matches the passed ones
     *
     * @param file       the file in question
     * @param extensions the stated extensions
     * @return true whether the file extension matches otherwise false
     */
    public static boolean matchesAnyExtension(File file, String... extensions) {

        String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);

        return Arrays.asList(extensions).contains(extension);
    }

    /**
     * Save data to file
     *
     * @param nameMap the filename
     * @param places  list to be saved
     * @throws IOException in case of i/o error
     */
    public static void saveData(String nameMap, List<Place> places) throws IOException {
        if (!places.isEmpty()) {
            FileHandler.saveToFile(nameMap, places);
            Functions.showBoxAlert(Alert.AlertType.INFORMATION, "Saving Places", "Saved successfully", "");
        } else {
            Functions.showBoxAlert(Alert.AlertType.INFORMATION, "Saving PLaces", "There is no data to save", "");
        }
    }

    public static void showSaveBox(String nameMap, List<Place> places) {

        Alert alertSave = new Alert(Alert.AlertType.CONFIRMATION);
        alertSave.setTitle("Saving Place(s)");
        alertSave.setHeaderText("Their are unsaved changes.");
        alertSave.setContentText("Are you want to exit without saving changes? ");

        Optional<ButtonType> optionalButtonType = alertSave.showAndWait();
        optionalButtonType.ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    saveData(nameMap, places);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    /**
     * Show a javafx alert
     *
     * @param alertType the type of the alert
     * @param title     of the alert
     * @param error     status of the alert
     * @param message   of the alert
     */
    public static void showBoxAlert(Alert.AlertType alertType, String title, String error, String message) {
        Alert alertError = new Alert(alertType);
        alertError.setTitle(title);
        alertError.setHeaderText(error);
        alertError.setContentText(message);
        alertError.showAndWait();
    }

    public static Place searchOnObjByPosition(List<Place> listObj, double posX, double posY) {

        for (Place place : listObj) {
            if (place.getPosition().getPosX() == posX && place.getPosition().getPosY() == posY) {
                return place;
            }
        }
        return null;
    }

    public static boolean checkPosition(List<Place> list, int x, int y) {
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                if (searchOnObjByPosition(list, x + i, y + j) != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public static List<Place> addObject(List<Place> listObj, String type, Place obj) {

        listObj.add(obj);

        return listObj;
    }
}
