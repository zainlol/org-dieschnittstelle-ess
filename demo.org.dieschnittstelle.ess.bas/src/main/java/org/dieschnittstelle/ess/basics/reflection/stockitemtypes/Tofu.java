package org.dieschnittstelle.ess.basics.reflection.stockitemtypes;

import org.dieschnittstelle.ess.basics.IStockItem;

public class Tofu implements IStockItem {

    private String brandname;
    private int units;
    private int weight;
    private int price;

    public Tofu() {

    }


    @Override
    public String toString() {
        return "Tofu{" +
                "brandname='" + brandname + '\'' +
                ", units=" + units +
                ", weight=" + weight +
                ", price=" + price +
                '}';
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    @Override
    public void initialise(int units, String brandname) {
        this.units = units;
        this.brandname = brandname;
    }

    @Override
    public void purchase(int units) {

    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
