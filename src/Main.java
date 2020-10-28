import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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

    private MenuItem loadMapBtn;
    private MenuItem loadPlacesBtn;
    private MenuItem saveBtn;
    private MenuItem exitBtn;

    private MenuBar menu;
    private Menu nameMenu;
    //
    private Button newBtn;
    private RadioButton named;
    private RadioButton described;
    private VBox newBox;
    //
    private ToggleGroup checkBox;
    //
    private TextField textSearch;
    private Button searchBtn;
    private Button hideBtn;
    private Button removeBtn;
    private Button coordinatesBtn;
    //
    private HBox headBar;
    private BorderPane root;
    //
    private Pane spaceMap;
    //
    private ListView<String> categoryList;
    private Button hideCategory;
    private VBox categoryBox;
    private VBox boxBar;
    //
    private String categoryName = "";
    private ImageView imageBackground;
    //
    private Scene baseScene;
    private String nameMap = "";
    private ImageView imagev;
    //
    private ArrayList<Place> listObj = new ArrayList<>();
    private ArrayList<Polygon> selectionPlaces = new ArrayList<>();
    private ArrayList<Polygon> hideItems = new ArrayList<>();
    //
    private ScrollPane scroller = new ScrollPane();
    private Group groupTriangle = new Group(scroller);
    //
    // Button[] btn = new Button[100];
    //private ArrayList<Button>[] btn2 = new ArrayList[4];
    private ArrayList<Polygon>[] btn2 = new ArrayList[4];
    private boolean selectionMode;
    private DropShadow shadow = new DropShadow(5, Color.BLACK);
    private boolean newBtnClicked;
    //
    private Button addByNameBtn;
    private Button addByDescribedBtn;
    private Stage addByNameStage;
    private Stage addByDescribedStage;
    private TextField textFieldNameAddByName;
    private TextField textFieldNameAddByDescribed;
    private TextField textFieldDescribedAddByDescribed;
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

        loadMapBtn.setOnAction(event -> {
            if ("".equals(nameMap)) {
                loadMap(primaryStage);
                if (!"".equals(nameMap)) {
                    nameMenu.getItems().clear();
                    nameMenu.getItems().addAll(loadPlacesBtn, saveBtn, exitBtn);
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
            spaceMap.setPrefHeight(850);
            spaceMap.setPrefWidth(900);
            root.setCenter(spaceMap);
            spaceMap.getChildren().add(imageBackground);
            spaceMap.setStyle("-fx-padding:7px;");
            nameMap = f.getName();
        } else {
            Functions.showBoxAlert(Alert.AlertType.ERROR, "Error", "Wrong file ", "The map is a common image ï¬پle (for example .png, .jpg or .bmp).");
        }

    }

    //************2
    public void onSelectLoadPlaces(Stage primaryStage) {

        loadPlacesBtn.setOnAction(event -> {
            if (!"".equals(nameMap)) {
                try {
                    List<Place> l = FileHandler.loadFromFile(primaryStage);

                    if (!l.isEmpty()) {
                        for (Place place : l) {
                            listObj.add(place);
                            categoryName = place.getCategory().getName();

                            putTriangleInMap(place.getPosition().getPosX(), place.getPosition().getPosY(), place);
                        }
                        nameMenu.getItems().clear();
                        nameMenu.getItems().addAll(saveBtn, exitBtn);
                    } else {
                        Functions.showBoxAlert(Alert.AlertType.ERROR, "Error", "This File is Empty", "Choose another file");
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
        saveBtn.setOnAction(event -> {
            try {
                Functions.saveData(nameMap, listObj);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    //************4
    public void onSelectExit() {
        System.out.println("exc");
        exitBtn.setOnAction(event -> {
            if (!listObj.isEmpty()) {
                System.out.println("savings");
                Functions.showSaveBox(nameMap, listObj);
            }
            Platform.exit();
        });
    }

    //************5
    public void onSelectItemFromCategory() {
        categoryList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            categoryName = newValue;
        });
    }

    //************6
    public void onClickNewBtn() {
        newBtn.setOnAction(event -> {
            if (!"".equals(nameMap)) {
                newBtnClicked = true;
                if (named.isSelected()) {
                    newObjByNamed();
                } else if (described.isSelected()) {
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
        onClickAddByNameBtn();
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
        textFieldNameAddByName = new TextField();
        addPane.add(textFieldNameAddByName, 1, 0);
        addByNameBtn = new Button("Add");
        addPane.add(addByNameBtn, 0, 2);
        Scene addScene = new Scene(addPane);
        addByNameStage = new Stage();
        addByNameStage.setTitle("Add Name for Position");
        addByNameStage.setScene(addScene);
        addByNameStage.show();
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
        textFieldNameAddByDescribed = new TextField();
        addPane.add(textFieldNameAddByDescribed, 1, 0);
        Text text2 = new Text("Described");
        addPane.add(text2, 0, 1);
        textFieldDescribedAddByDescribed = new TextField();
        addPane.add(textFieldDescribedAddByDescribed, 1, 1);
        addByDescribedBtn = new Button("Add");
        addPane.add(addByDescribedBtn, 0, 2);
        Scene addScene = new Scene(addPane);
        addByDescribedStage = new Stage();
        addByDescribedStage.setTitle("Add Name & Describe for Position");
        addByDescribedStage.setScene(addScene);
        addByDescribedStage.show();
    }

    public void onClickAddByNameBtn() {
        addByNameBtn.setOnAction(event -> {
            addByNameStage.close();
            imageBackground.setOnMouseClicked(event1 -> {
                if (newBtnClicked) {
                    if (Functions.checkPosition(listObj, (int) event1.getX(), (int) event1.getY())) {
                        Place obj = null;
                        Category cate;
                        if (categoryName.equals("")) {
                            cate = new Category("None");

                        } else {
                            cate = new Category(categoryName);
                        }
                        obj = new Place(textFieldNameAddByName.getText(), cate, (int) event1.getX(), (int) event1.getY());
                        listObj = Functions.addObject(listObj, "named", obj);
                        putTriangleInMap((int) event1.getX(), (int) event1.getY(), obj);
                        spaceMap.getChildren().add(obj);
                        obj.print();

                    } else {
                        Functions.showBoxAlert(Alert.AlertType.ERROR, "Error", "Position Error .", "There is another mark in this position .");
                    }
                    newBtnClicked = false;
                }
            });
        });
    }

    public void onClickAddByDescribedBtn() {
        addByDescribedBtn.setOnAction((ActionEvent event) -> {
            addByDescribedStage.close();
            imageBackground.setOnMouseClicked(event1 -> {
                if (newBtnClicked) {
                    if (Functions.checkPosition(listObj, (int) event1.getX(), (int) event1.getY())) {
                        Place obj = null;
                        Category cate;
                        if (categoryName.equals("")) {
                            cate = new Category("None");

                        } else {
                            cate = new Category(categoryName);
                        }
                        obj = new DescribedPlace(textFieldNameAddByName.getText(), new Category("described"), (int) event1.getX(), (int) event1.getY(), "Description");
                        listObj = Functions.addObject(listObj, "described", obj);
                        putTriangleInMap((int) event1.getX(), (int) event1.getY(), obj);
                        spaceMap.getChildren().add(obj);
                        obj.print();

                    } else {
                        Functions.showBoxAlert(Alert.AlertType.ERROR, "Error", "Position Error .", "There is another mark in this position .");
                    }
                    newBtnClicked = false;
                }
            });
        });
    }

    public void putTriangleInMap(double posX, double posY, Polygon obj) {
        newTriangle(posX, posY, obj);
        setActionForNewTriangle();
    }


    public void newTriangle(double posX, double posY, Polygon obj) {
        switch (categoryName) {
            case "Bus":
                addTriangleToCategoryList(0, posX, posY, obj);

                break;
            case "Underground":
                addTriangleToCategoryList(1, posX, posY, obj);
                break;
            case "Train":
                addTriangleToCategoryList(2, posX, posY, obj);
                break;
            default:
                addTriangleToCategoryList(3, posX, posY, obj);
                break;
        }
    }

    public void addTriangleToCategoryList(int typeTriangle, double posX, double posY, Polygon obj) {
        btn2[typeTriangle].add(obj);
        //btn2[typeTriangle].get(btn2[typeTriangle].size() - 1).setTranslateX(posX);
        //btn2[typeTriangle].get(btn2[typeTriangle].size() - 1).setTranslateY(posY);
        //btn2[typeTriangle].get(btn2[typeTriangle].size() - 1).setPrefSize(20, 20);
        //btn2[typeTriangle].get(btn2[typeTriangle].size() - 1).setBackground(Background.EMPTY);
        groupTriangle.getChildren().add(btn2[typeTriangle].get(btn2[typeTriangle].size() - 1));
    }

    public void setActionForNewTriangle() {
        for (int i = 0; i < btn2.length; i++) {
            for (int j = 0; j < btn2[i].size(); j++) {
                int d1 = i;
                int d2 = j;

                btn2[i].get(j).setOnMouseClicked(event -> {


                    Place p = Functions.searchOnObjByPosition(listObj, btn2[d1].get(d2).getPoints().get(0), btn2[d1].get(d2).getPoints().get(1));
                    if (event.getButton() == MouseButton.PRIMARY) {
                        if (!selectionMode) {
                            selectItem(p, d1, d2);
                            selectionMode = true;
                        } else {
                            if (!selectionPlaces.contains(btn2[d1].get(d2))) {
                                selectItem(p, d1, d2);
                            } else {
                                deSelectItem(p, d1, d2);
                                if (selectionPlaces.isEmpty()) {
                                    selectionMode = false;
                                }
                            }
                        }
                    }
                    if (event.getButton() == MouseButton.SECONDARY) {
                        if (selectionMode) {
                            Functions.showBoxAlert(Alert.AlertType.ERROR, "Error ", "There is more than one item selected .", "Cannot display Info for more than one item .");
                        } else {
                            String s = "Name:" + p.getName() + "[" + p.getPosition().getPosX() + "," + p.getPosition().getPosY() + "]";
                            if (p.getType().equals("Named")) {
                                Functions.showBoxAlert(Alert.AlertType.INFORMATION, "Info ", s, "");
                            } else {
                                if (p instanceof DescribedPlace) {
                                    String s1 = "Description:" + ((DescribedPlace) p).getDescription();
                                    Functions.showBoxAlert(Alert.AlertType.INFORMATION, "Info ", s + "\n" + s1, "");
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    public void selectItem(Place p, int d1, int d2) {
        btn2[d1].get(d2).setEffect(shadow);
        selectionPlaces.add(btn2[d1].get(d2));

        p.setSelect(true);
    }

    public void deSelectItem(Place p, int d1, int d2) {
        btn2[d1].get(d2).setEffect(null);
        selectionPlaces.remove(btn2[d1].get(d2));
        p.setSelect(false);
    }

    //************7
    public void onClickHideBtn() {
        hideBtn.setOnAction(event -> {
            if (!"".equals(nameMap)) {
                if (selectionMode) {
                    selectionMode = false;

                    for (int i = 0; i < selectionPlaces.size(); i++) {
                        Place p = Functions.searchOnObjByPosition(listObj, selectionPlaces.get(i).getPoints().get(0), selectionPlaces.get(i).getPoints().get(1));
                        p.setHidden(true);
                        p.setSelect(false);
                        hideItems.add(selectionPlaces.get(i));
                    }
                    selectionPlaces.clear();
                    for (int i = 0; i < hideItems.size(); i++) {
                        hideItems.get(i).setEffect(null);
                        hideItems.get(i).setVisible(false);
                    }
                } else {
                    if (!hideItems.isEmpty()) {
                        for (int j = 0; j < hideItems.size(); j++) {

                            Place p = Functions.searchOnObjByPosition(listObj, hideItems.get(j).getPoints().get(0), hideItems.get(j).getPoints().get(1));
                            p.setHidden(false);
                            hideItems.get(j).setVisible(true);
                        }
                        hideItems.clear();
                    } else {
                        Functions.showBoxAlert(Alert.AlertType.INFORMATION, "!!", "There is no data to show", "");
                    }
                }
            } else {
                Functions.showBoxAlert(Alert.AlertType.INFORMATION, "!!", "There is no map ", "");
            }

        });
    }

    //************8
    public void onClickRemoveBtn() {
        removeBtn.setOnAction(event -> {
            if (!"".equals(nameMap)) {
                if (selectionMode) {
                    selectionMode = false;
                    for (int i = 0; i < selectionPlaces.size(); i++) {
                        for (int j = 0; j < btn2.length; j++) {
                            if (btn2[j].contains(selectionPlaces.get(i))) {
                                selectionPlaces.get(i).setVisible(false);
                                btn2[j].remove(selectionPlaces.get(i));
                                Place p = Functions.searchOnObjByPosition(listObj, (int) selectionPlaces.get(i).getTranslateX(), (int) selectionPlaces.get(i).getTranslateY());
                                listObj.remove(p);
                                setActionForNewTriangle();
                            }
                        }
                    }
                    selectionPlaces.clear();
                } else {
                    Functions.showBoxAlert(Alert.AlertType.INFORMATION, "!!", "There is no any item selected to remove  ", "");
                }
            } else {
                Functions.showBoxAlert(Alert.AlertType.INFORMATION, "!!", "There is no map ", "");
            }
        });
    }

    //************9
    public void onClickHideCategory() {
        hideCategory.setOnAction(event -> {
            switch (categoryName) {
                case "Bus":
                    hideCategoryByType(0);
                    break;
                case "Underground":
                    hideCategoryByType(1);
                    break;
                case "Train":
                    hideCategoryByType(2);
                    break;
                default:
                    hideCategoryByType(3);
                    break;
            }
        });
    }

    public void hideCategoryByType(int typeCategory) {
        if (!btn2[typeCategory].isEmpty()) {
            if (hiddenCategory[typeCategory]) {
                for (int i = 0; i < btn2[typeCategory].size(); i++) {
                    //Place p = SearchOnObjByPosition((int) btn2[typeCategory].get(i).getTranslateX(), (int) btn2[typeCategory].get(i).getTranslateX());
                    //p.setHiden(false);
                    btn2[typeCategory].get(i).setVisible(true);
                    hideItems.remove(btn2[typeCategory].get(i));
                }
                hiddenCategory[typeCategory] = false;
            } else {
                for (int i = 0; i < btn2[typeCategory].size(); i++) {
                    //Place p = SearchOnObjByPosition((int) btn2[typeCategory].get(i).getTranslateX(), (int) btn2[typeCategory].get(i).getTranslateY());
                    //p.setHiden(true);
                    btn2[typeCategory].get(i).setVisible(false);
                    hideItems.add(btn2[typeCategory].get(i));
                }
                hiddenCategory[typeCategory] = true;
            }
        } else {
            Functions.showBoxAlert(Alert.AlertType.INFORMATION, "!!", "There are no any mark in this category ", "");
        }
    }

    //************10
    public void onSearch() {
        searchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean noResulte = true;
                if (!"".equals(nameMap)) {
                    if (selectionMode) {
                        for (int i = 0; i < selectionPlaces.size(); i++) {
                            Place p = Functions.searchOnObjByPosition(listObj, selectionPlaces.get(i).getPoints().get(0), selectionPlaces.get(i).getPoints().get(1));
                            p.setSelect(false);
                            p.setEffect(null);

                        }
                        selectionPlaces.clear();
                        selectionMode = false;
                    }
                    if (!listObj.isEmpty()) {
                        for (int i = 0; i < listObj.size(); i++) {
                            if (listObj.get(i).getName().equals(textSearch.getText())) {
                                noResulte = false;
                                Polygon btn = searchOnTriangleByPosition(listObj.get(i).getPosition().getPosX(), listObj.get(i).getPosition().getPosY());
                                Place p = Functions.searchOnObjByPosition(listObj, listObj.get(i).getPosition().getPosX(), listObj.get(i).getPosition().getPosY());
                                if (hideItems.contains(btn)) {
                                    p.setHidden(false);
                                    hideItems.remove(btn);
                                    btn.setVisible(true);
                                    //
                                    selectionPlaces.add(btn);
                                    p.setSelect(true);
                                    selectionMode = true;
                                    p.setEffect(shadow);
                                } else {
                                    selectionPlaces.add(btn);
                                    p.setSelect(true);
                                    selectionMode = true;
                                    p.setEffect(shadow);
                                }
                            }
                        }
                        if (noResulte) {
                            Functions.showBoxAlert(Alert.AlertType.ERROR, "Not exist", "There is no any results", "It should be checked that the name enterd are right");
                        }
                    } else {
                        Functions.showBoxAlert(Alert.AlertType.ERROR, "Not exist", "There is no any results", "It should be checked that the name enterd are right");
                    }
                } else {
                    Functions.showBoxAlert(Alert.AlertType.INFORMATION, "!!", "There is no map ", "");
                }
            }
        });
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
        okCoordinates.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (selectionMode) {
                    for (int i = 0; i < selectionPlaces.size(); i++) {
                        Functions.searchOnObjByPosition(listObj, (int) selectionPlaces.get(i).getTranslateX(), (int) selectionPlaces.get(i).getTranslateY()).setSelect(false);
                        selectionPlaces.get(i).setEffect(null);
                    }
                    selectionPlaces.clear();
                    selectionMode = false;
                }
                Polygon btn = searchOnTriangleByPosition(Integer.valueOf(textPosX.getText()), Integer.valueOf(textPosY.getText()));
                if (btn == null) {
                    Functions.showBoxAlert(Alert.AlertType.ERROR, "Not exist", "There is no any results", "It should be checked that the values enterd are right");
                } else {
                    Place p = Functions.searchOnObjByPosition(listObj, Integer.valueOf(textPosX.getText()), Integer.valueOf(textPosY.getText()));
                    if (hideItems.contains(btn)) {
                        p.setHidden(false);
                        p.setSelect(true);
                        btn.setVisible(true);
                        btn.setEffect(shadow);
                        selectionMode = true;
                        selectionPlaces.add(btn);
                        hideItems.remove(btn);
                    } else {
                        p.setSelect(true);
                        btn.setEffect(shadow);
                        selectionMode = true;
                        selectionPlaces.add(btn);
                    }
                }
                coordinatesStage.close();
            }
        });
    }

    public Polygon searchOnTriangleByPosition(double posX, double posY) {
        for (int i = 0; i < btn2.length; i++) {
            for (int j = 0; j < btn2[i].size(); j++) {
                if (posX == btn2[i].get(j).getTranslateX() && posY == btn2[i].get(j).getTranslateY()) {
                    return btn2[i].get(j);
                }
            }
        }
        return null;
    }

    /**
     * Initializes all the nodes of the application with a couple of containers
     */
    public void initialize() {
        spaceMap = new Pane();
        root = new BorderPane();
        baseScene = new Scene(root, 1100, 900);

        // Menu Items
        loadMapBtn = new MenuItem("Load Map");
        loadPlacesBtn = new MenuItem("Load Places");
        saveBtn = new MenuItem("Save");
        exitBtn = new MenuItem("Exit");

        // Menu Bar
        menu = new MenuBar();

        // Menu
        nameMenu = new Menu("File");
        //
        nameMenu.getItems().addAll(loadMapBtn, loadPlacesBtn, saveBtn, exitBtn);
        menu.getMenus().add(nameMenu);
        //
        newBtn = new Button("New");
        named = new RadioButton("Named");
        described = new RadioButton("Described");
        //
        checkBox = new ToggleGroup();
        named.setToggleGroup(checkBox);
        described.setToggleGroup(checkBox);
        newBox = new VBox();
        newBox.setSpacing(7);
        newBox.getChildren().addAll(named, described);
        //
        textSearch = new TextField();
        textSearch.setPromptText("Search");
        searchBtn = new Button("Search");
        hideBtn = new Button("Hide");
        removeBtn = new Button("Remove");
        coordinatesBtn = new Button("Coordinates");
        //
        headBar = new HBox();
        headBar.setAlignment(Pos.CENTER);
        headBar.setSpacing(7);
        headBar.getChildren().addAll(newBtn, newBox, textSearch, searchBtn, hideBtn, removeBtn, coordinatesBtn);
        headBar.setPrefHeight(60);
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
        categoryBox = new VBox();
        categoryBox.setSpacing(3);
        categoryBox.setAlignment(Pos.CENTER);
        categoryBox.getChildren().addAll(categoryText, categoryList, hideCategory);
        root.setRight(categoryBox);
        //
        boxBar = new VBox();
        boxBar.setSpacing(5);
        boxBar.getChildren().addAll(menu, headBar);
        root.setTop(boxBar);
        //
        if (btn2[0] == null) {
            btn2[0] = new ArrayList<>();
        }
        if (btn2[1] == null) {
            btn2[1] = new ArrayList<>();
        }
        if (btn2[2] == null) {
            btn2[2] = new ArrayList<>();
        }
        if (btn2[3] == null) {
            btn2[3] = new ArrayList<>();
        }
    }

}
