package br.com.rafaelvieira.shopbeer.views.about;

import br.com.rafaelvieira.shopbeer.views.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

@PageTitle("Sobre")
@Route(value = "about", layout = MainLayout.class)
@AnonymousAllowed
public class AboutView extends VerticalLayout {

    public AboutView() {
        setSpacing(false);

        Image img = new Image("/images/logo.png", "logo");
        img.setWidth("50%");
        add(img);

        H2 header = new H2("Conheça o Shop Beer");
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
        add(header);
        add(new Paragraph("Sistema de gerenciamento de estoque de cervejas. " +
                "Um distribuidor de cervejas precisa de um sistema para gerenciar seu estoque. " +
                "O sistema deve permitir o cadastro de cervejas, com nome, descrição, SKU, preço, teor alcoólico, comissão e quantidade em estoque. " +
                "O sistema deve permitir também o cadastro de clientes, com nome, CPF, sexo e data de nascimento. " +
                "O sistema deve permitir o cadastro de funcionários, com nome, CPF, e-mail, data de nascimento, data de contratação, salário e perfil de acesso (administrador ou funcionário). " +
                "O sistema deve permitir o cadastro de grupos de permissões, com nome e descrição. O sistema deve permitir o cadastro de permissões, com nome e descrição. " +
                "O sistema deve permitir o cadastro de grupos de usuários, com nome e descrição. " +
                "O sistema deve permitir o cadastro de usuários, com nome, e-mail, senha, confirmação de senha, grupo de usuários e status (ativo ou inativo)."
        ));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
