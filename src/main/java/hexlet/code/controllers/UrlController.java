package hexlet.code.controllers;

import hexlet.code.models.Url;
import hexlet.code.models.UrlCheck;
import hexlet.code.models.query.QUrl;
import hexlet.code.models.query.QUrlCheck;
import hexlet.code.services.UrlService;
import io.ebean.PagedList;
import io.javalin.core.validation.JavalinValidation;
import io.javalin.core.validation.ValidationError;
import io.javalin.core.validation.Validator;
import io.javalin.http.Handler;
import java.util.List;
import java.util.Map;

public final class UrlController {

    public static Handler createUrl = ctx -> {
        String line = ctx.formParam("url");

        Validator<String> validator = ctx.formParamAsClass("url", String.class)
                .check(l -> !l.isEmpty(), "Поле не должно быть пустым!");

        Map<String, List<ValidationError<? extends Object>>> errors
                = JavalinValidation.collectErrors(validator);

        if (!errors.isEmpty()) {
            ctx.status(422);
            ctx.attribute("errors", errors);
            ctx.attribute("url", new Url(line));
            ctx.render("home.html");
            return;
        }

        UrlService urlService = new UrlService(line);

        if (!urlService.isValid()) {
            ctx.status(422);
            ctx.sessionAttribute("flashError", "Пожалуйста, введите корректный URL!");
            ctx.attribute("errors", Map.of());
            ctx.attribute("url", new Url(line));
            ctx.render("home.html");
            return;
        }

        List<Url> urls = new QUrl().findList();
        Url url = new Url(urlService.getUrlName());

        if (UrlService.containedIn(urls, url)) {
            ctx.sessionAttribute("flashInfo", "URL уже добавлен!");
            ctx.redirect("/urls");
            return;
        }

        url.save();
        ctx.sessionAttribute("flashSuccess", "Пользователь успешно создан!");
        ctx.redirect("/urls");
        return;
    };

    public static Handler list = ctx -> {
        int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);
        int rowsPerPage = 5;
        int offset = (page - 1) * rowsPerPage;

        if (page < 1) {
            ctx.status(404);
            ctx.render("404.html");
            return;
        }

        PagedList<Url> pagedUrls = new QUrl()
                .setFirstRow(offset)
                .setMaxRows(rowsPerPage)
                .orderBy().id.desc()
                .findPagedList();

        List<Url> urls = pagedUrls.getList();

        Map<Long, UrlCheck> checks = new QUrlCheck()
                .url.id.asMapKey()
                .orderBy().createdAt.desc()
                .findMap();

        ctx.attribute("urls", urls);
        ctx.attribute("page", page);
        ctx.attribute("checks", checks);
        ctx.render("index.html");
    };
}
