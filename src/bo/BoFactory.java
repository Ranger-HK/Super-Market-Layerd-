package bo;

import bo.custome.impl.CustomerBoImpl;
import bo.custome.impl.ItemBoImpl;
import bo.custome.impl.ManageCustomerOrderBoImpl;
import bo.custome.impl.PurchaseOrderBoImpl;

public class BoFactory {
    private static BoFactory boFactory;

    private BoFactory() {
    }

    public static BoFactory getBOFactory() {
        if (boFactory == null) {
            boFactory = new BoFactory();
        }
        return boFactory;
    }

    public SuperBo getBO(BoTypes types) {
        switch (types) {
            case ITEM:
                return new ItemBoImpl();
            case CUSTOMER:
                return new CustomerBoImpl();
            case PURCHASE_ORDER:
                return new PurchaseOrderBoImpl();
            case MANAGE_ORDER:
                return new ManageCustomerOrderBoImpl();
            default:
                return null;
        }
    }

    public enum BoTypes {
        CUSTOMER, ITEM, PURCHASE_ORDER, MANAGE_ORDER
    }

}
