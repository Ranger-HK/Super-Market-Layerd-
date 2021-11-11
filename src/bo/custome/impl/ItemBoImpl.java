package bo.custome.impl;

import bo.custome.ItemBo;
import dao.DAOFactory;
import dao.custom.ItemDAO;
import dto.ItemDto;
import entity.Item;
import view.tm.ItemTM;

import java.sql.SQLException;

public class ItemBoImpl implements ItemBo {

    private final ItemDAO itemDAO = (ItemDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public boolean updateItem(ItemTM i1) throws SQLException, ClassNotFoundException {
        return itemDAO.update(i1);
    }

    @Override
    public boolean deleteItem(String itemCode) throws SQLException, ClassNotFoundException {
        return itemDAO.delete(itemCode);
    }

    @Override
    public boolean addItem(ItemDto i) throws SQLException, ClassNotFoundException {
        return itemDAO.add(new Item(i.getItemCode(), i.getDescription(), i.getPackSize(), i.getUnitPrice(), i.getQtyOnHand(), i.getDiscount()));
    }
}
