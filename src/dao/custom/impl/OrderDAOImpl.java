package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.OrderDAO;
import entity.Order;
import javafx.collections.ObservableList;
import view.tm.CartTM;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public boolean add(Order order) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("INSERT INTO `order` Values(?,?,?)", order.getOrderId(), order.getOrderDate(), order.getcId());

    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(CartTM t) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public ObservableList<CartTM> getList() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public String getOrderID() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM `order` ORDER BY orderId DESC LIMIT 1");
        if (rst.next()) {
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId = tempId + 1;

            if (tempId < 10) {
                return "O-00" + tempId;

            } else if (tempId < 100) {
                return "O-0" + tempId;

            } else {
                return "O-" + tempId;
            }
        } else {
            return "O-001";
        }
    }

    @Override
    public List<String> getAllOrderIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM `Order`");
        List<String> ids = new ArrayList<>();
        while (rst.next()) {
            ids.add(
                    rst.getString(1)
            );
        }
        return ids;
    }
}
