package dao.custom;

import dao.SuperDAO;
import javafx.collections.ObservableList;
import view.tm.CartTM;

import java.sql.SQLException;

public interface QueryDAO extends SuperDAO {

    ObservableList<CartTM> getOrderList(String oId) throws SQLException, ClassNotFoundException;
}
