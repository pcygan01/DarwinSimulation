package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class App extends Application implements IMapChangeObserver {
    private final VBox vbox = new VBox();
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
    private boolean savingToFileParameter; // false - nie, true - tak
    boolean parametersOk = true;
    boolean isRunning;
    public Image animalImage;

    {
        try {
            animalImage = new Image(new FileInputStream("src/main/resources/zebra.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Image grassImage;

    {
        try {
            grassImage = new Image(new FileInputStream("src/main/resources/grass.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


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
        startButton.setOnMouseClicked(event -> {
            showSecondScene();
        });

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
        mySettingsButton.setOnMouseClicked(event -> showMySettings());

        Button predefinedButton = new Button("Predefined settings");
        predefinedButton.setFont(new Font("Arial", 30));
        predefinedButton.setOnMouseClicked(event -> showPredefinedSettings());

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

        ChoiceBox<String> savingToFile = new ChoiceBox<>();
        savingToFile.getItems().addAll("Yes", "No");
        savingToFile.setPrefWidth(170);

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
        Label savingToFileLabel = new Label("Saving to a file: ");
        savingToFileLabel.setFont(new Font("Arial", 20));


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
        predefinedButton.setOnMouseClicked((event -> showPredefinedSettings()));

        Button startButton = new Button("Start simulation");
        startButton.setFont(new Font("Arial", 20));
        startButton.setOnMouseClicked(event -> {
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
            if (savingToFile.getValue() == "No")
                this.savingToFileParameter = false;
            else
                this.savingToFileParameter = true;

            InputParameters inputParameters = new InputParameters(heightWorldParameter, widthWorldParameter, initialNumberOfPlantsParameter, plantEnergyParameter, numberOfNewPlantsParameter, initialNumberOfAnimalsParameter, startAnimalEnergyParameter, minEnergyToReproductionParameter, energyUsedToReproductionParameter, minNumberOfMutationsParameter, maxNumberOfMutationsParameter, DNAlengthParameter, mapVariantParameter, grassVariantParameter, mutationVariantParameter, behaviourVariantParameter, savingToFileParameter);
            try {
                inputParameters.checkConditions();
                this.parametersOk = true;
            } catch (Exception e) {
                showException(e);
                this.parametersOk = false;
            }
            if (this.parametersOk) {
                try {
                    showSimulationScene(inputParameters);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

        });

        HBox buttonsBox = new HBox(predefinedButton, startButton);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setSpacing(100);


        GridPane inputGrid = new GridPane();
        inputGrid.setAlignment(Pos.CENTER);
        inputGrid.setBackground(new Background(new BackgroundFill(Color.web("#81c483"), CornerRadii.EMPTY, Insets.EMPTY)));
        inputGrid.setPrefHeight(950);
        inputGrid.setPrefWidth(950);
        inputGrid.setVgap(20);
        inputGrid.setHgap(50);

        inputGrid.add(mapSettingsLabel, 0, 0);
        inputGrid.add(mapLabelBox, 0, 1);
        inputGrid.add(mapTextBox, 1, 1);
        inputGrid.add(plantsSettingsLabel, 0, 2);
        inputGrid.add(plantsLabelBox, 0, 3);
        inputGrid.add(plantsTextBox, 1, 3);
        inputGrid.add(animalsSettingsLabel, 0, 4);
        inputGrid.add(animalsLabelBox, 0, 5);
        inputGrid.add(animalsTextBox, 1, 5);
        inputGrid.add(variantsSettingsLabel, 0, 6);
        inputGrid.add(variantsLabelBox, 0, 7);
        inputGrid.add(variantsTextBox, 1, 7);
        inputGrid.add(savingToFileLabel, 0, 8);
        inputGrid.add(savingToFile, 1, 8);
        inputGrid.add(predefinedButton, 0, 9);
        inputGrid.add(startButton, 1, 9);

        vbox.getChildren().clear();
        vbox.getChildren().add(inputGrid);
        vbox.setAlignment(Pos.CENTER);
    }

    public void showPredefinedSettings() {
        ChoiceBox<String> preSettings = new ChoiceBox<>();
        preSettings.getItems().addAll("option 1", "option 2");
        preSettings.setPrefWidth(300);

        Button mySettings = new Button("My settings");
        mySettings.setFont(new Font("Arial", 30));
        mySettings.setOnMouseClicked((event -> showMySettings()));

        Button startButton = new Button("Start simulation");
        startButton.setFont(new Font("Arial", 30));
        startButton.setOnMouseClicked(event -> {
            Scanner scan;
            String[] tab = new String[17];
            if (preSettings.getValue() == "option 1") {
                try {
                    scan = new Scanner(new File("src/main/resources/ps1.txt"));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                int i = 0;
                while (scan.hasNextLine()) {
                    String[] param = scan.nextLine().split(" ");
                    tab[i] = param[1];
                    i += 1;
                }
            } else if (preSettings.getValue() == "option 2") {
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
            InputParameters inputParameters = new InputParameters(Integer.parseInt(tab[0]), Integer.parseInt(tab[1]), Integer.parseInt(tab[2]), Integer.parseInt(tab[3]), Integer.parseInt(tab[4]), Integer.parseInt(tab[5]), Integer.parseInt(tab[6]), Integer.parseInt(tab[7]), Integer.parseInt(tab[8]), Integer.parseInt(tab[9]), Integer.parseInt(tab[10]), Integer.parseInt(tab[11]), Boolean.parseBoolean(tab[12]), Boolean.parseBoolean(tab[13]), Boolean.parseBoolean(tab[14]), Boolean.parseBoolean(tab[15]), Boolean.parseBoolean(tab[16]));
            try {
                showSimulationScene(inputParameters);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });


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
        worldMap.setPrefWidth(600);
        worldMap.setPrefHeight(600);

        VBox statsBox = new VBox();
        statsBox.setPrefWidth(300);
        statsBox.setPrefHeight(800);

        VBox animalBox = new VBox();
        animalBox.setPrefWidth(500);
        animalBox.setPrefHeight(250);

        Button startButton = new Button("Start");
        Button stopButton = new Button("Stop");
        Button stopTrackingButton = new Button("Stop tracking");
        HBox buttons = new HBox(20, startButton, stopButton, stopTrackingButton);


        StatisticsChart animalsChart = new StatisticsChart("Number of animals");
        StatisticsChart plantsChart = new StatisticsChart("Number of plants");
        StatisticsChart emptyFieldsChart = new StatisticsChart("Number of empty fields");
        StatisticsChart averageEnergyChart = new StatisticsChart("Average energy level");
        StatisticsChart averageLifespanChart = new StatisticsChart("Average lifespan of animals");
        StatisticsChart[] statisticsCharts = {animalsChart, plantsChart, emptyFieldsChart, averageEnergyChart, averageLifespanChart};

        Animal trackedAnimal = null;

        grid.setBackground(new Background(new BackgroundFill(Color.web("#81c483"), CornerRadii.EMPTY, Insets.EMPTY)));
        GrassField map = new GrassField(inputParameters.initialNumberOfPlants, inputParameters.heightWorld, inputParameters.widthWorld, inputParameters.mapVariant, inputParameters.plantEnergy, inputParameters.grassVariant, inputParameters.numberOfNewPlants, worldMap);
        SimulationEngine engine = new SimulationEngine(this, grid, map, statsBox, statisticsCharts, animalBox, 300, inputParameters.initialNumberOfAnimals, inputParameters.startAnimalEnergy, inputParameters.minEnergyToReproduction, inputParameters.energyUsedToReproduction, inputParameters.minNumberOfMutations, inputParameters.maxNumberOfMutations, inputParameters.DNAlength, inputParameters.mutationVariant, inputParameters.behaviourVariant, inputParameters.savingToFile);

        worldMap = createMap(map, engine, worldMap);
        statsBox = createStatistics(statisticsCharts, statsBox, engine.getMostPopularGenes(), engine.getDays(), engine.allAnimalsCount(), engine.allGrassesCount(), engine.getFreeFieldsCount(), engine.getAverageEnergy(), engine.getAverageLifeSpan());

        Thread engineThread = new Thread(engine);
        engineThread.start();

        stopButton.setOnMouseClicked(event -> {
            isRunning = false;
            engine.pause();
            stopButton.setDisable(true);
            startButton.setDisable(false);
        });

        startButton.setOnMouseClicked(event -> {
            isRunning = true;
            engine.start();
            stopButton.setDisable(false);
            startButton.setDisable(true);
        });

        stopTrackingButton.setOnMouseClicked(event -> {
            if (isRunning) {
                engine.stopTracking();
            }
        });


        VBox leftBox = new VBox(30, worldMap, buttons, animalBox);
        leftBox.setAlignment(Pos.CENTER);

        grid.getChildren().clear();
        grid.add(leftBox, 0, 0);
        grid.add(statsBox, 1, 0);
        grid.setHgap(20);


        Scene scene = new Scene(grid, 950, 950, Color.web("#81c483"));
        stage.setScene(scene);
        stage.setTitle("Darwin Simulation");
        stage.show();

    }

    public GridPane createMap(GrassField map, SimulationEngine engine, GridPane grid) throws FileNotFoundException {
        GridPane gridPane = new GridPane();
        gridPane.getChildren().clear();
        gridPane.setBackground(new Background(new BackgroundFill(Color.web("#CAE7D2"), CornerRadii.EMPTY, Insets.EMPTY)));
        int numberOfRows = map.getUpperRight().y + 1;
        int numberOfColumns = map.getUpperRight().x + 1;
        double height = (double) 600 / numberOfRows;
        double width = (double) 600 / numberOfColumns;

        for (int i = 0; i < numberOfColumns; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints(width);
            columnConstraints.setPercentWidth(100.0 / numberOfColumns);
            gridPane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < numberOfRows; i++) {
            RowConstraints rowConstraints = new RowConstraints(height);
            rowConstraints.setPercentHeight(100.0 / numberOfColumns);
            gridPane.getRowConstraints().add(rowConstraints);
        }

        for (int x = 0; x < numberOfColumns; x++) {
            for (int y = 0; y < numberOfRows; y++) {
                Vector2d pos = new Vector2d(x, y);
                Object object = map.objectAt(pos);
                if (map.isOccupied(pos)) {
                    boolean isTracked = false;
                    if (engine.getTrackedAnimal() != null) {
                        isTracked = true;
                    }
                    GuiElementBox VBox = new GuiElementBox((IMapElement) object, height, width, animalImage, grassImage, this.startAnimalEnergyParameter);
                    VBox objectBox = VBox.getBox();

                    if (object instanceof Animal && !isRunning) {
                        objectBox.setOnMouseClicked(new EventHandler<Event>() {
                            @Override
                            public void handle(Event event) {
                                engine.setAnimalTracked(pos);
                            }
                        });
                    }

                    gridPane.add(objectBox, pos.x, numberOfRows - pos.y - 1, 1, 1);
                }
            }
        }

        grid.getChildren().clear();
        grid.getChildren().add(gridPane);
        return grid;
    }


    private VBox createStatistics(StatisticsChart[] statisticsCharts, VBox statsBox, int[] mostPopularGenes, int days, int numOfAnimals, int numOfGrasses, int numOfFreeFields, double averageEnergy, double averageLifespan) {
        statsBox.getChildren().clear();
        StatisticsChart animalsChart = statisticsCharts[0];
        StatisticsChart plantsChart = statisticsCharts[1];
        StatisticsChart emptyFieldsChart = statisticsCharts[2];
        StatisticsChart averageEnergyChart = statisticsCharts[3];
        StatisticsChart averageLifespanChart = statisticsCharts[4];

        animalsChart.updateAnimalsChart(days, numOfAnimals);
        plantsChart.updateGrassesChart(days, numOfGrasses);
        emptyFieldsChart.updateFreeFieldsChart(days, numOfFreeFields);
        averageEnergyChart.updateEnergyChart(days, averageEnergy);
        averageLifespanChart.updateLifespanChart(days, averageLifespan);

        statsBox.getChildren().add(animalsChart.getChart());
        statsBox.getChildren().add(plantsChart.getChart());
        statsBox.getChildren().add(emptyFieldsChart.getChart());
        statsBox.getChildren().add(averageEnergyChart.getChart());
        statsBox.getChildren().add(averageLifespanChart.getChart());
        Label label = new Label("The most common genotypes");
        label.setFont(new Font("Arial", 17));
        statsBox.getChildren().add(label);
        //statsBox.getChildren().add(new Label(""+mostPopularGenes[0].toString()+"\n" + mostPopularGenes[1] +"\n" + mostPopularGenes[2]));
        statsBox.setAlignment(Pos.CENTER);

        return statsBox;
    }

    private VBox createAnimalStats(VBox animalBox, SimulationEngine engine) {
        Animal animal = engine.getTrackedAnimal();
        Label genome = new Label("Genome: ");
        genome.setFont(new Font("Arial", 15));
        Label activated = new Label("Activated part of the genome: ");
        activated.setFont(new Font("Arial", 15));
        Label energy = new Label("Energy: ");
        energy.setFont(new Font("Arial", 15));
        Label plantsEaten = new Label("Number of plants eaten: ");
        plantsEaten.setFont(new Font("Arial", 15));
        Label numOfChildren = new Label("Number of children: ");
        numOfChildren.setFont(new Font("Arial", 15));
        Label age = new Label("Age: ");
        age.setFont(new Font("Arial", 15));
        Label deathDay = new Label("Death day: ");
        deathDay.setFont(new Font("Arial", 15));

        Label genomeR = new Label("" + animal.getGenes());
        genomeR.setFont(new Font("Arial", 15));
        Label activatedR = new Label("" +animal.getActivatedGene());
        activatedR.setFont(new Font("Arial", 15));
        Label energyR = new Label("" + animal.getEnergy());
        energyR.setFont(new Font("Arial", 15));
        Label plantsEatenR = new Label("" + animal.getPlantsEaten());
        plantsEatenR.setFont(new Font("Arial", 15));
        Label numOfChildrenR = new Label("" + animal.getChildrenAmount());
        numOfChildrenR.setFont(new Font("Arial", 15));
        Label ageR = new Label("" + animal.getDaysLived());
        ageR.setFont(new Font("Arial", 15));
        Label deathDayR1 = new Label("Still alive");
        deathDayR1.setFont(new Font("Arial", 15));
        Label deathDayR2 = new Label("" + animal.getDeathDay());
        deathDayR2.setFont(new Font("Arial", 15));


        VBox genomeBox = new VBox(2, genome, genomeR);
        HBox activatedBox = new HBox(20, activated, activatedR);
        HBox energyBox = new HBox(30, energy, energyR);
        HBox plantsEatenBox = new HBox(30, plantsEaten, plantsEatenR);
        HBox numOfChildrenBox = new HBox(30, numOfChildren, numOfChildrenR);
        HBox ageBox = new HBox(30, age, ageR);
        HBox deathDayBox = new HBox(30, deathDay, deathDayR1);
        if (animal.getDeathDay() != -1) {
            deathDayBox.getChildren().clear();
            deathDayBox.getChildren().add(deathDay);
            deathDayBox.getChildren().add(deathDayR2);
            deathDayBox.setSpacing(30);
        }

        animalBox.getChildren().clear();
        animalBox.getChildren().add(genomeBox);
        animalBox.getChildren().add(activatedBox);
        animalBox.getChildren().add(energyBox);
        animalBox.getChildren().add(plantsEatenBox);
        animalBox.getChildren().add(numOfChildrenBox);
        animalBox.getChildren().add(ageBox);
        animalBox.getChildren().add(deathDayBox);
        animalBox.setBackground(new Background(new BackgroundFill(Color.web("#CAE7D2"), CornerRadii.EMPTY, Insets.EMPTY)));
        animalBox.setPrefWidth(600);
        animalBox.setSpacing(17);


        return animalBox;
    }

    @Override
    public void mapChanged(SimulationEngine engine) {
        Platform.runLater(() -> {
            try {
                Button startButton = new Button("Start");
                Button stopButton = new Button("Stop");
                Button stopTrackingButton = new Button("Stop tracking");
                HBox buttons = new HBox(20, startButton, stopButton, stopTrackingButton);
                stopButton.setOnMouseClicked(new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {
                        isRunning = false;
                        engine.isRunning = false;
                        stopButton.setDisable(true);
                        startButton.setDisable(false);
                    }

                });
                startButton.setOnMouseClicked(new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {
                        isRunning = true;
                        engine.isRunning = true;
                        stopButton.setDisable(false);
                        startButton.setDisable(true);
                    }

                });

                stopTrackingButton.setOnMouseClicked(new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {
                        if (isRunning) {
                            engine.stopTracking();
                        }
                    }

                });

                GridPane grid = engine.getGrid();
                GridPane worldMap = new GridPane();
                worldMap.setPrefWidth(600);
                worldMap.setPrefHeight(600);

                VBox statsBox = new VBox();
                statsBox.setPrefWidth(300);
                statsBox.setPrefHeight(800);

                VBox animalBox = new VBox();
                animalBox.setPrefWidth(500);
                animalBox.setPrefHeight(250);


                worldMap.getChildren().add(createMap(engine.getMap(), engine, engine.getMap().getWorldMap()));
                statsBox.getChildren().add(createStatistics(engine.getStatisticsCharts(), engine.getStatsBox(), engine.getMostPopularGenes(), engine.getDays(), engine.allAnimalsCount(), engine.allGrassesCount(), engine.getFreeFieldsCount(), engine.getAverageEnergy(), engine.getAverageLifeSpan()));
                if (engine.getTrackedAnimal() != null) {
                    animalBox.getChildren().add(createAnimalStats(engine.getAnimalBox(), engine));
                }


                VBox leftBox = new VBox(30, worldMap, buttons, animalBox);
                leftBox.setAlignment(Pos.CENTER);

                grid.getChildren().clear();
                grid.add(leftBox, 0, 0);
                grid.add(statsBox, 1, 0);
                grid.setHgap(20);


            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }


}

