package br.com.rafaelvieira.shopbeer.views.cadastros.logradouroRegister;

import br.com.rafaelvieira.shopbeer.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@PageTitle("Logradouro Register")
@Route(value = "logradouro-register", layout = MainLayout.class)
@AnonymousAllowed
@Uses(Icon.class)
public class LogradouroRegisterView extends Composite<VerticalLayout> {

    public LogradouroRegisterView() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        TextField textFieldStreet = new TextField();
        TextField textFieldNumber = new TextField();
        TextField textFieldComplement = new TextField();
        TextField textFieldZipCode = new TextField();
        ComboBox comboBoxCity = new ComboBox();
        ComboBox comboBoxState = new ComboBox();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Button buttonSecondary = new Button();


        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);

        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");

        h3.setText("Cadastro de Endereço");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");

        textFieldStreet.setLabel("Rua");
        textFieldStreet.setWidth("100%");
        textFieldNumber.setLabel("Numero");
        textFieldComplement.setLabel("Complemento");
        textFieldZipCode.setLabel("CEP");
        comboBoxCity.setLabel("Cidade");
        comboBoxState.setLabel("Estado");

        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        buttonPrimary.setText("Save");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSecondary.setText("Cancel");
        buttonSecondary.setWidth("min-content");

        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(textFieldStreet);
        formLayout2Col.add(textFieldNumber);
        formLayout2Col.add(textFieldComplement);
        formLayout2Col.add(textFieldZipCode);
        formLayout2Col.add(comboBoxCity);
        formLayout2Col.add(comboBoxState);
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonSecondary);
    }
}
