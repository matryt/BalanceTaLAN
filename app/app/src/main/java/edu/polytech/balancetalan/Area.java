package edu.polytech.balancetalan;

import java.util.List;

public class Area {
    private char letter;
    private List<Place> places;

    public Area() {
        // Default constructor
    }

    public Area(char letter, List<Place> places) {
        this.letter = letter;
        this.places = places;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public char getLetter() {
        return letter;
    }
}
