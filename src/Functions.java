

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Functions {

    public static void showScene(Stage primaryStage, Scene baseScene) {
        primaryStage.setTitle("");
        primaryStage.setScene(baseScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static File chooseFileFromPc(Stage primaryStage) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.home")));
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(primaryStage);
    }

    public static boolean checkImage(File file) {
        String name = file.getName();
        System.out.println(name);
        String ex = name.substring(name.indexOf(".") + 1, name.length());
        System.out.println(ex);
        if (ex.equals("jpg") || ex.equals("png") || ex.equals("bmp")) {
            return true;
        }
        return false;
    }

    public static boolean checkPlacesFile(File file) {
        String name = file.getName();
        String ex = name.substring(name.indexOf("") + 1, name.length());
        return ex.equals("txt") || ex.equals("places");
    }

    public static void saveData(String nameMap, ArrayList<Place> listObj) throws IOException {
        if (!listObj.isEmpty()) {
            new SaveAndLoad().save((String) nameMap.subSequence(0, nameMap.indexOf("")), listObj);
            Functions.showBoxAlert(Alert.AlertType.INFORMATION, "Saved", "Saved successfully", "");
        } else {
            Functions.showBoxAlert(Alert.AlertType.ERROR, "!!", "There is no data to save ", "");
        }
    }

    public static void showSaveBox(String nameMap, ArrayList<Place> listObj) {
        Alert alertSave = new Alert(Alert.AlertType.CONFIRMATION);
        alertSave.setTitle("Worning save ");
        alertSave.setHeaderText("Ther are unsaved changes .");
        alertSave.setContentText("Are you want to save changes ? ");
        alertSave.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    saveData(nameMap, listObj);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                alertSave.close();
            } else {
                alertSave.close();
            }
        });
    }

    public static void showBoxAlert(Alert.AlertType alertType, String title, String error, String message) {
        Alert alertError = new Alert(alertType);
        alertError.setTitle(title);
        alertError.setHeaderText(error);
        alertError.setContentText(message);
        alertError.showAndWait();
    }

    public static Place searchOnObjByPosition(ArrayList<Place> listObj, double posX, double posY) {

        for (int i = 0; i < listObj.size(); i++) {

            if (listObj.get(i).getPoint().getPosX() == posX && listObj.get(i).getPoint().getPosY() == posY) {
                return listObj.get(i);
            }
        }
        return null;
    }

    public static boolean checkPosition(ArrayList<Place> list, int x, int y) {
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                if (searchOnObjByPosition(list, x + i, y + j) != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public static ArrayList<Place> addObject(ArrayList<Place>listObj ,String type, Place obj) {

        listObj.add(obj);
        return listObj;
    }
}
