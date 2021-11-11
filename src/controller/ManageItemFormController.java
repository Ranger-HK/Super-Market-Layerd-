package controller;

import bo.BoFactory;
import bo.custome.ItemBo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import dao.CrudUtil;
import dto.ItemDto;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import utill.Validation;
import view.tm.ItemTM;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ManageItemFormController {
    private final ItemBo itemBo = (ItemBo) BoFactory.getBOFactory().getBO(BoFactory.BoTypes.ITEM);
    public AnchorPane contextManageItem;
    public JFXTextField txtDescription;
    public JFXTextField txtPacketSize;
    public JFXTextField txtItemCode;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtUnitPrice;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colPacketSize;
    public TableColumn colUnitPrice;
    public TableColumn colQtyOnHand;
    public TableView<ItemTM> tblBox;
    public TableColumn colDiscount;
    public JFXTextField txtDiscount;
    public JFXButton updateBtn;
    public JFXButton deleteItemBtn;
    public JFXButton addItemBtn;
    public Label lblTime;
    public Label lblDate;
    int SelectedRow = -1;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();
    Pattern itemCodePattern = Pattern.compile("^(I)[-]?[0-9]{3,5}$");
    Pattern descriptionPattern = Pattern.compile("^[A-z ]{3,30}$");
    Pattern packSizePattern = Pattern.compile("^[0-9]{1,4}$");
    Pattern unitPricePattern = Pattern.compile("^[0-9]{2,4}$");
    Pattern QTYPattern = Pattern.compile("^[0-9]{1,10}$");
    Pattern discountPattern = Pattern.compile("^[0-9]{1,2}$");

    private void storeValidations() {
        map.put(txtItemCode, itemCodePattern);
        map.put(txtDescription, descriptionPattern);
        map.put(txtPacketSize, packSizePattern);
        map.put(txtUnitPrice, unitPricePattern);
        map.put(txtQtyOnHand, QTYPattern);
        map.put(txtDiscount, discountPattern);
    }

    public void backToAdmin(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/SuperMarketForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) contextManageItem.getScene().getWindow();
        window.setScene(new Scene(load));
    }

    public void backToSelectFormAdmin(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/SelectFormAdmin.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) contextManageItem.getScene().getWindow();
        window.setScene(new Scene(load));

    }

    public void addItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        String itemCode = txtItemCode.getText();
        String description = txtDescription.getText();
        String packetSize = txtPacketSize.getText();
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
        int discount = Integer.parseInt(txtDiscount.getText());

        ItemTM i1 = new ItemTM(
                itemCode,
                description,
                packetSize,
                unitPrice,
                qtyOnHand,
                discount


        );
        getItemList().add(i1);

        tblBox.setItems(getItemList());


        ItemDto i = new ItemDto(txtItemCode.getText(), txtDescription.getText(), txtPacketSize.getText(), Double.parseDouble(txtUnitPrice.getText()), Integer.parseInt(txtQtyOnHand.getText()), Integer.parseInt(txtDiscount.getText()));
        if (itemBo.addItem(i))
            new Alert(Alert.AlertType.CONFIRMATION, "Saved..").show();
        else
            new Alert(Alert.AlertType.WARNING, "Try Again..").show();
        showItem();
        clearField();
    }


    public void initialize() {
        loadDateAndTime();
        storeValidations();
        try {
            showItem();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        updateBtn.setDisable(true);
        deleteItemBtn.setDisable(true);

        tblBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            SelectedRow = (int) newValue;
            if (SelectedRow == -1) {
                addItemBtn.setDisable(false);
                updateBtn.setDisable(true);
                deleteItemBtn.setDisable(true);
                txtItemCode.setEditable(true);
            } else {
                addItemBtn.setDisable(true);
                updateBtn.setDisable(false);
                deleteItemBtn.setDisable(false);
                txtItemCode.setEditable(false);
            }
        });

    }

    private void loadDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MMMM-dd");
        lblDate.setText(f.format(date));

        Timeline time = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(
                    currentTime.getHour() + " : " + currentTime.getMinute() + " : " + currentTime.getSecond()
            );
        }),
                new KeyFrame(Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }


    private void clearField() {
        txtItemCode.clear();
        txtDescription.clear();
        txtPacketSize.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
        txtDiscount.clear();
        txtItemCode.requestFocus();
        addItemBtn.setDisable(false);
        updateBtn.setDisable(true);
        deleteItemBtn.setDisable(true);
        txtItemCode.setEditable(true);

    }

    public void clearFiledOnAction(ActionEvent actionEvent) {
        clearField();

    }

    public void deleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ItemTM item = tblBox.getSelectionModel().getSelectedItem();
        String itemCode = item.getItemCode();

        if (itemBo.deleteItem(itemCode)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Deleted").show();
            showItem();

        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
        clearField();
    }


    public void editOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ItemTM i1 = new ItemTM(
                txtItemCode.getText(),
                txtDescription.getText(),
                txtPacketSize.getText(),
                Double.parseDouble(txtUnitPrice.getText()),
                Integer.parseInt(txtQtyOnHand.getText()),
                Integer.parseInt(txtDiscount.getText())
        );


        if (itemBo.updateItem(i1)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Updated").show();
            showItem();

        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();

        }

    }

    public ObservableList<ItemTM> getItemList() throws SQLException, ClassNotFoundException {
        ObservableList<ItemTM> obList = FXCollections.observableArrayList();
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Item");
        while (rst.next()) {
            obList.add(new ItemTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getInt(5),
                    rst.getInt(6)
            ));

        }
        return obList;

    }

    public void showItem() throws SQLException, ClassNotFoundException {

        ObservableList<ItemTM> list = getItemList();

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPacketSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        tblBox.setItems(list);

    }


    public void moveToQtyOnHand(ActionEvent actionEvent) {
        txtQtyOnHand.requestFocus();
    }

    public void moveToPacketSize(ActionEvent actionEvent) {
        txtPacketSize.requestFocus();
    }

    public void moveToUnitPrice(ActionEvent actionEvent) {
        txtUnitPrice.requestFocus();
    }

    public void moveToDescription(ActionEvent actionEvent) {
        txtDescription.requestFocus();
    }

    public void moveToDiscount(ActionEvent actionEvent) {
        txtDiscount.requestFocus();

    }

    public void handleMouseOnAction(MouseEvent mouseEvent) {
        ItemTM item = null;
        try {
            item = tblBox.getSelectionModel().getSelectedItem();
            txtItemCode.setText(item.getItemCode());
            txtUnitPrice.setText("" + item.getUnitPrice());
            txtDescription.setText(item.getDescription());
            txtQtyOnHand.setText("" + item.getQtyOnHand());
            txtDiscount.setText("" + item.getDiscount());
            txtPacketSize.setText("" + item.getPackSize());
        } catch (Exception e) {

        }


    }

    public void txtItemsKeyReleased(KeyEvent keyEvent) {
        Object response = Validation.validate(map, addItemBtn);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
            }
        }
    }
}

