

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class SaveAndLoad {

    private String[] obj = new String[6];
    private String string = "Named,None,332,115,gh, .";
    private FileReader file1;
    private FileWriter file2;
    private ArrayList<Place> list = new ArrayList<>();

    public ArrayList<Place> load(Stage primaryStage) throws IOException {
        this.formatList(primaryStage);
        return this.list;
    }

    public void save(String nameFile, ArrayList<Place> list) throws IOException {
        this.list = list;
        file2 = new FileWriter(nameFile + ".txt");
        for (int i = 0; i < list.size(); i++) {
            parseObjectToString(list.get(i));
            file2.append(string + "\n");
        }
        file2.close();
    }

    public void parse() {
        for (int i = 0; i < 6; i++) {
            if (i == 5) {
                obj[i] = string.substring(0, string.indexOf(""));
            } else {
                this.obj[i] = string.substring(0, string.indexOf(","));
                this.string = string.substring(obj[i].length() + 1, string.length());
            }
        }
    }

    public void formatList(Stage primaryStage) throws FileNotFoundException, IOException {
        File f = Functions.chooseFileFromPc(primaryStage);
        if (Functions.checkPlacesFile(f)) {
            file1 = new FileReader(f);
            Scanner sc = new Scanner(file1);
            while (sc.hasNext()) {
                string = sc.nextLine();
                parse();
                list.add(parseStringToObject());
            }
            sc.close();
            file1.close();
        } else {
            Functions.showBoxAlert(Alert.AlertType.ERROR, "Error", "Wrong file", "The Places File should be 'TXT File'");
        }

    }

    public Place parseStringToObject() {
        Category cate = new Category(obj[1]);
        if (obj[0].equals("Named")) {
            return new NamedPlace(obj[4], cate, Integer.valueOf(obj[2]), Integer.valueOf(obj[3]));
        } else {
            return new DiscribedPlace(obj[4], obj[5], cate, Integer.valueOf(obj[2]), Integer.valueOf(obj[3]));
        }
    }

    public void parseObjectToString(Place object) {
        if (object.getTypeObj().equals("Named")) {
            this.string = object.getTypeObj() + "," + object.getCategory() + "," + object.getPoint().getPosX() + "," + object.getPoint().getPosY() + "," + object.getName() + "," + " " + "";
        } else {
            this.string = object.getTypeObj() + "," + object.getCategory() + "," + object.getPoint().getPosX() + "," + object.getPoint().getPosY() + "," + object.getName() + "," + object.getDescription() + "";
        }
    }

}
