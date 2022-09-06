package hexlet.code.controllers;

import hexlet.code.models.Url;
import hexlet.code.models.UrlCheck;
import hexlet.code.models.query.QUrl;
import hexlet.code.models.query.QUrlCheck;
import io.ebean.PagedList;
import io.javalin.core.validation.JavalinValidation;
import io.javalin.core.validation.ValidationError;
import io.javalin.core.validation.Validator;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
import java.net.URL;
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

        URL parsedUrl = null;
        try {
            parsedUrl = new URL(line);
        } catch (Exception e) {
            ctx.status(422);
            ctx.sessionAttribute("flashError", "Пожалуйста, введите корректный URL!");
            ctx.attribute("errors", Map.of());
            ctx.attribute("url", new Url(line));
            ctx.render("home.html");
            return;
        }

        String normalizedUrl = String
                .format(
                        "%s://%s%s",
                        parsedUrl.getProtocol(),
                        parsedUrl.getHost(),
                        parsedUrl.getPort() == -1 ? "" : ":" + parsedUrl.getPort()
                )
                .toLowerCase();

        Url url = new QUrl().name.equalTo(normalizedUrl)
                .findOne();

        if (url != null) {
            ctx.sessionAttribute("flashInfo", "URL уже добавлен!");
            ctx.redirect("/urls");
            return;
        }

        Url newUrl = new Url(normalizedUrl);

        newUrl.save();
        ctx.sessionAttribute("flashSuccess", "URL успешно обавлен!");
        ctx.redirect("/urls");
        return;
    };

    public static Handler list = ctx -> {
        int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);
        int rowsPerPage = 5;
        int offset = (page - 1) * rowsPerPage;

        if (page < 1) {
            throw new NotFoundResponse();
        }

        PagedList<Url> pagedUrls = new QUrl()
                .setFirstRow(offset)
                .setMaxRows(rowsPerPage)
                .orderBy().id.desc()
                .findPagedList();

        List<Url> urls = pagedUrls.getList();

        Map<Long, UrlCheck> checks = new QUrlCheck().url.id.asMapKey()
                .orderBy().createdAt.desc()
                .findMap();

        ctx.attribute("urls", urls);
        ctx.attribute("page", page);
        ctx.attribute("checks", checks);
        ctx.render("index.html");
    };
}
