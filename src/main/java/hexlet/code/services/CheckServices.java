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

    public int checkStatusCode() throws IOException {
        HttpResponse<String> response = Unirest.get(url).asString();
        return response.getStatus();
    }

    public String checkTitle() throws IOException {
        Document doc = Jsoup.connect(url).get();
        return doc.title();
    }

    public String checkH1() throws IOException {
        Document doc = Jsoup.connect(url).get();
        return doc.getElementsByTag("h1").text();
    }

    public String checkDescription() throws IOException {
        Document doc = Jsoup.connect(url).get();

        List<Element> metas = doc.select("meta");
        for (var meta : metas) {
            if (meta.attr("name").equals("description")) {
                return meta.attr("content");
            }
        }
        return "";
    }
}
