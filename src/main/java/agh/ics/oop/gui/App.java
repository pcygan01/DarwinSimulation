package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

import java.util.List;


public class App extends Application {
    private final VBox vbox = new VBox();

    public static void main(String[] args){
        Application.launch(App.class);
    }
    public void start(Stage primaryStage) throws Exception{
        Scene scene = new Scene(vbox, 950, 950, Color.web("#81c483"));
        primaryStage.setScene(scene);
        primaryStage.setTitle("Darwin Simulation");
        primaryStage.show();
    }

    public void init() throws Exception{
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

    public void showSecondScene(){
        GridPane inputParameters = new GridPane();
        inputParameters.setAlignment(Pos.CENTER);
        inputParameters.setBackground(new Background(new BackgroundFill(Color.web("#81c483"), CornerRadii.EMPTY, Insets.EMPTY)));
        inputParameters.setPrefWidth(950);
        inputParameters.setPrefHeight(950);

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
        mapVariant.setPrefWidth(150);

        ChoiceBox<String> grassVariant = new ChoiceBox<>();
        grassVariant.getItems().addAll("Green Equators", "Toxic Corpses");
        grassVariant.setPrefWidth(150);

        ChoiceBox<String> mutationVariant = new ChoiceBox<>();
        mutationVariant.getItems().addAll("Randomness", "Correction");
        mutationVariant.setPrefWidth(150);

        ChoiceBox<String> behaviourVariant = new ChoiceBox<>();
        behaviourVariant.getItems().addAll("In order", "A bit of madness");
        behaviourVariant.setPrefWidth(150);

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

        HBox mapBox = new HBox(mapLabelBox, mapTextBox);
        mapBox.setAlignment(Pos.CENTER);
        mapBox.setSpacing(202);
        HBox plantsBox = new HBox(plantsLabelBox, plantsTextBox);
        plantsBox.setAlignment(Pos.CENTER);
        plantsBox.setSpacing(23);
        HBox animalsBox = new HBox(animalsLabelBox, animalsTextBox);
        animalsBox.setAlignment(Pos.CENTER);
        animalsBox.setSpacing(20);
        HBox variantsBox = new HBox(variantsLabelBox, variantsTextBox);
        variantsBox.setAlignment(Pos.CENTER);
        variantsBox.setSpacing(117);

        Button startButton = new Button("Start simulation");
        Button predefinedButton = new Button("Predefined settings");
        HBox buttonsBox = new HBox(predefinedButton, startButton);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setSpacing(100);

        VBox inputBox = new VBox(
                mapSettingsLabel,
                mapBox,
                plantsSettingsLabel,
                plantsBox,
                animalsSettingsLabel,
                animalsBox,
                variantsSettingsLabel,
                variantsBox,
                buttonsBox);


        inputBox.setAlignment(Pos.CENTER);
        inputBox.setSpacing(20);

        inputParameters.getChildren().add(inputBox);


        vbox.getChildren().clear();
        vbox.getChildren().add(inputParameters);
        vbox.setAlignment(Pos.CENTER);

    }



}
