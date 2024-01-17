package br.com.rafaelvieira.shopbeer.views.cadastros.cityregister;

import br.com.rafaelvieira.shopbeer.domain.*;
import br.com.rafaelvieira.shopbeer.domain.City;
import br.com.rafaelvieira.shopbeer.domain.enums.Countries;
import br.com.rafaelvieira.shopbeer.repository.StateRepository;
import br.com.rafaelvieira.shopbeer.security.UserPermissionChecker;
import br.com.rafaelvieira.shopbeer.services.CityService;
import br.com.rafaelvieira.shopbeer.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.util.*;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.lineawesome.LineAwesomeIcon;

@PageTitle("City Register")
@Route(value = "city-register", layout = MainLayout.class)
@AnonymousAllowed
@Uses(Icon.class)
@CssImport("themes/shopbeer-v/views/city-register-view.css")
public class CityRegisterView extends Composite<VerticalLayout> {

    @Autowired()
    private static CityService cityService;
    
    @Autowired()
    private static StateRepository stateRepository;
    
    @Autowired
    private SecurityContextHolder securityContextHolder;

    List<String> allowedRoles = Arrays.asList("ROLE_ADMIN", "USER");
    UserPermissionChecker<UserEmployee> checker = new UserPermissionChecker<>(securityContextHolder, allowedRoles);
    VerticalLayout layoutColumn2 = new VerticalLayout();
    H3 h3 = new H3();
    FormLayout formLayout2Col = new FormLayout();
    TextField textField = new TextField();
    ComboBox comboBoxCountry = new ComboBox();
    ComboBox comboBoxState = new ComboBox();
    HorizontalLayout layoutRow = new HorizontalLayout();
    Button buttonPrimary = new Button();
    Button buttonSecondary = new Button();
    Grid<City> stripedGrid = new Grid<>(City.class, false);
    Dialog dialog = new Dialog();
    Button stateButton = new Button();
    TextField textDialogStateName = new TextField();
    TextField textDialogStateAcronym = new TextField();


    public CityRegisterView(CityService cityService, StateRepository stateRepository) {
        this.cityService = cityService;
        this.stateRepository = stateRepository;

        comboBoxCountry.addClassName("city-register-view-country-combobox");
        comboBoxState.addClassName("city-register-view-state-combobox");
        textField.addClassName("city-register-view-text-field");
        stateButton.addClassName("city-register-view-state-button");
        buttonSecondary.addClassName("city-register-view-button-secondary");
        stripedGrid.addClassName("city-register-view-grid");

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);

        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("1190px");
        layoutColumn2.getStyle().set("flex", "1");

        layoutRow.addClassName(LumoUtility.Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getThemeList().add("spacing-s");
        layoutRow.getStyle().set("flex", "1");
        layoutRow.setSpacing(true);
        layoutRow.setMargin(true);
        layoutRow.setAlignItems(FlexComponent.Alignment.CENTER);
        
        h3.setText("Cadastro de Cidades");
        h3.setWidth("100%");

        formLayout2Col.setWidth("100%");
        formLayout2Col.getStyle().setBorder("1px solid var(--lumo-contrast-30pct)");
        formLayout2Col.getStyle().setBorderRadius("5px");
        formLayout2Col.getStyle().set("padding-left", "20px");
        formLayout2Col.getStyle().set("padding-top", "10px");
        formLayout2Col.getStyle().set("padding-right", "0");
        formLayout2Col.getStyle().set("padding-bottom", "20px");
        formLayout2Col.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("700px", 2),
                new FormLayout.ResponsiveStep("900px", 3),
                new FormLayout.ResponsiveStep("1100px", 4)
        );

        textField.setLabel("Name");
        textField.setWidth("100%");
        textField.setMaxWidth("350px");
        textField.setClearButtonVisible(true);
        textField.setPlaceholder("Digite o nome da cidade");
        textField.setRequired(true);
        textField.setPrefixComponent(LineAwesomeIcon.CITY_SOLID.create());

