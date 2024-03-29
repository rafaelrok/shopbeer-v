package br.com.rafaelvieira.shopbeer.views.usermanager;

import br.com.rafaelvieira.shopbeer.domain.GroupEmployee;
import br.com.rafaelvieira.shopbeer.domain.UserEmployee;
import br.com.rafaelvieira.shopbeer.domain.enums.Role;
import br.com.rafaelvieira.shopbeer.domain.enums.StatusUserEmployee;
import br.com.rafaelvieira.shopbeer.security.UserPermissionChecker;
import br.com.rafaelvieira.shopbeer.services.GroupEmployeeService;
import br.com.rafaelvieira.shopbeer.services.UserEmployeeService;
import br.com.rafaelvieira.shopbeer.storage.PhotoStorage;
import br.com.rafaelvieira.shopbeer.views.MainLayout;
import com.vaadin.componentfactory.addons.inputmask.InputMask;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;

@PageTitle("Gerenciador de usuários")
@Route(value = "user-manager", layout = MainLayout.class)
@AnonymousAllowed
@Uses(Icon.class)
@CssImport("themes/shopbeer-v/views/user-manager-view.css")
public class UserManagerView extends Composite<VerticalLayout> {

    @Autowired()
    private static UserEmployeeService userEmployeeService;

    @Autowired()
    private GroupEmployeeService groupEmployeeService;

    @Autowired()
    private SecurityContextHolder securityContextHolder;

    int maxFileSizeInBytes = 10 * 1024 * 1024; // 10MB
    private final Span passwordStrengthText;
    private final Icon checkIcon;

    private final PhotoStorage photoStorage;

    VerticalLayout layoutColumn2 = new VerticalLayout();
    H3 h3 = new H3();
    FormLayout formLayout2Col = new FormLayout();
    TextField textField = new TextField();
    TextArea textAreaInfoGrid = new TextArea();
    TextField textUsername = new TextField();
    TextField searchField = new TextField();
    DatePicker datePicker = new DatePicker();
    InputMask datePickerInputMask = new InputMask("00/00/0000");
    EmailField emailField = new EmailField();
    ComboBox comboBoxRole = new ComboBox();
    ComboBox comboBoxStatus = new ComboBox();
    ComboBox comboBoxGroup = new ComboBox();
    Div passwordStrength = new Div();
    PasswordField passwordField = new PasswordField();
    PasswordField passwordField2 = new PasswordField();
    HorizontalLayout layoutRow = new HorizontalLayout();
    HorizontalLayout layoutRow2 = new HorizontalLayout();
    Button buttonSave = new Button();
    Button buttonCancel = new Button();
    Grid<UserEmployee> stripedGrid = new Grid<>(UserEmployee.class, false);
    MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    Upload upload = new Upload(buffer);
    Dialog dialog = new Dialog();
    List<String> allowedRoles = Arrays.asList("ROLE_ADMIN", "USER");
    UserPermissionChecker<UserEmployee> checker = new UserPermissionChecker<>(securityContextHolder, allowedRoles);

    public UserManagerView(UserEmployeeService userEmployeeService,
                           GroupEmployeeService groupEmployeeService,
                           PhotoStorage photoStorage, List<String> allowedRoles) {
        this.userEmployeeService = userEmployeeService;
        this.groupEmployeeService = groupEmployeeService;
        this.photoStorage = photoStorage;
        this.allowedRoles = allowedRoles;

        //<theme-editor-local-classname>
        layoutColumn2.addClassName("user-manager-view-vertical-layout-1");
        formLayout2Col.addClassName("user-manager-view-form-layout-1");
        textUsername.addClassName("user-manager-view-text-field-1");
        textField.addClassName("user-manager-view-text-field-2");
        emailField.addClassName("user-manager-view-email-field-1");
        datePicker.setOverlayClassName("user-manager-view-date-picker-1");
        datePicker.addClassName("user-manager-view-date-picker");
        passwordField.addClassName("user-manager-view-password-field-1");
        passwordField2.addClassName("user-manager-view-password-field-2");
        stripedGrid.addClassName("user-manager-view-grid-striped");
        comboBoxRole.addClassName("user-manager-view-combo-box-1");
        comboBoxStatus.addClassName("user-manager-view-combo-box-2");
        comboBoxGroup.addClassName("user-manager-view-multi-select-combo-box-1");
        upload.addClassName("user-manager-view-upload-1");
        buttonSave.addClassName("user-manager-view-button-save");
        buttonCancel.addClassName("user-manager-view-button-cancel");

        passwordStrengthText = new Span();

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);

        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("1170px");
        layoutColumn2.getStyle().set("flex", "1");

