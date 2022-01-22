import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.util.*;
import java.io.*;
import java.util.Collections;
import java.util.stream.*;
import java.text.DecimalFormat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.lang.Math;

public class Exchange {
    
    Scanner scan = new Scanner(System.in);
    Market myMarket = new Market();
    List<Trader> myTraders = new ArrayList<Trader>();
    List<Trade> myTrades = new ArrayList<Trade>();
    List<Order> mySellBook = myMarket.getSellBook();
    List<Order> myBuyBook = myMarket.getBuyBook();
    DecimalFormat df = new DecimalFormat("0.00");
    int increment = 0;
    List<String> hex = new ArrayList<String>();
    //String hexString = "0123456789ABCDEF";




    public String MakeOrder(int increment) {
        String returnOrder = "";
        if (hex.size() != 16) {
            hex.add("0");
            hex.add("1");
            hex.add("2");
            hex.add("3");
            hex.add("4");
            hex.add("5");
            hex.add("6");
            hex.add("7");
            hex.add("8");
            hex.add("9");
            hex.add("A");
            hex.add("B");
            hex.add("C");
            hex.add("D");
            hex.add("E");
            hex.add("F");
        }
        if (increment <= 16) {
            returnOrder += "000";
            for (int i = 0; i < hex.size(); i++) {
                if (increment == i) {
                    returnOrder += hex.get(i);
                    break;
                }
            }
        }
        else if (increment <= 256) {
            returnOrder += "00";
            int j = 0;
            while (j * 16 <= increment) {
                j++;
            }
            j --;
            returnOrder += hex.get(j);
            int num = increment;
            num = num - (16 * j);
            for (int i = 0; i < hex.size(); i++) {
                if (num == i) {
                    returnOrder += hex.get(i);
                    break;
                }
            }
        }
        else if (increment <= 4096) {
            returnOrder += "0";
            int j = 0;
            while (j * 16 * 16 <= increment) {
                j++;
            }
            j --;
            returnOrder += hex.get(j);
            int num = increment;
            num = num - (16 * 16 * j);
            int k = 0;
            while (k * 16 <= num) {
                k++;
            }
            k --;
            returnOrder += hex.get(k);
            num = num - (16 * k);
            for (int i = 0; i < hex.size(); i++) {
                if (num == i) {
                    returnOrder += hex.get(i);
                    break;
                }
            }
        }
        else {
            int j = 0;
            while (j * 16 * 16 * 16 <= increment) {
                j++;
            }
            j --;
            returnOrder += hex.get(j);
            int num = increment;
            num = num - (16 * 16 * 16 * j);
            int k = 0;
            while (k * 16 * 16 <= num) {
                k++;
            }
            k --;
            returnOrder += hex.get(k);
            num = num - (16 * 16 * k);
            int m = 0;
            while (m * 16 <= num) {
                m++;
            }
            m --;
            returnOrder += hex.get(m);
            num = num - (16 * m);
            for (int i = 0; i < hex.size(); i++) {
                if (num == i) {
                    returnOrder += hex.get(i);
                    break;
                }
            }
        }
        return returnOrder;
    }


    public void Add(String id, double balance) {
        boolean traderExists = false;
        for (int i = 0; i < myTraders.size(); i++) {
            if (id.equals(myTraders.get(i).getID())) {
                traderExists = true;
            }
        }
        if (traderExists) {
            System.out.println("Trader with given ID already exists.");
        }
        else if (balance < 0) {
            System.out.println("Initial balance cannot be negative.");
        }
        else {
            Trader newTrader = new Trader(id, balance);
            myTraders.add(newTrader);
            System.out.println("Success.");
        }
    }

    public void Balance(String id) {
        int traderIndex = 0;
        boolean traderExists = false;
        for (int i = 0; i < myTraders.size(); i++) {
            if (id.equals(myTraders.get(i).getID())) {
                traderExists = true;
                traderIndex += i;
                break;
            }
        }
        if (!traderExists) {
            System.out.println("No such trader in the market.");
        }
        else {
            double traderBalance = myTraders.get(traderIndex).getBalance();
            String traderBalanceAsString = df.format(traderBalance);
            String balanceLine = "$" + traderBalanceAsString;
            System.out.println(balanceLine);
        }
    }

