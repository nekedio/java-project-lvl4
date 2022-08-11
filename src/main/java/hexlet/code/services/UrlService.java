package hexlet.code.services;

import hexlet.code.models.Url;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class UrlService {

    private String line;

    public UrlService(String line) {
        this.line = line;
    }

    public boolean isValid() {
        try {
            URL url = new URL(this.line);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public String getUrlName() {
        URL url = null;

        try {
            url = new URL(this.line);
        } catch (MalformedURLException ex) {
            Logger.getLogger(UrlService.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

        return url.getProtocol() + "://" + url.getAuthority();
    }

    public static boolean containedIn(List<Url> urls, Url newUrl) {
        for (Url url : urls) {
            if (url.getName().equals(newUrl.getName())) {
                return true;
            }
        }
        return false;
    }
}
