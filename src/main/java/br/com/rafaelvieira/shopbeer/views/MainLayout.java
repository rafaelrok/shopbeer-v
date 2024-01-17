package br.com.rafaelvieira.shopbeer.views;


import br.com.rafaelvieira.shopbeer.domain.UserEmployee;
import br.com.rafaelvieira.shopbeer.security.AuthenticatedUser;
import br.com.rafaelvieira.shopbeer.views.about.AboutView;
import br.com.rafaelvieira.shopbeer.views.beersregister.BeersRegisterView;
import br.com.rafaelvieira.shopbeer.views.beerssearch.BeersSearchView;
import br.com.rafaelvieira.shopbeer.views.cadastros.CadastrosView;
import br.com.rafaelvieira.shopbeer.views.costumerregister.CostumerRegisterView;
import br.com.rafaelvieira.shopbeer.views.dashboard.DashboardView;
import br.com.rafaelvieira.shopbeer.views.reports.ReportsView;
import br.com.rafaelvieira.shopbeer.views.salessearch.SalesSearchView;
import br.com.rafaelvieira.shopbeer.views.usermanager.UserManagerView;
import br.com.rafaelvieira.shopbeer.views.venda.VendaView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.io.ByteArrayInputStream;
import java.util.Optional;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {
    
    private H2 viewTitle;
    private final AuthenticatedUser authenticatedUser;
    private final AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
        addToNavbar(true, createHeaderContent());

        Button toggleButton = new Button(VaadinIcon.ADJUST.create(), click -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();

            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
            } else {
                themeList.add(Lumo.DARK);
            }
        });
        toggleButton.getStyle().set("margin-right", "10px");
        toggleButton.setTooltipText("Alterar tema");

        addToNavbar(toggleButton);
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");
        toggle.getStyle().setBorder("1px solid var(--lumo-contrast-30pct)");
        toggle.getStyle().setBorderRadius("10%");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(toggle, viewTitle);
    }

    private void addDrawerContent() {
        VerticalLayout layout = new VerticalLayout();

        layout.getElement().getThemeList().add("dark");
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setSizeUndefined();
        layout.setWidthFull();
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);

        Image img = new Image("images/favicon.png", "Shop Beer logo");
        img.addClassNames(LumoUtility.Margin.Left.XLARGE);

        H1 appName = new H1();
        appName.add(new Text("SHOP BEER"));
        appName.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.FlexDirection.COLUMN);
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(img,appName);
        header.getElement().getThemeList().add("dark");

        SideNav nav = createNavigation();
        SideNav navFilters =createNavigationFilters();
        SideNav navRegister =createNavigationRegister();
        SideNav navReports =createNavigationReports();
        SideNav navAdmin =createNavigationAdmin();
        SideNav navInfo =createNavigationInfo();
        //<theme-editor-local-classname>
        // Adicionando todos os SideNavs a uma VerticalLayout
        VerticalLayout navLayout = new VerticalLayout(nav, navFilters, navRegister, navReports, navAdmin, navInfo);
        navLayout.setSpacing(false);
        navLayout.getElement().getThemeList().add("dark");

        // Adicionando a VerticalLayout a um Scroller
        Scroller scroller = new Scroller(navLayout);
        scroller.getElement().getThemeList().add("dark");
        scroller.addClassName("main-layout-scroller-1");

        addToDrawer(header, scroller, createFooter()
        );
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        if (accessChecker.hasAccess(DashboardView.class)) {
            nav.addItem(new SideNavItem("Dashboard", DashboardView.class, LineAwesomeIcon.CHART_AREA_SOLID.create()));
        }
        if (accessChecker.hasAccess(VendaView.class)) {
            nav.addItem(new SideNavItem("Venda", VendaView.class, LineAwesomeIcon.CREDIT_CARD.create()));
        }

        return nav;
    }

    private SideNav createNavigationFilters() {
        SideNav navFilters = new SideNav();
        navFilters.getElement().getThemeList().add("navFilters-custom-font");
        navFilters.setLabel("Filtros");
        navFilters.setWidthFull();
        navFilters.setCollapsible(true);

        if (accessChecker.hasAccess(SalesSearchView.class)) {
            navFilters.addItem(new SideNavItem("Filtrar Vendas", SalesSearchView.class, LineAwesomeIcon.FILTER_SOLID.create()));
        }
        if (accessChecker.hasAccess(BeersSearchView.class)) {
            navFilters.addItem(new SideNavItem("Filtrar Produtos", BeersSearchView.class, LineAwesomeIcon.FILTER_SOLID.create()));
        }
        
        return navFilters;
    }

    private SideNav createNavigationRegister() {
        SideNav navRegister = new SideNav();
        navRegister.setLabel("Cadastros");
        navRegister.setWidthFull();
        navRegister.setCollapsible(true);

        if (accessChecker.hasAccess(CostumerRegisterView.class)) {
            navRegister.addItem(new SideNavItem("Clientes", CostumerRegisterView.class,
                    LineAwesomeIcon.USER_FRIENDS_SOLID.create()));
        }
        if (accessChecker.hasAccess(BeersRegisterView.class)) {
            navRegister.addItem(
                    new SideNavItem("Produtos", BeersRegisterView.class, LineAwesomeIcon.PRODUCT_HUNT.create()));
        }
        if (accessChecker.hasAccess(CadastrosView.class)) {
            navRegister.addItem(new SideNavItem("Geral", CadastrosView.class, LineAwesomeIcon.PLUS_SQUARE_SOLID.create()));
        }
        
        return navRegister;
    }

    private SideNav createNavigationReports() {
        SideNav navReports = new SideNav();
        navReports.setLabel("Relatórios");
        navReports.setWidthFull();
        navReports.setCollapsible(true);

        if (accessChecker.hasAccess(ReportsView.class)) {
            navReports.addItem(new SideNavItem("Vendas no mês", ReportsView.class, LineAwesomeIcon.WPFORMS.create()));
        }
        
        return navReports;
    }
    
    private SideNav createNavigationAdmin() {
        SideNav navAdmin = new SideNav();
        navAdmin.setLabel("Administração");
        navAdmin.setWidthFull();
        navAdmin.setCollapsible(true);

        if (accessChecker.hasAccess(UserManagerView.class)) {
            navAdmin.addItem(
                    new SideNavItem("Usuários", UserManagerView.class, LineAwesomeIcon.USER.create()));
        }
        if (accessChecker.hasAccess(UserManagerView.class)) {
            navAdmin.addItem(
                    new SideNavItem("Permissões", UserManagerView.class, LineAwesomeIcon.KEY_SOLID.create()));
        }

        return navAdmin;
    }

    private SideNav createNavigationInfo() {
        SideNav navInfo = new SideNav();

        if (accessChecker.hasAccess(UserManagerView.class)) {
            navInfo.addItem(
                    new SideNavItem("Sobre", AboutView.class, LineAwesomeIcon.INFO_SOLID.create()));
        }
        if (accessChecker.hasAccess(UserManagerView.class)) {
            navInfo.addItem(
                    new SideNavItem("Ajuda", "https://github.com/rafaelrok/shopbeer-v", LineAwesomeIcon.QUESTION_SOLID.create()));
        }

        return navInfo;
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();

        layout.setId("header");
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.addClassName(LumoUtility.Padding.Vertical.SMALL);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        layout.addClassName(LumoUtility.Margin.Horizontal.MEDIUM);

        viewTitle = new H2();
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setWidthFull();
        titleLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        titleLayout.add(viewTitle);

        layout.add(titleLayout);

        Optional<UserEmployee> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            UserEmployee userEmployee = maybeUser.get();

            Avatar avatar = new Avatar(userEmployee.getName());
            StreamResource resource = new StreamResource("profile-pic",
                    () -> new ByteArrayInputStream(userEmployee.getProfilePicture().getBytes()));
            avatar.setImageResource(resource);
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(userEmployee.getName());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);

            HorizontalLayout profileLayout = new HorizontalLayout(LineAwesomeIcon.USER_EDIT_SOLID.create(), new Text("Perfil"));
            userName.getSubMenu().addItem(profileLayout, e -> UI.getCurrent().navigate("profile"));

            HorizontalLayout settingsLayout = new HorizontalLayout(LineAwesomeIcon.COGS_SOLID.create(), new Text("Configurações"));
            userName.getSubMenu().addItem(settingsLayout, e -> UI.getCurrent().navigate("settings"));

            HorizontalLayout logoutLayout = new HorizontalLayout(LineAwesomeIcon.SIGN_OUT_ALT_SOLID.create(), new Text("Sair"));
            userName.getSubMenu().addItem(logoutLayout, e -> authenticatedUser.logout());

            HorizontalLayout helpLayout = new HorizontalLayout(LineAwesomeIcon.QUESTION_CIRCLE.create(), new Text("Ajuda"));
            userName.getSubMenu().addItem(helpLayout, e -> UI.getCurrent().navigate("Ajuda"));

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        layout.setId("footer");
        layout.setWidthFull();
        layout.setTitle("Shop Beer - 2024");
        layout.addClassName(LumoUtility.JustifyContent.CENTER);
        layout.addClassName(LumoUtility.AlignItems.CENTER);

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