    public void Inventory(String id) {
        int traderIndex = 0;
        boolean traderExists = false;
        for (int i = 0; i < myTraders.size(); i++) {
            if (id.equals(myTraders.get(i).getID())) {
                traderExists = true;
                traderIndex += i;
                break;
            }
        }
        if (!traderExists) {
            System.out.println("No such trader in the market.");
        }
        else {
            List<String> traderInventory = myTraders.get(traderIndex).getProductsInInventory();
            if (traderInventory.isEmpty() || traderInventory.size() == 0) {
                System.out.println("Trader has an empty inventory.");
            }
            else {
                for (int j = 0; j < traderInventory.size(); j ++) {
                    String newProduct = traderInventory.get(j);
                    System.out.println(newProduct);
                }
            }
        }
    }


    public void Amount(String id, String product) {
        int traderIndex = 0;
        boolean traderExists = false;
        for (int i = 0; i < myTraders.size(); i++) {
            if (id.equals(myTraders.get(i).getID())) {
                traderExists = true;
                traderIndex += i;
                break;
            }
        }
        if (!traderExists) {
            System.out.println("No such trader in the market.");
        }
        else {
            List<String> traderInventory = myTraders.get(traderIndex).getProductsInInventory();
            boolean productExists = false;
            for (int j = 0; j < traderInventory.size(); j ++) {
                String newProduct = traderInventory.get(j);
                if (product.equals(newProduct)) {
                    productExists = true;
                    break;
                }
            }
            if (!productExists) {
                System.out.println("Product not in inventory.");
            }
            else {
                double traderAmount = myTraders.get(traderIndex).getAmountStored(product);
                String traderAmountAsString = df.format(traderAmount);
                System.out.println(traderAmountAsString);
            }
        }
    }


    public void Sell(String id, String product, double amount, double price) {
        boolean traderExists = false;
        int traderIndex = 0;
        for (int i = 0; i < myTraders.size(); i++) {
            if (id.equals(myTraders.get(i).getID())) {
                traderExists = true;
                traderIndex += i;
                break;
            }
        }
        if (!traderExists) {
            System.out.println("No such trader in the market.");
        }
        else {
            Trader myTrader = myTraders.get(traderIndex);
            String orderID = MakeOrder(increment);
            increment ++;
            Order newSellOrder = new Order(product, false, amount, price, myTrader, orderID);
            boolean productMatch = false;
            for (int j = 0; j < myTrader.getProductsInInventory().size(); j ++) {
                if (product.equals(myTrader.getProductsInInventory().get(j))) {
                    productMatch = true;
                    break;
                }
            }
            if (productMatch) {
                List<Trade> mySellOrder = myMarket.placeSellOrder(newSellOrder);
                double newAmount = newSellOrder.getAmount();
                //System.out.println(newAmount);
                /*for (int k = 0; k < mySellOrder.size(); k++) {
                    double num = mySellOrder.get(k).getAmount();
                    newAmount -= num; 
                    System.out.println(newAmount);
                }*/
                if (mySellOrder.isEmpty() || mySellOrder.size() == 0) {
                    System.out.println("No trades could be made, order added to sell book.");
                    //mySellBook.add(newSellOrder);
                    myTrader.exportProduct(product, amount);
                }
                else if (newAmount == 0) {
                    System.out.println("Product sold in entirety, trades as follows:");
                    //myTrader.exportProduct(product, amount);
                    for (int k = 0; k < mySellOrder.size(); k++) {
                        Trade myTrade = mySellOrder.get(k);
                        String myTradeToString = myTrade.toString();
                        System.out.println(myTradeToString);
                        myTrades.add(myTrade);
                    }
                    //mySellBook.add(newSellOrder);
                    myTrader.exportProduct(product, amount);
                }
                else if ((newAmount != 0 && !mySellOrder.isEmpty()) || (newAmount != 0 && mySellOrder.size() != 0)){
                    System.out.println("Product sold in part, trades as follows:");
                    //System.out.println(mySellOrder.size());
                    for (int k = 0; k < mySellOrder.size(); k++) {
                        Trade myTrade = mySellOrder.get(k);
                        String myTradeToString = myTrade.toString();
                        System.out.println(myTradeToString);
                        myTrades.add(myTrade);
                    }
                    mySellBook.add(newSellOrder);
                    myTrader.exportProduct(product, amount);
                    //System.out.println(mySellBook.size());
                }
                else {
                    System.out.println("Order could not be placed onto the market.");
                }
            }
            else {
                System.out.println("Order could not be placed onto the market.");
            }
        }
    }


