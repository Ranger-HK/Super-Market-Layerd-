package bo.custome;

import bo.SuperBo;
import dto.ItemDto;
import view.tm.ItemTM;

import java.sql.SQLException;

public interface ItemBo extends SuperBo {


    boolean updateItem(ItemTM i1) throws SQLException, ClassNotFoundException;

    boolean deleteItem(String itemCode) throws SQLException, ClassNotFoundException;

    boolean addItem(ItemDto i) throws SQLException, ClassNotFoundException;

}
