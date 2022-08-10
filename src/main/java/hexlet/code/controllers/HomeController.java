package hexlet.code.controllers;

import hexlet.code.models.Url;
import io.javalin.http.Handler;
import java.util.Map;

public class HomeController {
    public static Handler welcome = ctx -> {
        Url emptyUrl = new Url();
        ctx.attribute("errors", Map.of());
        ctx.attribute("url", emptyUrl);
        ctx.render("home.html");
    };
}