    public void Buy(String id, String product, double amount, double price) {
        boolean traderExists = false;
        int traderIndex = 0;
        for (int i = 0; i < myTraders.size(); i++) {
            if (id.equals(myTraders.get(i).getID())) {
                traderExists = true;
                traderIndex += i;
                break;
            }
        }
        if (!traderExists) {
            System.out.println("No such trader in the market.");
        }
        else {
            Trader myTrader = myTraders.get(traderIndex);
            String orderID = MakeOrder(increment);
            increment ++;
            Order newBuyOrder = new Order(product, true, amount, price, myTrader, orderID);
            boolean productMatch = false;
            List<Trade> myBuyOrder = myMarket.placeBuyOrder(newBuyOrder);
            if (myBuyOrder.isEmpty() || myBuyOrder.size() == 0) {
                System.out.println("No trades could be made, order added to buy book.");
                //myBuyBook.add(newBuyOrder);
            }
            else if (newBuyOrder.getAmount() == 0) {
                System.out.println("Product bought in entirety, trades as follows:");
                for (int k = 0; k < myBuyOrder.size(); k++) {
                    Trade myTrade = myBuyOrder.get(k);
                    String myTradeToString = myTrade.toString();
                    System.out.println(myTradeToString);
                    myTrades.add(myTrade);
                }
                //myBuyBook.add(newBuyOrder);
                myTrader.exportProduct(product, amount);
            }
            else if ((newBuyOrder.getAmount() != 0 && !myBuyOrder.isEmpty()) || (newBuyOrder.getAmount() != 0 && myBuyOrder.size() != 0)) {
                //System.out.println(
                System.out.println("Product bought in part, trades as follows:");
                for (int k = 0; k < myBuyOrder.size(); k++) {
                    Trade myTrade = myBuyOrder.get(k);
                    String myTradeToString = myTrade.toString();
                    System.out.println(myTradeToString);
                    myTrades.add(myTrade);
                }
                //myBuyBook.add(newBuyOrder);
                myTrader.exportProduct(product, amount);
            }
            else {
                System.out.println("Order could not be placed onto the market.");
            }
        }
    }


    public void Import(String id, String product, double amount) {
        boolean traderExists = false;
        int traderIndex = 0;
        for (int i = 0; i < myTraders.size(); i++) {
            if (id.equals(myTraders.get(i).getID())) {
                traderExists = true;
                traderIndex += i;
                break;
            }
        }
        if (!traderExists) {
            System.out.println("No such trader in the market.");
        }
        else if (amount <= 0) {
            System.out.println("Could not import product into market.");
        }
        else {
            Trader myTrader = myTraders.get(traderIndex);
            double updatedAmount = myTrader.importProduct(product, amount);
            String updatedAmountAsString = df.format(updatedAmount);
            String returnLine = "Trader now has " + updatedAmountAsString + " units of " + product + ".";
            System.out.println(returnLine);
        }
    }


