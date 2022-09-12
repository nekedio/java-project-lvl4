package hexlet.code;

import hexlet.code.models.Url;
import hexlet.code.models.UrlCheck;
import hexlet.code.models.query.QUrl;
import io.ebean.DB;
import io.ebean.Database;
import io.ebean.SqlRow;
import io.javalin.Javalin;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class AppTest {

    private static Javalin app;
    private static String baseUrl;
    private static UrlCheck check;
    private static Database database;

    @BeforeEach
    public void afterEach() {
        database.script().run("/truncate.sql");
        database.script().run("/seed-test.sql");
    }

    @BeforeAll
    public static void beforeAll() {
        app = App.getApp();
        app.start(0);
        int port = app.port();
        baseUrl = "http://localhost:" + port;
        database = DB.getDefault();
    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }

    @Test
    public void testHomePage() {
        HttpResponse<String> response = Unirest.get(baseUrl).asString();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody()).contains(List.of(
                "Анализатор страниц",
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
    public void testShowPage() {
        String urlName = "https://www.test-show-page.ru";
        HttpResponse<String> response = Unirest.post(baseUrl + "/urls")
                .field("url", urlName)
                .asEmpty();

        Url url = new QUrl().name.equalTo(urlName).findOne();

        HttpResponse<String> responseUrl = Unirest.get(baseUrl + "/urls/" + url.getId()).asString();

        assertThat(responseUrl.getStatus()).isEqualTo(200);

        assertThat(responseUrl.getBody()).contains(List.of(
                urlName,
                "Запустить анализ!"
        ));
    }

    @Test
    public void testAddingValidUrl() {
        String urlName = "https://www.test-adding-valid-url.ru";

        HttpResponse<String> response = Unirest.post(baseUrl + "/urls")
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

        int beforCountUrl = new QUrl().findList().size();

        List<Url> urlss = new QUrl().findList();

        HttpResponse<String> response = Unirest.post(baseUrl + "/urls")
                .field("url", invalidUrl)
                .asEmpty();
        assertThat(response.getStatus()).isEqualTo(422);

        HttpResponse<String> response1 = Unirest.post(baseUrl + "/urls")
                .field("url", invalidUrl1)
                .asEmpty();
        assertThat(response1.getStatus()).isEqualTo(422);

        int afterCountUrls = new QUrl().findList().size();
        assertThat(beforCountUrl).isEqualTo(afterCountUrls);
    }

    @Test
    public void testCheck() throws IOException {

//        String mockHtml = Files.readString(Path.of("src/test/resources/mock-site.html"));

        String mockHtml = readFixture("mock-site.html");

        MockWebServer server = new MockWebServer();

        server.enqueue(new MockResponse().setBody(mockHtml));

        server.start();

        String mockUrl = server.url("/").toString();

        HttpResponse<String> response = Unirest.post(baseUrl + "/urls")
                .field("url", mockUrl)
                .asEmpty();

        Url url = new QUrl().name.equalTo(mockUrl.substring(0, mockUrl.length() - 1))
                .findOne();

        HttpResponse<String> responseCheck = Unirest.post(baseUrl + "/urls/" + url.getId() + "/checks")
                .asEmpty();

        HttpResponse<String> responseShow = Unirest.get(baseUrl + "/urls/" + url.getId()).asString();

        server.shutdown();

        assertThat(200).isEqualTo(responseShow.getStatus());

        assertThat(responseShow.getBody()).contains(List.of(
                "test_title",
                "test_h1",
                "test_description"
        ));
    }

    @Test
    void testStore() {
        String inputUrl = "https://ru.hexlet.io";
        HttpResponse<String> responsePost = Unirest
                .post(baseUrl + "/urls")
                .field("url", inputUrl)
                .asEmpty();

        assertThat(responsePost.getStatus()).isEqualTo(302);
        assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/urls");

        HttpResponse<String> response = Unirest
                .get(baseUrl + "/urls")
                .asString();
        String body = response.getBody();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(body).contains(inputUrl);
        assertThat(body).contains("Страница успешно добавлена");

        String selectUrl = String.format("SELECT * FROM url WHERE name = '%s';", inputUrl);
        SqlRow actualUrl = DB.sqlQuery(selectUrl).findOne();

        assertThat(actualUrl).isNotNull();
        assertThat(actualUrl.getString("name")).isEqualTo(inputUrl);
    }

    private static String readFixture(String fileName) throws IOException {
        Path filePath = getFixturePath(fileName);
        return Files.readString(filePath).trim();
    }

    private static Path getFixturePath(String fileName) {
        return Path.of("src", "test", "resources", "fixtures", fileName)
                .toAbsolutePath().normalize();
    }
}
