package entity;

public class OrderDetails {
    private String itemCode;
    private String orderId;
    private int orderQTY;
    private double discount;

    public OrderDetails() {
    }

    public OrderDetails(String itemCode, String orderId, int orderQTY, double discount) {
        this.setItemCode(itemCode);
        this.setOrderId(orderId);
        this.setOrderQTY(orderQTY);
        this.setDiscount(discount);
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderQTY() {
        return orderQTY;
    }

    public void setOrderQTY(int orderQTY) {
        this.orderQTY = orderQTY;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
