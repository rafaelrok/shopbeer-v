package br.com.rafaelvieira.shopbeer.views.costumerregister;

import br.com.rafaelvieira.shopbeer.domain.Costumer;
import br.com.rafaelvieira.shopbeer.services.CostumerService;
import br.com.rafaelvieira.shopbeer.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Costumer Register")
@Route(value = "costumer-register", layout = MainLayout.class)
@AnonymousAllowed
@Uses(Icon.class)
public class CostumerRegisterView extends Composite<VerticalLayout> {

    public CostumerRegisterView() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        TextField textField = new TextField();
        TextField textField2 = new TextField();
        TextField textField3 = new TextField();
        ComboBox comboBox = new ComboBox();
        EmailField emailField = new EmailField();
        TextField textField4 = new TextField();
        TextField textField5 = new TextField();
        TextField textField6 = new TextField();
        TextField textField7 = new TextField();
        ComboBox comboBox2 = new ComboBox();
        ComboBox comboBox3 = new ComboBox();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Button buttonSecondary = new Button();
        Grid basicGrid = new Grid(Costumer.class);
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("1200px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Cadastro de Cliente");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        textField.setLabel("Nome");
        textField.setWidth("100%");
        textField.setMaxWidth("1150px");
        textField2.setLabel("CPF/CNPJ");
        textField2.setWidth("100%");
        textField2.setMaxWidth("300px");
        textField3.setLabel("Phone Number");
        textField3.getStyle().set("flex-grow", "1");
        textField3.setMaxWidth("300px");
        comboBox.setLabel("Type Person");
        comboBox.setWidth("300px");
        comboBox.setMaxWidth("300px");
        setComboBoxSampleData(comboBox);
        emailField.setLabel("Email");
        emailField.setWidth("100%");
        emailField.setMaxWidth("950px");
        textField4.setLabel("Logradouro");
        textField4.setWidth("100%");
        textField4.setMaxWidth("437px");
        textField5.setLabel("Número");
        textField5.setWidth("100%");
        textField5.setMaxWidth("270px");
        textField6.setLabel("Complemento");
        textField6.setWidth("100%");
        textField6.setMaxWidth("389px");
        textField7.setLabel("CEP");
        textField7.setWidth("250px");
        textField7.setMaxWidth("250px");
        comboBox2.setLabel("Estado");
        comboBox2.setWidth("min-content");
        setComboBoxSampleData(comboBox2);
        comboBox3.setLabel("Cidade");
        comboBox3.setWidth("min-content");
        setComboBoxSampleData(comboBox3);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        buttonPrimary.setText("Save");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSecondary.setText("Cancel");
        buttonSecondary.setWidth("min-content");
        basicGrid.setWidth("100%");
        basicGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(basicGrid);
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(textField);
        formLayout2Col.add(textField2);
        formLayout2Col.add(textField3);
        formLayout2Col.add(comboBox);
        formLayout2Col.add(emailField);
        formLayout2Col.add(textField4);
        formLayout2Col.add(textField5);
        formLayout2Col.add(textField6);
        formLayout2Col.add(textField7);
        formLayout2Col.add(comboBox2);
        formLayout2Col.add(comboBox3);
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonSecondary);
        getContent().add(basicGrid);
    }

    record SampleItem(String value, String label, Boolean disabled) {
    }

    private void setComboBoxSampleData(ComboBox comboBox) {
        List<SampleItem> sampleItems = new ArrayList<>();
        sampleItems.add(new SampleItem("first", "First", null));
        sampleItems.add(new SampleItem("second", "Second", null));
        sampleItems.add(new SampleItem("third", "Third", Boolean.TRUE));
        sampleItems.add(new SampleItem("fourth", "Fourth", null));
        comboBox.setItems(sampleItems);
        comboBox.setItemLabelGenerator(item -> ((SampleItem) item).label());
    }

    private void setGridSampleData(Grid grid) {
        grid.setItems(query -> samplePersonService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
    }

    @Autowired()
    private CostumerService samplePersonService;
}
