package client.util;


import common.model.HumanBeing;

public class Persons {
    private final Localizator localizator;

    public Persons(Localizator localizator) {
        this.localizator = localizator;
    }

    public String describe(HumanBeing product) {
        String info = "";
        info += " ID: " + product.getId();
        info += "\n " + localizator.getKeyString("Name") + ": " + product.getName();
        info += "\n " + localizator.getKeyString("Owner") + ": " + product.getOwnerId();
        info += "\n " + localizator.getKeyString("CreationDate") + ": " + localizator.getDate(product.getCreationDate());
        info += "\n X: " + product.getCoordinates().getX();
        info += "\n Y: " + product.getCoordinates().getY();
        info += "\n " + localizator.getKeyString("Price") + ": " + product.getImpactSpeed();

        return info;
    }
}