    public void Export(String id, String product, double amount) {
        boolean traderExists = false;
        int traderIndex = 0;
        for (int i = 0; i < myTraders.size(); i++) {
            if (id.equals(myTraders.get(i).getID())) {
                traderExists = true;
                traderIndex += i;
                break;
            }
        }
        if (!traderExists) {
            System.out.println("No such trader in the market.");
        }
        else if (amount <= 0 || amount > myTraders.get(traderIndex).getAmountStored(product)) {
            System.out.println("Could not export product out of market.");
        }
        else {
            Trader myTrader = myTraders.get(traderIndex);
            double updatedAmount = myTrader.exportProduct(product, amount);
            if (updatedAmount != 0) {
                String updatedAmountAsString = df.format(updatedAmount);
                String returnLine = "Trader now has " + updatedAmountAsString + " units of " + product + ".";
                System.out.println(returnLine);
            }
            else {
                System.out.println("Trader now has no units of " + product + ".");
            }
        }
    }


    public void CancelSell(String order) {
        boolean orderExist = false;
        int orderIndex = 0;
        for (int i = 0; i < mySellBook.size(); i++) {
            Order nextOrder = mySellBook.get(i);
            if (order.equals(nextOrder.getID())) {
                orderExist = true;
                orderIndex += i;
                break;
            }
        }
        if (!orderExist) {
            System.out.println("No such order in sell book.");
        }
        else {
            Order getOrder = mySellBook.get(orderIndex);
            mySellBook.remove(order);
            System.out.println("Order successfully cancelled.");
            String orderProduct = getOrder.getProduct();
            double orderAmount = getOrder.getAmount();
            Trader orderTrader = getOrder.getTrader();
            orderTrader.importProduct(orderProduct, orderAmount);
        }
    }


    public void CancelBuy(String order) {
        boolean orderExist = false;
        int orderIndex = 0;
        for (int i = 0; i < myBuyBook.size(); i++) {
            if (order.equals(myBuyBook.get(i).getID())) {
                orderExist = true;
                orderIndex += i;
                break;
            }
        }
        if (!orderExist) {
            System.out.println("No such order in buy book.");
        }
        else {
            myBuyBook.remove(order);
            System.out.println("Order successfully cancelled.");
        }
    }


    public void OrderMethod(String order) {
        if (((myBuyBook.isEmpty() || myBuyBook.size() == 0)) && (mySellBook.isEmpty() || mySellBook.size() == 0)) {
            System.out.println("No orders in either book in the market.");
        }
        else {
            boolean orderPresentInBuy = false;
            int orderIndex = 0;
            for (int i = 0; i < myBuyBook.size(); i ++) {
                if (order.equals(myBuyBook.get(i).getID())) {
                    orderPresentInBuy = true;
                    orderIndex += i;
                    break;
                }
            }
            boolean orderPresentInSell = false;
            for (int j = 0; j < mySellBook.size(); j ++) {
                if (order.equals(mySellBook.get(j).getID())) {
                    orderPresentInSell = true;
                    orderIndex += j;
                    break;
                }
            }
            if (orderPresentInBuy) {
                Order myOrder = myBuyBook.get(orderIndex);
                String myOrderToString = myOrder.toString();
                System.out.println(myOrderToString);
            }
            else if (orderPresentInSell) {
                Order myOrder = mySellBook.get(orderIndex);
                String myOrderToString = myOrder.toString();
                System.out.println(myOrderToString);
            }
            else {
                System.out.println("Order is not present in either order book.");
            }
        }
    }


    public void Traders() {
        if (myTraders.isEmpty() || myTraders.size() == 0) {
            System.out.println("No traders in the market.");
        }
        else {
            List<String> myTradersID = new ArrayList<String>();
            for (int i = 0; i < myTraders.size(); i ++) {
                String newTraderID = myTraders.get(i).getID();
                myTradersID.add(newTraderID);
            }
            List<String> sortedTraders = myTradersID.stream().sorted().collect(Collectors.toList());
            for (int j = 0; j < sortedTraders.size(); j ++) {
                System.out.println(sortedTraders.get(j));
            }
        }
    }


    public void TradesMethod() {
        if (myTrades.isEmpty() || myTrades.size() == 0) {
            System.out.println("No trades have been completed.");
        }
        else {
            for (int i = 0; i < myTrades.size(); i++) {
                Trade newTrades = myTrades.get(i);
                String newTradesToString = newTrades.toString();
                System.out.println(newTradesToString);
            }
        }
    }