        h3.setText("Cadastro de usuários");
        h3.setWidth("100%");

        formLayout2Col.setWidth("auto");
        formLayout2Col.getStyle().setBorder("1px solid var(--lumo-contrast-30pct)");
        formLayout2Col.getStyle().setBorderRadius("5px");
        formLayout2Col.getStyle().setPaddingLeft("20px");
        formLayout2Col.getStyle().setPaddingTop("10px");
        formLayout2Col.getStyle().setPaddingBottom("20px");
        formLayout2Col.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("700px", 2),
                new FormLayout.ResponsiveStep("900px", 3)
        );

        textField.setLabel("Nome*");
        textField.setPlaceholder("Digite seu nome completo");
        textField.setWidth("100%");
        textField.setMaxWidth("380px");
        textField.setClearButtonVisible(true);
        textField.setRequired(true);
        textField.setRequiredIndicatorVisible(true);
        textField.setErrorMessage("Por favor, digite seu nome completo");
        textField.setPrefixComponent(VaadinIcon.USER.create());

        datePicker.setLabel("Data de Nascimento");
        datePicker.setWidth("100%");
        datePicker.setMaxWidth("180px");
        datePicker.setClearButtonVisible(true);
        datePicker.setPlaceholder("__/__/____");
        datePicker.setRequired(true);
        datePicker.setRequiredIndicatorVisible(true);
        datePicker.setErrorMessage("Data deve ser preenchida");
        datePicker.setLocale(Locale.forLanguageTag("pt-BR"));
        datePickerInputMask.extend(datePicker);

        emailField.setLabel("Email");
        emailField.setPlaceholder("Digite seu e-mail");
        emailField.setWidth("100%");
        emailField.setMaxWidth("380px");
        emailField.setClearButtonVisible(true);
        emailField.setRequired(true);
        emailField.setPattern("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$");
        emailField.setErrorMessage("Por favor, digite um e-mail válido");
        emailField.setPrefixComponent(VaadinIcon.ENVELOPE_O.create());
        emailField.setValueChangeMode(ValueChangeMode.ON_BLUR);
        setValidEmail(emailField);

        comboBoxRole.setLabel("Perfil");
        comboBoxRole.setWidth("100%");
        comboBoxRole.setMaxWidth("340px");
        comboBoxRole.setClearButtonVisible(true);
        comboBoxRole.setPlaceholder("Selecione um perfil");
        comboBoxRole.setRequired(true);
        comboBoxRole.setErrorMessage("Por favor, selecione um perfil");
        setMultiSelectComboBoxRole(comboBoxRole);

        comboBoxStatus.setLabel("Situação");
        comboBoxStatus.setWidth("100%");
        comboBoxStatus.setMaxWidth("280px");
        comboBoxStatus.setRequired(true);
        comboBoxStatus.setRequiredIndicatorVisible(true);
        comboBoxStatus.setErrorMessage("Por favor, selecione uma situação");
        comboBoxStatus.setPlaceholder("Situação do usuário");
        comboBoxStatus.setClearButtonVisible(true);
        setComboBoxStatusUserEmployee(comboBoxStatus);

        comboBoxGroup.setLabel("Grupo");
        comboBoxGroup.setWidth("100%");
        comboBoxGroup.setMaxWidth("340px");
        comboBoxGroup.setClearButtonVisible(true);
        comboBoxGroup.setRequired(true);
        comboBoxGroup.setRequiredIndicatorVisible(true);
        comboBoxGroup.setErrorMessage("Por favor, selecione um grupo");
        comboBoxGroup.setPlaceholder("Selecione um grupo");
        setMultiSelectComboBoxGroupEmployee(comboBoxGroup);

        checkIcon = VaadinIcon.CHECK.create();
        checkIcon.setVisible(false);
        checkIcon.getStyle().set("color", "var(--lumo-success-color)");

        passwordStrength.add(new Text("4 a 8 caracteres/Nível da senha: "),
                passwordStrengthText);

        passwordField.setLabel("Senha");
        passwordField.setPlaceholder("Digite sua senha");
        passwordField.setWidth("100%");
        passwordField.setMaxWidth("380px");
        passwordField.setRequired(true);
        passwordField.setClearButtonVisible(true);
        passwordField.setPrefixComponent(VaadinIcon.LOCK.create());
        passwordField.setAllowedCharPattern("[0-9]|[a-z]|[A-Z]|[@#$%^&+=]");
        passwordField.setMinLength(4);
        passwordField.setMaxLength(8);
        passwordField.setErrorMessage("Essa senha não é forte o suficiente");
        passwordField.setSuffixComponent(checkIcon);
        passwordField.setHelperComponent(passwordStrength);
        passwordField.setValueChangeMode(ValueChangeMode.EAGER);
        passwordField.addValueChangeListener(e -> {
            String password = e.getValue();
            updateHelper(password);
        });

        passwordField2.setLabel("Confirma Senha");
        passwordField2.setPlaceholder("Confirme sua senha");
        passwordField2.setWidth("100%");
        passwordField2.setMaxWidth("280px");
        passwordField2.setRequired(true);
        passwordField2.setClearButtonVisible(true);
        passwordField2.setPrefixComponent(VaadinIcon.LOCK.create());
        passwordField2.setAllowedCharPattern(passwordField.getAllowedCharPattern());
        passwordField.setMinLength(4);
        passwordField.setMaxLength(8);
        passwordField2.setValueChangeMode(ValueChangeMode.EAGER);
        passwordField2.addValueChangeListener(e -> {
            if (passwordField.getValue().equals(e.getValue())) {
                passwordField2.setSuffixComponent(checkIcon);
                passwordField2.setInvalid(false);
            } else {
                passwordField2.setSuffixComponent(null);
                passwordField2.setInvalid(true);
                passwordField2.setErrorMessage("As senhas não conferem");
            }
        });

        textUsername.setLabel("Nome de Usuário");
        textUsername.setPlaceholder("Digite o nome de usuário");
        textUsername.setWidth("100%");
        textUsername.setMaxWidth("280px");
        textUsername.setClearButtonVisible(true);
        textUsername.setRequired(true);
        textUsername.setRequiredIndicatorVisible(true);
        textUsername.setErrorMessage("Por favor, digite o nome de usuário");
        textUsername.setPrefixComponent(VaadinIcon.USER.create());
        textUsername.setValueChangeMode(ValueChangeMode.ON_BLUR);
        setValidUserName(textUsername);

        upload.setAutoUpload(false);
        upload.setWidth("100%");
        upload.getStyle().setBackgroundColor("var(--lumo-contrast-20pct)");
        upload.setMaxFiles(1);
        upload.setAcceptedFileTypes("image/jpeg", "image/png");
        upload.setMaxFileSize(maxFileSizeInBytes);
        upload.setUploadButton(new Button("Upload Foto"));
        upload.setDropLabel(new Span("Arraste e solte a foto aqui"));
        upload.setDropAllowed(true);
        upload.setDropLabelIcon(VaadinIcon.CLOUD_UPLOAD_O.create());
        upload.getElement().addEventListener("file-remove", event -> {
            Notification notification = Notification.show("Imagem removido da fila", 5000,
                    Notification.Position.TOP_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
            upload.getElement().appendChild(notification.getElement());
        });
        upload.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();
            Notification notification = Notification.show(errorMessage, 5000,
                    Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });
        upload.addSucceededListener(event -> {
            //Implementação de upload de imagem. PENDENTE
        });

        layoutRow.addClassName(Gap.SMALL);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");

        layoutRow2.addClassName(Gap.SMALL);
        layoutRow2.setWidth("100%");
        layoutRow2.getStyle().set("flex-grow", "1");

        if(checker.checkPermission()){
            buttonSave.setEnabled(true);
            buttonSave.setText("Salvar");
            buttonSave.setWidth("min-content");
            buttonSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            buttonSave.addClickListener(event -> {
                this.saveUserEmployee();
                //Implementação de upload de imagem, /save button/. PENDENTE
//                upload.getElement().callJsFunction("uploadFiles");
            });
        } else {
            buttonSave.setEnabled(false);
            buttonSave.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        }

        buttonCancel.setText("Cancelar");
        buttonCancel.setWidth("min-content");
        buttonCancel.addClickListener(event -> this.cancelFields());

        List<UserEmployee> userEmployees = userEmployeeService.findAll();

        stripedGrid.setWidth("100%");
        stripedGrid.getStyle().set("flex-grow", "0");
        stripedGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        stripedGrid.setPageSize(10);
        stripedGrid.getStyle().setBackgroundColor("var(--lumo-contrast-5pct)");
        stripedGrid.getStyle().set("border", "1px solid var(--lumo-contrast-40pct)");
        stripedGrid.getStyle().set("border-radius", "5px");
        stripedGrid.getHeaderRows().stream()
                .flatMap(row -> row.getCells().stream())
                .forEach(cell -> cell.getComponent().getElement().getStyle().set("font-weight", "600"));

        stripedGrid.addColumn(new ComponentRenderer<>(userEmployee -> {
            Div divCode = new Div();
            divCode.setText(String.valueOf(userEmployee.getCode()));
            divCode.getStyle().set("display", "flex");
            divCode.getStyle().set("justify-content", "center");
            divCode.getStyle().set("align-items", "center");
            return divCode;
        })).setHeader(new Html("<div style='text-align:center; color:white'>CÓDIGO</div>"))
                .setAutoWidth(true).setFlexGrow(0)
                .setFooter(new Html("<div style='color:white'>" + String.format("Total Usuários: %d", userEmployees.size()) + "</div>"));

        stripedGrid.addColumn(createEmployeeRenderer())
                .setHeader(new Html("<div style='text-align:center; color:white'>DADOS PESSOAL</div>"))
                .setAutoWidth(true).setFlexGrow(0);

        stripedGrid.addColumn(new ComponentRenderer<>(userEmployee -> {
            Div divCode = new Div();
            divCode.setText(String.valueOf(userEmployee.getUsername()));
            divCode.getStyle().set("display", "flex");
            divCode.getStyle().set("justify-content", "center");
            divCode.getStyle().set("align-items", "center");
            return divCode;
        })).setHeader(new Html("<div style='text-align:center; color:white'>USUÁRIO</div>"))
                .setAutoWidth(true).setFlexGrow(0);

        stripedGrid.addColumn(createComponentGroupEmployee())
                .setHeader(new Html("<div style='text-align:center; color:white'>GRUPO</div>"))
                .setAutoWidth(true).setFlexGrow(0);

        stripedGrid.addColumn(createStatusComponentRenderer())
                .setHeader(new Html("<div style='text-align:center; color:white'>STATUS</div>"))
                .setAutoWidth(true).setFlexGrow(0)
                .setFooter(createStatusFooterText(userEmployees));

        stripedGrid.addColumn(createProfileComponentRenderer())
                .setHeader(new Html("<div style='text-align:center; color:white'>PERFIL</div>"))
                .setAutoWidth(true).setFlexGrow(0);

        stripedGrid.addColumn(
                new ComponentRenderer<>(this::actionManager))
                .setHeader(new Html("<div style='text-align:center; color:white'>AÇÕES</div>"));

        stripedGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        setGridSampleData(stripedGrid);

        textAreaInfoGrid.setLabel("Informações completa");
        textAreaInfoGrid.setReadOnly(true);
        textAreaInfoGrid.setClearButtonVisible(true);
        textAreaInfoGrid.setWidthFull();

        stripedGrid.addCellFocusListener(event -> {
            String row = event.getItem().map(value -> String.valueOf(userEmployees.indexOf(value))).orElse("Not available");
            String code = event.getItem().map(u -> String.valueOf(u.getCode())).orElse("Not available");
            String name = event.getItem().map(u -> u.getName() + " - " + u.getUsername()).orElse("Not available");
            String email = event.getItem().map(u -> String.valueOf(u.getEmail())).orElse("Not available");
            String group = event.getItem().map(u -> String.valueOf(u.getGroupEmployees().get(0).getName())).orElse("Not available");
            String profile = event.getItem().map(u -> u.getRoles().iterator().next().getName()).orElse("Not available");

            String eventSummary = String.format(
                    "Código: %s%nFuncionário: %s%nE-mail: %s%nGrupo: %s%nPerfil: %s%nLinha: %s",
                    code, name, email, group, profile, row);

            textAreaInfoGrid.setValue(eventSummary);
        });

        GridListDataView<UserEmployee> dataView = stripedGrid.setItems(userEmployees);

        searchField.setWidth("100%");
        searchField.setPlaceholder("Procurar usuário");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> dataView.refreshAll());

        dataView.addFilter(user -> {
            String searchTerm = searchField.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            boolean matchesFullName = matchesTerm(user.getName(), searchTerm);
            boolean matchesEmail = matchesTerm(user.getEmail(), searchTerm);
            boolean matchesUsername = matchesTerm(user.getUsername(), searchTerm);

            return matchesFullName || matchesEmail || matchesUsername;
        });

        getContent().add(layoutColumn2);
        layoutColumn2.setPadding(false);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(textField);
        formLayout2Col.add(textUsername);
        formLayout2Col.add(datePicker);
        formLayout2Col.add(emailField);
        formLayout2Col.add(comboBoxStatus);
        formLayout2Col.add(comboBoxGroup);
        formLayout2Col.add(passwordField);
        formLayout2Col.add(passwordField2);
        formLayout2Col.add(comboBoxRole);
        layoutColumn2.add(layoutRow);
        layoutColumn2.add(layoutRow2);
        layoutRow.add(upload);
        layoutRow2.add(buttonSave);
        layoutRow2.add(buttonCancel);
        layoutColumn2.add(searchField, stripedGrid);
        getContent().add(stripedGrid, textAreaInfoGrid);
    }

    public void setValidEmail(EmailField emailField) {
        emailField.addValueChangeListener(event ->  {
            UserEmployee userEmployee = new UserEmployee();
            userEmployee.setEmail(emailField.getValue());
            Optional<UserEmployee> existingUser = userEmployeeService.findByEmail(userEmployee.getEmail());
            if (existingUser.isPresent() && !existingUser.get().equals(userEmployee)) {
                emailField.setInvalid(true);
                emailField.setErrorMessage("Este email já foi cadastrado");
            } else {
                emailField.setInvalid(false);
            }
        });
    }

    public void setValidUserName(TextField textUsername) {
        textUsername.addValueChangeListener(event ->  {
            UserEmployee userEmployee = new UserEmployee();
            userEmployee.setUsername(textUsername.getValue());
            Optional<UserEmployee> existingUser = userEmployeeService.findByUsernameIgnoreCase(userEmployee.getUsername());
            if (existingUser.isPresent() && !existingUser.get().equals(userEmployee)) {
                textUsername.setInvalid(true);
                textUsername.setErrorMessage("Nome de usuário já esta cadastrado");
            } else {
                textUsername.setInvalid(false);
            }
        });
    }

    public void setComboBoxStatusUserEmployee(ComboBox comboBox) {
        List<StatusUserEmployee> statusUserEmployees = new ArrayList<>();
        statusUserEmployees.add(StatusUserEmployee.ACTIVATE);
        statusUserEmployees.add(StatusUserEmployee.DISABLE);
        comboBox.setItems(statusUserEmployees);
        comboBox.setItemLabelGenerator(g -> ((StatusUserEmployee) g).getName());
    }

    public void setMultiSelectComboBoxGroupEmployee(ComboBox comboBox) {
            List<GroupEmployee> groups = groupEmployeeService.findAll();
        comboBox.setItems(groups);
        comboBox.setItemLabelGenerator(g -> ((GroupEmployee) g).getName());
    }

    public void setMultiSelectComboBoxRole(ComboBox comboBox) {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        roles.add(Role.USER);
        comboBox.setItems(roles);
        comboBox.setItemLabelGenerator(g -> ((Role) g).getName());
    }

    private void setGridSampleData(Grid grid) {
        grid.setItems(query -> userEmployeeService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream()
                .map(u -> new UserEmployee(
                        u.getCode(),
                        u.getProfilePicture(),
                        u.getName(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getGroupEmployees(),
                        u.getActive()
                )));
    }

    private static final SerializableBiConsumer<Span, UserEmployee> groupEmployeeComponentUpdater =
            (span, userEmployee) -> {
                List<GroupEmployee> groupEmployees = userEmployee.getGroupEmployees();
                if (groupEmployees == null || groupEmployees.isEmpty()) {
                    span.setText("");
                }
                List<String> groupNames = new ArrayList<>();
                assert groupEmployees != null;
                for (GroupEmployee groupEmployee : groupEmployees) {
                    String theme =
                    String.format("badge contrast %s",  groupNames.add(groupEmployee.getName()));
                    span.getElement().setAttribute("theme", theme);
                    span.setText(groupEmployee.getName());

                }
                span.setText(String.join(", ", groupNames));
            };

    private static Renderer<UserEmployee> createEmployeeRenderer() {
        return LitRenderer.<UserEmployee> of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                + "<vaadin-avatar img=\"${item.pictureUrl}\" name=\"${item.fullName}\" alt=\"User avatar\"></vaadin-avatar>"
                                + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                                + "    <span> ${item.fullName} </span>"
                                + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                                + "      ${item.email}" + "    </span>"
                                + "  </vaadin-vertical-layout>"
                                + "</vaadin-horizontal-layout>")
                .withProperty("pictureUrl", UserEmployee::getProfilePicture)
                .withProperty("fullName", UserEmployee::getName)
                .withProperty("email", UserEmployee::getEmail);
    }

    private static final SerializableBiConsumer<Span, UserEmployee> statusComponentUpdater =
        (span, userEmployee) -> {
            StatusUserEmployee status =
                Boolean.TRUE.equals(userEmployee.getActive())
                    ? StatusUserEmployee.ACTIVATE
                    : StatusUserEmployee.DISABLE;
            String theme =
                String.format("badge %s", status == StatusUserEmployee.ACTIVATE ? "success" : "error");
            span.getElement().setAttribute("theme", theme);
            span.setText(status.getName()
        );
    };

    private static final SerializableBiConsumer<Span, UserEmployee> profileComponentUpdater =
            (span, userEmployee) -> {
                Set<Role> profile = userEmployeeService.get(userEmployee.getCode()).orElseThrow().getRoles();
                if (profile == null || profile.isEmpty()) {
                    span.setText("");
                }
                List<String> roleNames = new ArrayList<>();
                assert profile != null;
                for (Role role : profile) {
                    String theme =
                            String.format("badge %s",  roleNames.add(role.getName()));
                    span.getElement().setAttribute("theme", theme);
                    span.setText(role.getName());

                }
                span.setText(String.join(", ", roleNames));
            };


    private static ComponentRenderer<Span, UserEmployee> createComponentGroupEmployee() {
        return new ComponentRenderer<>(Span::new, groupEmployeeComponentUpdater);
    }

    private static ComponentRenderer<Span, UserEmployee> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }

    private static ComponentRenderer<Span, UserEmployee> createProfileComponentRenderer() {
        return new ComponentRenderer<>(Span::new, profileComponentUpdater);
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    public void editUserEmployee(UserEmployee userEmployeeEdit) {
        cleanFields();
        try {
            cleanDialogFields();
            dialog.setHeaderTitle(
                    String.format("Atualizar usuário \"%s\"?", userEmployeeEdit.getName()));
            dialog.add("Tem certeza de que deseja editar este usuário?");
            dialog.open();
            Button confirmButton = new Button("Confirmar", (e1) -> dialog.close());
            confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            confirmButton.getStyle().set("margin-right", "auto");
            confirmButton.addClickListener(event -> {
                textField.setValue(userEmployeeEdit.getName());
                textUsername.setValue(userEmployeeEdit.getUsername());
                textUsername.setInvalid(false);
                textUsername.setReadOnly(true);
                datePicker.setValue(userEmployeeEdit.getBirthDate());
                emailField.setValue(userEmployeeEdit.getEmail());
                emailField.setRequired(false);
                emailField.setInvalid(false);
                StatusUserEmployee statusUserEmployee = userEmployeeEdit.isActive() ? StatusUserEmployee.ACTIVATE : StatusUserEmployee.DISABLE;
                comboBoxStatus.setValue(statusUserEmployee);
                GroupEmployee groupEmployee = userEmployeeEdit.getGroupEmployees().get(0);
                comboBoxGroup.setValue(groupEmployee);
                Role role = userEmployeeEdit.getRoles().stream().findFirst().orElseThrow();
                comboBoxRole.setValue(role);
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

    public void deleteUserEmployee(UserEmployee user) {
        cleanDialogFields();
        dialog.setHeaderTitle(
                String.format("Delete user \"%s\"?", user.getName()));
        dialog.add("Tem certeza de que deseja excluir este usuário permanentemente?");

        Button deleteButton = new Button("Deletar", (e) -> dialog.close());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        deleteButton.getStyle().set("margin-right", "auto");
        deleteButton.addClickListener(event -> {
            userEmployeeService.delete(user.getCode());
            stripedGrid.getDataProvider().refreshAll();
            dialog.close();
        });
        dialog.getFooter().add(deleteButton);

        Button cancelButton = new Button("Cancel", (e) -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getFooter().add(cancelButton);

        dialog.open();
    }

    private static Html createStatusFooterText(List<UserEmployee> userEmployees) {
        long activeCount = userEmployees.stream().filter(UserEmployee::isActive)
                .count();
        return new Html("<div style='color:white'>" + String.format("%s Ativos", activeCount) + "</div>");
    }

    private void updateHelper(String password) {
        boolean hasUpperCase = !password.equals(password.toLowerCase());
        boolean hasSpecialChar = password.matches(".*[!@#$%&*()_+=|<>?{}\\[\\]~-].*");

        if (password.length() > 7 && hasUpperCase && hasSpecialChar) {
            passwordStrengthText.setText("Forte");
            passwordStrengthText.getStyle().set("color", "var(--lumo-success-color)");
            checkIcon.setVisible(true);
        } else if (password.length() > 3) {
            passwordStrengthText.setText("Media");
            passwordStrengthText.getStyle().set("color", "#e7c200");
            checkIcon.setVisible(false);
        } else {
            passwordStrengthText.setText("Fraco");
            passwordStrengthText.getStyle().set("color", "var(--lumo-error-color)");
            checkIcon.setVisible(false);
        }
    }

    private void saveUserEmployee(){
        UserEmployee userEmployee = userEmployeeService.findByUsernameIgnoreCase(textUsername.getValue()).orElse(new UserEmployee());
        if(userEmployee.isNew()){
            try {
                userEmployee.setName(textField.getValue());
                userEmployee.setUsername(textUsername.getValue());
                userEmployee.setBirthDate(datePicker.getValue());
                userEmployee.setEmail(emailField.getValue());
                StatusUserEmployee statusUserEmployee = (StatusUserEmployee) comboBoxStatus.getValue();
                boolean active = statusUserEmployee == StatusUserEmployee.ACTIVATE;
                userEmployee.setActive(active);
                GroupEmployee selectedGroup = (GroupEmployee) comboBoxGroup.getValue();
                userEmployee.setGroupEmployees(new ArrayList<>(Arrays.asList(selectedGroup)));
                userEmployee.setRoles(new HashSet<>(Collections.singletonList((Role) comboBoxRole.getValue())));
                userEmployee.setPassword(passwordField.getValue());
                userEmployee.setConfirmPassword(passwordField2.getValue());
                userEmployeeService.insert(userEmployee);
                Notification notification = Notification.show("Usuário cadastrado com sucesso!", 6000,
                        Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                cleanFields();
                stripedGrid.getDataProvider().refreshAll();
            } catch (Exception e) {
                Notification notification = Notification.show("Erro ao cadastrar usuário: " + e.getMessage(), 6000,
                        Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        } else {
            try {
                userEmployee.setName(textField.getValue());
                userEmployee.setBirthDate(datePicker.getValue());
                String currentEmail = emailField.getValue();
                String existingEmail = userEmployeeService.get(userEmployee.getCode()).orElseThrow().getEmail();
                if(!currentEmail.isEmpty()){
                    if(!currentEmail.equals(existingEmail)){
                        userEmployee.setEmail(currentEmail);
                    }
                }
                StatusUserEmployee statusUserEmployee = (StatusUserEmployee) comboBoxStatus.getValue();
                boolean active = statusUserEmployee == StatusUserEmployee.ACTIVATE;
                userEmployee.setActive(active);
                GroupEmployee selectedGroup = (GroupEmployee) comboBoxGroup.getValue();
                userEmployee.setGroupEmployees(new ArrayList<>(Arrays.asList(selectedGroup)));
                userEmployee.setRoles(new HashSet<>(Collections.singletonList((Role) comboBoxRole.getValue())));
                if(!passwordField.getValue().isEmpty()){
                    userEmployee.setPassword(passwordField.getValue());
                    userEmployee.setConfirmPassword(passwordField2.getValue());
                } else {
                    userEmployee.setPassword(userEmployee.getPassword());
                    userEmployee.setConfirmPassword(userEmployee.getPassword());
                }
                userEmployee.setPassword(userEmployee.getPassword().isEmpty() ? passwordField.getValue() : userEmployee.getPassword());
                userEmployee.setConfirmPassword(userEmployee.getPassword().isEmpty() ? passwordField2.getValue() : userEmployee.getPassword());
                userEmployeeService.update(userEmployee);
                Notification notification = Notification.show("Usuário atualizado com sucesso!", 6000,
                        Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                cleanFields();
                stripedGrid.getDataProvider().refreshAll();
            } catch (Exception e) {
                Notification notification = Notification.show("Erro ao atualizar usuário: " + e.getMessage(), 6000,
                        Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
    }

    private HorizontalLayout actionManager(UserEmployee userEmployee) {
        HorizontalLayout layoutManager = new HorizontalLayout();
        userEmployee = userEmployeeService.get(userEmployee.getCode()).orElseThrow();

        if (checker.checkPermission()) {
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                    ButtonVariant.LUMO_TERTIARY_INLINE,
                    ButtonVariant.LUMO_TERTIARY);
            UserEmployee userEmployeeEdit = userEmployee;
            editButton.addClickListener(e -> this.editUserEmployee(userEmployeeEdit));

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                    ButtonVariant.LUMO_ERROR,
                    ButtonVariant.LUMO_TERTIARY);
            UserEmployee userEmployeeDelete = userEmployee;
            deleteButton.addClickListener(e -> this.deleteUserEmployee(userEmployeeDelete));

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

    private void cancelFields() {
        cleanDialogFields();
        dialog.removeAll();
        dialog.setHeaderTitle("Cadastro de usuário");
        dialog.add("Tem certeza de que deseja cancelar o cadastro deste usuário?");
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
        textUsername.clear();
        textUsername.setInvalid(false);
        textUsername.setEnabled(true);
        textUsername.setReadOnly(false);
        textAreaInfoGrid.clear();
        textAreaInfoGrid.setInvalid(false);
        datePicker.clear();
        datePicker.setInvalid(false);
        emailField.clear();
        emailField.setInvalid(false);
        comboBoxStatus.clear();
        comboBoxStatus.setInvalid(false);
        comboBoxGroup.clear();
        comboBoxGroup.setInvalid(false);
        passwordField.clear();
        passwordField.setInvalid(false);
        passwordField.setEnabled(true);
        passwordField2.clear();
        passwordField2.setInvalid(false);
        passwordField2.setEnabled(true);
        comboBoxRole.clear();
        comboBoxRole.setInvalid(false);
    }

    private void cleanDialogFields() {
        dialog.removeAll();
        dialog.getHeader().removeAll();
        dialog.getFooter().removeAll();
    }
}
