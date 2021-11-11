package view.tm;

public class OrderDetailTM {
    private String itemCode;
    private String orderId;
    private int qtyOnSell;
    private int unitPrice;

    public OrderDetailTM(String itemCode, String orderId, int qtyOnSell, int unitPrice) {
        this.setItemCode(itemCode);
        this.setOrderId(orderId);
        this.setQtyOnSell(qtyOnSell);
        this.setUnitPrice(unitPrice);
    }

    public OrderDetailTM() {
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

    public int getQtyOnSell() {
        return qtyOnSell;
    }

    public void setQtyOnSell(int qtyOnSell) {
        this.qtyOnSell = qtyOnSell;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "OrderDetailTM{" +
                "itemCode='" + itemCode + '\'' +
                ", orderId='" + orderId + '\'' +
                ", qtyOnSell=" + qtyOnSell +
                ", unitPrice=" + unitPrice +
                '}';
    }
}

