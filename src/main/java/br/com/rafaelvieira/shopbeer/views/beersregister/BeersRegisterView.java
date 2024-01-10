package br.com.rafaelvieira.shopbeer.views.beersregister;

import br.com.rafaelvieira.shopbeer.domain.Beer;
import br.com.rafaelvieira.shopbeer.services.BeerService;
import br.com.rafaelvieira.shopbeer.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
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

@PageTitle("Beers Register")
@Route(value = "beers-register", layout = MainLayout.class)
@AnonymousAllowed
@Uses(Icon.class)
public class BeersRegisterView extends Composite<VerticalLayout> {

    @Autowired()
    private BeerService beerService;

    public BeersRegisterView() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        TextField textField = new TextField();
        TextField textField2 = new TextField();
        TextArea textArea = new TextArea();
        ComboBox comboBox = new ComboBox();
        Button buttonTertiary = new Button();
        ComboBox comboBox2 = new ComboBox();
        TextField textField3 = new TextField();
        ComboBox comboBox3 = new ComboBox();
        TextField textField4 = new TextField();
        TextField textField5 = new TextField();
        TextField textField6 = new TextField();
        Button buttonTertiary2 = new Button();
        Hr hr = new Hr();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Button buttonSecondary = new Button();
        Grid stripedGrid = new Grid(Beer.class);
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidth("1200px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Cadastro de Cerveja");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        textField.setLabel("SKU");
        textField.setWidth("100%");
        textField.setMaxWidth("150px");
        textField2.setLabel("Nome*");
        textField2.setWidth("100%");
        textField2.setMaxWidth("970px");
        textArea.setLabel("Text area");
        textArea.setWidth("100%");
        textArea.setMaxWidth("1150px");
        comboBox.setLabel("Estilo*");
        comboBox.setWidth("100%");
        comboBox.setMaxWidth("260px");
        comboBox.setHeight("61px");
        setComboBoxSampleData(comboBox);
        buttonTertiary.setText("+");
        buttonTertiary.setWidth("min-content");
        buttonTertiary.setMaxWidth("50px");
        buttonTertiary.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        comboBox2.setLabel("Sabor*");
        comboBox2.setWidth("100%");
        comboBox2.setMaxWidth("200px");
        setComboBoxSampleData(comboBox2);
        textField3.setLabel("Teor Alcoólico*");
        textField3.setWidth("100%");
        textField3.setMaxWidth("275px");
        comboBox3.setLabel("Nacionalidade*");
        comboBox3.setWidth("100%");
        comboBox3.setMaxWidth("275px");
        setComboBoxSampleData(comboBox3);
        textField4.setLabel("Preço*");
        textField4.setWidth("100%");
        textField4.setMaxWidth("275px");
        textField5.setLabel("Comissão*");
        textField5.setWidth("100%");
        textField5.setMaxWidth("250px");
        textField6.setLabel("Estoque*");
        textField6.setWidth("100%");
        textField6.setMaxWidth("275px");
        buttonTertiary2.setText("Upload de imagem");
        buttonTertiary2.setWidth("100%");
        buttonTertiary2.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        buttonPrimary.setText("Save");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSecondary.setText("Cancel");
        buttonSecondary.setWidth("min-content");
        stripedGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        stripedGrid.setWidth("100%");
        stripedGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(stripedGrid);
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(textField);
        formLayout2Col.add(textField2);
        formLayout2Col.add(textArea);
        formLayout2Col.add(comboBox);
        formLayout2Col.add(buttonTertiary);
        formLayout2Col.add(comboBox2);
        formLayout2Col.add(textField3);
        formLayout2Col.add(comboBox3);
        formLayout2Col.add(textField4);
        formLayout2Col.add(textField5);
        formLayout2Col.add(textField6);
        formLayout2Col.add(buttonTertiary2);
        layoutColumn2.add(hr);
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonSecondary);
        getContent().add(stripedGrid);
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
        grid.setItems(query -> beerService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
    }
}