    public void TradesTrader(String id) {
        boolean traderExists = false;
        int traderIndex = 0;
        for (int i = 0; i < myTraders.size(); i++) {
            if (id.equals(myTraders.get(i).getID())) {
                traderExists = true;
                traderIndex += i;
                break;
            }
        }
        if (!traderExists) {
            System.out.println("No such trader in the market.");
        }
        else {
            List<Trade> traderTrades = new ArrayList<Trade>();
            Trader myTrader = myTraders.get(traderIndex);
            boolean traderInvolved = false;
            for (int j = 0; j < myTrades.size(); j++) {
                Trade nextTrade = myTrades.get(j);
                if (nextTrade.involvesTrader(myTrader)) {
                    traderInvolved = true;
                    traderTrades.add(nextTrade);
                    //System.out.println(nextTrade);
                }
            }
            if (!traderInvolved) {
                System.out.println("No trades have been completed by trader.");
            }
            else {
                for (int j = 0; j < traderTrades.size(); j++) {
                    Trade nextTrade = traderTrades.get(j);
                    String nextTradeToString = nextTrade.toString();
                    System.out.println(nextTradeToString);
                }
            }
        }
    }


    public void TradesProduct(String product) {
        boolean productExists = false;
        int productIndex = 0;
        for (int i = 0; i < myTraders.size(); i++) {
            Trader nextTrader = myTraders.get(i);
            List<String> traderProducts = nextTrader.getProductsInInventory();
            for (int j = 0; j < traderProducts.size(); j++) {
                if (product.equals(traderProducts.get(j))) {
                    productExists = true;
                    break;
                }
            }
            if (productExists) {
                productIndex += i;
                break;
            }
        }
        if (!productExists) {
            System.out.println("No trades have been completed with given product.");
        }
        else {
            List<Trade> productTrades = new ArrayList<Trade>();
            Trader myTrader = myTraders.get(productIndex);
            boolean productInvolved = false;
            for (int j = 0; j < myTrades.size(); j++) {
                Trade nextTrade = myTrades.get(j);
                if (product.equals(nextTrade.getProduct())) {
                    productInvolved = true;
                    productTrades.add(nextTrade);
                    //System.out.println(nextTrade);
                }
            }
            if (!productInvolved) {
                System.out.println("No trades have been completed with given product.");
            }
            else {
                for (int j = 0; j < productTrades.size(); j++) {
                    Trade nextTrade = productTrades.get(j);
                    String nextTradeToString = nextTrade.toString();
                    System.out.println(nextTradeToString);
                }
            }
        }
    }


    public void BookSell() {
        if (mySellBook.isEmpty() || mySellBook.size() == 0) {
            System.out.println("The sell book is empty.");
        }
        else { 
            //System.out.println(mySellBook.size());
            for (int i = 0; i < mySellBook.size(); i ++) {
                Order myOrder = mySellBook.get(i);
                String myOrderToString = myOrder.toString();
                System.out.println(myOrderToString);
            }
        }
    }


    public void BookBuy() {
        if (myBuyBook.isEmpty() || myBuyBook.size() == 0) {
            System.out.println("The buy book is empty.");
        }
        else { 
            for (int i = 0; i < myBuyBook.size(); i ++) {
                Order myOrder = myBuyBook.get(i);
                String myOrderToString = myOrder.toString();
                System.out.println(myOrderToString);
            }
        }
    }


    public void Save(String traderPath, String tradesPath) {

    }


    public void Binary(String traderPath, String tradesPath) {

    }


