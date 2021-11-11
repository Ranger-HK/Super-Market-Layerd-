package dao.custom;

import dao.CrudDAO;
import dto.CustomerDto;
import entity.Customer;
import view.tm.CustomerTM;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO extends CrudDAO<Customer, String, CustomerTM> {

    CustomerDto passCustomerDetails(String id) throws SQLException, ClassNotFoundException;

    List<String> getAllCustomerIds() throws SQLException, ClassNotFoundException;

    List<CustomerTM> searchCustomer(String value) throws SQLException, ClassNotFoundException;

}
