package bo.custome;

import bo.SuperBo;
import dto.OrderDetailsDto;
import javafx.collections.ObservableList;
import view.tm.CartTM;

import java.sql.SQLException;
import java.util.List;

public interface ManageCustomerOrderBo extends SuperBo {
    ObservableList<CartTM> getOrderList(String oId) throws SQLException, ClassNotFoundException;

    boolean updateOrder(OrderDetailsDto c1) throws SQLException;

    List<String> getAllOrderIds() throws SQLException, ClassNotFoundException;

}
