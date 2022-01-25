import java.text.DecimalFormat;


public class Order {

    private String product;
    private boolean buy;
    private double amount;
    private double price;
    private String id;
    private Trader trader;
    private boolean closed;


    public Order(String product, boolean buy, double amount, double price, Trader trader, String id) {

        this.product = product;
        this.buy = buy;
        this.amount = amount;
        this.price = price;
        this.id = id;
        this.trader = trader;
        this.closed = false;
    }


    public String getProduct() {

        if (product != null) {

            return this.product;
        }

        else {

            return null;
        }
    }


    public boolean isBuy() {

        return this.buy;
    }


    public double getAmount() {

        if (amount > 0) {

            return this.amount;
        }

        else {

            return 0.0;
        }
    }


    public Trader getTrader() {

        return this.trader;
    }


    public void close() {

        this.closed = true;
    }


    public boolean isClosed() {

        if (closed == true) {

            return true;
        }

        else {

            return false;
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


    public String getID() {

        if (id != null) {

            return this.id;
        }

        else {

            return null;
        }
    }


    public void adjustAmount(double change) {

        if (change != 0 && (amount + change) > 0) {

            this.amount += change;
        }
    }


    public String toString() {

        if (amount > 0 && product != null && price > 0 && id != null) {

            DecimalFormat df = new DecimalFormat("0.00");
            String amountAsString = df.format(amount);
            String priceAsString = df.format(price);

            if (buy == true) {

                String out = id + ": " + "BUY " + amountAsString + "x" + product + " @ $" + priceAsString;
                return out;
            }

            else {

                String out = id + ": " + "SELL " + amountAsString + "x" + product.toString() + " @ $" + priceAsString;
                return out;
            }
        }

        else {

            return null;
        }
    }   
}