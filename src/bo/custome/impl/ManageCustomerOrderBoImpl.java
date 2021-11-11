package bo.custome.impl;

import bo.custome.ManageCustomerOrderBo;
import dao.DAOFactory;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailsDAO;
import dao.custom.QueryDAO;
import db.DbConnection;
import dto.OrderDetailsDto;
import entity.OrderDetails;
import javafx.collections.ObservableList;
import view.tm.CartTM;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ManageCustomerOrderBoImpl implements ManageCustomerOrderBo {

    private final OrderDAO orderDAO = (OrderDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    private final OrderDetailsDAO orderDetailsDAO = (OrderDetailsDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAILS);
    private final QueryDAO queryDAO = (QueryDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.QUERY);
    private final ItemDAO itemDAO = (ItemDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ITEM);


    @Override
    public ObservableList<CartTM> getOrderList(String oId) throws SQLException, ClassNotFoundException {
        return queryDAO.getOrderList(oId);
    }

    @Override
    public boolean updateOrder(OrderDetailsDto o) throws SQLException {
        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            if (orderDetailsDAO.updateOrder(new OrderDetails(o.getItemCode(), o.getOrderId(), o.getQtyOnSell(), o.getDiscount()))) {

                if (itemDAO.updateReturnQty(o.getItemCode(), o.getQtyOnSell())) {
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
    public List<String> getAllOrderIds() throws SQLException, ClassNotFoundException {
        return orderDAO.getAllOrderIds();
    }
}
