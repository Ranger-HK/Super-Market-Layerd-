package bo.custome;

import bo.SuperBo;
import dto.CustomerDto;
import javafx.collections.ObservableList;
import view.tm.CustomerTM;

import java.sql.SQLException;
import java.util.List;

public interface CustomerBo extends SuperBo {

    boolean addCustomer(CustomerDto c) throws SQLException, ClassNotFoundException;

    List<CustomerTM> searchCustomer(String text) throws SQLException, ClassNotFoundException;

    boolean deleteCustomer(String customerId) throws SQLException, ClassNotFoundException;

    boolean updateCustomer(CustomerTM c) throws SQLException, ClassNotFoundException;

    ObservableList<CustomerTM> getCustomerList() throws SQLException, ClassNotFoundException;

}
