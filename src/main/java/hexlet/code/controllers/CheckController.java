package hexlet.code.controllers;

import hexlet.code.models.Url;
import hexlet.code.models.query.QUrl;
import io.javalin.http.Handler;

public class CheckController {

    public static Handler show = ctx -> {
        int id = ctx.pathParamAsClass("id", Integer.class).getOrDefault(null);
        Url url = new QUrl().id.eq(id).findOne();
        ctx.attribute("url", url);
        ctx.render("show.html");
    };
}
