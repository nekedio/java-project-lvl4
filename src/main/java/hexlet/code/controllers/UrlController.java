package hexlet.code.controllers;

import hexlet.code.models.Url;
import hexlet.code.models.query.QUrl;
import io.javalin.core.validation.JavalinValidation;
import io.javalin.core.validation.ValidationError;
import io.javalin.core.validation.Validator;
import io.javalin.http.Handler;
import java.util.List;
import java.util.Map;

public final class UrlController {

    public static Handler createUrl = ctx -> {
        String line = ctx.formParam("url");

        Validator<String> urlValidator = ctx.formParamAsClass("url", String.class)
                .check(l -> !l.isEmpty(), "Пожалуйста, введите корректный адрес сайта!");

        Map<String, List<ValidationError<? extends Object>>> errors
                = JavalinValidation.collectErrors(urlValidator);

        if (!errors.isEmpty()) {
            ctx.status(422);
            ctx.attribute("errors", errors);
            ctx.attribute("url", new Url(line));
            ctx.render("home.html");
            return;
        }

        Url url = new Url(line);
        url.save();
        ctx.sessionAttribute("flash", "Пользователь успешно создан!");
        ctx.redirect("/urls");
    };

    public static Handler list = ctx -> {
        List<Url> urls = new QUrl().findList();
        ctx.attribute("urls", urls);
        ctx.render("index.html");
    };

}
