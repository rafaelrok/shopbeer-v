package br.com.rafaelvieira.shopbeer.views.costumerregister;

import br.com.rafaelvieira.shopbeer.domain.*;
import br.com.rafaelvieira.shopbeer.domain.enums.Countries;
import br.com.rafaelvieira.shopbeer.domain.enums.TypePerson;
import br.com.rafaelvieira.shopbeer.repository.StateRepository;
import br.com.rafaelvieira.shopbeer.security.UserPermissionChecker;
import br.com.rafaelvieira.shopbeer.services.CityService;
import br.com.rafaelvieira.shopbeer.services.CostumerService;
import br.com.rafaelvieira.shopbeer.views.MainLayout;
import com.vaadin.componentfactory.addons.inputmask.InputMask;
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
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.value.ValueChangeMode;
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

@PageTitle("Cadastro de Clientes")
@Route(value = "costumer-register", layout = MainLayout.class)
@AnonymousAllowed
@Uses(Icon.class)
@CssImport("themes/shopbeer-v/views/costumer-register-view.css")
public class CostumerRegisterView extends Composite<VerticalLayout> {

    private static final String CPF_PATTERN = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$";
    private static final String CNPJ_PATTERN = "^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$";
    
    private static final String ZIP_CODE_PATTERN = "^\\d{5}-\\d{3}$";

    @Autowired()
    private static CostumerService costumerService;

    @Autowired()
    private static CityService cityService;

    @Autowired()
    private static StateRepository stateRepository;

    @Autowired
    private SecurityContextHolder securityContextHolder;

    List<String> allowedRoles = Arrays.asList("ROLE_ADMIN", "USER");
    UserPermissionChecker<UserEmployee> checker = new UserPermissionChecker<>(securityContextHolder, allowedRoles);

    VerticalLayout layoutColumn = new VerticalLayout();
    VerticalLayout layoutColumn2 = new VerticalLayout();
    Span spanPersonalData = new Span();
    Span spanAddress = new Span();
    H3 h3PersonalData = new H3();
    H3 h3Address = new H3();
    FormLayout formLayout2Col = new FormLayout();
    FormLayout formLayout2Col2 = new FormLayout();
    HorizontalLayout layoutRow = new HorizontalLayout();
    HorizontalLayout layoutRow2 = new HorizontalLayout();
    TextField textFieldName = new TextField();
    TextField textFieldCpfCnpj = new TextField();
    InputMask cpfFieldMask = new InputMask("000.000.000-00");
    InputMask cnpjFieldMask = new InputMask("00.000.000/0000-00");
    TextField textFieldPhone = new TextField();
    InputMask phoneFieldMask = new InputMask("(00) 00000-0000");
    InputMask phoneCorpFieldMask = new InputMask("(00) 0000-0000");
    ComboBox comboBoxPerson = new ComboBox();
    EmailField emailField = new EmailField();
    TextField textFieldStreet = new TextField();
    IntegerField numberField = new IntegerField ();
    TextField textFieldComplement = new TextField();
  TextField textFieldZipCode = new TextField();
    InputMask maskZipCode = new InputMask("00000-000");
    ComboBox comboBoxState = new ComboBox();
    ComboBox comboBoxCity = new ComboBox();
    Button cityButton = new Button();
    Button stateButton = new Button();
    Button buttonSave = new Button();
    Button buttonCancel = new Button();
    Button buttonSearchAddress = new Button();
    Grid<Costumer> costumerGrid = new Grid<>(Costumer.class, false);
    Dialog dialog = new Dialog();
    TextField textDialogCityName = new TextField();
    TextField textDialogStateName = new TextField();
    TextField textDialogStateAcronym = new TextField();
    ComboBox comboBoxCountry = new ComboBox();

