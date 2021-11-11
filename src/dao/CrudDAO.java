package dao;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface CrudDAO<T, ID, TM> extends SuperDAO {

    boolean add(T t) throws SQLException, ClassNotFoundException;


    boolean delete(ID id) throws SQLException, ClassNotFoundException;


    boolean update(TM t) throws SQLException, ClassNotFoundException;


    ObservableList<TM> getList() throws SQLException, ClassNotFoundException;


}
