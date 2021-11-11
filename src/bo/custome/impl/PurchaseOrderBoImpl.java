package bo.custome.impl;

import bo.custome.PurchaseOrderBo;
import dao.DAOFactory;
import dao.custom.CustomerDAO;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailsDAO;
import db.DbConnection;
import dto.CustomerDto;
import dto.ItemDto;
import dto.OrderDto;
import entity.Order;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PurchaseOrderBoImpl implements PurchaseOrderBo {

    private final ItemDAO itemDAO = (ItemDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    private final OrderDAO orderDAO = (OrderDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    private final OrderDetailsDAO orderDetailsDAO = (OrderDetailsDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAILS);

    @Override
    public boolean purchaseOrder(OrderDto orderDto) throws SQLException, ClassNotFoundException {
        Connection con = null;
        con = DbConnection.getInstance().getConnection();
        try {
            con.setAutoCommit(false);
            if (orderDAO.add(new Order(orderDto.getOrderId(), orderDto.getOrderDate(), orderDto.getCustomerId()))) {

                if (orderDetailsDAO.saveOrderDetails(orderDto.getOrderId(), orderDto.getItems())) {
                    con.commit();
                    return true;
                } else {
                    con.rollback();
                    return false;
                }

            } else {
                con.rollback();
                return false;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            {
                con.setAutoCommit(true);
            }

        }
        return false;
    }

    @Override
    public List<String> getAllIds() throws SQLException, ClassNotFoundException {
        return itemDAO.getAllIds();

    }

    @Override
    public ItemDto getItem(String itemCode) throws SQLException, ClassNotFoundException {
        return itemDAO.getItem(itemCode);
    }

    @Override
    public String getOrderID() throws SQLException, ClassNotFoundException {
        return orderDAO.getOrderID();
    }

    @Override
    public List<String> getAllCustomerIds() throws SQLException, ClassNotFoundException {
        return customerDAO.getAllCustomerIds();

    }

    @Override
    public CustomerDto passCustomerDetails(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.passCustomerDetails(id);
    }

}
