package hexlet.code;

import io.javalin.Javalin;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;

class App {

    static final int LOCALHOST_PORT = 5000;

    private static int getPort() {
        String port = System.getenv("PORT");
        if (port != null) {
            return Integer.valueOf(port);
        }
        return LOCALHOST_PORT;

    }

    public static Javalin getApp() {
        Javalin app = Javalin.create(config -> {
            config.enableDevLogging();
        });

        addRoutes(app);

        app.before(ctx -> {
            ctx.attribute("ctx", ctx);
        });

        return app;
    }

    private static void addRoutes(Javalin app) {
        app.get("/", ctx -> ctx.result("Hello World"));
    }

    private static String getFileContent(String fileName) throws IOException {
        Path pathToSolution = Paths.get(fileName).toAbsolutePath();
        return Files.readString(pathToSolution).trim();
    }

    public static void main(String[] args) throws SQLException, IOException {

        // Соединение с базой и миграции
//        Connection connection = DriverManager.getConnection("jdbc:h2:./page_analyzer");
//        Statement statement = connection.createStatement();
//        String initSql = getFileContent("init.sql");
//        statement.execute(initSql);


        // Принт таблицы
//        String query = "SELECT * FROM url";
//        PreparedStatement st = connection.prepareStatement(query);
//        ResultSet rs = st.executeQuery();
//        while (rs.next()) {
//            System.out.println(
//                    rs.getString("id") + "  "
//                    + rs.getString("name") + "  "
//                    + rs.getString("createdAt")
//            );
//        }

        // Старт сервера
        Javalin app = getApp();
        app.start(getPort());
    }
}
