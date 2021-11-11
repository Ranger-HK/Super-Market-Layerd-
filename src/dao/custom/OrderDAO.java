package dao.custom;

import dao.CrudDAO;
import entity.Order;
import view.tm.CartTM;

import java.sql.SQLException;
import java.util.List;

public interface OrderDAO extends CrudDAO<Order, String, CartTM> {

    String getOrderID() throws SQLException, ClassNotFoundException;

    List<String> getAllOrderIds() throws SQLException, ClassNotFoundException;


}
