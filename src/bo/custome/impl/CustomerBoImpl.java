package bo.custome.impl;

import bo.custome.CustomerBo;
import dao.DAOFactory;
import dao.custom.CustomerDAO;
import dto.CustomerDto;
import entity.Customer;
import javafx.collections.ObservableList;
import view.tm.CustomerTM;

import java.sql.SQLException;
import java.util.List;

public class CustomerBoImpl implements CustomerBo {

    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    @Override
    public boolean addCustomer(CustomerDto c) throws SQLException, ClassNotFoundException {
        return customerDAO.add(new Customer(c.getCustomerId(), c.getCustomerTitle(), c.getCustomerName(), c.getCustomerAddress(), c.getCustomerCity(), c.getProvince(), c.getPostalCode()));
    }

    @Override
    public List<CustomerTM> searchCustomer(String text) throws SQLException, ClassNotFoundException {
        return customerDAO.searchCustomer(text);
    }

    @Override
    public boolean deleteCustomer(String customerId) throws SQLException, ClassNotFoundException {
        return customerDAO.delete(customerId);
    }

    @Override
    public boolean updateCustomer(CustomerTM c) throws SQLException, ClassNotFoundException {
        return customerDAO.update(c);
    }

    @Override
    public ObservableList<CustomerTM> getCustomerList() throws SQLException, ClassNotFoundException {
        return customerDAO.getList();
    }

}
