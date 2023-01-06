package agh.ics.oop;


public class InputParameters {
    // mapa
    public int heightWorld; // wysokość mapy
    public int widthWorld; // szerokość mapy

    // rośliny
    public int initialNumberOfPlants; // startowa liczba roślin
    public int plantEnergy; // energia zapewniana przez zjedzenie rośliny
    public int numberOfNewPlants; // liczba roślin wyrastająca każdego dnia

    // zwierzęta
    public int initialNumberOfAnimals; // startowa liczba zwierzaków
    public int startAnimalEnergy; // startowa energia zwierzaków
    public int minEnergyToReproduction; // energia konieczna, by uznać zwierzaka za najedzonego (i gotowego do rozmnażania)
    public int energyUsedToReproduction; // energia rodziców zużywana by stworzyć potomka
    public int minNumberOfMutations; // minimalna liczba mutacji u potomków
    public int maxNumberOfMutations; // maksymalna liczba mutacji u potomków
    public int DNAlength; // długość genomu zwierzaków

    // warianty
    public boolean mapVariant; // false - kula ziemska, true - piekielny portal
    public boolean grassVariant; // false - zalesione równiki, true - toksyczne trupy
    public boolean mutationVariant; // false - pełna losowość, true - lekka korekta
    public boolean behaviourVariant; // false - pełna predystancja, true - nieco szaleństwa

    public InputParameters(int heightWorld, int widthWorld, int initialNumberOfPlants, int plantEnergy, int numberOfNewPlants, int initialNumberOfAnimals, int startAnimalEnergy, int minEnergyToReproduction, int energyUsedToReproduction, int minNumberOfMutations, int maxNumberOfMutations, int DNAlength, boolean mapVariant, boolean grassVariant, boolean mutationVariant, boolean behaviourVariant) {
        this.heightWorld = heightWorld;
        this.widthWorld = widthWorld;
        this.initialNumberOfPlants = initialNumberOfPlants;
        this.plantEnergy = plantEnergy;
        this.numberOfNewPlants = numberOfNewPlants;
        this.initialNumberOfAnimals = initialNumberOfAnimals;
        this.startAnimalEnergy = startAnimalEnergy;
        this.minEnergyToReproduction = minEnergyToReproduction;
        this.energyUsedToReproduction = energyUsedToReproduction;
        this.minNumberOfMutations = minNumberOfMutations;
        this.maxNumberOfMutations = maxNumberOfMutations;
        this.DNAlength = DNAlength;
        this.mapVariant = mapVariant;
        this.grassVariant = grassVariant;
        this.mutationVariant = mutationVariant;
        this.behaviourVariant = behaviourVariant;
    }


    public void checkConditions() throws IllegalArgumentException {
        if (this.heightWorld <= 0)
            throw new IllegalArgumentException("Invalid map height");
        if (this.widthWorld <= 0)
            throw new IllegalArgumentException("Invalid map width");
        if (this.initialNumberOfPlants <= 0)
            throw new IllegalArgumentException("Invalid initial number of plants");
        if (this.plantEnergy <= 0)
            throw new IllegalArgumentException("Invalid plant energy");
        if (this.numberOfNewPlants <= 0)
            throw new IllegalArgumentException("Invalid number of new plants each day");
        if (this.initialNumberOfAnimals <= 0)
            throw new IllegalArgumentException("Invalid initial number of animals");
        if (this.startAnimalEnergy <= 0)
            throw new IllegalArgumentException("Invalid animal's initial energy");
        if (this.minEnergyToReproduction <= 0)
            throw new IllegalArgumentException("Invalid initial number of animals");
        if (this.energyUsedToReproduction <= 0)
            throw new IllegalArgumentException("Invalid energy used to reproduction");
        if (this.minNumberOfMutations < 0)
            throw new IllegalArgumentException("Invalid minimal number of mutations");
        if (this.maxNumberOfMutations <= 0)
            throw new IllegalArgumentException("Invalid maximal number of mutations");
        if (this.DNAlength <= 0)
            throw new IllegalArgumentException("Invalid DNA length");
    }


}