    public CostumerRegisterView(CostumerService costumerService, CityService cityService, StateRepository stateRepository) {
        this.costumerService = costumerService;
        this.cityService = cityService;
        this.stateRepository = stateRepository;
        addClassName("costumer-register-view");

        //<theme-editor-local-classname>
        layoutColumn.addClassName("costumer-register-view-layout-column");
        textFieldName.addClassName("costumer-register-view-text-field-name");
        textFieldZipCode.addClassName("costumer-register-view-text-field-zip-code");
        textFieldStreet.addClassName("costumer-register-view-text-field-street");
        textFieldComplement.addClassName("costumer-register-view-text-field-complement");
        textFieldCpfCnpj.addClassName("costumer-register-view-text-field-cpf-cnpj");
        textFieldPhone.addClassName("costumer-register-view-text-field-phone");
        comboBoxPerson.setOverlayClassName("costumer-register-view-combo-box-person");
        comboBoxPerson.addClassName("costumer-register-view-combo-box-person");
        emailField.addClassName("costumer-register-view-email-field-email");
        comboBoxState.setOverlayClassName("costumer-register-view-combo-box-state");
        comboBoxState.addClassName("costumer-register-view-combo-box-state");
        comboBoxCity.setOverlayClassName("costumer-register-view-combo-box-city");
        comboBoxCity.addClassName("costumer-register-view-combo-box-city");
        buttonCancel.addClassName("costumer-register-view-button-cancel");
        buttonSearchAddress.addClassName("costumer-register-view-button-search-address");
        costumerGrid.addClassName("costumer-register-view-grid-costumer");
        numberField.addClassName("costumer-register-view-integer-field-number");
        stateButton.addClassName("costumer-register-view-state-button");
        cityButton.addClassName("costumer-register-view-city-button");
        comboBoxCountry.setOverlayClassName("costumer-register-view-combo-box-country");

        getContent().setWidth("100%");
        getContent().getStyle().set("flex", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);

        h3PersonalData.setText("Dados pessoais");
        h3PersonalData.setWidth("100%");
        spanPersonalData.addClassName(LumoUtility.Gap.SMALL);
        spanPersonalData.getStyle().setColor("var(--lumo-secondary-text-color)");
        spanPersonalData.setWidth("100%");
        spanPersonalData.setText("Preencha os dados pessoais do cliente");

        h3Address.setText("Endereço");
        h3Address.setWidth("100%");
        spanAddress.addClassName(LumoUtility.Gap.SMALL);
        spanAddress.getStyle().setColor("var(--lumo-secondary-text-color)");
        spanAddress.setWidth("100%");
        spanAddress.setText("Preencha os dados de endereço do cliente");

        layoutRow.addClassName(LumoUtility.Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex", "1");
        layoutRow.setSpacing(true);
        layoutRow.setMargin(true);
        layoutRow.setAlignItems(FlexComponent.Alignment.CENTER);

        layoutRow2.addClassName(LumoUtility.Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.getStyle().set("flex", "1");
        layoutRow2.setSpacing(true);
        layoutRow2.setMargin(true);
        layoutRow2.setAlignItems(FlexComponent.Alignment.CENTER);

        /*
         * Layout reponsavel pelo cadastro de dados pessoais
         */
        layoutColumn.setWidth("100%");
        layoutColumn.setMaxWidth("1190px");
        layoutColumn.getStyle().set("flex-grow", "1");

        formLayout2Col.setWidth("100%");
        formLayout2Col.getStyle().setBorder("1px solid var(--lumo-contrast-30pct)");
        formLayout2Col.getStyle().setBorderRadius("5px");
        formLayout2Col.getStyle().set("padding", "10px");
        formLayout2Col.getStyle().set("spacing", "true");

        formLayout2Col.setResponsiveSteps(
                new FormLayout.ResponsiveStep("300px", 1),
                new FormLayout.ResponsiveStep("600px", 2),
                new FormLayout.ResponsiveStep("900px", 3)
        );

        comboBoxPerson.setLabel("Tipo de pessoa");
        comboBoxPerson.setWidth("100%");
        comboBoxPerson.setMaxWidth("250px");
        comboBoxPerson.setRequired(true);
        comboBoxPerson.setClearButtonVisible(true);
        comboBoxPerson.setPlaceholder("Selecione o Tipo");
        comboBoxPerson.setPrefixComponent(LineAwesomeIcon.USER_SOLID.create());
        setComboBoxPerson(comboBoxPerson);
        comboBoxPerson.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                if (event.getValue().equals(TypePerson.PHYSICAL_PERSON)) {
                    textFieldCpfCnpj.setLabel("CPF");
                    textFieldCpfCnpj.setPlaceholder("Digite o CPF");
                    textFieldCpfCnpj.setRequired(true);
                    textFieldName.setLabel("Nome Completo");
                    textFieldName.setPlaceholder("Digite o nome completo");
                    emailField.setLabel("E-mail");
                    textFieldPhone.setLabel("Celular");
                    phoneFieldMask.extend(textFieldPhone);
                    textFieldCpfCnpj.setErrorMessage("CPF inválido");
                    cpfFieldMask.extend(textFieldCpfCnpj);
                } else {
                    textFieldCpfCnpj.setLabel("CNPJ");
                    textFieldCpfCnpj.setPlaceholder("Digite o CNPJ");
                    textFieldCpfCnpj.setRequired(true);
                    textFieldName.setLabel("Razão Social");
                    textFieldName.setPlaceholder("Razão social ou nome fantasia");
                    emailField.setLabel("E-mail Corporativo");
                    textFieldPhone.setLabel("Telefone Corporativo");
                    phoneCorpFieldMask.extend(textFieldPhone);
                    textFieldCpfCnpj.setErrorMessage("CNPJ inválido");
                    spanAddress.setText("Preencha os dados de endereço da empresa");
                    cnpjFieldMask.extend(textFieldCpfCnpj);
                }
            }
        });

