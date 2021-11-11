package controller;

import bo.BoFactory;
import bo.custome.PurchaseOrderBo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dto.CustomerDto;
import dto.ItemDto;
import dto.OrderDto;
import entity.OrderDetails;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import view.tm.CartTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OrderFormController {

    public static ObservableList<CartTM> obList = FXCollections.observableArrayList();
    private final PurchaseOrderBo purchaseOrderBo = (PurchaseOrderBo) BoFactory.getBOFactory().getBO(BoFactory.BoTypes.PURCHASE_ORDER);
    public AnchorPane contextOrderForm;
    public Label lblTime;
    public Label lblDate;
    public JFXComboBox<String> cmbItemCode;
    public JFXTextField txtDescription;
    public JFXTextField txtPacketSize;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtQuantity;
    public JFXTextField txtDiscount;
    public TableView<CartTM> tblManage;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colQuantity;
    public TableColumn colDiscount;
    public TableColumn colTotal;
    public Label lblTotal;
    public TableColumn colUnitPrice;
    public Label lblOrderId;
    public JFXButton customerContext;
    public JFXTextField txtSearch;
    public JFXComboBox<String> cmbSearch;
    int cartSelectedRowForRemove = -1;

    public void initialize() {

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));


        try {
            loadItemIds();
            loadCustomerIds();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                SetItemData(newValue);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        });

        cmbSearch.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                setCustomerData(newValue);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        tblManage.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            cartSelectedRowForRemove = (int) newValue;

        });
        loadDateAndTime();
        setOrderId();
    }


    private void loadDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
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


    public void backToCashierLogin(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/SuperMarketForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) contextOrderForm.getScene().getWindow();
        window.setScene(new Scene(load));

    }


    private void loadItemIds() throws SQLException, ClassNotFoundException {
        List<String> itemIds = purchaseOrderBo.getAllIds();
        cmbItemCode.getItems().addAll(itemIds);
    }


    private void SetItemData(String itemCode) throws SQLException, ClassNotFoundException {
        ItemDto i1 = purchaseOrderBo.getItem(itemCode);

        if (i1 == null) {
            new Alert(Alert.AlertType.WARNING, "Empty Result Set");

        } else {
            txtDescription.setText(i1.getDescription());
            txtUnitPrice.setText(String.valueOf(i1.getUnitPrice()));
            txtPacketSize.setText(i1.getPackSize());
            txtQtyOnHand.setText(String.valueOf(i1.getQtyOnHand()));
            txtDiscount.setText(String.valueOf(i1.getDiscount()));

        }
    }


    public void manageCustomerOrderOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/ManageCustomerOrder.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) contextOrderForm.getScene().getWindow();
        window.setScene(new Scene(load));

    }


    public void deleteOnAction(ActionEvent actionEvent) {
        if (cartSelectedRowForRemove == -1) {
            new Alert(Alert.AlertType.WARNING, "Please Select a row").show();
        } else {
            new Alert(Alert.AlertType.CONFIRMATION, "Delete Completed").show();

            obList.remove(cartSelectedRowForRemove);
            calculateCost();
            tblManage.refresh();
        }
        clearField();
    }


    public void addToCartOnAction(ActionEvent actionEvent) {
        String description = txtDescription.getText();
        int packetSize = Integer.parseInt(txtPacketSize.getText());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
        int qtyForCustomer = Integer.parseInt(txtQuantity.getText());
        int discount = Integer.parseInt(txtDiscount.getText());
        double total = qtyForCustomer * unitPrice - qtyForCustomer * unitPrice * discount / 100;


        if (qtyOnHand < qtyForCustomer) {
            new Alert(Alert.AlertType.WARNING, "Invalid QTY").show();
            return;
        }


        CartTM tm = new CartTM(
                cmbItemCode.getValue(),
                description,
                qtyForCustomer,
                unitPrice,
                discount,
                total
        );

        int rowNumber = isExists(tm);

        if (rowNumber == -1) {
            obList.add(tm);

        } else {
            CartTM temp = obList.get(rowNumber);
            CartTM newTm = new CartTM(
                    temp.getCode(),
                    temp.getDescription(),
                    temp.getQty() + qtyForCustomer,
                    unitPrice,
                    temp.getDiscount(),
                    total + temp.getTotal()

            );

            if (qtyOnHand < temp.getQty() + qtyForCustomer) {
                new Alert(Alert.AlertType.WARNING, "Invalid QTY").show();
                return;
            }
            obList.remove(rowNumber);
            obList.add(newTm);

        }
        tblManage.setItems(obList);
        calculateCost();
        clearField();

    }

    private int isExists(CartTM tm) {
        for (int i = 0; i < obList.size(); i++) {
            if (tm.getCode().equals(obList.get(i).getCode())) {
                return i;
            }
        }
        return -1;
    }

    void calculateCost() {
        double ttl = 0;
        for (CartTM tm : obList
        ) {
            ttl += tm.getTotal();
        }

        String formatTotal = new DecimalFormat("0.00").format(ttl);
        lblTotal.setText(formatTotal + " /=");
    }

    public void cancelOrderOnAction(ActionEvent actionEvent) {
        clearField();
        lblTotal.setText("");
        obList.clear();
    }

    public void orderBtnOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        String cusId;
        if (cmbSearch.getValue().isEmpty()) {
            cusId = null;
        } else {
            cusId = cmbSearch.getValue();
        }

        ArrayList<OrderDetails> items = new ArrayList<>();
        for (CartTM tempTm : obList
        ) {
            items.add(
                    new OrderDetails(
                            tempTm.getCode(),
                            lblOrderId.getText(),
                            tempTm.getQty(),
                            tempTm.getDiscount()

                    ));

        }
        OrderDto orderDto = new OrderDto(
                lblOrderId.getText(),
                lblDate.getText(),
                cusId,
                items
        );
        if (purchaseOrderBo.purchaseOrder(orderDto)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Success").show();
            try {

                JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("../view/report/Bill.jrxml"));
                JasperReport compileReport = JasperCompileManager.compileReport(design);
                ObservableList<CartTM> item = tblManage.getItems();

                String oId = lblOrderId.getText();
                String subTotal = lblTotal.getText();
                String cusID = cmbSearch.getValue();
                String cusName = txtSearch.getText();

                HashMap map = new HashMap();
                map.put("OrderId", oId);
                map.put("SubTotal", subTotal);
                map.put("customerID", cusID);
                map.put("customerName", cusName);

                JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, map /*null*/, new JRBeanArrayDataSource(item.toArray()));
                JasperViewer.viewReport(jasperPrint, false);
            }catch (JRException e) {
                e.printStackTrace();
            }
            setOrderId();
            clearField();
            obList.clear();
            lblTotal.setText("");


        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    private void clearField() {
        txtDescription.clear();
        txtPacketSize.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
        txtQuantity.clear();
        txtDiscount.clear();
        cmbItemCode.requestFocus();
        tblManage.getSelectionModel().clearSelection();
        cmbItemCode.setValue("");
        cmbSearch.setValue("");
        txtSearch.clear();

    }

    private void setOrderId() {
        try {
            lblOrderId.setText(purchaseOrderBo.getOrderID());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void moveToPacketSize(ActionEvent actionEvent) {
        txtPacketSize.requestFocus();
    }

    public void moveToUnitePrice(ActionEvent actionEvent) {
        txtUnitPrice.requestFocus();
    }

    public void moveToQtyOnHand(ActionEvent actionEvent) {
        txtQtyOnHand.requestFocus();
    }

    public void moveToQuntity(ActionEvent actionEvent) {
        txtQuantity.requestFocus();
    }

    public void moveToDiscount(ActionEvent actionEvent) {
        txtDiscount.requestFocus();
    }

    public void btnCustomerOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/CustomersForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) contextOrderForm.getScene().getWindow();
        window.setScene(new Scene(load));
        window.centerOnScreen();

    }

    private void loadCustomerIds() throws SQLException, ClassNotFoundException {
        List<String> itemIds = purchaseOrderBo.getAllCustomerIds();
        cmbSearch.getItems().addAll(itemIds);
    }

    private void setCustomerData(String id) throws SQLException, ClassNotFoundException {
        CustomerDto c1 = purchaseOrderBo.passCustomerDetails(id);
        if (c1 == null) {
            //  new Alert(Alert.AlertType.WARNING,"Empty Result Set").show();

        } else {
            txtSearch.setText(c1.getCustomerName());

        }
    }

}
