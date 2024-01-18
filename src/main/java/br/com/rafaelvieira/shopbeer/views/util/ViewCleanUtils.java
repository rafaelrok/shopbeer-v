package br.com.rafaelvieira.shopbeer.views.util;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

public class ViewCleanUtils {

    public static void cleanTextFields(TextField... textFields) {
        for (TextField textField : textFields) {
            textField.clear();
        }
    }

    public static void cleanEmailFields(EmailField... emailFields) {
        for (EmailField emailField : emailFields) {
            emailField.clear();
        }
    }

    public static void cleanNumberFields(NumberField... numberFields) {
        for (NumberField numberField : numberFields) {
            numberField.clear();
        }
    }

    public static void cleanPasswordFields(PasswordField... passwordFields) {
        for (PasswordField passwordField : passwordFields) {
            passwordField.clear();
            passwordField.setEnabled(true);
        }
    }

    public static void cleanDatePickers(DatePicker... datePickers) {
        for (DatePicker datePicker : datePickers) {
            datePicker.clear();
        }
    }

    public static void cleanComboBoxes(ComboBox... comboBoxes) {
        for (ComboBox comboBox : comboBoxes) {
            comboBox.clear();
        }
    }

    public static void cleanDialogFields(Dialog dialog) {
        dialog.removeAll();
        dialog.getHeader().removeAll();
        dialog.getFooter().removeAll();
    }

}