        textFieldName.setLabel("Nome Completo");
        textFieldName.setWidth("100%");
        textFieldName.setMaxWidth("390px");
        textFieldName.setRequired(true);
        textFieldName.setPlaceholder("Digite o nome completo");
        textFieldName.setPrefixComponent(LineAwesomeIcon.USER_SOLID.create());
        textFieldName.setClearButtonVisible(true);

        textFieldCpfCnpj.setLabel("CPF/CNPJ");
        textFieldCpfCnpj.setWidth("100%");
        textFieldCpfCnpj.setMaxWidth("200px");
        textFieldCpfCnpj.setRequired(true);
        textFieldCpfCnpj.setClearButtonVisible(true);
        textFieldCpfCnpj.setPlaceholder("CPF ou CNPJ");
        textFieldCpfCnpj.setPrefixComponent(LineAwesomeIcon.ID_CARD_SOLID.create());

        textFieldPhone.setLabel("Celular");
        textFieldPhone.getStyle().set("flex-grow", "1");
        textFieldPhone.setMaxWidth("200px");
        textFieldPhone.setWidth("100%");
        textFieldPhone.setClearButtonVisible(true);
        textFieldPhone.setPlaceholder("Digite o telefone");
        textFieldPhone.setPrefixComponent(LineAwesomeIcon.PHONE_SOLID.create());
        phoneFieldMask.extend(textFieldPhone);

        emailField.setLabel("Email");
        emailField.setWidth("100%");
        emailField.setMaxWidth("300px");
        emailField.setRequired(true);
        emailField.setClearButtonVisible(true);
        emailField.setPlaceholder("Digite o e-mail");
        emailField.setPattern("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$");
        emailField.setErrorMessage("Por favor, digite um e-mail válido");
        emailField.setPrefixComponent(LineAwesomeIcon.ENVELOPE_SOLID.create());

        /*
        * Layout reponsavel pelo cadastro de endereços
         */
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex", "1");
        layoutColumn2.setMaxWidth("1190px");

