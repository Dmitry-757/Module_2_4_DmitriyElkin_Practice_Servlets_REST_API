import org.flywaydb.core.Flyway;

public class AppMain {
    private static final String USER = "root";
    private static final String PASS = "dingo1975";
    //    private static final String URL = "jdbc:mysql://localhost:3306/proselyte_developers_hibernate_db";
    private static final String URL = "jdbc:postgresql://localhost:5432/proselyte_developers_hibernate_db";



    public static void main(String[] args) {
        System.out.println("Start...");
        flyWayMigrations();

        System.out.println("\nstarting...");
    }

    public static void flyWayClean(){
        var flyWay = Flyway.configure()
                .cleanDisabled(false)
                .dataSource(URL, USER, PASS)
                .locations("classpath:/db/migration")
                .load();
        flyWay.clean();
    }

    public static void flyWayMigrations(){
//        logger.info("db migration started...");
        var flyWay = Flyway.configure()
                .dataSource(URL, USER, PASS)
                .locations("classpath:/db/migration")
                .load();
        flyWay.migrate();
    }
}
