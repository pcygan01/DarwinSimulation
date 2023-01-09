package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox extends Node {
    private final VBox vbox;

    public GuiElementBox(IMapElement object, double height, double width, Image animalImage, Image grassImage, int energy) throws FileNotFoundException {
        ImageView imageView = new ImageView();
        if (object instanceof Animal a) {
            imageView.setImage(animalImage);
            if (a.getEnergy() < 0.25 * energy){
                imageView.setEffect(new ColorAdjust(-1, 0, 0, 0));
            }
            else if (a.getEnergy() < 0.5 * energy){
                imageView.setEffect(new ColorAdjust(-0.3, 0, 0, 0));
            }
            else if (a.getEnergy() < 0.75 * energy){
                imageView.setEffect(new ColorAdjust(0.3, 0, 0, 0));
            }
            else if (a.getEnergy() < energy){
                imageView.setEffect(new ColorAdjust(1, 0, 0, 0));
            }
        }
        if (object instanceof Grass){
            imageView.setImage(grassImage);
        }
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);


        vbox = new VBox(imageView);
        vbox.setAlignment(Pos.CENTER);
    }

    public VBox getBox() {
        return vbox;
    }
}