        comboBoxState.setLabel("Estado");
        comboBoxState.setWidth("100%");
        comboBoxState.setMaxWidth("250px");
        comboBoxState.setClearButtonVisible(true);
        comboBoxState.setPlaceholder("Localizar estado");
        comboBoxState.setRequired(true);
        comboBoxState.setPrefixComponent(LineAwesomeIcon.BUILDING_SOLID.create());
        setComboBoxState(comboBoxState);

        stateButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        stateButton.setIconAfterText(true);
        stateButton.setWidth("100%");
        stateButton.setMaxWidth("30px");
        stateButton.setIcon(new Icon(VaadinIcon.PLUS));
        stateButton.addClickListener(e -> {
            State state = new State();
            cleanDialogFields();
            stateNew(state);
        });

        comboBoxCountry.setLabel("Pais");
        comboBoxCountry.setWidth("100%");
        comboBoxCountry.setMaxWidth("250px");
        comboBoxCountry.setClearButtonVisible(true);
        comboBoxCountry.setPlaceholder("Selecione um país");
        comboBoxCountry.setRequired(true);
        comboBoxCountry.setPrefixComponent(LineAwesomeIcon.GLOBE_SOLID.create());
        comboBoxCountry.addClassName("country-combobox");
        setComboBoxCountry(comboBoxCountry);

        if(checker.checkPermission()){
            buttonPrimary.setEnabled(true);
            buttonPrimary.setText("Salvar");
            buttonPrimary.setWidth("min-content");
            buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            buttonPrimary.addClickListener(event -> {
                this.saveCities();
            });
        } else {
            buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            buttonPrimary.setEnabled(false);
        }

        buttonSecondary.setText("Cancelar");
        buttonSecondary.setWidth("min-content");
        buttonSecondary.addClickListener(event -> this.cancelFields());

        List<City> cities = cityService.findAll();
        GridListDataView<City> dataViewCity = stripedGrid.setItems(cities);

        stripedGrid.setWidth("100%");
            stripedGrid.getStyle().set("flex-grow", "0");
            stripedGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            stripedGrid.setPageSize(10);
            stripedGrid.getStyle().setBackgroundColor("var(--lumo-contrast-5pct)");
            stripedGrid.getStyle().set("border", "1px solid var(--lumo-contrast-40pct)");
            stripedGrid.getStyle().set("border-radius", "5px");
//            stripedGrid.getStyle().set("padding", "10px");
            stripedGrid.getHeaderRows().stream()
                    .flatMap(row -> row.getCells().stream())
                    .forEach(cell -> cell.getComponent().getElement().getStyle().set("font-weight", "600"));

            stripedGrid.addColumn(new ComponentRenderer<>(city -> {
                        Div divCode = new Div();
                        divCode.setText(String.valueOf(city.getCode()));
                        divCode.getStyle().set("display", "flex");
                        divCode.getStyle().set("justify-content", "start");
                        divCode.getStyle().set("align-items", "start");
                        return divCode;
                    })).setHeader(new Html("<div style='text-align:center;'>Código</div>"))
                    .setWidth("95px")
                    .setFooter(String.format("%d Total", cities.size()))
                    .setSortOrderProvider(
                            direction -> (Stream<QuerySortOrder>) dataViewCity
                                    .setSortOrder(City::getCode, direction));

            stripedGrid.addColumn(new ComponentRenderer<>(city -> {
                        Div divCode = new Div();
                        divCode.setText(String.valueOf(city.getName()));
                        divCode.getStyle().set("display", "flex");
                        divCode.getStyle().set("justify-content", "start");
                        divCode.getStyle().set("align-items", "start");
                        return divCode;
                    })).setHeader(new Html("<div style='text-align:center;'>Nome</div>"))
                    .setSortOrderProvider(
                            direction -> (Stream<QuerySortOrder>) dataViewCity
                                    .setSortOrder(City::getName, direction))
                    .setWidth("250px");

