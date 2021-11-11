package entity;

public class Order {
    private String orderId;
    private String orderDate;
    private String cId;

    public Order() {
    }

    public Order(String orderId, String orderDate, String cId) {
        this.setOrderId(orderId);
        this.setOrderDate(orderDate);
        this.setcId(cId);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }
}
