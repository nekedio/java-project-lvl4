package hexlet.code.controllers;

import hexlet.code.models.Url;
import hexlet.code.models.UrlCheck;
import hexlet.code.models.query.QUrl;
import hexlet.code.models.query.QUrlCheck;
import hexlet.code.services.CheckServices;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
import java.util.List;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class CheckController {

    public static Handler show = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);

        Url url = new QUrl().id.eq(id).findOne();

        if (url == null) {
            throw new NotFoundResponse();
        }

        List<UrlCheck> checks = new QUrlCheck().url.equalTo(url)
                .orderBy().id.desc()
                .findList();

        ctx.attribute("url", url);
        ctx.attribute("checks", checks);
        ctx.render("show.html");
    };

    public static Handler createCheck = ctx -> {

        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);

        Url url = new QUrl().id.eq(id).findOne();

        if (url == null) {
            throw new NotFoundResponse();
        }

        int status;
        String title;
        String h1;
        String description;

        try {
            HttpResponse<String> response = Unirest.get(url.getName()).asString();
            status = CheckServices.checkStatusCode(response);
            title = CheckServices.checkTitle(response);
            h1 = CheckServices.checkH1(response);
            description = CheckServices.checkDescription(response);
        } catch (Exception e) {
            ctx.sessionAttribute("flashError", e.getMessage());
            ctx.redirect("/urls/" + id);
            return;
        }

        UrlCheck check = new UrlCheck(status, title, h1, description, url);
        check.save();

        ctx.sessionAttribute("flashSuccess", "Сайт успешно прошел проверку!");
        ctx.redirect("/urls/" + id);
        return;
    };
}
