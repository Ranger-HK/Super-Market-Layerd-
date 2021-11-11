package dao.custom;

import dao.CrudDAO;
import entity.OrderDetails;
import view.tm.OrderDetailTM;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderDetailsDAO extends CrudDAO<OrderDetails, String, OrderDetailTM> {

    boolean saveOrderDetails(String orderId, ArrayList<OrderDetails> items) throws SQLException, ClassNotFoundException;

    boolean updateOrder(OrderDetails o) throws SQLException, ClassNotFoundException;

}
