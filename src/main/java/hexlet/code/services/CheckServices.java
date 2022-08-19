package hexlet.code.services;

import java.io.IOException;
import java.util.List;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public final class CheckServices {

    private String url;

    public CheckServices(String url) {
        this.url = url;
    }

    public int checkStatusCode() {
        int status;

        try {
            HttpResponse<String> response = Unirest.get(url).asString();
            status = response.getStatus();
        } catch (Exception e) {
            status = 500;
        }
        return status;
    }

    public String checkTitle() {
        String title;

        try {
            Document doc = Jsoup.connect(url).get();
            title = doc.title();
        } catch (IOException e) {
            title = "-";
        }

        return title;
    }

    public String checkH1() {
        String h1;

        try {
            Document doc = Jsoup.connect(url).get();
            h1 = doc.getElementsByTag("h1").text();
        } catch (IOException e) {
            h1 = "-";
        }

        return h1;
    }

    public String checkDescription() {
        String description;

        try {
            Document doc = Jsoup.connect(url).get();

            List<Element> metas = doc.select("meta");
            for (var meta : metas) {
                if (meta.attr("name").equals("description")) {
                    return meta.attr("content");
                }
            }
            description = "-";
        } catch (IOException e) {
            description = "-";
        }

        return description;
    }
}
