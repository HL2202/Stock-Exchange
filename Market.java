import java.util.List;
import java.util.ArrayList;
import java.lang.Math;


public class Market {

    private List<Order> buyBook;
    private List<Order> sellBook;
    private List<Trade> trades;


    public Market() {

        this.buyBook = new ArrayList<Order>();
        this.sellBook = new ArrayList<Order>();
        this.trades = new ArrayList<Trade>();
    }


    public List<Trade> placeSellOrder(Order order) {

        if (order != null && !order.isBuy()) {

            Trader seller = order.getTrader();
            String productSold = order.getProduct();
            List<Trade> listOfTradesSell = new ArrayList<Trade>();
            List<Order> buyBookStored = new ArrayList<Order>();

            if (order.getAmount() <= seller.getAmountStored(productSold)) {

                if (buyBook != null && sellBook != null) {

                    if (!buyBook.isEmpty()) {

                        if (buyBook.size() > 0) {

                            for (int j = 0; j < buyBook.size(); j++) {

                                String nextProduct = buyBook.get(j).getProduct();
                                Trader nextTrader = buyBook.get(j).getTrader();
                                boolean productStored = false;

                                for (int k = 0; k < buyBookStored.size(); k++) {

                                    if (nextProduct.equals(buyBookStored.get(k).getProduct()) && nextTrader.getID().equals(buyBookStored.get(k).getTrader().getID())) {

                                        productStored = true;
                                        break;
                                    }
                                }

                                if (productSold.equals(nextProduct) && !productStored) {

                                    buyBookStored.add(buyBook.get(j));
                                }
                            }

                            while (buyBookStored.size() > 0 && order.getAmount() > 0) {

                                double maxPrice = buyBookStored.get(0).getPrice();
                                int positionOfMaxPrice = 0;

                                if (buyBookStored.size() != 1) {

                                    for (int i = 0; i < buyBookStored.size(); i ++) {

                                        Order newOrder = buyBookStored.get(i);
                                        double priceOfOrder = newOrder.getPrice();

                                        if (priceOfOrder > maxPrice && priceOfOrder != 0) {

                                            maxPrice = 0;
                                            maxPrice += priceOfOrder;
                                            positionOfMaxPrice = 0;
                                            positionOfMaxPrice += i;
                                        }
                                    }
                                }

                                else {

                                    positionOfMaxPrice = 0;
                                }

                                if (maxPrice < order.getPrice()) {

                                    break;
                                }

                                else {

                                    Order topOrder = buyBookStored.get(positionOfMaxPrice);
                                    String newProductEntry = topOrder.getProduct();
                                    double newAmountEntry = topOrder.getAmount();
                                    double newPriceEntry = topOrder.getPrice();
                                    double amountAvailable = order.getAmount();

                                    if (newAmountEntry > order.getAmount()) {

                                        Trade newTradeEntry = new Trade(newProductEntry, amountAvailable, newPriceEntry, order, topOrder);
                                        listOfTradesSell.add(newTradeEntry);
                                        Trader buyer = topOrder.getTrader();
                                        double balanceToAdd = amountAvailable * newPriceEntry;
                                        double balanceToRemove = -balanceToAdd;
                                        seller.adjustBalance(balanceToAdd);
                                        buyer.adjustBalance(balanceToRemove);
                                        buyer.importProduct(newProductEntry, amountAvailable);

                                        double amountToRemove = -amountAvailable;
                                        order.adjustAmount(amountToRemove);
                                        double amountRemaining = newAmountEntry - amountAvailable;
                                        String buyerID = buyer.getID();
                                        Order updatedOrder = new Order(newProductEntry, true, amountRemaining, newPriceEntry, buyer, buyerID);

                                        for (int k = 0; k < buyBook.size(); k++) {

                                            if (buyerID.equals(buyBook.get(k).getTrader().getID()) && newTradeEntry.equals(buyBook.get(k).getProduct())) {

                                                buyBook.set(k, updatedOrder);
                                                break;
                                            }
                                        }

                                        buyBookStored.set(positionOfMaxPrice, updatedOrder);
                                    }

                                    else {

                                        Trade newTradeEntry = new Trade(newProductEntry, newAmountEntry, newPriceEntry, order, topOrder);
                                        listOfTradesSell.add(newTradeEntry);
                                        Trader buyer = topOrder.getTrader();
                                        double balanceToAdd = newAmountEntry * newPriceEntry;
                                        double balanceToRemove = -balanceToAdd;
                                        seller.adjustBalance(balanceToAdd);
                                        buyer.adjustBalance(balanceToRemove);
                                        buyer.importProduct(newProductEntry, newAmountEntry);

                                        double amountToRemove = -newAmountEntry;
                                        order.adjustAmount(amountToRemove);
                                        String buyerID = buyer.getID();
                                        buyBookStored.remove(topOrder);

                                        for (int k = 0; k < buyBook.size(); k++) {

                                            if (buyerID.equals(buyBook.get(k).getTrader().getID()) && newTradeEntry.equals(buyBook.get(k).getProduct())) {

                                                Order orderToRemove = buyBook.get(k);
                                                buyBook.remove(orderToRemove);
                                                break;
                                            }
                                        }
                                    }

                                    double amountRemoved = amountAvailable;
                                    for (int k = 0; k < listOfTradesSell.size(); k++) {

                                        double num = listOfTradesSell.get(k).getAmount();
                                        amountRemoved -= num; 
                                    }
                                    double finalAmount = amountAvailable - amountRemoved;
                                    seller.exportProduct(newProductEntry, finalAmount);
                                }
                            }

                            if (order.getAmount() == 0){

                                String sellerID = seller.getID();
                                for (int m = 0; m < sellBook.size(); m++) {

                                    if (sellerID.equals(sellBook.get(m).getTrader().getID()) && productSold.equals(sellBook.get(m).getProduct())) {

                                        Order orderToRemove = sellBook.get(m);
                                        sellBook.remove(orderToRemove);
                                        break;
                                    }
                                }
                                order.close();
                            }

                            return listOfTradesSell;
                        }

                        else {

                            sellBook.add(order);
                            return listOfTradesSell;
                        }
                    }

                    else {

                        sellBook.add(order);
                        return listOfTradesSell;
                    }
                }

                else {

                    sellBook.add(order);
                    return listOfTradesSell;
                }
            }

            else {

                return null;
            }
        }

        else {

            return null;
        }
    }


