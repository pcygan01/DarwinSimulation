package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class App extends Application implements IMapChangeObserver{
    private final VBox vbox = new VBox();
    static GridPane gridPane = new GridPane();
    private int heightWorldParameter; // wysokość mapy
    private int widthWorldParameter; // szerokość mapy

    // rośliny
    private int initialNumberOfPlantsParameter; // startowa liczba roślin
    private int plantEnergyParameter; // energia zapewniana przez zjedzenie rośliny
    private int numberOfNewPlantsParameter; // liczba roślin wyrastająca każdego dnia

    // zwierzęta
    private int initialNumberOfAnimalsParameter; // startowa liczba zwierzaków
    private int startAnimalEnergyParameter; // startowa energia zwierzaków
    private int minEnergyToReproductionParameter; // energia konieczna, by uznać zwierzaka za najedzonego (i gotowego do rozmnażania)
    private int energyUsedToReproductionParameter; // energia rodziców zużywana by stworzyć potomka
    private int minNumberOfMutationsParameter; // minimalna liczba mutacji u potomków
    private int maxNumberOfMutationsParameter; // maksymalna liczba mutacji u potomków
    private int DNAlengthParameter; // długość genomu zwierzaków

    // warianty
    private boolean mapVariantParameter; // false - kula ziemska, true - piekielny portal
    private boolean grassVariantParameter; // false - zalesione równiki, true - toksyczne trupy
    private boolean mutationVariantParameter; // false - pełna losowość, true - lekka korekta
    private boolean behaviourVariantParameter; // false - pełna predystancja, true - nieco szaleństwa
/*
    private final StatisticsGraph animals = new StatisticsGraph("Animals");
    private final StatisticsGraph plants = new StatisticsGraph("Plants");
    private final StatisticsGraph emptyFields = new StatisticsGraph("Empty fields");
    private final StatisticsGraph averageEnergy = new StatisticsGraph("Average energy level");
    private final StatisticsGraph averageLife = new StatisticsGraph("Average lifespan of animals");*/



    public static void main(String[] args) {
        Application.launch(App.class);
    }

    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(vbox, 950, 950, Color.web("#81c483"));
        primaryStage.setScene(scene);
        primaryStage.setTitle("Darwin Simulation");
        primaryStage.show();
    }

    public void init() throws Exception {
        Label header = new Label("Welcome in Darwin Simulation");
        header.setFont(new Font("Arial", 40));
        header.setTextFill(Color.WHITE);

        Button startButton = new Button("Let's start");
        startButton.setFont(new Font("Arial", 40));
        startButton.setTextFill(Color.web("#81c483"));
        startButton.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        startButton.setOnAction((event -> {
            showSecondScene();
        }));

        Image image = new Image("world.png");
        ImageView imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(300);
        imageView.setImage(image);

        VBox simulationStart = new VBox(header, imageView, startButton);
        simulationStart.setSpacing(100);
        simulationStart.setAlignment(Pos.CENTER);
        simulationStart.setBackground(new Background(new BackgroundFill(Color.web("#81c483"), CornerRadii.EMPTY, Insets.EMPTY)));
        simulationStart.setPrefWidth(950);
        simulationStart.setPrefHeight(950);

        vbox.getChildren().clear();
        vbox.getChildren().add(simulationStart);
        vbox.setAlignment(Pos.CENTER);
    }

    public void showSecondScene() {
        Button mySettingsButton = new Button("My settings");
        mySettingsButton.setFont(new Font("Arial", 30));
        mySettingsButton.setOnAction((event -> showMySettings()));

        Button predefinedButton = new Button("Predefined settings");
        predefinedButton.setFont(new Font("Arial", 30));
        predefinedButton.setOnAction((event -> showPredefinedSettings()));

        HBox buttonsBox = new HBox(predefinedButton, mySettingsButton);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setSpacing(100);
        buttonsBox.setPrefHeight(950);
        buttonsBox.setPrefWidth(950);
        buttonsBox.setBackground(new Background(new BackgroundFill(Color.web("#81c483"), CornerRadii.EMPTY, Insets.EMPTY)));

        vbox.getChildren().clear();
        vbox.getChildren().add(buttonsBox);
        vbox.setAlignment(Pos.CENTER);

    }

    public void showMySettings() {
        TextField heightWorld = new TextField();
        TextField widthWorld = new TextField();
        TextField initialNumberOfPlants = new TextField();
        TextField plantEnergy = new TextField();
        TextField numberOfNewPlants = new TextField();
        TextField initialNumberOfAnimals = new TextField();
        TextField startAnimalEnergy = new TextField();
        TextField minEnergyToReproduction = new TextField();
        TextField energyUsedToReproduction = new TextField();
        TextField minNumberOfMutations = new TextField();
        TextField maxNumberOfMutations = new TextField();
        TextField DNAlength = new TextField();

        ChoiceBox<String> mapVariant = new ChoiceBox<>();
        mapVariant.getItems().addAll("Globe", "Hell Portal");
        mapVariant.setPrefWidth(170);

        ChoiceBox<String> grassVariant = new ChoiceBox<>();
        grassVariant.getItems().addAll("Green Equators", "Toxic Corpses");
        grassVariant.setPrefWidth(170);

        ChoiceBox<String> mutationVariant = new ChoiceBox<>();
        mutationVariant.getItems().addAll("Randomness", "Correction");
        mutationVariant.setPrefWidth(170);

        ChoiceBox<String> behaviourVariant = new ChoiceBox<>();
        behaviourVariant.getItems().addAll("In order", "A bit of madness");
        behaviourVariant.setPrefWidth(170);

        Label heightWorldLabel = new Label("Map height: ");
        heightWorldLabel.setFont(new Font("Arial", 20));
        Label widthWorldLabel = new Label("Map width: ");
        widthWorldLabel.setFont(new Font("Arial", 20));
        Label initialNumberOfPlantsLabel = new Label("Initial number of plants: ");
        initialNumberOfPlantsLabel.setFont(new Font("Arial", 20));
        Label plantEnergyLabel = new Label("Grass energy profit: ");
        plantEnergyLabel.setFont(new Font("Arial", 20));
        Label numberOfNewPlantsLabel = new Label("Number of new plants each day: ");
        numberOfNewPlantsLabel.setFont(new Font("Arial", 20));
        Label initialNumberOfAnimalsLabel = new Label("Initial number of animals: ");
        initialNumberOfAnimalsLabel.setFont(new Font("Arial", 20));
        Label startAnimalEnergyLabel = new Label("Animal's initial energy: ");
        startAnimalEnergyLabel.setFont(new Font("Arial", 20));
        Label minEnergyToReproductionLabel = new Label("Energy required to reproduction: ");
        minEnergyToReproductionLabel.setFont(new Font("Arial", 20));
        Label energyUsedToReproductionLabel = new Label("Energy used for reproduction: ");
        energyUsedToReproductionLabel.setFont(new Font("Arial", 20));
        Label minNumberOfMutationsLabel = new Label("Minimum number of mutations: ");
        minNumberOfMutationsLabel.setFont(new Font("Arial", 20));
        Label maxNumberOfMutationsLabel = new Label("Maximum number of mutations: ");
        maxNumberOfMutationsLabel.setFont(new Font("Arial", 20));
        Label DNAlengthLabel = new Label("DNA length: ");
        DNAlengthLabel.setFont(new Font("Arial", 20));
        Label mapVariantLabel = new Label("Map variant: ");
        mapVariantLabel.setFont(new Font("Arial", 20));
        Label grassVariantLabel = new Label("Plants growth variant: ");
        grassVariantLabel.setFont(new Font("Arial", 20));
        Label mutationVariantLabel = new Label("Mutation variant: ");
        mutationVariantLabel.setFont(new Font("Arial", 20));
        Label behaviourVariantLabel = new Label("Behaviour variant: ");
        behaviourVariantLabel.setFont(new Font("Arial", 20));
        Label mapSettingsLabel = new Label("Map settings");
        mapSettingsLabel.setFont(new Font("Arial", 30));
        Label plantsSettingsLabel = new Label("Plants settings");
        plantsSettingsLabel.setFont(new Font("Arial", 30));
        Label animalsSettingsLabel = new Label("Animals settings");
        animalsSettingsLabel.setFont(new Font("Arial", 30));
        Label variantsSettingsLabel = new Label("Simulation variants");
        variantsSettingsLabel.setFont(new Font("Arial", 30));


        VBox mapLabelBox = new VBox(
                heightWorldLabel,
                widthWorldLabel);
        mapLabelBox.setSpacing(12);

        VBox plantsLabelBox = new VBox(
                initialNumberOfPlantsLabel,
                plantEnergyLabel,
                numberOfNewPlantsLabel);
        plantsLabelBox.setSpacing(12);

        VBox animalsLabelBox = new VBox(
                initialNumberOfAnimalsLabel,
                startAnimalEnergyLabel,
                minEnergyToReproductionLabel,
                energyUsedToReproductionLabel,
                minNumberOfMutationsLabel,
                maxNumberOfMutationsLabel,
                DNAlengthLabel);
        animalsLabelBox.setSpacing(12);

        VBox variantsLabelBox = new VBox(
                mapVariantLabel,
                grassVariantLabel,
                mutationVariantLabel,
                behaviourVariantLabel);
        variantsLabelBox.setSpacing(12);

        VBox mapTextBox = new VBox(heightWorld,
                widthWorld);
        mapTextBox.setSpacing(10);

        VBox plantsTextBox = new VBox(
                initialNumberOfPlants,
                plantEnergy,
                numberOfNewPlants);
        plantsTextBox.setSpacing(10);

        VBox animalsTextBox = new VBox(
                initialNumberOfAnimals,
                startAnimalEnergy,
                minEnergyToReproduction,
                energyUsedToReproduction,
                minNumberOfMutations,
                maxNumberOfMutations,
                DNAlength);
        animalsTextBox.setSpacing(10);

        VBox variantsTextBox = new VBox(
                mapVariant,
                grassVariant,
                mutationVariant,
                behaviourVariant);
        variantsTextBox.setSpacing(10);

        Button predefinedButton = new Button("Predefined settings");
        predefinedButton.setFont(new Font("Arial", 20));
        predefinedButton.setOnAction((event -> showPredefinedSettings()));

        Button startButton = new Button("Start simulation");
        startButton.setFont(new Font("Arial", 20));
        startButton.setOnAction((event -> {
            this.heightWorldParameter = Integer.parseInt(heightWorld.getText());
            this.widthWorldParameter = Integer.parseInt(widthWorld.getText());
            this.initialNumberOfPlantsParameter = Integer.parseInt(initialNumberOfPlants.getText());
            this.plantEnergyParameter = Integer.parseInt(plantEnergy.getText());
            this.numberOfNewPlantsParameter = Integer.parseInt(numberOfNewPlants.getText());
            this.initialNumberOfAnimalsParameter = Integer.parseInt(initialNumberOfAnimals.getText());
            this.startAnimalEnergyParameter = Integer.parseInt(startAnimalEnergy.getText());
            this.minEnergyToReproductionParameter = Integer.parseInt(minEnergyToReproduction.getText());
            this.energyUsedToReproductionParameter = Integer.parseInt(energyUsedToReproduction.getText());
            this.minNumberOfMutationsParameter = Integer.parseInt(minNumberOfMutations.getText());
            this.maxNumberOfMutationsParameter = Integer.parseInt(maxNumberOfMutations.getText());
            this.DNAlengthParameter = Integer.parseInt(DNAlength.getText());
            if (mapVariant.getValue() == "Globe")
                this.mapVariantParameter = false;
            else
                this.mapVariantParameter = true;
            if (grassVariant.getValue() == "Green Equators")
                this.grassVariantParameter = false;
            else
                this.grassVariantParameter = true;
            if (mutationVariant.getValue() == "Randomness")
                this.mutationVariantParameter = false;
            else
                this.mutationVariantParameter = true;
            if (behaviourVariant.getValue() == "In order")
                this.behaviourVariantParameter = false;
            else
                this.behaviourVariantParameter = true;

            InputParameters inputParameters = new InputParameters(heightWorldParameter, widthWorldParameter, initialNumberOfPlantsParameter, plantEnergyParameter, numberOfNewPlantsParameter, initialNumberOfAnimalsParameter, startAnimalEnergyParameter, minEnergyToReproductionParameter, energyUsedToReproductionParameter, minNumberOfMutationsParameter, maxNumberOfMutationsParameter, DNAlengthParameter, mapVariantParameter, grassVariantParameter, mutationVariantParameter, behaviourVariantParameter);
            try{
                inputParameters.checkConditions();
            }catch(Exception e){
                showException(e);
            }
            try {
                showSimulationScene(inputParameters);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }));

        HBox buttonsBox = new HBox(predefinedButton, startButton);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setSpacing(100);


        GridPane inputGrid = new GridPane();
        inputGrid.setAlignment(Pos.CENTER);
        inputGrid.setBackground(new Background(new BackgroundFill(Color.web("#81c483"), CornerRadii.EMPTY, Insets.EMPTY)));
        inputGrid.setPrefHeight(950);
        inputGrid.setPrefWidth(950);
        inputGrid.setVgap(25);
        inputGrid.setHgap(50);

        inputGrid.add(mapSettingsLabel,0,0);
        inputGrid.add(mapLabelBox, 0,1);
        inputGrid.add(mapTextBox,1,1);
        inputGrid.add(plantsSettingsLabel, 0, 2);
        inputGrid.add(plantsLabelBox, 0, 3);
        inputGrid.add(plantsTextBox, 1, 3);
        inputGrid.add(animalsSettingsLabel, 0, 4);
        inputGrid.add(animalsLabelBox, 0, 5);
        inputGrid.add(animalsTextBox, 1, 5);
        inputGrid.add(variantsSettingsLabel,0,6);
        inputGrid.add(variantsLabelBox, 0, 7);
        inputGrid.add(variantsTextBox, 1, 7);
        inputGrid.add(predefinedButton, 0, 8);
        inputGrid.add(startButton, 1, 8);

        vbox.getChildren().clear();
        vbox.getChildren().add(inputGrid);
        vbox.setAlignment(Pos.CENTER);
    }

    public void showPredefinedSettings(){
        ChoiceBox<String> preSettings = new ChoiceBox<>();
        preSettings.getItems().addAll("option 1", "option 2");
        preSettings.setPrefWidth(300);

        Button mySettings = new Button("My settings");
        mySettings.setFont(new Font("Arial", 30));
        mySettings.setOnAction((event -> showMySettings()));

        Button startButton = new Button("Start simulation");
        startButton.setFont(new Font("Arial", 30));
        startButton.setOnAction((event -> {
            Scanner scan;
            String[] tab = new String[16];
            if (preSettings.getValue() == "option 1"){
                try {
                    scan = new Scanner(new File("src/main/resources/ps1.txt"));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                int i = 0;
                while (scan.hasNextLine()) {
                    String[] param= scan.nextLine().split(" ");
                    tab[i] = param[1];
                    i += 1;
                }
            }
            else if(preSettings.getValue() == "option 2"){
                try {
                    scan = new Scanner(new File("src/main/resources/ps2.txt"));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                int i = 0;
                while (scan.hasNextLine()) {
                    String[] param = scan.nextLine().split(" ");
                    tab[i] = param[1];
                    i += 1;
                }
            }
            InputParameters inputParameters = new InputParameters(Integer.parseInt(tab[0]), Integer.parseInt(tab[1]), Integer.parseInt(tab[2]), Integer.parseInt(tab[3]), Integer.parseInt(tab[4]), Integer.parseInt(tab[5]), Integer.parseInt(tab[6]), Integer.parseInt(tab[7]), Integer.parseInt(tab[8]), Integer.parseInt(tab[9]), Integer.parseInt(tab[10]), Integer.parseInt(tab[11]), Boolean.parseBoolean(tab[12]), Boolean.parseBoolean(tab[13]), Boolean.parseBoolean(tab[14]), Boolean.parseBoolean(tab[15]));
            try {
                showSimulationScene(inputParameters);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }));


        HBox buttonsBox = new HBox(mySettings, startButton);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setSpacing(100);

        VBox inputBox = new VBox(
                preSettings,
                buttonsBox);
        inputBox.setBackground(new Background(new BackgroundFill(Color.web("#81c483"), CornerRadii.EMPTY, Insets.EMPTY)));
        inputBox.setPrefHeight(950);
        inputBox.setPrefWidth(950);

        inputBox.setAlignment(Pos.CENTER);
        inputBox.setSpacing(100);
        vbox.getChildren().clear();
        vbox.getChildren().add(inputBox);
        vbox.setAlignment(Pos.CENTER);
    }

    public void showException(Exception e) {
        e.printStackTrace();
        Label exceptionText = new Label(e.toString());
        VBox exceptionBox = new VBox(exceptionText);
        exceptionBox.setPrefHeight(100);
        exceptionBox.setPrefWidth(500);
        exceptionBox.setAlignment(Pos.CENTER);
        Stage stage = new Stage();
        stage.setTitle("Wrong value");
        stage.setScene(new Scene(exceptionBox, 500, 100));
        stage.show();
    }

    public void showSimulationScene(InputParameters inputParameters) throws FileNotFoundException {
        Stage stage = new Stage();

        GridPane grid = new GridPane();
        GridPane worldMap = new GridPane();
        worldMap.setPrefWidth(700);
        worldMap.setPrefHeight(700);
        grid.setBackground(new Background(new BackgroundFill(Color.web("#81c483"), CornerRadii.EMPTY, Insets.EMPTY)));
        GrassField map = new GrassField(inputParameters.initialNumberOfPlants, inputParameters.heightWorld, inputParameters.widthWorld, inputParameters.mapVariant, inputParameters.plantEnergy, inputParameters.grassVariant, inputParameters.numberOfNewPlants, worldMap);
        SimulationEngine engine = new SimulationEngine(this, map,300, inputParameters.initialNumberOfAnimals, inputParameters.startAnimalEnergy, inputParameters.minEnergyToReproduction, inputParameters.energyUsedToReproduction,inputParameters.minNumberOfMutations, inputParameters.maxNumberOfMutations, inputParameters.DNAlength, inputParameters.mutationVariant, inputParameters.behaviourVariant);
        createMap(map, worldMap);

        Thread engineThread  = new Thread(engine);
        engineThread.start();

        grid.getChildren().clear();
        grid.getChildren().add(worldMap);
        Scene scene = new Scene(grid,950, 950, Color.web("#81c483"));
        stage.setScene(scene);
        stage.setTitle("Darwin Simulation");
        stage.show();


        //animals.makeGraph();
        //plants.makeGraph();
        //emptyFields.makeGraph();
        //averageEnergy.makeGraph();
        //averageLife.makeGraph();
        //VBox statsBox = createStatistics(map, engine);



    }

    public static void createMap(GrassField map, GridPane grid) throws FileNotFoundException {
        GridPane gridPane = new GridPane();
        gridPane.getChildren().clear();
        gridPane.setBackground(new Background(new BackgroundFill(Color.web("#CAE7D2"), CornerRadii.EMPTY, Insets.EMPTY)));
        int numberOfRows = map.getUpperRight().y+1;
        int numberOfColumns = map.getUpperRight().x+1;
        double height = (double) 700 / numberOfRows;
        double width = (double) 700 / numberOfColumns;

        for (int i=0; i<numberOfColumns; i++){
            ColumnConstraints columnConstraints = new ColumnConstraints(width);
            columnConstraints.setPercentWidth(100.0 / numberOfColumns);
            gridPane.getColumnConstraints().add(columnConstraints);
        }

        for (int i=0; i<numberOfRows; i++){
            RowConstraints rowConstraints = new RowConstraints(height);
            rowConstraints.setPercentHeight(100.0 / numberOfColumns);
            gridPane.getRowConstraints().add(rowConstraints);
        }

        for (int x = 0; x < numberOfColumns; x++) {
            for (int y = 0; y < numberOfRows; y++) {
                Vector2d pos = new Vector2d(x, y);
                Object object = map.objectAt(pos);
                if (map.isOccupied(pos)){
                    GuiElementBox VBox = new GuiElementBox((IMapElement) object, height, width);
                    gridPane.add(VBox.getBox(), pos.x, numberOfRows - pos.y - 1, 1, 1);
                }

            }
        }
        grid.getChildren().clear();
        grid.getChildren().add(gridPane);
    }


    /*private VBox createStatistics(GrassField map, SimulationEngine engine){
        animals.updateAnimalsGraph(engine);
        plants.updatePlantsGraph(engine, map);
        emptyFields.updateEmptyFieldsGraph(engine, map);
        averageEnergy.updateAverageEnergyGraph(engine);
        averageLife.updateAverageLifeGraph(engine);

        VBox statsBox = new VBox(animals.makeGraph(), plants.makeGraph(), emptyFields.makeGraph(), averageEnergy.makeGraph(), averageLife.makeGraph());
        statsBox.setAlignment(Pos.CENTER);

        return statsBox;
    }*/


    @Override
    public void mapChanged(GrassField map, GridPane worldMap) {
        Platform.runLater(() -> {
            try {
                createMap(map,worldMap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }


}

