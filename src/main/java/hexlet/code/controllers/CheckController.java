package hexlet.code.controllers;

import hexlet.code.models.Url;
import hexlet.code.models.UrlCheck;
import hexlet.code.models.query.QUrl;
import hexlet.code.models.query.QUrlCheck;
import hexlet.code.services.CheckServices;
import io.javalin.http.Handler;
import java.util.List;

public class CheckController {

    public static Handler show = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);

        Url url = new QUrl().id.eq(id).findOne();

        if (url == null) {
            ctx.status(404);
            ctx.render("404.html");
            return;
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
            ctx.status(404);
            ctx.render("404.html");
            return;
        }

        CheckServices checkServices = new CheckServices(url.getName());

        int status;
        String title;
        String h1;
        String description;

        try {
            status = checkServices.checkStatusCode();
            title = checkServices.checkTitle();
            h1 = checkServices.checkH1();
            description = checkServices.checkDescription();
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
