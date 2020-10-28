import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    /**
     * Loads places saved in a file that will be chosen
     *
     * @param primaryStage to anchor the FileChooser
     * @return List of Place found in the file
     * @throws IOException in case of any i/o errors
     */
    public static List<Place> loadFromFile(Stage primaryStage) throws IOException {

        List<Place> places = new ArrayList<>();

        File f = Functions.chooseFileFromPc(primaryStage);

        if (Functions.matchesAnyExtension(f, "txt", "places")) {

            Files.lines(Paths.get(f.getAbsolutePath())).forEach(line -> {

                if (line.split(",").length == 5) {
                    places.add(Place.fromString(line));
                } else if (line.split(",").length == 6) {
                    places.add(DescribedPlace.fromString(line));
                }

            });
        } else {
            Functions.showBoxAlert(Alert.AlertType.ERROR, "Error", "Wrong file", "The Places File should be 'TXT File'");
        }

        return places;
    }

    /**
     * Save a list of places to a file specified
     *
     * @param fileName the name of the file to be appended to
     * @param list     places to be appended
     * @throws IOException when the is an i/o error
     */
    public static void saveToFile(String fileName, ArrayList<Place> list) throws IOException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
            for (Place place : list) {
                writer.append(place.toString()).append("\n");
            }
        }
    }
}
