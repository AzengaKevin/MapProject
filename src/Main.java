import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The launcher class of the application, where the application is bootstrapped
 */
public class Main extends Application {

    private MenuItem loadMapMenuItem;
    private MenuItem loadPlacesMenuItem;
    private MenuItem savePlacesMenuItem;
    private MenuItem exitProgramMenuItem;

    private Menu nameMenu;
    //
    private Button newBtn;
    private RadioButton namedRadioButton;
    private RadioButton describedRadioButton;
    //
    private TextField searchTextField;
    private Button searchBtn;
    private Button hideBtn;
    private Button removeBtn;
    private Button coordinatesBtn;
    private BorderPane root;
    //
    private Pane mapPane;
    //
    private ListView<String> categoryList;
    private Button hideCategory;
    //
    private String categoryName = "";
    private ImageView imageBackground;
    //
    private Scene baseScene;
    private String nameMap = "";
    //
    private ArrayList<Polygon> selectionPlaces = new ArrayList<>();
    private ArrayList<Polygon> hideItems = new ArrayList<>();
    //
    private ScrollPane scrollPane = new ScrollPane();
    private Group groupTriangle = new Group(scrollPane);
    //

    private List<Place> savedPlaces;
    private List<Place> newPlaces;

    private boolean selectionMode;
    private DropShadow shadow = new DropShadow(5, Color.BLACK);
    private boolean newBtnClicked;
    //
    private Button addNamedPlaceBtn;
    private Button addDescribedPlaceBtn;
    private Stage addNamedPlaceStage;
    private Stage addDescribedPlaceButton;

    private TextField nameTextField;
    //
    private boolean[] hiddenCategory = new boolean[4];
    //
    private Button okCoordinates;
    private Button cancelCoordinates;
    private Stage coordinatesStage;
    private TextField textPosX;
    private TextField textPosY;

    @Override
    public void start(Stage primaryStage) throws IOException {
        //
        initialize();
        //
        onSelectLoadMap(primaryStage);
        //
        onSelectLoadPlaces(primaryStage);
        //
        onSelectSave();
        //
        onSelectExit();
        //
        onSelectItemFromCategory();
        //
        onClickNewBtn();
        //
        onClickHideBtn();
        //
        onClickRemoveBtn();
        //
        onClickHideCategory();
        //
        onSearch();
        //
        onClickCoordinates();
        //
        Functions.showScene(primaryStage, baseScene);
    }