    public List<Trade> placeBuyOrder(Order order) {

        if (order != null && order.isBuy()) {

            Trader buyer = order.getTrader();
            String productBought = order.getProduct();
            List<Trade> listOfTradesBuy = new ArrayList<Trade>();
            List<Order> sellBookStored = new ArrayList<Order>();

            if (buyBook != null && sellBook != null) {

                if (!sellBook.isEmpty()) {

                    if (sellBook.size() > 0) {

                        for (int j = 0; j < sellBook.size(); j++) {

                            String nextProduct = sellBook.get(j).getProduct();
                            Trader nextTrader = sellBook.get(j).getTrader();
                            boolean productStored = false;

                            for (int k = 0; k < sellBookStored.size(); k++) {

                                if (nextProduct.equals(sellBookStored.get(k).getProduct()) && nextTrader.getID().equals(sellBookStored.get(k).getTrader().getID())) {

                                    productStored = true;
                                    break;
                                }
                            }

                            if (productBought.equals(nextProduct) && !productStored) {

                                sellBookStored.add(sellBook.get(j));
                            }
                        }

                        while (sellBookStored.size() > 0 && order.getAmount() > 0) {

                            double minPrice = sellBookStored.get(0).getPrice();
                            int positionOfMinPrice = 0;

                            if (sellBookStored.size() != 1) {

                                for (int i = 0; i < sellBookStored.size(); i ++) {

                                    Order newOrder = sellBookStored.get(i);
                                    double priceOfOrder = newOrder.getPrice();

                                    if (priceOfOrder < minPrice && priceOfOrder != 0) {

                                        minPrice = Math.min(minPrice, priceOfOrder);
                                        positionOfMinPrice = 0;
                                        positionOfMinPrice += i;
                                    }
                                }
                            }

                            else {

                                positionOfMinPrice = 0;
                            }

                            if (minPrice > order.getPrice()) {

                                break;
                            }

                            else {
                                Order topOrder = sellBookStored.get(positionOfMinPrice);
                                String newProductEntry = topOrder.getProduct();
                                double newAmountEntry = topOrder.getAmount();
                                double newPriceEntry = topOrder.getPrice();
                                double amountAvailable = order.getAmount();

                                if (newAmountEntry > order.getAmount()) {
                                    Trade newTradeEntry = new Trade(newProductEntry, amountAvailable, newPriceEntry, order, topOrder);
                                    listOfTradesBuy.add(newTradeEntry);
                                    Trader seller = topOrder.getTrader();
                                    double balanceToAdd = amountAvailable * newPriceEntry;
                                    double balanceToRemove = -balanceToAdd;
                                    seller.adjustBalance(balanceToAdd);
                                    buyer.adjustBalance(balanceToRemove);

                                    seller.exportProduct(newProductEntry, amountAvailable);
                                    double amountToRemove = -amountAvailable;
                                    order.adjustAmount(amountToRemove);
                                    double amountRemaining = newAmountEntry - amountAvailable;
                                    String sellerID = seller.getID();
                                    Order updatedOrder = new Order(newProductEntry, false, amountRemaining, newPriceEntry, seller, sellerID);

                                    for (int k = 0; k < sellBook.size(); k++) {

                                        if (sellerID.equals(sellBook.get(k).getTrader().getID()) && newTradeEntry.equals(sellBook.get(k).getProduct())) {

                                            sellBook.set(k, updatedOrder);
                                            break;
                                        }
                                    }

                                    sellBookStored.set(positionOfMinPrice, updatedOrder);
                                }

                                else {
                                    Trade newTradeEntry = new Trade(newProductEntry, newAmountEntry, newPriceEntry, order, topOrder);
                                    listOfTradesBuy.add(newTradeEntry);
                                    Trader seller = topOrder.getTrader();
                                    double balanceToAdd = newAmountEntry * newPriceEntry;
                                    double balanceToRemove = -balanceToAdd;
                                    seller.adjustBalance(balanceToAdd);
                                    buyer.adjustBalance(balanceToRemove);

                                    seller.exportProduct(newProductEntry, newAmountEntry);
                                    double amountToRemove = -newAmountEntry;
                                    order.adjustAmount(amountToRemove);
                                    sellBookStored.remove(topOrder);
                                    String sellerID = seller.getID();

                                    for (int k = 0; k < sellBook.size(); k++) {

                                        if (sellerID.equals(sellBook.get(k).getTrader().getID()) && newTradeEntry.equals(sellBook.get(k).getProduct())) {

                                            Order orderToRemove = sellBook.get(k);
                                            sellBook.remove(orderToRemove);
                                            break;
                                        }
                                    }
                                }

                                double amountTotal = 0;

                                for (int k = 0; k < listOfTradesBuy.size(); k++) {

                                    double num = listOfTradesBuy.get(k).getAmount();
                                    amountTotal += num; 
                                }

                                buyer.importProduct(newProductEntry, amountTotal);
                            }
                        }

                        if (order.getAmount() == 0) {

                            String buyerID = buyer.getID();

                            for (int m = 0; m < buyBook.size(); m++) {

                                if (buyerID.equals(buyBook.get(m).getTrader().getID()) && productBought.equals(buyBook.get(m).getProduct())) {

                                    Order orderToRemove = buyBook.get(m);
                                    buyBook.remove(orderToRemove);
                                    break;
                                }
                            }
                            order.close();
                        }

                        return listOfTradesBuy;
                    }

                    else {

                        buyBook.add(order);
                        return listOfTradesBuy;
                    }
                }

                else {

                    buyBook.add(order);
                    return listOfTradesBuy;
                }
            }

            else {

                return listOfTradesBuy;
            }
        }

        else {

            return null;
        }
    }


