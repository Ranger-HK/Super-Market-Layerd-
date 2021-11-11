package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.CustomerDAO;
import db.DbConnection;
import dto.CustomerDto;
import entity.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import view.tm.CustomerTM;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public boolean add(Customer c) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("INSERT INTO Customer VALUES(?,?,?,?,?,?,?)", c.getId(), c.getCustTitle(), c.getName(), c.getAddress(), c.getCity(), c.getProvince(), c.getPostalCode());

    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Customer WHERE id='" + s + "'").executeUpdate() > 0;

    }

    @Override
    public boolean update(CustomerTM c) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("UPDATE Customer SET  custTitle=?,name=?,address=?,city=?,province=?,postalCode=? WHERE id=?", c.getCustomerTitle(), c.getCustomerName(), c.getCustomerAddress(), c.getCustomerCity(), c.getProvince(), c.getPostalCode(), c.getCustomerId());

    }

    @Override
    public ObservableList<CustomerTM> getList() throws SQLException, ClassNotFoundException {
        ObservableList<CustomerTM> obList = FXCollections.observableArrayList();
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Customer");
        while (rst.next()) {
            obList.add(new CustomerTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return obList;
    }

    @Override
    public CustomerDto passCustomerDetails(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Customer WHERE  id=?", id);
        if (rst.next()) {
            return new CustomerDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            );
        } else {
            return null;
        }
    }

    @Override
    public List<String> getAllCustomerIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT  * FROM Customer");
        List<String> ids = new ArrayList<>();
        while (rst.next()) {
            ids.add(rst.getString(1)
            );
        }
        return ids;
    }


    @Override
    public List<CustomerTM> searchCustomer(String value) throws SQLException, ClassNotFoundException {
        List<CustomerTM> list = new ArrayList<>();
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Customer WHERE CONCAT(name,address,city,province,postalCode,id) LIKE '%" + value + "%'");
        while (rst.next()) {
            list.add(
                    new CustomerTM(
                            rst.getString(1),
                            rst.getString(2),
                            rst.getString(3),
                            rst.getString(4),
                            rst.getString(5),
                            rst.getString(6),
                            rst.getString(7)
                    )
            );
        }
        return list;
    }
}
