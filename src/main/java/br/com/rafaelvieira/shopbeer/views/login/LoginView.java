package br.com.rafaelvieira.shopbeer.views.login;

import br.com.rafaelvieira.shopbeer.security.AuthenticatedUser;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;

    public LoginView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

        LoginI18n.ErrorMessage errorMessage = new LoginI18n.ErrorMessage();
        LoginOverlay loginOverlay = new LoginOverlay();
        loginOverlay.setError(true);

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Shop Beer");
        i18n.getHeader().setDescription("Efetue o login para acessar o sistema");
        i18n.setAdditionalInformation("Entre em contato com admin@shopbeer.com.br se estiver tendo problemas para fazer login em sua conta");
//        i18n.setErrorMessage(errorMessage);
        setI18n(i18n);

        addLoginListener(e -> authenticatedUser.get().map(user -> {
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("user", user);
            return user;
        }).ifPresent(user -> {
            if(user.getActive().equals(false)) {
                errorMessage.setTitle("Permissão negada");
                errorMessage.setMessage("Usuário não esta ativo, entre em contato com administrator");
                i18n.setErrorMessage(errorMessage);
            }
        }));

        setForgotPasswordButtonVisible(true);
        setOpened(true);
        //<theme-editor-local-classname>
        addClassName("login-view-login-overlay-1");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            // Already logged in
            setOpened(false);
            event.forwardTo("");
        }

        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }
}
