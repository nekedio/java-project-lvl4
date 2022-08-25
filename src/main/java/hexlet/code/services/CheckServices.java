package hexlet.code.services;

import java.util.List;
import kong.unirest.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public final class CheckServices {

    public static int checkStatusCode(HttpResponse<String> response) {
        return response.getStatus();
    }

    public static String checkTitle(HttpResponse<String> response) {
        Document doc = Jsoup.parse(response.getBody());
        return doc.title();
    }

    public static String checkH1(HttpResponse<String> response) {
        Document doc = Jsoup.parse(response.getBody());
        return doc.getElementsByTag("h1").text();
    }

    public static String checkDescription(HttpResponse<String> response) {
        Document doc = Jsoup.parse(response.getBody());
        List<Element> metas = doc.select("meta");
        for (var meta : metas) {
            if (meta.attr("name").equals("description")) {
                return meta.attr("content");
            }
        }
        return "";
    }
}
