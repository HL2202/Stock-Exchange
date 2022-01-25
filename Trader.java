import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.*;
import java.util.stream.*;

import java.text.DecimalFormat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;


public class Trader {

    private List<String> productsInInventory;
    private String id;
    private double balance;
    private List<Double> totalAmount;


    public Trader(String id, double balance) {

        this.id = id;
        this.balance = balance;
        this.totalAmount = new ArrayList<>();
        this.productsInInventory = new ArrayList<String>();
    }


    public String getID() {

        if (id != null) {
            
            return this.id;
        }

        else {

            return null;
        }
    }


    public double getBalance() {

        if (balance > 0) {

            return this.balance;
        }

        else {

            return 0.0;
        }
    }


    public double importProduct(String product, double amount) {

        if (product != null && amount > 0) {

            if (productsInInventory.contains(product)) {

                int productIndex = 0;

                for (int i = 0; i < productsInInventory.size(); i++) {

                    if (product.equals(productsInInventory.get(i))) {

                        productIndex += i;
                        break;
                    }
                }

                double productAmount = totalAmount.get(productIndex);
                productAmount += amount;
                totalAmount.set(productIndex, productAmount);
                return productAmount;
            }

            else {

                productsInInventory.add(product);
                double productAmount = 0;
                productAmount += amount;
                totalAmount.add(productAmount);
                return productAmount;
            }
        }

        else {

            return -1.0;
        }
    }


    public double exportProduct(String product, double amount) {

        if (product != null && amount > 0) {

            if (productsInInventory != null && !productsInInventory.isEmpty()) {

                if (productsInInventory.size() > 0) {

                    if (productsInInventory.contains(product)) {

                        int productIndex = 0;

                        for (int i = 0; i < productsInInventory.size(); i++) {

                            if (product.equals(productsInInventory.get(i))) {

                                productIndex += i;
                                break;
                            }
                        }

                        double productAmount = totalAmount.get(productIndex);

                        if ((productAmount - amount) >= 0) {

                            productAmount -= amount;
                            totalAmount.set(productIndex, productAmount);

                            if (productAmount == amount) {

                                productsInInventory.remove(product);
                            }

                            return productAmount;
                        }

                        else {

                            return -1.0;
                        }
                    }

                    else {

                        return -1.0;
                    }
                }

                else {

                    return -1.0;
                }
            }

            else {

                return -1.0;
            }
        }

        else {

            return -1.0;
        }
    }


    public double getAmountStored(String product) {

        if (product != null) {

            if (productsInInventory != null && !productsInInventory.isEmpty()) {

                if (productsInInventory.size() > 0) {

                    if (productsInInventory.contains(product)) {

                        int productIndex = 0;

                        for (int i = 0; i < productsInInventory.size(); i++) {

                            if (product.equals(productsInInventory.get(i))) {

                                productIndex += i;
                                break;
                            }
                        }

                        double productAmount = totalAmount.get(productIndex);
                        return productAmount;
                    }

                    else {

                        return 0.0;
                    }
                }

                else {

                    return 0.0;
                }
            }

            else {

                return 0.0;
            }
        }

        else {

            return 0.0;
        }
    }


    public List<String> getProductsInInventory() {
        if (productsInInventory != null && !productsInInventory.isEmpty()) {
            if (productsInInventory.size() > 0) {
                for (int i = 0; i < productsInInventory.size(); i ++) {
                    String newProduct = productsInInventory.get(i);
                    if (getAmountStored(newProduct) == 0) {
                        productsInInventory.remove(newProduct);
                    }
                }
                List<String> sortedInventory = productsInInventory.stream().sorted().collect(Collectors.toList());
                return sortedInventory;
            }
            else {
                return productsInInventory;
            }
        }
        else {
            return productsInInventory;
        }
    }

    public double adjustBalance(double change) {
        if (balance >= 0) {
            this.balance += change;
            return balance;
        }
        else {
            return 0.0;
        }
    }

    public String toString() {

        if (balance > 0) {
            String out = "";
            DecimalFormat df = new DecimalFormat("0.00");
            String balanceAsString = df.format(balance);
            out = id + ": $" + balanceAsString + " {";

            if (id != null && balance > 0 && productsInInventory != null && !productsInInventory.isEmpty()) {

                if (productsInInventory.size() > 0) {

                    List<String> sortInventory = getProductsInInventory();

                    for (int i = 0; i < sortInventory.size(); i ++) {

                        if (i != sortInventory.size() - 1) {

                            String nextProduct = sortInventory.get(i);
                            double nextAmount = getAmountStored(nextProduct);
                            String nextAmountAsString = df.format(nextAmount);
                            out = out + sortInventory.get(i);
                            out = out + ": ";
                            out = out + nextAmountAsString;
                            out = out + ", ";
                        }

                        else {

                            String nextProduct = sortInventory.get(i);
                            double nextAmount = getAmountStored(nextProduct);
                            String nextAmountAsString = df.format(nextAmount);
                            out = out + sortInventory.get(i);
                            out = out + ": ";
                            out = out + nextAmountAsString;
                        }
                    }
                }

                out = out + "}";
                return out;
            }

            else {

                out = out + "}";
                return out; 
            }
        }

        else {

            return null;
        }
    }


    public static void writeTraders(List<Trader> traders, String path) {

        if (traders != null) {

            if (path != null && traders.size() > 0) {

                try {

                    FileWriter newEntry = new FileWriter(path);

                    for (int i = 0; i < traders.size(); i++) {

                        Trader newTrader = traders.get(i);
                        String traderRecord = newTrader.toString();
                        newEntry.write(traderRecord.concat("\r\n"));
                    }

                    newEntry.close();
                } catch(Exception e){
                }
            }

            else {
                ;
            }
        }

        else {
            ;
        }
    }


    public static void writeTradersBinary(List<Trader> traders, String path) {

        if (traders != null) {

            if (path != null && traders.size() > 0) {

                try {

                    FileOutputStream newEntry = new FileOutputStream(path);
                    DataOutputStream output = new DataOutputStream(newEntry);
                    for (int i = 0; i < traders.size(); i++) {
                        Trader newTrader = traders.get(i);
                        String traderRecord = newTrader.toString();
                        output.writeUTF(traderRecord);
                        output.writeUTF("\u001f");
                    }

                    output.close();
                    newEntry.close();
                } catch(Exception e){
                }
            }

            else {
                ;
            }
        }

        else {
            ;
        }
    }
}