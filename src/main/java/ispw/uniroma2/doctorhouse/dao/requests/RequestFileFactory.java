package ispw.uniroma2.doctorhouse.dao.requests;

import ispw.uniroma2.doctorhouse.Main;
import ispw.uniroma2.doctorhouse.dao.responses.ResponseDao;

public class RequestFileFactory implements RequestDaoFactory{

    private final ResponseDao responseDao;

    public RequestFileFactory(ResponseDao responseDao) {
        this.responseDao = responseDao;
    }


    @Override
    public RequestDao create() {
        String filePath = Main.APP_DIR_PATH + "/requests.csv";
        return new RequestFile(filePath, responseDao);
    }
}
