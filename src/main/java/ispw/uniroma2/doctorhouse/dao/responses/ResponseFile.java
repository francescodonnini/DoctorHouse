package ispw.uniroma2.doctorhouse.dao.responses;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import ispw.uniroma2.doctorhouse.beans.PrescriptionBean;
import ispw.uniroma2.doctorhouse.beans.ResponseBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.dao.prescriptions.PrescriptionDao;
import ispw.uniroma2.doctorhouse.dao.requests.RequestFile;
import ispw.uniroma2.doctorhouse.model.Prescription;
import ispw.uniroma2.doctorhouse.model.Response;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResponseFile implements ResponseDao {

    private PrescriptionDao prescriptionDao;

    private final String path;

    public ResponseFile(PrescriptionDao prescriptionDao, String filePath) {
        this.prescriptionDao = prescriptionDao;
        this.path = filePath;
    }

    private int getLastKey(File file) throws PersistentLayerException {
        String [] line;
        String id = "";
        try (FileReader fileReader = new FileReader(file)) {
            CSVReader csvReader = new CSVReader(fileReader);
            while((line = csvReader.readNext()) != null)
                id = line[0];
            if(id.isEmpty())
                id = "0";
            return Integer.parseInt(id);
        } catch (IOException | CsvValidationException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public void insertResponse(ResponseBean responseBean, PrescriptionBean prescriptionBean) throws PersistentLayerException {
        int prescriptionId = prescriptionDao.savePrescription(prescriptionBean);
        File response = new File(path);
        StringBuilder builder = new StringBuilder();
        //Create file writer
        try (FileWriter writer = new FileWriter(path, true)) {
            int lastKey = getLastKey(response) + 1;
            builder.append(lastKey).append(",").append(responseBean.getRequestId()).append(",").append(responseBean.getMessage()).append(",").append(prescriptionId).append("\n");
            writer.write(builder.toString());
        } catch (IOException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public Optional<List<Response>> responseFinder() throws PersistentLayerException {
        String [] line;
        String requestId = "";
        String prescriptionId = "";
        String message = "";
        List<Response> responses = new ArrayList<>();
        try (FileReader fileReader = new FileReader(path)) {
            CSVReader csvReader = new CSVReader(fileReader);
            while((line = csvReader.readNext()) != null) {
                if(line[0].isEmpty())
                    break;
                requestId = line[1];
                if (RequestFile.checkRequest(Integer.parseInt(requestId))) {
                    message = line[2];
                    prescriptionId = line[3];
                    if (!prescriptionId.isEmpty()) {
                        Prescription prescription = prescriptionDao.getPrescription(Integer.parseInt(prescriptionId));
                        responses.add(new Response(message, prescription, Integer.parseInt(requestId)));
                    } else responses.add(new Response(message, null, Integer.parseInt(requestId)));
                }
            }
            return Optional.of(responses);
        } catch (IOException | CsvValidationException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public void insertRejection(ResponseBean bean) throws PersistentLayerException {
        File response = new File(path);
        StringBuilder builder = new StringBuilder();
        //Create file writer
        try (FileWriter writer = new FileWriter(path, true)) {
            builder.append(getLastKey(response) + 1).append(",").append(bean.getRequestId()).append(",").append(bean.getMessage()).append(",").append("\n");
            writer.write(builder.toString());
        } catch (IOException e) {
            throw new PersistentLayerException(e);
        }
    }
}
