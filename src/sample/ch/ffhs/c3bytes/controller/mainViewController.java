package sample.ch.ffhs.c3bytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class mainViewController {



    public void addNewItemAction(ActionEvent event){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("../gui/add_new_item_view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add new Item");
            stage.setScene(new Scene(root, 400, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML private javafx.scene.control.Button logoutButton;
    public void logoutAction(ActionEvent event){
        System.out.println("Logout Action");
        //TODO: Add necessary methods to clear/reencrypt/delete before closing the app.
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
    }

    public void modifyProfileAction(ActionEvent event){
        //TODO: Open view_item.fxml and edit the entry.
        System.out.println("Modify Profile Action");

    }


    public void searchAction(ActionEvent event){
        //TODO: Search entries based on parameter.
        //TODO: Define how search works.
        System.out.println("Search Action");
    }

    public void copyPasswordAction(ActionEvent event){
        //TODO: Copy password -> get password text field content
        System.out.println("Copy Password Action");
    }

    public void deleteProfileAction(ActionEvent event){
        //TODO: Delete profile. Pop up alert.
        System.out.println("Delete Profile Action");
    }

    public void changeMasterAction(ActionEvent actionEvent) {
        //TODO: Change password action
        System.out.println("Change Master Password Action");
    }

    public void deleteAccountAction(ActionEvent actionEvent) {
        //TODO: Define deleting account
        System.out.println("Delete Account Action");
    }
}
