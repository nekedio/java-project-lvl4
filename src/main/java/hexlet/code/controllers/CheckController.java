package hexlet.code.controllers;

import hexlet.code.models.Url;
import hexlet.code.models.query.QUrl;
import io.javalin.http.Handler;

public class CheckController {

    public static Handler show = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);

        Url url = new QUrl().id.eq(id).findOne();

        if (url == null) {
            ctx.status(404);
            ctx.render("404.html");
            return;
        }

        ctx.attribute("url", url);
        ctx.render("show.html");
    };
}
