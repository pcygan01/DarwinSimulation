package agh.ics.oop.gui;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class StatisticsChart {

    private XYChart.Series series = new XYChart.Series();

    private LineChart<Number, Number> chart;

    public StatisticsChart(String string) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Day");
        this.chart = new LineChart(xAxis, yAxis);

        series = new XYChart.Series();

        chart.setTitle(string);
        chart.setCreateSymbols(true);
        chart.getData().add(series);
        chart.setHorizontalGridLinesVisible(false);
        chart.setVerticalGridLinesVisible(false);
        chart.setPrefHeight(200);

    }


    public LineChart getChart() {
        return chart;
    }


    public void updateAnimalsChart(int days, int numOfAnimals) {
        this.series.getData().add(new XYChart.Data(days, numOfAnimals));
    }
    public void updateGrassesChart(int days, int numOfGrasses) {
        this.series.getData().add(new XYChart.Data(days, numOfGrasses));
    }
    public void updateFreeFieldsChart(int days,int numOfFreeFields) {
        this.series.getData().add(new XYChart.Data(days, numOfFreeFields));
    }
    public void updateEnergyChart(int days, double averageEnergy) {
        this.series.getData().add(new XYChart.Data(days, averageEnergy));
    }
    public void updateLifespanChart(int days, double averageLifespan) {
        this.series.getData().add(new XYChart.Data(days, averageLifespan));
    }
}
