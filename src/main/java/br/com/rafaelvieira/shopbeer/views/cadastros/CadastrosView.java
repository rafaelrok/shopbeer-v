package br.com.rafaelvieira.shopbeer.views.cadastros;

import br.com.rafaelvieira.shopbeer.repository.StateRepository;
import br.com.rafaelvieira.shopbeer.services.CityService;
import br.com.rafaelvieira.shopbeer.services.StyleService;
import br.com.rafaelvieira.shopbeer.views.MainLayout;
import br.com.rafaelvieira.shopbeer.views.cadastros.cityregister.CityRegisterView;
import br.com.rafaelvieira.shopbeer.views.cadastros.addressRegister.AddressRegisterView;
import br.com.rafaelvieira.shopbeer.views.cadastros.styleRegister.StyleRegisterView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.vaadin.lineawesome.LineAwesomeIcon;

@PageTitle("Registers")
@Route(value = "registers", layout = MainLayout.class)
@AnonymousAllowed
@Uses(Icon.class)
public class CadastrosView extends Composite<VerticalLayout> {

    private final CityService cityService;
    private final StateRepository stateRepository;

    private final StyleService styleService;


    public CadastrosView(CityService cityService, StateRepository stateRepository, StyleService styleService) {
        this.cityService = cityService;
        this.stateRepository = stateRepository;
        this.styleService = styleService;

        TabSheet tabs = new TabSheet ();
        H3 h3 = new H3();

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        tabs.setWidth("100%");
        setTabsSampleData(tabs);
        getContent().add(tabs);
    }

    public void setTabsSampleData(TabSheet tabs) {
        CityRegisterView cityRegisterView = new CityRegisterView(cityService, stateRepository);
        StyleRegisterView stateRegisterView = new StyleRegisterView(styleService);
        AddressRegisterView addressRegisterView = new AddressRegisterView();

        tabs.addThemeVariants(TabSheetVariant.LUMO_TABS_CENTERED, TabSheetVariant.LUMO_BORDERED);
        tabs.add(new Tab(LineAwesomeIcon.CITY_SOLID.create(), new Span("Cidades")), cityRegisterView);
        tabs.add(new Tab(LineAwesomeIcon.BULLSEYE_SOLID.create(), new Span("Estilos")), stateRegisterView);
        tabs.add(new Tab(LineAwesomeIcon.ROAD_SOLID.create(), new Span("Endereços")), addressRegisterView);
    }
}
