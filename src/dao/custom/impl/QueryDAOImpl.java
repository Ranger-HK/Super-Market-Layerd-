package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.QueryDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import view.tm.CartTM;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryDAOImpl implements QueryDAO {

    @Override
    public ObservableList<CartTM> getOrderList(String oId) throws SQLException, ClassNotFoundException {
        ObservableList<CartTM> obList = FXCollections.observableArrayList();
        ResultSet rst = CrudUtil.executeQuery("SELECT od.*, round(((100-od.Discount)*(i.UnitPrice*od.OrderQty))/100,2) AS Total, i.unitPrice FROM `order detail` od INNER JOIN item i ON od.ItemCode=i.ItemCode WHERE od.orderId='" + oId + "'");
        while (rst.next()) {
            obList.add(new CartTM(
                    rst.getString(1),
                    rst.getInt(3),
                    rst.getDouble(6),
                    rst.getInt(4),
                    rst.getDouble(5),
                    rst.getString(2)

            ));
        }
        return obList;
    }


}
