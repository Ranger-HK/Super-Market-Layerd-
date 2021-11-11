package controller;

import bo.BoFactory;
import bo.custome.ManageCustomerOrderBo;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dto.OrderDetailsDto;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import view.tm.CartTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class ManageCustomerOrderController implements Initializable {

    private final ManageCustomerOrderBo manageCustomerOrderBo = (ManageCustomerOrderBo) BoFactory.getBOFactory().getBO(BoFactory.BoTypes.MANAGE_ORDER);
    public AnchorPane contextManage;
    public TableColumn colItemCode;
    public TableColumn colQty;
    public JFXTextField txtItemCode;
    public JFXTextField txtQty;
    public TableView<CartTM> tblOrder;
    public TableColumn colOrderId;
    public TableColumn colUnitPrice;
    public JFXTextField txtOrderId;
    public JFXTextField txtUnitPrice;
    public JFXComboBox<String> cmbOrderId;
    public String oId;
    public TableColumn colDiscount;
    public TableColumn colTotal;
    public JFXTextField txtDiscount;
    public JFXTextField txtTotal;
    public JFXTextField txtReturnQty;

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/OrderForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) contextManage.getScene().getWindow();
        window.setScene(new Scene(load));

    }

    private void loadOrderIds() throws SQLException, ClassNotFoundException {
        List<String> allOrderIds = manageCustomerOrderBo.getAllOrderIds();
        cmbOrderId.getItems().addAll(allOrderIds);

    }


    public void mouseEvent(MouseEvent mouseEvent) {
        CartTM cart = null;
        try {
            cart = tblOrder.getSelectionModel().getSelectedItem();
            txtItemCode.setText(cart.getCode());
            txtQty.setText("" + cart.getQty());
            txtOrderId.setText("" + cart.getOrderId());
            txtUnitPrice.setText("" + cart.getUnitPrice());
            txtDiscount.setText("" + cart.getDiscount());
            txtTotal.setText("" + cart.getTotal());

        } catch (Exception e) {
        }
    }


    public void updateOnAction(ActionEvent actionEvent) throws SQLException {
        OrderDetailsDto c1 = new OrderDetailsDto(
                txtItemCode.getText(),
                txtOrderId.getText(),
                Integer.parseInt(txtReturnQty.getText()),
                Double.parseDouble(txtUnitPrice.getText())

        );
        if (manageCustomerOrderBo.updateOrder(c1)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Updated..").show();
            try {

                JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("../view/report/ReturnBill.jrxml"));
                JasperReport compileReport = JasperCompileManager.compileReport(design);

                String itemCode = txtItemCode.getText();
                String qty = txtReturnQty.getText();
                String unitPrice = txtUnitPrice.getText();
                String discount = txtDiscount.getText();
                String total = txtTotal.getText();
                String orderId = txtOrderId.getText();

                HashMap map = new HashMap();
                map.put("itemCode", itemCode);
                map.put("Qty", qty);
                map.put("unitPrice", unitPrice);
                map.put("Discount", discount);
                map.put("Total", total);
                map.put("OrderId", orderId);

                JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, map /*null*/, new JREmptyDataSource(1));
                JasperViewer.viewReport(jasperPrint, false);

            } catch (JRException e) {
                e.printStackTrace();
            }
            clearField();
        } else {
            new Alert(Alert.AlertType.WARNING, "TryAgain..").show();
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadOrderIds();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        cmbOrderId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            oId = newValue;
            ObservableList<CartTM> orderList;
            try {
                orderList = manageCustomerOrderBo.getOrderList(oId);
                colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
                colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
                colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
                colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
                colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
                colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

                tblOrder.setItems(orderList);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void clearField() {
        txtItemCode.clear();
        txtOrderId.clear();
        txtQty.clear();
        txtUnitPrice.clear();
        txtReturnQty.clear();
        txtDiscount.clear();
        txtTotal.clear();
        cmbOrderId.requestFocus();
        tblOrder.getSelectionModel().clearSelection();
        cmbOrderId.setValue("");
    }

    public void keyReleasedOnAction(KeyEvent keyEvent) {
        CartTM selectedItem = (CartTM) tblOrder.getSelectionModel().getSelectedItem();

        try {
            if (txtTotal.getText().isEmpty()) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!txtReturnQty.getText().isEmpty()) {
            try {
                txtReturnQty.editableProperty();
                double discount = calDiscount(Integer.parseInt(txtReturnQty.getText()), selectedItem.getUnitPrice(), selectedItem.getDiscount());
                txtTotal.setText(discount + "");
            } catch (NumberFormatException e) {
            }
        } else {
            txtTotal.setText(selectedItem.getTotal() + "");
        }
    }

    private double calDiscount(int qtyForCustomer, double unitPrice, int discount) {
        double total = qtyForCustomer * unitPrice;
        double s = 100 - discount;
        double price = (s * total) / 100;
        return price;
    }
}