        stripedGrid.addColumn(createStateRenderer())
                .setHeader(new Html("<div style='text-align:center;'>Estado</div>"))
                .setSortOrderProvider(
                        direction -> (Stream<QuerySortOrder>) dataViewCity
                                .setSortOrder(city -> city.getState().getName(), direction))
                .setWidth("200px");

        stripedGrid.addColumn(new ComponentRenderer<>(city -> {
                    Div divCode = new Div();
                    divCode.setText(createCountryeComponentRenderer().createComponent(city).getText());
                    divCode.getStyle().set("display", "flex");
                    divCode.getStyle().set("justify-content", "center");
                    divCode.getStyle().set("align-items", "center");
                    divCode.getStyle().set("background-color", "var(--lumo-contrast-5pct)");
                    divCode.getStyle().set("border-radius", "5px");
                    divCode.getStyle().set("padding", "0.1em 0.1em");
                    return divCode;
                })).setHeader(new Html("<div style='text-align:center;'>Pais</div>"))
                .setWidth("100px");

            stripedGrid.addColumn(
                            new ComponentRenderer<>(this::actionManager))
                    .setHeader(new Html("<div style='text-align:center;'>Ações</div>"))
                    .setWidth("80px").setFlexGrow(0)
                    .setAutoWidth(true);

            stripedGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
            setGridSampleData(stripedGrid);

