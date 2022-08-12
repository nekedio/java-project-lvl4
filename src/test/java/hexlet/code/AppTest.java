package hexlet.code;

import hexlet.code.models.Url;
import hexlet.code.models.query.QUrl;
import io.ebean.DB;
import io.ebean.Transaction;
import io.javalin.Javalin;
import java.util.List;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class AppTest {

    @Test
    public void testTest() {
        assertThat("test").isEqualTo("test");
    }

    private static Javalin app;
    private static String baseUrl;
    private static Transaction transaction;

    @BeforeAll
    public static void beforeAll() {
        app = App.getApp();
        app.start(0);
        int port = app.port();
        baseUrl = "http://localhost:" + port;
    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }

    @BeforeEach
    void beforeEach() {
        transaction = DB.beginTransaction();
    }

    @AfterEach
    void afterEach() {
        transaction.rollback();
    }

    @Test
    public void testHomePage() {
        HttpResponse<String> response = Unirest.get(baseUrl).asString();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody()).contains(List.of(
                "Анализатор страниц",
                "Бесплатный СЕО анализ страницы вашего сайта",
                "Отправить на проверку!"
        ));
    }

    @Test
    public void testSitesPage() {
        HttpResponse<String> response = Unirest.get(baseUrl + "/urls").asString();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody()).contains(List.of(
                "Имя",
                "Последняя проверка",
                "Код ответа"
        ));
    }

    @Test
    public void testCheckPage() {
        String urlName = "https://www.test.ru";
        HttpResponse<String> response = Unirest.post(baseUrl)
                .field("url", urlName)
                .asEmpty();
        HttpResponse<String> responseUrl = Unirest.get(baseUrl + "/urls/1").asString();

        assertThat(responseUrl.getStatus()).isEqualTo(200);

        assertThat(responseUrl.getBody()).contains(List.of(
                urlName,
                "Запустить анализ!"
        ));
    }

    @Test
    public void testAddingValidUrl() {
        String urlName = "https://www.test.ru";

        HttpResponse<String> response = Unirest.post(baseUrl)
                .field("url", urlName)
                .asEmpty();
        assertThat(response.getStatus()).isEqualTo(302);

        HttpResponse<String> responseUrls = Unirest.get(baseUrl + "/urls").asString();
        assertThat(responseUrls.getStatus()).isEqualTo(200);
        assertThat(responseUrls.getBody()).contains(urlName);

        Url url = new QUrl().name.equalTo(urlName)
                .findOne();
        assertThat(url).isNotNull();
        assertThat(url.getName()).isEqualTo(urlName);
    }

    @Test
    public void testAddingInvalidUrl() {
        String invalidUrl = "test.ru";
        String invalidUrl1 = "";

        HttpResponse<String> response = Unirest.post(baseUrl)
                .field("url", invalidUrl)
                .asEmpty();
        assertThat(response.getStatus()).isEqualTo(422);

        HttpResponse<String> response1 = Unirest.post(baseUrl)
                .field("url", invalidUrl1)
                .asEmpty();
        assertThat(response1.getStatus()).isEqualTo(422);

        List<Url> urls = new QUrl().findList();
        assertThat(0).isEqualTo(urls.size());
    }
}