    /**
     * args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    //************1
    public void onSelectLoadMap(Stage primaryStage) {

        loadMapMenuItem.setOnAction(event -> {
            if ("".equals(nameMap)) {
                loadMap(primaryStage);
                if (!"".equals(nameMap)) {
                    nameMenu.getItems().clear();
                    nameMenu.getItems().addAll(loadPlacesMenuItem, savePlacesMenuItem, exitProgramMenuItem);
                }
            } else {

                //Functions.ShowSaveBox();
                //ClearAllData();
                //loadMap(primaryStage);

            }
        });
    }

    /**
     * Loads the map image and initializes the main window
     *
     * @param primaryStage the windows primary stage
     */
    public void loadMap(Stage primaryStage) {

        File f = Functions.chooseFileFromPc(primaryStage);

        System.out.println(f.getAbsolutePath());

        if (Functions.matchesAnyExtension(f, "jpg", "jpeg", "bmp", "png")) {

            Image image = null;

            try {
                image = new Image(new FileInputStream(f));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            imageBackground = new ImageView(image);
            imageBackground.setFitHeight(600);
            imageBackground.setFitWidth(900);
            mapPane.setPrefHeight(850);
            mapPane.setPrefWidth(900);
            root.setCenter(mapPane);
            mapPane.getChildren().add(imageBackground);
            mapPane.setStyle("-fx-padding:7px;");
            nameMap = f.getName();
        } else {
            Functions.showBoxAlert(Alert.AlertType.ERROR, "Error", "Wrong file ", "The map is a common image ï¬پle (for example .png, .jpg or .bmp).");
        }

    }

    //************2
    public void onSelectLoadPlaces(Stage primaryStage) {

        loadPlacesMenuItem.setOnAction(event -> {
            if (!nameMap.isBlank()) {
                try {

                    //Get places from file
                    savedPlaces = FileHandler.loadFromFile(primaryStage);

                    if (!savedPlaces.isEmpty()) {
                        drawPlaces();
                    } else {
                        Functions.showBoxAlert(Alert.AlertType.INFORMATION, "Load Places", "This File is Empty", "Choose another file");
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                Functions.showBoxAlert(Alert.AlertType.ERROR, "Error", "Map is not exist ", "Can't be loaded places if there is no map");
            }
        });
    }


    //************3
    public void onSelectSave() {
        savePlacesMenuItem.setOnAction(event -> {
            try {
                Functions.saveData(nameMap, newPlaces);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    //************4
    public void onSelectExit() {

        exitProgramMenuItem.setOnAction(event -> {

            if (!newPlaces.isEmpty()) {
                System.out.println("Saving Nee Locations");
                Functions.showSaveBox(nameMap, newPlaces);
            }

            Platform.exit();
        });
    }

    //************5
    public void onSelectItemFromCategory() {
        categoryList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> categoryName = newValue);
    }

    //************6
    public void onClickNewBtn() {

        newBtn.setOnAction(event -> {

            if (nameMap.isBlank()) {
                newBtnClicked = true;
                if (namedRadioButton.isSelected()) {
                    newObjByNamed();
                } else if (describedRadioButton.isSelected()) {
                    newObjByDescribed();
                } else {
                    Functions.showBoxAlert(Alert.AlertType.ERROR, "!!", " object type is null ", "the object type should be selected (Named , Described) ");
                }
            } else {
                Functions.showBoxAlert(Alert.AlertType.ERROR, "!!", "There is no map to mark it", "");
            }

        });
    }

    public void newObjByNamed() {
        showBoxAddObjByName();
        handleAddNamePlaceButtonClick();
    }

    public void newObjByDescribed() {
        showBoxAddObjByDescribed();
        onClickAddByDescribedBtn();
    }

    public void showBoxAddObjByName() {
        GridPane addPane = new GridPane();
        addPane.setMinSize(200, 100);
        addPane.setPadding(new Insets(10, 10, 10, 10));
        addPane.setVgap(5);
        addPane.setHgap(5);
        addPane.setAlignment(Pos.CENTER);
        Text text = new Text("Name");
        addPane.add(text, 0, 0);
        nameTextField = new TextField();
        addPane.add(nameTextField, 1, 0);
        addNamedPlaceBtn = new Button("Add");
        addPane.add(addNamedPlaceBtn, 0, 2);
        Scene addScene = new Scene(addPane);
        addNamedPlaceStage = new Stage();
        addNamedPlaceStage.setTitle("Add Name for Position");
        addNamedPlaceStage.setScene(addScene);
        addNamedPlaceStage.show();
    }

    public void showBoxAddObjByDescribed() {
        GridPane addPane;
        addPane = new GridPane();
        addPane.setMinSize(400, 200);
        addPane.setPadding(new Insets(10, 10, 10, 10));
        addPane.setVgap(5);
        addPane.setHgap(5);
        addPane.setAlignment(Pos.CENTER);
        Text text1 = new Text("Name");
        addPane.add(text1, 0, 0);
        TextField descriptionTextField = new TextField();
        addPane.add(descriptionTextField, 1, 0);
        Text text2 = new Text("Described");
        addPane.add(text2, 0, 1);
        TextField descriptionTextField1 = new TextField();
        addPane.add(descriptionTextField1, 1, 1);
        addDescribedPlaceBtn = new Button("Add");
        addPane.add(addDescribedPlaceBtn, 0, 2);
        Scene addScene = new Scene(addPane);
        addDescribedPlaceButton = new Stage();
        addDescribedPlaceButton.setTitle("Add Name & Describe for Position");
        addDescribedPlaceButton.setScene(addScene);
        addDescribedPlaceButton.show();
    }

    /**
     * Handles the action of adding a new named place
     */
    public void handleAddNamePlaceButtonClick() {

        addNamedPlaceBtn.setOnAction(event -> {

            //Close the stage
            addNamedPlaceStage.close();

            imageBackground.setOnMouseClicked(e -> {
                if (Functions.checkPosition(newPlaces, (int) e.getX(), (int) e.getY())) {
                    //Create new Named Place
                    Place place = new Place(nameTextField.getText(), Category.fromString(categoryName), (int) e.getX(), (int) e.getY());

                    //Add the place to new ones
                    newPlaces.add(place);

                    //Draw the node
                    putTriangleInMap((int) e.getX(), (int) e.getY(), place);

                    mapPane.getChildren().add(place);

                } else {
                    Functions.showBoxAlert(Alert.AlertType.ERROR, "Error", "Position Error .", "There is another mark in this position .");
                }
            });
        });
    }

    /**
     * Handles adding described place adding when the relevant button is clicked
     */
    public void onClickAddByDescribedBtn() {

        addDescribedPlaceBtn.setOnAction((ActionEvent event) -> {

            //Close the stage
            addDescribedPlaceButton.close();

            imageBackground.setOnMouseClicked(e -> {
                if (newBtnClicked) {
                    if (Functions.checkPosition(newPlaces, (int) e.getX(), (int) e.getY())) {

                        Place place = new DescribedPlace(nameTextField.getText(), Category.fromString(categoryName), (int) e.getX(), (int) e.getY(), "Description");

                        newPlaces.add(place);

                        putTriangleInMap((int) e.getX(), (int) e.getY(), place);

                        mapPane.getChildren().add(place);

                    } else {
                        Functions.showBoxAlert(Alert.AlertType.ERROR, "Error", "Position Error .", "There is another mark in this position .");
                    }
                    newBtnClicked = false;
                }
            });
        });
    }

    public void putTriangleInMap(double posX, double posY, Polygon obj) {

    }

    //************7
    public void onClickHideBtn() {
        hideBtn.setOnAction(event -> {

        });
    }

    //************8
    public void onClickRemoveBtn() {
        removeBtn.setOnAction(event -> {

        });
    }

    //************9
    public void onClickHideCategory() {
        hideCategory.setOnAction(event -> {

        });
    }

    public void hideCategoryByType(Category category) {

    }

    //************10
    public void onSearch() {
    }

    public void onClickCoordinates() {
        coordinatesBtn.setOnAction(event -> {
            if (!"".equals(nameMap)) {
                showBoxCoordinates();
                onClickCancelCoordinates();
                onClickOkCoordinates();
            } else {
                Functions.showBoxAlert(Alert.AlertType.INFORMATION, "!!", "There is no map ", "");
            }
        });
    }

    public void showBoxCoordinates() {
        GridPane coordinatesPane = new GridPane();
        coordinatesPane.setMinSize(250, 150);
        coordinatesPane.setVgap(3);
        coordinatesPane.setHgap(3);
        coordinatesPane.setAlignment(Pos.CENTER);
        Text text1 = new Text("X: ");
        coordinatesPane.add(text1, 0, 0);
        textPosX = new TextField();
        coordinatesPane.add(textPosX, 1, 0);
        Text text2 = new Text("Y: ");
        coordinatesPane.add(text2, 0, 1);
        textPosY = new TextField();
        coordinatesPane.add(textPosY, 1, 1);
        okCoordinates = new Button("OK");
        coordinatesPane.add(okCoordinates, 0, 2);
        cancelCoordinates = new Button("Cancel");
        coordinatesPane.add(cancelCoordinates, 1, 2);
        Scene coordinatesScene = new Scene(coordinatesPane);
        coordinatesStage = new Stage();
        coordinatesStage.setTitle("Input Coordinates");
        coordinatesStage.setScene(coordinatesScene);
        coordinatesStage.show();
    }

    public void onClickCancelCoordinates() {
        cancelCoordinates.setOnAction(event -> {
            coordinatesStage.close();
        });

    }

    public void onClickOkCoordinates() {
        okCoordinates.setOnAction(event -> coordinatesStage.close());
    }

    public Polygon searchOnTriangleByPosition(double posX, double posY) {
        return null;
    }

    /**
     * Initializes all the nodes of the application with a couple of containers
     */
    public void initialize() {
        mapPane = new Pane();
        root = new BorderPane();
        baseScene = new Scene(root, 1100, 900);

        // Menu Items
        loadMapMenuItem = new MenuItem("Load Map");
        loadPlacesMenuItem = new MenuItem("Load Places");
        savePlacesMenuItem = new MenuItem("Save");
        exitProgramMenuItem = new MenuItem("Exit");

        // Menu Bar
        MenuBar menuBar = new MenuBar();

        // Menu
        nameMenu = new Menu("File");
        //
        nameMenu.getItems().addAll(loadMapMenuItem, loadPlacesMenuItem, savePlacesMenuItem, exitProgramMenuItem);
        menuBar.getMenus().add(nameMenu);
        //
        newBtn = new Button("New");
        namedRadioButton = new RadioButton("Named");
        describedRadioButton = new RadioButton("Described");
        //
        //
        ToggleGroup checkBox = new ToggleGroup();
        namedRadioButton.setToggleGroup(checkBox);
        describedRadioButton.setToggleGroup(checkBox);
        VBox newBox = new VBox();
        newBox.setSpacing(7);
        newBox.getChildren().addAll(namedRadioButton, describedRadioButton);
        //
        searchTextField = new TextField();
        searchTextField.setPromptText("Search");
        searchBtn = new Button("Search");
        hideBtn = new Button("Hide");
        removeBtn = new Button("Remove");
        coordinatesBtn = new Button("Coordinates");
        //
        //
        HBox toolBar = new HBox();
        toolBar.setAlignment(Pos.CENTER);
        toolBar.setSpacing(7);
        toolBar.getChildren().addAll(newBtn, newBox, searchTextField, searchBtn, hideBtn, removeBtn, coordinatesBtn);
        toolBar.setPrefHeight(60);
        //
        Label categoryText = new Label("Category");
        categoryText.setAlignment(Pos.CENTER);
        //
        categoryList = new ListView<>();
        categoryList.getItems().addAll("Bus", "Underground", "Train", "");
        categoryList.setPrefHeight(100);
        categoryList.setPrefWidth(200);
        hideCategory = new Button("Hide Category");
        hideCategory.setAlignment(Pos.CENTER);
        VBox categoryBox = new VBox();
        categoryBox.setSpacing(3);
        categoryBox.setAlignment(Pos.CENTER);
        categoryBox.getChildren().addAll(categoryText, categoryList, hideCategory);
        root.setRight(categoryBox);
        //
        VBox boxBar = new VBox();
        boxBar.setSpacing(5);
        boxBar.getChildren().addAll(menuBar, toolBar);
        root.setTop(boxBar);

        newPlaces = new ArrayList<>();
    }

    private void drawPlaces() {

        if (savedPlaces != null && !savedPlaces.isEmpty()) {
            mapPane.getChildren().addAll(savedPlaces);
        }

        if (newPlaces != null && !newPlaces.isEmpty()) {
            mapPane.getChildren().addAll(savedPlaces);
        }

    }

}
