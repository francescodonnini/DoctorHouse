package ispw.uniroma2.doctorhouse.dao.requests;

public class RequestFileFactory implements RequestDaoFactory{
    @Override
    public RequestDao create() {
        return new RequestFile();
    }
}
