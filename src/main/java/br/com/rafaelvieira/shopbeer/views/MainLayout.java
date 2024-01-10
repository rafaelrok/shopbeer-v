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
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
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
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        VerticalLayout layout = new VerticalLayout();

        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);

        Image img = new Image("images/favicon.png", "Shop Beer logo");
        img.addClassNames(LumoUtility.Margin.Left.XLARGE);

        H1 appName = new H1();
        appName.add(new Text("SHOP BEER"));
        appName.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.FlexDirection.COLUMN);
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(img,appName);

        Scroller scroller = new Scroller(createNavigation());
        //<theme-editor-local-classname>
        scroller.addClassName("main-layout-scroller-1");

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        if (accessChecker.hasAccess(DashboardView.class)) {
            nav.addItem(new SideNavItem("Dashboard", DashboardView.class, LineAwesomeIcon.CHART_AREA_SOLID.create()));

        }
        if (accessChecker.hasAccess(VendaView.class)) {
            nav.addItem(new SideNavItem("Venda", VendaView.class, LineAwesomeIcon.CREDIT_CARD.create()));

        }
        if (accessChecker.hasAccess(SalesSearchView.class)) {
            nav.addItem(new SideNavItem("Filtrar Vendas", SalesSearchView.class, LineAwesomeIcon.FILTER_SOLID.create()));

        }
        if (accessChecker.hasAccess(BeersSearchView.class)) {
            nav.addItem(new SideNavItem("Filtrar Produto", BeersSearchView.class, LineAwesomeIcon.FILTER_SOLID.create()));

        }
        if (accessChecker.hasAccess(CostumerRegisterView.class)) {
            nav.addItem(new SideNavItem("Registrar Cliente", CostumerRegisterView.class,
                    LineAwesomeIcon.USER_FRIENDS_SOLID.create()));

        }
        if (accessChecker.hasAccess(BeersRegisterView.class)) {
            nav.addItem(
                    new SideNavItem("Registrar Produto", BeersRegisterView.class, LineAwesomeIcon.PRODUCT_HUNT.create()));

        }
        if (accessChecker.hasAccess(UserManagerView.class)) {
            nav.addItem(
                    new SideNavItem("Gerenciar Usuários", UserManagerView.class, LineAwesomeIcon.USERS_COG_SOLID.create()));

        }
        if (accessChecker.hasAccess(ReportsView.class)) {
            nav.addItem(new SideNavItem("Relatórios", ReportsView.class, LineAwesomeIcon.WPFORMS.create()));

        }
        if (accessChecker.hasAccess(CadastrosView.class)) {
            nav.addItem(new SideNavItem("Cadastros", CadastrosView.class, LineAwesomeIcon.REGISTERED.create()));

        }
        if (accessChecker.hasAccess(AboutView.class)) {
            nav.addItem(new SideNavItem("Sobre", AboutView.class, LineAwesomeIcon.FILE.create()));

        }

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

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
            userName.getSubMenu().addItem("Sign out", e -> authenticatedUser.logout());

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

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
