package br.com.rafaelvieira.shopbeer.mail;

import br.com.rafaelvieira.shopbeer.domain.Beer;
import br.com.rafaelvieira.shopbeer.domain.ItemSale;
import br.com.rafaelvieira.shopbeer.domain.Sale;
import br.com.rafaelvieira.shopbeer.storage.PhotoStorage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class Mailer {

    private static final Logger logger = LoggerFactory.getLogger(Mailer.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine thymeleaf;
    private final PhotoStorage photoStorage;

    public Mailer(JavaMailSender mailSender, TemplateEngine thymeleaf, PhotoStorage photoStorage) {
        this.mailSender = mailSender;
        this.thymeleaf = thymeleaf;
        this.photoStorage = photoStorage;
    }

    @Async
    public void send(Sale sale) {
        Context context = new Context(new Locale("pt", "BR"));

        context.setVariable("sale", sale);
        context.setVariable("logo", "logo");

        Map<String, String> photos = new HashMap<>();
        boolean addMockBeer = false;
        for (ItemSale item : sale.getItens()) {
            Beer beer = item.getBeer();
            if (beer.withPhoto()) {
                String cid = "photo-" + beer.getCode();
                context.setVariable(cid, cid);

                photos.put(cid, beer.getPhoto() + "|" + beer.getContentType());
            } else {
                addMockBeer = true;
                context.setVariable("mockBeer", "mockBeer");
            }
        }

        try {
            String email = thymeleaf.process("mail/SummarySale", context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("teste@rafaelvieiradev.com");
            helper.setTo(sale.getCostumer().getEmail());
            helper.setSubject(String.format("ShopBeer - Venda nº %d", sale.getCode()));
            helper.setText(email, true);

            helper.addInline("logo",     new ClassPathResource("static/images/logo-gray.png"));

            if (addMockBeer) {
                helper.addInline("mockBeer", new ClassPathResource("static/images/beer-mock.png"));
            }

            for (String cid : photos.keySet()) {
                String[] photoContentType = photos.get(cid).split("\\|");
                String photo = photoContentType[0];
                String contentType = photoContentType[1];
                byte[] arrayPhoto = photoStorage.recoverThumbnail(photo);
                helper.addInline(cid, new ByteArrayResource(arrayPhoto), contentType);
            }

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("Error sending email", e);
        }
    }

}