        getContent().add(layoutColumn2, dialog);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(textField);
        formLayout2Col.add(comboBoxState);
        formLayout2Col.add(stateButton);
        formLayout2Col.add(comboBoxCountry);
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonSecondary);
        layoutColumn2.add(stripedGrid);
    }

    public void stateNew(State state) {
        try {
            cleanDialogFields();
            dialog.setHeaderTitle("Cadastro de Estado");
            dialog.open();
            VerticalLayout dialogLayout = createStateLayout();
            dialog.add(dialogLayout);
            dialog.setModal(false);
            dialog.setDraggable(true);
            createFooterState(dialog, state);
        } catch (Exception exception) {
            Notification notification = Notification.show("Estado não cadastrado: " + exception.getMessage(), 6000,
                    Notification.Position.TOP_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private VerticalLayout createStateLayout() {
        textDialogStateName.setRequired(true);
        textDialogStateName.setClearButtonVisible(true);
        textDialogStateName.setLabel("Nome do Estado");
        textDialogStateName.setPlaceholder("Digite o nome do estado");

        textDialogStateAcronym.setRequired(true);
        textDialogStateAcronym.setClearButtonVisible(true);
        textDialogStateAcronym.setLabel("Sigla do Estado");
        textDialogStateAcronym.setPlaceholder("Digite a UF do estado");

        VerticalLayout fieldLayout = new VerticalLayout(textDialogStateName,
                textDialogStateAcronym);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldLayout.getStyle().set("width", "400px").set("max-width", "100%");
        return fieldLayout;
    }

    private void createFooterState(Dialog dialog, State state) {
        Button cancelButton = new Button("Cancelar", e -> dialog.close());
        Button saveButton = new Button("Salvar", e -> {
            try {
                String stateName = textDialogStateName.getValue();
                boolean stateExists = stateRepository.existsByName(stateName);
                if (stateExists) {
                    textDialogStateName.setInvalid(true);
                    textDialogStateName.setErrorMessage("Estado já existe!");
                    textDialogStateName.clear();
                    textDialogStateAcronym.clear();
                } else {
                    state.setName(textDialogStateName.getValue());
                    state.setAcronym(textDialogStateAcronym.getValue());
                    stateRepository.save(state);
                    dialog.close();
                    comboBoxState.setItems(stateRepository.findAll());
                    Notification notification =
                            Notification.show(
                                    "Estado cadastrado com sucesso!", 6000, Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
            } catch (Exception exception) {
                Notification notification =
                        Notification.show(
                                "Estado não cadastrado: " + exception.getMessage(),
                                6000,
                                Notification.Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);
    }

    private static Renderer<City> createStateRenderer() {
    return LitRenderer.<City>of(
            "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                + "    <span> " +
                    "${item.fullName}, " +
                    "<span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">" +
                    "${item.acronym} </span>" +
                    "</span>"
                + "  </vaadin-vertical-layout>"
                + "</vaadin-horizontal-layout>")
        .withProperty("fullName", city -> city.getState().getName())
        .withProperty("acronym", city -> city.getState().getAcronym());
    }

    private static final SerializableBiConsumer<Span, City> countryComponentUpdater =
            (span, city) -> {
                Countries country = cityService.get(city.getCode()).orElseThrow().getCountry();
                if (country == null || country.getName().isEmpty()) {
                    span.setText("");
                } else {
                    span.setText(country.getName());
                }
            };
    
    private static ComponentRenderer<Span, City> createCountryeComponentRenderer() {
        return new ComponentRenderer<>(Span::new, countryComponentUpdater);
    }

    public void setComboBoxState(ComboBox comboBox) {
        List<State> states = stateRepository.findAll();
        comboBox.setItems(states);
        comboBox.setItemLabelGenerator(s -> ((State) s).getName() + " - " + ((State) s).getAcronym());
    }
    
    private void setGridSampleData(Grid grid) {
        if (cityService != null) {
            grid.setItems(query -> cityService.list(
                            PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                    .stream()
                    .map(u -> new City(
                            u.getCode(),
                            u.getName(),
                            u.getState()
                    )));
        } else {
            grid.setItems(Collections.emptyList());
        }
    }

    private HorizontalLayout actionManager(City city) {
        HorizontalLayout layoutManager = new HorizontalLayout();
        city = cityService.get(city.getCode()).orElseThrow();

        if (checker.checkPermission()) {
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                    ButtonVariant.LUMO_TERTIARY_INLINE,
                    ButtonVariant.LUMO_TERTIARY);
            City cityEdit = city;
            editButton.addClickListener(e -> this.editCity(cityEdit));

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                    ButtonVariant.LUMO_ERROR,
                    ButtonVariant.LUMO_TERTIARY);
            City cityDelete = city;
            deleteButton.addClickListener(e -> this.deleteCity(cityDelete));

            layoutManager.add(editButton, deleteButton);
            layoutManager.setAlignItems(Alignment.CENTER);
            layoutManager.setJustifyContentMode(JustifyContentMode.CENTER);
            layoutManager.setSpacing(false);
            layoutManager.setPadding(false);
            layoutManager.setMargin(false);
            layoutManager.getStyle().set("flex-grow", "0");
            return layoutManager;
        }
        Button editButton = new Button(new Icon(VaadinIcon.EDIT));
        editButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_TERTIARY_INLINE,
                ButtonVariant.LUMO_TERTIARY);
        editButton.setEnabled(false);

        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_TERTIARY);
        deleteButton.setEnabled(false);

        layoutManager.add(editButton, deleteButton);
        layoutManager.setAlignItems(Alignment.CENTER);
        layoutManager.setJustifyContentMode(JustifyContentMode.CENTER);
        layoutManager.setSpacing(false);
        layoutManager.setPadding(false);
        layoutManager.setMargin(false);
        layoutManager.getStyle().set("flex-grow", "0");
        return layoutManager;
    }

    private void saveCities(){
        City city = cityService.findByName(textField.getValue()).orElse(new City());
        State state = stateRepository.findById(((State) comboBoxState.getValue()).getCode()).orElse(new State());
            if (city.isNew()) {
                try {
                    city.setName(textField.getValue());
                    city.setState(state);
                    city.setCountry((Countries) comboBoxCountry.getValue());
                    cityService.save(city);
                    Notification notification =
                        Notification.show(
                            "Cidade cadastrado com sucesso!", 6000, Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    cleanFields();
                    stripedGrid.getDataProvider().refreshAll();
                } catch (Exception e) {
                    Notification notification =
                        Notification.show(
                            "Erro ao cadastrar Cidade: " + e.getMessage(),
                            6000,
                            Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
    } else {
              try {
                city.setName(textField.getValue());
                city.setState(state);
                Countries countries = (Countries) comboBoxCountry.getValue();
                city.setCountry(countries);
                cityService.update(city);
                Notification notification =
                    Notification.show(
                        "Cidade atualizado com sucesso!", 6000, Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                cleanFields();
                stripedGrid.getDataProvider().refreshAll();
              } catch (Exception e) {
                Notification notification =
                    Notification.show(
                        "Erro ao atualizar Cidade: " + e.getMessage(),
                        6000,
                        Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
              }
            }
    }

    public void editCity(City cityEdit) {
        cleanFields();
        try {
            cleanDialogFields();
            dialog.setHeaderTitle(
                    String.format("Atualizar cidade: \"%s\"?", cityEdit.getName()));
            dialog.add("Tem certeza de que deseja editar esta cidade?");
            dialog.open();
            Button confirmButton = new Button("Confirmar", (e1) -> dialog.close());
            confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            confirmButton.getStyle().set("margin-right", "auto");
            confirmButton.addClickListener(event -> {
                textField.setValue(cityEdit.getName());
                State state = stateRepository.findById(cityEdit.getState().getCode()).orElseThrow();
                comboBoxState.setValue(state);
                Countries countries = cityEdit.getCountry().getName().isEmpty() ? null : cityEdit.getCountry();
                comboBoxCountry.setValue(countries);
                dialog.close();
            });
            dialog.getFooter().add(confirmButton);
            Button cancelButton = new Button("Cancelar", (e2) -> dialog.close());
            cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            dialog.getFooter().add(cancelButton);
        } catch (Exception exception) {
            Notification notification = Notification.show("Dados da cidade não foi carregado: " + exception.getMessage(), 6000,
                    Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    public void deleteCity(City city) {
        try{
            cleanDialogFields();
            dialog.setHeaderTitle(
                    String.format("Deletar cidade: \"%s\"?", city.getName()));
            dialog.add("Tem certeza de que deseja excluir esta cidade permanentemente?");

            Button deleteButton = new Button("Deletar", (e) -> dialog.close());
            deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                    ButtonVariant.LUMO_ERROR);
            deleteButton.getStyle().set("margin-right", "auto");
            deleteButton.addClickListener(event -> {
                cityService.delete(city.getCode());
                stripedGrid.getDataProvider().refreshAll();
                dialog.close();
                Notification notification = Notification.show("Cidade excluído com sucesso!", 6000,
                        Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            });
            dialog.getFooter().add(deleteButton);

            Button cancelButton = new Button("Cancelar", (e) -> dialog.close());
            cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            dialog.getFooter().add(cancelButton);

            dialog.open();
        } catch (Exception exception) {
            Notification notification = Notification.show("Cidade não identificada: " + exception.getMessage(), 6000,
                    Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    public void setComboBoxCountry(ComboBox comboBox) {
        List<Countries> countries = Arrays.asList(Countries.values());
        comboBox.setItems(countries);
        comboBox.setItemLabelGenerator(c -> ((Countries) c).getName());
    }

    private void cancelFields() {
        cleanDialogFields();
        dialog.setHeaderTitle("Cadastro de Cidade");
        dialog.add("Tem certeza de que deseja cancelar o cadastro deste cidade?");
        dialog.open();
        Button confirmButton = new Button("Confirmar", (e) -> dialog.close());
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.getStyle().set("margin-right", "auto");
        confirmButton.addClickListener(e -> {
            cleanFields();
            dialog.close();
        });
        dialog.getFooter().add(confirmButton);
        Button cancelButton = new Button("Cancelar", (e) -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getFooter().add(cancelButton);
    }

    private void cleanFields() {
        textField.clear();
        textField.setInvalid(false);
        comboBoxCountry.clear();
        comboBoxCountry.setInvalid(false);
        comboBoxState.clear();
        comboBoxState.setInvalid(false);
    }

    private void cleanDialogFields() {
        dialog.removeAll();
        dialog.getHeader().removeAll();
        dialog.getFooter().removeAll();
    }

}
