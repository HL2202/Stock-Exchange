import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class Trade {
    
    private double amount;
    private Order buyOrder;
    private double price;
    private String product;
    private Order sellOrder;
    private Trader seller;
    private Trader buyer;

    public Trade(String product, double amount, double price, Order sellOrder, Order buyOrder) {
        this.product = product;
        this.amount = amount;
        this.price = price;
        this.sellOrder = sellOrder;
        this.buyOrder = buyOrder;
        this.seller = seller;
        this.buyer = buyer;
    }

    public String getProduct() {
        if (product != null) {
            return this.product;
        }
        else {
            return null;
        }
    }

    public double getAmount() {
        if (amount > 0) {
            return this.amount;
        }
        else {
            return 0.0;
        }
    }

    public Order getSellOrder() {
        if (sellOrder != null && !sellOrder.isBuy()) {
            return this.sellOrder;
        }
        else {
            return this.sellOrder;
        }
    }

    public Order getBuyOrder() {
        if (buyOrder != null) {
            return this.buyOrder;
        }
        else {
            return null;
        }
    }

    public double getPrice() {
        if (price > 0) {
            return this.price;
        }
        else {
            return 0.0;
        }
    }

    public String toString() {
        if (product != null && amount > 0 && price > 0) {
            DecimalFormat df = new DecimalFormat("0.00");
            String amountAsString = df.format(amount);
            String priceAsString = df.format(price);
            String sellerName = getSellOrder().getTrader().getID();
            String buyerName = getBuyOrder().getTrader().getID();
            String out = sellerName + "->" + buyerName + ": " + amountAsString + "x" + product + " for $" + priceAsString + ".";
            return out;
        }
        else {
            return null;
        }
    }

    public boolean involvesTrader(Trader trader) {
        if (trader != null) {
            if (trader.equals(getSellOrder().getTrader()) || trader.equals(getBuyOrder().getTrader())) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public static void writeTrades(List<Trade> trades, String path) {
        if (trades != null) {
            if (path != null && trades.size() > 0) {
                try {
                    FileWriter newEntry = new FileWriter(path);
                    for (int i = 0; i < trades.size(); i++) {
                        Trade newTrade = trades.get(i);
                        String tradeRecord = newTrade.toString();
                        newEntry.write(tradeRecord.concat("\r\n"));
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

    public static void writeTradesBinary(List<Trade> trades, String path) {
        if (trades != null) {
            if (path != null && trades.size() > 0) {
                try {
                    FileOutputStream newEntry = new FileOutputStream(path);
                    DataOutputStream output = new DataOutputStream(newEntry);
                    for (int i = 0; i < trades.size(); i++) {
                        Trade newTrade = trades.get(i);
                        String tradeRecord = newTrade.toString();
                        output.writeUTF(tradeRecord);
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