    public void CommandRun(String nextCommand) {
        String command = nextCommand.split(" ")[0];
        command = command.toLowerCase();
        if (command.equals("exit")) {
            System.out.println("Have a nice day.");
        }

        else if (command.equals("add")) {
            String newId = nextCommand.split(" ")[1];
            String newBalanceAsString = nextCommand.split(" ")[2];
            double newBalance = Double.parseDouble(newBalanceAsString);
            Add(newId, newBalance);
        }

        else if (command.equals("balance")) {
            String newId = nextCommand.split(" ")[1];
            Balance(newId);
        }

        else if (command.equals("inventory")) {
            String newId = nextCommand.split(" ")[1];
            Inventory(newId);
        }

        else if (command.equals("amount")) {
            String newId = nextCommand.split(" ")[1];
            String traderProduct = nextCommand.split(" ")[2];
            Amount(newId, traderProduct);
        }

        else if (command.equals("sell")) {
            String newId = nextCommand.split(" ")[1];
            String newProduct = nextCommand.split(" ")[2];
            String newAmountAsString = nextCommand.split(" ")[3];
            String newPriceAsString = nextCommand.split(" ")[4];
            double newAmount = Double.parseDouble(newAmountAsString);
            double newPrice = Double.parseDouble(newPriceAsString);
            Sell(newId, newProduct, newAmount, newPrice);
        }

        else if (command.equals("buy")) {
            String newId = nextCommand.split(" ")[1];
            String newProduct = nextCommand.split(" ")[2];
            String newAmountAsString = nextCommand.split(" ")[3];
            String newPriceAsString = nextCommand.split(" ")[4];
            double newAmount = Double.parseDouble(newAmountAsString);
            double newPrice = Double.parseDouble(newPriceAsString);
            Buy(newId, newProduct, newAmount, newPrice);
                    /*for (int i = 0; i < myBuyBook.size(); i++) {
                        System.out.println(myBuyBook.get(i));
                    }*/
        }

        else if (command.equals("import")) {
            String newId = nextCommand.split(" ")[1];
            String newProduct = nextCommand.split(" ")[2];
            String newAmountAsString = nextCommand.split(" ")[3];
            double newAmount = Double.parseDouble(newAmountAsString);
            Import(newId, newProduct, newAmount);
        }

        else if (command.equals("export")) {
            String newId = nextCommand.split(" ")[1];
            String newProduct = nextCommand.split(" ")[2];
            String newAmountAsString = nextCommand.split(" ")[3];
            double newAmount = Double.parseDouble(newAmountAsString);
            Export(newId, newProduct, newAmount);
        }

        else if (command.equals("traders")) {
            Traders();
        }

        else if (command.equals("trades") && nextCommand.split(" ").length == 1) {
            TradesMethod();
        }

        else if ((nextCommand.split(" ")[0].toLowerCase().equals("trades") && (nextCommand.split(" ")[1].toLowerCase().equals("trader") || nextCommand.split(" ")[1].toLowerCase().equals("product")))) {
            String secondCommand = nextCommand.split(" ")[1];
            secondCommand = secondCommand.toLowerCase();
            if (secondCommand.equals("trader")) {
                String newId = nextCommand.split(" ")[2];
                TradesTrader(newId);
            }
            else {
                String newProduct = nextCommand.split(" ")[2];
                TradesProduct(newProduct);
            }
        }

        else if (command.equals("book")) {
            String secondCommand = nextCommand.split(" ")[1];
            secondCommand = secondCommand.toLowerCase();
            if (secondCommand.equals("sell")) {
                BookSell();
            }
            else {
                BookBuy();
            }
        }

        else if (command.equals("cancel")) {
            String secondCommand = nextCommand.split(" ")[1];
            secondCommand = secondCommand.toLowerCase();
            String newOrderId = nextCommand.split(" ")[2];
            if (secondCommand.equals("sell")){
                CancelSell(newOrderId);
            }
            else {
                CancelBuy(newOrderId);
            }
        }
    }


    public void run() {
        // TODO
        Scanner scan = new Scanner(System.in);
        while (scan.hasNextLine()) {
            String nextCommand = scan.nextLine();
            System.out.print("$ ");
            String command = nextCommand.split(" ")[0];
            command = command.toLowerCase();
            if (command.equals("exit")) {
                System.out.println("Have a nice day.");
                break;
            }
            else {
                CommandRun(nextCommand);
            }
        }
    }

    public static void main(String[] args) {
        Exchange exchange = new Exchange();
        exchange.run();
    }
}