    public boolean cancelBuyOrder(String order) {

        if (order != null) {

            if (buyBook != null && !buyBook.isEmpty()) {

                if (buyBook.size() > 0) {

                    for (int i = 0; i < buyBook.size(); i++) {

                        if (order.equals(buyBook.get(i).getID())) {

                            Order removeOrder = buyBook.get(i);
                            buyBook.remove(removeOrder);
                            return true;
                        }
                    }

                    return false;
                }

                else {

                    return false;
                }
            }

            else {

                return false;
            }
        }

        else {

            return false;
        }
    }


    public boolean cancelSellOrder(String order) {

        if (order != null) {

            if (sellBook != null && !sellBook.isEmpty()) {

                if (sellBook.size() > 0) {

                    for (int i = 0; i < sellBook.size(); i++) {

                        if (order.equals(sellBook.get(i).getID())) {

                            Order removeOrder = sellBook.get(i);
                            sellBook.remove(removeOrder);
                            return true;
                        }
                    }

                    return false;
                }

                else {

                    return false;
                }
            }

            else {

                return false;
            }
        }

        else {

            return false;
        }
    }


    public List<Order> getSellBook() {

        if (sellBook != null && !sellBook.isEmpty()) {

            if (sellBook.size() > 0) {

                return this.sellBook;
            }

            else {

                return this.sellBook;
            }
        }

        else {

            return this.sellBook;
        }
    }


    public List<Order> getBuyBook() {

        if (buyBook != null && !buyBook.isEmpty()) {

            if (buyBook.size() > 0) {

                return this.buyBook;
            }

            else {

                return this.buyBook;
            }
        }

        else {

            return this.buyBook;
        }
    }


    public List<Trade> getTrades() {

        if (trades != null && !trades.isEmpty()) {

            if (trades.size() > 0) {

                return this.trades;
            }

            else {

                return null;
            }
        }

        else {

            return null;
        }
    }


    public static List<Trade> filterTradesByTrader(List<Trade> trades, Trader trader) {

        if (trades != null && trader != null) {

            if (!trades.isEmpty()) {

                if (trades.size() > 0) {

                    List<Trade> filteredListByTrader = new ArrayList<Trade>();

                    for (int i = 0; i < trades.size(); i ++) {

                        Trade newTrade = trades.get(i);

                        if (newTrade.involvesTrader(trader)) {

                            filteredListByTrader.add(newTrade);
                        }
                    }

                    return filteredListByTrader;
                }

                else {

                    return null;
                }
            }

            else {

                return null;
            }
        }

        else {
            
            return null;
        }
    }


    public static List<Trade> filterTradesByProduct(List<Trade> trades, String product) {

        if (trades != null && product != null) {
            
            if (!trades.isEmpty()) {

                if (trades.size() > 0) {

                    List<Trade> filteredListByProduct = new ArrayList<Trade>();

                    for (int i = 0; i < trades.size(); i ++) {

                        Trade newTrade = trades.get(i);

                        if (newTrade.getProduct() == product) {

                            filteredListByProduct.add(newTrade);
                        }
                    }

                    return filteredListByProduct;
                }

                else {

                    return null;
                }
            }

            else {

                return null;
            }
        }

        else {

            return null;
        }
    }
}