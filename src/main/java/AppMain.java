import com.DmitryElkin_Servlets_REST_API.Service.PrepareDB;
import com.DmitryElkin_Servlets_REST_API.repository.utils.HibernateUtil;

public class AppMain {


    public static void main(String[] args) {
//        PrepareDB.doPrepare();
        HibernateUtil.getSession();
    }

}
