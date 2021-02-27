package lk.ijse.customersystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.customersystem.entity.Customer;
import lk.ijse.customersystem.tm.CustomerTM;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class CustomerFormController {
    public TextField txtCId;
    public TextField txtCName;
    public TextField txtCAddress;
    public TextField txtCSalary;
    public Button btnSave;
    public TextField txtSearch;
    public TableView<CustomerTM> tblCustomer;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colSalary;
    public TableColumn colOperate;

    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory("salary"));
        colOperate.setCellValueFactory(new PropertyValueFactory("btn"));


        //------------------------
        tblCustomer.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    loadUniqueCustomerData(newValue);
                });
        //---------------------
        try {
            loadAllCustomers();
        } catch (Exception e) {
        }
    }

    private void loadUniqueCustomerData(CustomerTM newValue) {
        txtCId.setText(newValue.getId());
        txtCName.setText(newValue.getName());
        txtCAddress.setText(newValue.getAddress());
        txtCSalary.setText(newValue.getSalary()+"");
        btnSave.setText("Update");
    }

    private void loadAllCustomers() throws ClassNotFoundException, SQLException {
        //Class.forName("com.mysql.cj.jdbc.Driver");
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection
                ("jdbc:mysql://127.0.0.1:3306/dep6",
                        "root", "1234");
        String sql = "SELECT * FROM Customer";
        PreparedStatement stm = connection.prepareStatement(sql);
        ResultSet resultSet = stm.executeQuery();
        //-----------Customer List
        ArrayList<Customer> cusList = new ArrayList<>();
        //-----------
        while (resultSet.next()) {
            cusList.add(
                    new Customer(resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getDouble(4))
            );
        }
        // Load Table Data
        ObservableList<CustomerTM> obList =
                FXCollections.observableArrayList();

        for (Customer c : cusList
        ) {
            Button btn = new Button("Delete");
            CustomerTM tm = new CustomerTM(
                    c.getId(), c.getName(), c.getAddress(),
                    c.getSalary(),
                    btn
            );

            btn.setOnAction(event -> {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are You Sure ?", ButtonType.OK, ButtonType.NO);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    //-------------
                    try {
                        if (connection.prepareStatement(
                                "DELETE FROM Customer WHERE c_Id='" + tm.getId() + "'")
                                .executeUpdate() > 0) {
                            new Alert(Alert.AlertType.CONFIRMATION,
                                    "Deleted!", ButtonType.OK).show();
                            loadAllCustomers();
                            //------
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }


            });


            obList.add(tm);
        }
        tblCustomer.setItems(obList);

    }


    public void btnSaveOnAction(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        if (btnSave.getText().equals("Save")) {
            // 1st Step
            Class.forName("com.mysql.jdbc.Driver");
            // 2nd Step
            Connection con = DriverManager.
                    getConnection("jdbc:mysql://localhost:3306/dep6",
                            "root", "1234");
            //3rd Step
            PreparedStatement stm =
                    con.prepareStatement("INSERT INTO Customer VALUES(?,?,?,?)");
            //4th Step
            //stm.setString(1,txtCId.getText());
            stm.setObject(1, txtCId.getText());
            stm.setObject(2, txtCName.getText());
            stm.setObject(3, txtCAddress.getText());
            stm.setObject(4, Double.parseDouble(txtCSalary.getText()));
            // 5th Step
            if (stm.executeUpdate() > 0) {
                loadAllCustomers();
                new Alert(Alert.AlertType.
                        CONFIRMATION, "Saved", ButtonType.OK).show();
            }


        } else {

            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.
                    getConnection("jdbc:mysql://localhost:3306/dep6",
                    "root",
                    "1234");
            PreparedStatement stm = con.prepareStatement
("UPDATE Customer SET c_Name=?,address=?,salary=? WHERE c_id=?");
        stm.setObject(1,txtCName.getText());
        stm.setObject(2,txtCAddress.getText());
        stm.setObject(3,Double.parseDouble(txtCSalary.getText()));
        stm.setObject(4,txtCId.getText());

        if (stm.executeUpdate()>0){
            new Alert(Alert.AlertType.CONFIRMATION,"Updated !",
                    ButtonType.OK).show();
        }else{
            new Alert(Alert.AlertType.WARNING,"Try Again !",
                    ButtonType.OK).show();
        }
        }
    }

    public void btnNewOnAcction(ActionEvent actionEvent) {
    }
}
