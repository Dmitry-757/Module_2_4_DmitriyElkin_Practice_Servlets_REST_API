import com.DmitryElkin_Servlets_REST_API.repository.utils.PrepareDB;

public class AppMain {
    private static final String USER = "root";
    private static final String PASS = "dingo1975";
    private static final String URL = "jdbc:mysql://localhost:3306/proselyte_module_2_4_db";
//    private static final String URL = "jdbc:postgresql://localhost:5432/proselyte_developers_hibernate_db";





    public static void main(String[] args) {
        PrepareDB.doPrepare();
//        System.out.println("Start...");
//
//        flyWayMigrations();
//        HibernateUtil.getSession();
//        System.out.println("\nstarting...");
    }

//    public static void flyWayClean(){
//        var flyWay = Flyway.configure()
//                .cleanDisabled(false)
//                .dataSource(URL, USER, PASS)
//                .locations("classpath:/db/migration")
//                .load();
//        flyWay.clean();
//    }
//
//    public static void flyWayMigrations(){
////        logger.info("db migration started...");
//        var flyWay = Flyway.configure()
//                .dataSource(URL, USER, PASS)
//                .locations("classpath:/db/migration")
//                .load();
//        flyWay.migrate();
//    }
}
