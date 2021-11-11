package dao.custom;

import dao.CrudDAO;
import dto.ItemDto;
import entity.Item;
import view.tm.ItemTM;

import java.sql.SQLException;
import java.util.List;

public interface ItemDAO extends CrudDAO<Item, String, ItemTM> {

    List<String> getAllIds() throws SQLException, ClassNotFoundException;

    ItemDto getItem(String id) throws SQLException, ClassNotFoundException;

    boolean updateQty(String itemCode, int qty) throws SQLException, ClassNotFoundException;

    boolean updateReturnQty(String itemCode, int qty) throws SQLException, ClassNotFoundException;


}
