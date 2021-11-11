package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.OrderDetailsDAO;
import entity.OrderDetails;
import javafx.collections.ObservableList;
import view.tm.OrderDetailTM;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailsDAOImpl implements OrderDetailsDAO {

    @Override
    public boolean add(OrderDetails orderDetails) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(OrderDetailTM t) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public ObservableList<OrderDetailTM> getList() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean saveOrderDetails(String orderId, ArrayList<OrderDetails> items) throws SQLException, ClassNotFoundException {
        for (OrderDetails temp : items
        ) {
            if (CrudUtil.executeUpdate("INSERT INTO `order detail` VALUES(?,?,?,?)", temp.getItemCode(), orderId, temp.getOrderQTY(), temp.getDiscount())) {
                if (new ItemDAOImpl().updateQty(temp.getItemCode(), temp.getOrderQTY())) {

                } else {
                    return false;
                }

            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean updateOrder(OrderDetails o) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("UPDATE `order detail` SET orderQTY=(orderQTY-?) WHERE orderId=? AND itemCode=?", o.getOrderQTY(), o.getOrderId(), o.getItemCode());

    }
}