        formLayout2Col2.setWidth("100%");
        formLayout2Col2.getStyle().setBorder("1px solid var(--lumo-contrast-30pct)");
        formLayout2Col2.getStyle().setBorderRadius("5px");
        formLayout2Col2.getStyle().set("padding", "10px");
        formLayout2Col2.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("700px", 2),
                new FormLayout.ResponsiveStep("900px", 3)
        );

        textFieldZipCode.setLabel("CEP");
        textFieldZipCode.setWidth("100%");
        textFieldZipCode.setMaxWidth("280px");
        textFieldZipCode.setRequired(true);
        textFieldZipCode.setPlaceholder("_____-___");
        textFieldZipCode.setPrefixComponent(LineAwesomeIcon.MAP_MARKER_ALT_SOLID.create());
        maskZipCode.extend(textFieldZipCode);
        
        buttonSearchAddress.setIcon(new Icon(VaadinIcon.SEARCH));
        buttonSearchAddress.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
        buttonSearchAddress.setIconAfterText(true);
        buttonSearchAddress.setWidth("min-content");
        buttonSearchAddress.setMaxWidth("50px");

        textFieldStreet.setLabel("Nome da Rua");
        textFieldStreet.setWidth("100%");
        textFieldStreet.setMaxWidth("650px");
        textFieldStreet.setRequired(true);
        textFieldStreet.setClearButtonVisible(true);
        textFieldStreet.setPlaceholder("Digite nome da rua");
        textFieldStreet.setPrefixComponent(LineAwesomeIcon.ROAD_SOLID.create());
        
        numberField.setLabel("Número");
        numberField.setWidth("100%");
        numberField.setMaxWidth("180px");
        numberField.setRequired(true);
        numberField.setClearButtonVisible(true);
        numberField.setPlaceholder("Nº da residência");
        numberField.getStyle().set("margin-left", "10px");

        textFieldComplement.setLabel("Complemento");
        textFieldComplement.setWidth("100%");
        textFieldComplement.setMaxWidth("320px");
        textFieldComplement.setClearButtonVisible(true);
        textFieldComplement.setPlaceholder("Digite o complemento");
        textFieldComplement.setPrefixComponent(LineAwesomeIcon.BUILDING_SOLID.create());

        comboBoxCity.setLabel("Cidade");
        comboBoxCity.setWidth("100%");
        comboBoxCity.setMaxWidth("250px");
        comboBoxCity.setWidth("min-content");
        comboBoxCity.setClearButtonVisible(true);
        comboBoxCity.setPlaceholder("Selecione uma cidade");
        comboBoxCity.setRequired(true);
        comboBoxCity.setPrefixComponent(LineAwesomeIcon.CITY_SOLID.create());
        setComboBoxCity(comboBoxCity, comboBoxState);

        cityButton.addClassName("state-button");
        cityButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        cityButton.setIconAfterText(true);
        cityButton.setWidth("100%");
        cityButton.setMaxWidth("30px");
        cityButton.setIcon(new Icon(VaadinIcon.PLUS));
        cityButton.addClickListener(e -> {
            City city = new City();
            cleanDialogFields();
            newCity(city);
        });

        comboBoxState.setLabel("Estado");
        comboBoxState.setWidth("100%");
        comboBoxState.setMaxWidth("250px");
        comboBoxState.setClearButtonVisible(true);
        comboBoxState.setPlaceholder("Selecione um estado");
        comboBoxState.setRequired(true);
        comboBoxState.setPrefixComponent(LineAwesomeIcon.BUILDING_SOLID.create());
        setComboBoxState(comboBoxState);

        stateButton.addClassName("state-button");
        stateButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        stateButton.setIconAfterText(true);
        stateButton.setWidth("100%");
        stateButton.setMaxWidth("30px");
        stateButton.setIcon(new Icon(VaadinIcon.PLUS));
        stateButton.addClickListener(e -> {
            State state = new State();
            cleanDialogFields();
            newState(state);
        });

        buttonSave.setText("Salvar");
        buttonSave.setWidth("min-content");
        buttonSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSave.addClickListener(event -> {
            this.saveCostumer();
        });

        buttonCancel.setText("Cancelar");
        buttonCancel.setWidth("min-content");
        buttonCancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        buttonCancel.addClickListener(event -> {this.cancelFields();});

        List<Costumer> costumers = costumerService.findAll();
        if(costumers == null){
            costumers = new ArrayList<>();
        }
        GridListDataView<Costumer> dataView = costumerGrid.setItems(costumers);

        TextField searchField = new TextField();
        searchField.setWidth("100%");
        searchField.setPlaceholder("Procurar");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> dataView.refreshAll());

        dataView.addFilter(costumer -> {
            String searchTerm = searchField.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            boolean matchesFullName = matchesTerm(costumer.getName(),
                    searchTerm);
            boolean matchesEmail = matchesTerm(costumer.getEmail(), searchTerm);
            boolean matchesCpfcnpj = matchesTerm(costumer.getCpfcnpj(),
                    searchTerm);

            return matchesFullName || matchesEmail || matchesCpfcnpj;
        });

        costumerGrid.setWidth("100%");
        costumerGrid.getStyle().set("flex-grow", "0");
        costumerGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        costumerGrid.setPageSize(10);
        costumerGrid.getStyle().setBackgroundColor("var(--lumo-contrast-5pct)");
        costumerGrid.getStyle().set("border", "1px solid var(--lumo-contrast-40pct)");
        costumerGrid.getStyle().set("border-radius", "5px");
        costumerGrid.getStyle().set("padding", "10px");
        costumerGrid.getHeaderRows().stream()
                .flatMap(row -> row.getCells().stream())
                .forEach(cell -> cell.getComponent().getElement().getStyle().set("font-weight", "600"));

        costumerGrid.addColumn(new ComponentRenderer<>(costumer -> {
                    Div divCode = new Div();
                    divCode.setText(String.valueOf(costumer.getCode()));
                    divCode.getStyle().set("display", "flex");
                    divCode.getStyle().set("justify-content", "center");
                    divCode.getStyle().set("align-items", "center");
                    divCode.getStyle().set("width", "40px");
                    return divCode;
                })).setHeader(new Html("<div style='text-align:center; width: 55px; color:white'>CÓDIGO</div>"))
                .setAutoWidth(true).setFlexGrow(0)
                .setSortable(true).setKey("code")
                .setFooter(new Html("<div style='color:white'>" + String.format("Total Clientes: %d", costumers.size()) + "</div>"));

        costumerGrid.addColumn(new ComponentRenderer<>( costumer -> {
                    Div divType = new Div();
                    divType.setText(createTypePersonComponentRenderer().createComponent(costumer).getText());
                    divType.getStyle().set("display", "flex");
                    divType.getStyle().set("justify-content", "start");
                    divType.getStyle().set("align-items", "start");
                    divType.getStyle().set("width", "80px");
                    return divType;
                }))
                .setHeader(new Html("<div style='text-align:start; width: 70px; color:white'>PESSOA</div>"))
                .setAutoWidth(true).setFlexGrow(0);

        costumerGrid.addColumn(createCostumerInfoRenderer())
                .setHeader(new Html("<div style='text-align:start; width: 200px; color:white'>INFO PESSOAL</div>"))
                .setSortOrderProvider(
                        direction -> (Stream<QuerySortOrder>) dataView
                                .setSortOrder(Costumer::getName, direction))
                .setAutoWidth(true).setFlexGrow(0);

        costumerGrid.addColumn(createCostumerAddressRenderer())
                .setHeader(new Html("<div style='text-align:start; width: 170px; color:white'>ENDEREÇO</div>"))
                .setSortOrderProvider(
                        direction -> (Stream<QuerySortOrder>) dataView
                                .setSortOrder(c -> c.getAddress().getStreet(), direction))
                .setAutoWidth(true).setFlexGrow(0);

        costumerGrid.addColumn(createStateRenderer())
                .setHeader(new Html("<div style='text-align:center; width: 100px; color:white'>CIDADE/UF</div>"))
                .setSortOrderProvider(
                        direction -> (Stream<QuerySortOrder>) dataView
                                .setSortOrder(c -> c.getAddress().getState().getName(), direction))
                .setWidth("200px");

        costumerGrid.addColumn(
                        new ComponentRenderer<>(this::actionManager))
                .setHeader(new Html("<div style='text-align:center; width: 50px; color:white'>AÇÕES</div>"));

        costumerGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        setGridSampleData(costumerGrid);

        getContent().add(layoutColumn);
        getContent().add(layoutColumn2);

        layoutColumn.add(h3PersonalData);
        layoutColumn.add(spanPersonalData);
        layoutColumn.add(formLayout2Col);
        formLayout2Col.add(comboBoxPerson);
        formLayout2Col.add(textFieldCpfCnpj);
        formLayout2Col.add(textFieldName);
        formLayout2Col.add(emailField);
        formLayout2Col.add(textFieldPhone);

        layoutColumn2.add(h3Address);
        layoutColumn2.add(spanAddress);
        layoutColumn2.add(formLayout2Col2);
        formLayout2Col2.add(textFieldZipCode);
        formLayout2Col2.add(buttonSearchAddress);
        formLayout2Col2.add(textFieldStreet);
        formLayout2Col2.add(numberField);
        formLayout2Col2.add(textFieldComplement);
        formLayout2Col2.add(comboBoxCity);
        formLayout2Col2.add(cityButton);
        formLayout2Col2.add(comboBoxState);
        formLayout2Col2.add(stateButton);

        layoutColumn2.add(layoutRow2);
        layoutColumn.add(layoutRow);

        layoutRow2.add(buttonSave);
        layoutRow2.add(buttonCancel);
        getContent().add(costumerGrid);
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private static Renderer<Costumer> createCostumerInfoRenderer() {
        return LitRenderer.<Costumer> of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                                + "    <span> ${item.fullName} </span>"
                                + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                                + "      ${item.email}" + "    </span>"
                                + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                                + "      ${item.phone}" + "    </span>"
                                + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                                + "      ${item.cpfCnpj}" + "    </span>"
                                + "  </vaadin-vertical-layout>"
                                + "</vaadin-horizontal-layout>")
                .withProperty("fullName", Costumer::getName)
                .withProperty("email", Costumer::getEmail)
                .withProperty("phone", Costumer::getTelephone)
                .withProperty("cpfCnpj", Costumer::getCpfcnpj);
    }

    private static Renderer<Costumer> createCostumerAddressRenderer() {
        return LitRenderer.<Costumer> of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                                + "    <span> ${item.street} - ${item.number} </span>"
                                + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                                + "      ${item.complement}" + "    </span>"
                                + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                                + "      ${item.zipCode}" + "    </span>"
                                + "  </vaadin-vertical-layout>"
                                + "</vaadin-horizontal-layout>")
                .withProperty("street", c -> c.getAddress().getStreet())
                .withProperty("number", c -> c.getAddress().getNumber())
                .withProperty("complement", c -> c.getAddress().getComplement())
                .withProperty("zipCode", c -> c.getAddress().getZipCode());
    }

    private static Renderer<Costumer> createStateRenderer() {
        return LitRenderer.<Costumer>of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                                + "     <span> "
                                + "        ${item.cityName} "
                                + "     </span>"
                                + "     <span> "
                                + "         ${item.stateName}, "
                                + "     <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                                + "         ${item.acronym} </span>"
                                + "     </span>"
                                + "  </vaadin-vertical-layout>"
                                + "</vaadin-horizontal-layout>")
                .withProperty("cityName", c -> c.getAddress().getCity() != null ?
                        c.getAddress().getCity().getName() : "")
                .withProperty("stateName", c -> c.getAddress().getCity().getState() != null ?
                        c.getAddress().getCity().getState().getName() : "")
                .withProperty("acronym", c -> c.getAddress().getCity().getState() != null ?
                        c.getAddress().getCity().getState().getAcronym() : "");
    }

    private static final SerializableBiConsumer<Span, Costumer> typePersonComponentUpdater =
            (span, costumer) -> {
                TypePerson person = costumerService.get(costumer.getCode()).orElseThrow().getTypePerson();
                if (person == null || person.getDescription().isEmpty()) {
                    span.setText("");
                } else {
                    String theme =
                            String.format("badge contrast %s",  person.getDescription());
                    span.getElement().setAttribute("theme", theme);
                    span.setText(person.getDescription());
                }
            };

    private static ComponentRenderer<Span, Costumer> createTypePersonComponentRenderer() {
        return new ComponentRenderer<>(Span::new, typePersonComponentUpdater);
    }

    public void setComboBoxPerson(ComboBox comboBox) {
        List<TypePerson> types = new ArrayList<>();
        types.add(TypePerson.LEGAL_PERSON);
        types.add(TypePerson.PHYSICAL_PERSON);
        comboBox.setItems(types);
        comboBox.setItemLabelGenerator(p -> ((TypePerson) p).getDescription());
    }

    private void setGridSampleData(Grid grid) {
        grid.setItems(query -> costumerService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
    }

    public void setComboBoxCity(ComboBox<City> boxCity, ComboBox<State> boxState) {
        List<City> cities = cityService.findAll();
        boxCity.setItems(cities);
        boxCity.setItemLabelGenerator(City::getName);

        boxCity.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                State state  = cities.stream()
                        .filter(c -> c.getName().equals(event.getValue().getName()))
                        .map(City::getState)
                        .findFirst()
                        .orElse(new State());

                boxState.setItems(Collections.singletonList(state));
                boxState.setItemLabelGenerator(s -> s.getName() + " - " + s.getAcronym());
                boxState.setValue(state);
            } else {
                boxState.clear();
                setComboBoxState(boxState);
            }
        });
    }

    public void setComboBoxState(ComboBox comboBox) {
        List<State> states = stateRepository.findAll();
        comboBox.setItems(states);
        comboBox.setItemLabelGenerator(s -> ((State) s).getName() + " - " + ((State) s).getAcronym());
    }

    public void newCity(City city) {
        try {
            cleanDialogFields();
            dialog.setHeaderTitle("Cadastro Rápido Cidade");
            dialog.open();
            VerticalLayout dialogLayout = createCityLayout();
            dialog.add(dialogLayout);
            dialog.setModal(false);
            dialog.setDraggable(true);
            createFooterCity(dialog, city);
        } catch (Exception exception) {
            Notification notification = Notification.show("Erro ao cadastrar: " + exception.getMessage(), 6000,
                    Notification.Position.TOP_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private VerticalLayout createCityLayout() {
        textDialogCityName.setRequired(true);
        textDialogCityName.setClearButtonVisible(true);
        textDialogCityName.setLabel("Nome do Cidade");
        textDialogCityName.setPlaceholder("Digite nome da cidade");
        textDialogCityName.setPrefixComponent(LineAwesomeIcon.CITY_SOLID.create());

        comboBoxCountry.setLabel("País");
        comboBoxCountry.setWidth("100%");
        comboBoxCountry.setMaxWidth("300px");
        comboBoxCountry.setClearButtonVisible(true);
        comboBoxCountry.setPlaceholder("Selecione um país");
        comboBoxCountry.setRequired(true);
        comboBoxCountry.setPrefixComponent(LineAwesomeIcon.GLOBE_AMERICAS_SOLID.create());
        setComboBoxCountry(comboBoxCountry);

        VerticalLayout fieldLayout = new VerticalLayout(
                textDialogCityName,
                comboBoxCountry
        );
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldLayout.getStyle().set("width", "400px").set("max-width", "100%");
        return fieldLayout;
    }

    public void setComboBoxCountry(ComboBox comboBox) {
        List<Countries> countries = Arrays.asList(Countries.values());
        comboBox.setItems(countries);
        comboBox.setItemLabelGenerator(c -> ((Countries) c).getName());
    }

    private void createFooterCity(Dialog dialog, City city) {
        Button cancelButton = new Button("Cancelar", e -> dialog.close());
        Button saveButton = new Button("Salvar", e -> {
            try{
                String cityName = textDialogCityName.getValue();
                boolean cityExists = cityService.existsByName(cityName);

                if (cityExists) {
                    textDialogCityName.setInvalid(true);
                    textDialogCityName.setErrorMessage("Cidade já existe!");
                    textDialogCityName.clear();
                } else {
                    city.setName(textDialogCityName.getValue());
                    Countries countries = (Countries) comboBoxCountry.getValue();
                    city.setCountry(countries);
                    cityService.save(city);
                    dialog.close();
                    comboBoxCity.setValue(city);
                    Notification notification = Notification.show("Dados cadastrado com sucesso!", 6000,
                            Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
            } catch (Exception exception) {
                Notification notification = Notification.show("Erro ao cadastrar: " + exception.getMessage(), 6000,
                        Notification.Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);
    }

    public void newState(State state) {
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

    private void saveCostumer() {
        Optional<Costumer> optionalCostumer = costumerService.findByCpfcnpj(textFieldCpfCnpj.getValue());
        Optional<State> optionalState = stateRepository.findById(((State) comboBoxState.getValue()).getCode());

        if (optionalCostumer.isPresent() && optionalState.isPresent()) {
            Costumer costumer = optionalCostumer.get();
            State state = optionalState.get();

            if (costumer.isNew()) {
                createCostumer(costumer, state);
            } else {
                updateCostumer(costumer, state);
            }
        } else {
            Notification notification = Notification.show("Cliente ou Estado não encontrado!", 6000,
                    Notification.Position.TOP_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void createCostumer(Costumer costumer, State state) {
        try {
            fillCostumerData(costumer);
            if (costumer.getAddress() == null) {
                costumer.setAddress(new Address());
            }
            fillAddressData(costumer, state);
            costumerService.save(costumer);
            showNotification("Cliente cadastrado com sucesso!", NotificationVariant.LUMO_SUCCESS);
        } catch (Exception e) {
            showNotification("Erro ao cadastrar novo cliente: " + e.getMessage(), NotificationVariant.LUMO_ERROR);
        }
        refreshGridAndCleanFields();
    }

    private void updateCostumer(Costumer costumer, State state) {
        try {
            fillCostumerData(costumer);
            if (costumer.getAddress() == null) {
                costumer.setAddress(new Address());
            }
            fillAddressData(costumer, state);
            costumerService.update(costumer);
            showNotification("Cliente atualizado com sucesso!", NotificationVariant.LUMO_SUCCESS);
        } catch (Exception e) {
            showNotification("Erro ao atualizar cliente: " + e.getMessage(), NotificationVariant.LUMO_ERROR);
        }
        refreshGridAndCleanFields();
    }

    private void fillCostumerData(Costumer costumer) {
        costumer.setName(textFieldName.getValue());

        String currentCpfCnpj = textFieldCpfCnpj.getValue();
        String existingCpfCnpj = costumerService.get(costumer.getCode()).orElseThrow().getCpfcnpj();
        if(!currentCpfCnpj.isEmpty()){
            if(!currentCpfCnpj.equals(existingCpfCnpj)){
                costumer.setCpfcnpj(currentCpfCnpj);
            }
        }

        String currentEmail = emailField.getValue();
        String existingEmail = costumerService.get(costumer.getCode()).orElseThrow().getEmail();
        if(!currentEmail.isEmpty()){
            if(!currentEmail.equals(existingEmail)){
                costumer.setEmail(currentEmail);
            }
        }

        TypePerson typePerson = (TypePerson) comboBoxPerson.getValue();
        costumer.setTypePerson(typePerson);
        costumer.setTelephone(textFieldPhone.getValue());
    }

    private void fillAddressData(Costumer costumer, State state) {
        costumer.getAddress().setZipCode(textFieldZipCode.getValue());
        costumer.getAddress().setStreet(textFieldStreet.getValue());
        costumer.getAddress().setNumber(numberField.getValue().toString());
        costumer.getAddress().setComplement(textFieldComplement.getValue());
        costumer.getAddress().setCity((City) comboBoxCity.getValue());
        costumer.getAddress().setState(state);
    }

    private void showNotification(String message, NotificationVariant variant) {
        Notification notification = Notification.show(message, 6000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(variant);
    }

    private void refreshGridAndCleanFields() {
        cleanFields();
        costumerGrid.getDataProvider().refreshAll();
    }

    private HorizontalLayout actionManager(Costumer costumer) {
        HorizontalLayout layoutManager = new HorizontalLayout();
        costumer = costumerService.get(costumer.getCode()).orElseThrow();

        if (checker.checkPermission()) {
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                    ButtonVariant.LUMO_TERTIARY_INLINE,
                    ButtonVariant.LUMO_TERTIARY);
            Costumer costumerEdit = costumer;
            editButton.addClickListener(e -> this.editCostumer(costumerEdit));

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                    ButtonVariant.LUMO_ERROR,
                    ButtonVariant.LUMO_TERTIARY);
            Costumer costumerDelete = costumer;
            deleteButton.addClickListener(e -> this.deleteCostumer(costumerDelete));

            layoutManager.add(editButton, deleteButton);
            layoutManager.setAlignItems(FlexComponent.Alignment.CENTER);
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
        layoutManager.setAlignItems(FlexComponent.Alignment.CENTER);
        layoutManager.setJustifyContentMode(JustifyContentMode.CENTER);
        layoutManager.setSpacing(false);
        layoutManager.setPadding(false);
        layoutManager.setMargin(false);
        layoutManager.getStyle().set("flex-grow", "0");
        return layoutManager;
    }

    public void editCostumer(Costumer costumerEdit) {
        cleanFields();
        try {
            cleanDialogFields();
            dialog.setHeaderTitle(
                    String.format("Atualizar cliente \"%s\"?", costumerEdit.getName()));
            dialog.add("Tem certeza de que deseja editar este cliente?");
            dialog.open();
            Button confirmButton = new Button("Confirmar", (e1) -> dialog.close());
            confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            confirmButton.getStyle().set("margin-right", "auto");
            confirmButton.addClickListener(event -> {
                textFieldName.setValue(costumerEdit.getName());
                textFieldCpfCnpj.setValue(costumerEdit.getCpfcnpj());
                textFieldPhone.setValue(costumerEdit.getTelephone());
                comboBoxPerson.setValue(costumerEdit.getTypePerson());
                emailField.setValue(costumerEdit.getEmail());
                textFieldStreet.setValue(costumerEdit.getAddress().getStreet());
                numberField.setValue(Integer.valueOf(costumerEdit.getAddress().getNumber()));
                textFieldComplement.setValue(costumerEdit.getAddress().getComplement());
                textFieldZipCode.setValue(costumerEdit.getAddress().getZipCode());
                comboBoxState.setValue(costumerEdit.getAddress().getState());
                comboBoxCity.setValue(costumerEdit.getAddress().getCity());
                costumerGrid.getDataProvider().refreshAll();
                dialog.close();
            });
            dialog.getFooter().add(confirmButton);
            Button cancelButton = new Button("Cancelar", (e2) -> dialog.close());
            cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            dialog.getFooter().add(cancelButton);
        } catch (Exception exception) {
            Notification.show("Usuário não identificado: " + exception.getMessage(), 6000,
                    Notification.Position.MIDDLE);
        }
    }

    public void deleteCostumer(Costumer costumer) {
        cleanDialogFields();
        dialog.setHeaderTitle(
                String.format("Delete Cliente \"%s\"?", costumer.getName()));
        dialog.add("Tem certeza de que deseja excluir este cliente permanentemente?");

        Button deleteButton = new Button("Deletar", (e) -> dialog.close());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        deleteButton.getStyle().set("margin-right", "auto");
        deleteButton.addClickListener(event -> {
            costumerService.delete(costumer.getCode());
            costumerGrid.getDataProvider().refreshAll();
            dialog.close();
        });
        dialog.getFooter().add(deleteButton);

        Button cancelButton = new Button("Cancel", (e) -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getFooter().add(cancelButton);

        dialog.open();
    }

    private void cancelFields() {
        cleanDialogFields();
        dialog.removeAll();
        dialog.setHeaderTitle("Cadastro de cliente");
        dialog.add("Tem certeza de que deseja cancelar o cadastro deste cliente?");
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
        textFieldName.clear();
        textFieldName.setInvalid(false);
        textFieldCpfCnpj.clear();
        textFieldCpfCnpj.setInvalid(false);
        textFieldPhone.clear();
        textFieldPhone.setInvalid(false);
        comboBoxPerson.clear();
        comboBoxPerson.setInvalid(false);
        emailField.clear();
        emailField.setInvalid(false);
        textFieldStreet.clear();
        textFieldStreet.setInvalid(false);
        numberField.clear();
        numberField.setInvalid(false);
        textFieldComplement.clear();
        textFieldComplement.setInvalid(false);
        textFieldZipCode.clear();
        textFieldZipCode.setInvalid(false);
        comboBoxState.clear();
        comboBoxState.setInvalid(false);
        comboBoxCity.clear();
        comboBoxCity.setInvalid(false);
    }

    private void cleanDialogFields() {
        dialog.removeAll();
        dialog.getHeader().removeAll();
        dialog.getFooter().removeAll();
    }
}
