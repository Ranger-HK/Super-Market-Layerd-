package controller;

import bo.BoFactory;
import bo.custome.CustomerBo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dto.CustomerDto;
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
import view.tm.CustomerTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

public class CustomersFormController {
    private final CustomerBo customerBo = (CustomerBo) BoFactory.getBOFactory().getBO(BoFactory.BoTypes.CUSTOMER);
    public Label lblTime;
    public Label lblDate;
    public JFXTextField txtCustomerId;
    public JFXTextField txtCustomerTitle;
    public JFXTextField txtCustomerName1;
    public JFXTextField txtCustomerAddress;
    public JFXTextField txtCustomerCity;
    public JFXTextField txtCustomerProvince;
    public JFXTextField txtCustomerPostalCode;
    public TableView<CustomerTM> tblCustomer;
    public TableColumn colCustomerId;
    public TableColumn colCustomerTitle;
    public TableColumn colCustomerNAme;
    public TableColumn colCustomerAddress;
    public TableColumn colCustomerCity;
    public TableColumn colCustomerProvince;
    public TableColumn colPostalCode;
    public JFXButton addContext;
    public JFXButton updateContext;
    public JFXButton deleteContext;
    public JFXButton clearContext;
    public JFXTextField txtSearch;
    public JFXComboBox<String> cmbCustomerTitle;
    public JFXButton btnBack;
    public AnchorPane contextCustomer;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();
    Pattern customerIdPattern = Pattern.compile("^(C)[-]?[0-9]{3,6}$");
    Pattern customerNamePattern = Pattern.compile("^[A-z ]{3,30}$");
    Pattern addressPattern = Pattern.compile("^[A-z 0-9 \\/\\,]{2,50}[A-z 0-9]{1,50}$");
    Pattern cityPattern = Pattern.compile("^[A-z ]{5,20}$");
    Pattern provincePattern = Pattern.compile("^[A-z ]{5,10}$");
    Pattern postalCodePattern = Pattern.compile("^[0-9]{4,5}$");

    private void storeValidations() {
        map.put(txtCustomerId, customerIdPattern);
        map.put(txtCustomerName1, customerNamePattern);
        map.put(txtCustomerAddress, addressPattern);
        map.put(txtCustomerCity, cityPattern);
        map.put(txtCustomerProvince, provincePattern);
        map.put(txtCustomerPostalCode, postalCodePattern);
    }

    public void initialize() {
        loadDateAndTime();
        storeValidations();
        cmbCustomerTitle.getItems().addAll("Mr.", "Mrs.", "Miss.", "Ms.");
        try {
            showCustomerInTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public void showCustomerInTable() throws SQLException, ClassNotFoundException {
        ObservableList<CustomerTM> list = customerBo.getCustomerList();
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCustomerTitle.setCellValueFactory(new PropertyValueFactory<>("customerTitle"));
        colCustomerNAme.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colCustomerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        colCustomerCity.setCellValueFactory(new PropertyValueFactory<>("customerCity"));
        colCustomerProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));

        tblCustomer.setItems(list);
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


    public void btnAddCustomer(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        CustomerDto c = new CustomerDto(
                txtCustomerId.getText(),
                cmbCustomerTitle.getValue(),
                txtCustomerName1.getText(),
                txtCustomerAddress.getText(),
                txtCustomerCity.getText(),
                txtCustomerProvince.getText(),
                txtCustomerPostalCode.getText()


        );
        if (customerBo.addCustomer(c)) {
            new Alert(Alert.AlertType.CONFIRMATION, "SAVED").show();
            showCustomerInTable();
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();

        }
        Clear();

    }

    public void btnUpdateCustomer(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        CustomerTM c = new CustomerTM(
                txtCustomerId.getText(),
                cmbCustomerTitle.getValue(),
                txtCustomerName1.getText(),
                txtCustomerAddress.getText(),
                txtCustomerCity.getText(),
                txtCustomerProvince.getText(),
                txtCustomerPostalCode.getText()

        );
        if (customerBo.updateCustomer(c)) {
            new Alert(Alert.AlertType.CONFIRMATION, "UPDATED").show();
            showCustomerInTable();
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();

        }
        Clear();


    }

    public void btnDeleteCustomer(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        CustomerTM customer = tblCustomer.getSelectionModel().getSelectedItem();
        String customerId = customer.getCustomerId();
        if (customerBo.deleteCustomer(customerId)) {
            new Alert(Alert.AlertType.CONFIRMATION, "DELETED").show();
            showCustomerInTable();

        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
        Clear();

    }

    public void btnClearCustomer(ActionEvent actionEvent) {
        Clear();


    }


    private void Clear() {
        txtCustomerAddress.clear();
        txtCustomerCity.clear();
        txtCustomerId.clear();
        txtCustomerName1.clear();
        txtCustomerProvince.clear();
        txtCustomerPostalCode.clear();
        txtCustomerId.requestFocus();
        txtSearch.clear();
        tblCustomer.getSelectionModel().clearSelection();
        cmbCustomerTitle.getSelectionModel().clearSelection();
    }

    public void MouseOnAction(MouseEvent mouseEvent) {
        try {
            CustomerTM c = tblCustomer.getSelectionModel().getSelectedItem();
            txtCustomerId.setText(c.getCustomerId());
            cmbCustomerTitle.setValue(c.getCustomerTitle());
            txtCustomerName1.setText(c.getCustomerName());
            txtCustomerAddress.setText(c.getCustomerAddress());
            txtCustomerCity.setText(c.getCustomerCity());
            txtCustomerProvince.setText(c.getProvince());
            txtCustomerPostalCode.setText(c.getPostalCode());
        } catch (Exception e) {

        }

    }

    public void searchKeyEvent(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        List<CustomerTM> list = customerBo.searchCustomer(txtSearch.getText());
        ObservableList<CustomerTM> customerTMS = FXCollections.observableArrayList();
        for (CustomerTM customerDto : list) {
            customerTMS.add(new CustomerTM(
                    customerDto.getCustomerId(),
                    customerDto.getCustomerTitle(),
                    customerDto.getCustomerName(),
                    customerDto.getCustomerAddress(),
                    customerDto.getCustomerCity(),
                    customerDto.getProvince(),
                    customerDto.getPostalCode()
            ));
        }
        tblCustomer.getItems().setAll(customerTMS);

    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/OrderForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) contextCustomer.getScene().getWindow();
        window.setScene(new Scene(load));
    }


    public void moveToCustomerAddress(ActionEvent actionEvent) {
        txtCustomerAddress.requestFocus();
    }

    public void moveToCustomerCity(ActionEvent actionEvent) {
        txtCustomerCity.requestFocus();
    }

    public void moveToCustomerProvince(ActionEvent actionEvent) {
        txtCustomerProvince.requestFocus();
    }

    public void moveToPostalCode(ActionEvent actionEvent) {
        txtCustomerPostalCode.requestFocus();

    }

    public void txtCustomerKeyRelease(KeyEvent keyEvent) {
        Object response = Validation.validate(map, addContext);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
            }
        }
    }
}
