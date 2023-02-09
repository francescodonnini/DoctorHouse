package ispw.uniroma2.doctorhouse.dao.requests;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Request;
import ispw.uniroma2.doctorhouse.model.Session;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class RequestFile implements RequestDao {
    private static final int ID = 0;

    private static final int DOCTOR_EMAIL = 1;

    private static final int PATIENT_EMAIL = 2;
    private static final int MESSAGE = 3;

    private final String path;

    public RequestFile() {
        this.path = Objects.requireNonNull(getClass().getResource("requests.csv")).getPath();
    }

    private int getLastKey(File file) throws PersistentLayerException {
       String [] line;
       String id = "";
       try (FileReader fileReader = new FileReader(file)) {
               CSVReader csvReader = new CSVReader(fileReader);
               while((line = csvReader.readNext()) != null)
                   id = line[ID];
               if(id.isEmpty())
                   id = "0";
               return Integer.parseInt(id);
       } catch (IOException | CsvValidationException e) {
           throw new PersistentLayerException(e);
       }
    }

    @Override
    public void addRequestOfPrescription(String message) throws PersistentLayerException {
        //Create file object
        File requests = new File(path);
        StringBuilder builder = new StringBuilder();
        int lastId = getLastKey(requests);
            //Create file writer
        try (FileWriter writer = new FileWriter(path, true)) {
            builder.append(lastId+1).append(",").append(Session.getSession().getUser().getFamilyDoctor().orElseThrow().getEmail()).append(",").append(Session.getSession().getUser().getEmail()).append(",").append(message).append("\n");
            writer.write(builder.toString());
        } catch (IOException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public Optional<List<Request>> requestFinder() throws PersistentLayerException {
        List<Request> requests = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                String id = line[ID];
                if(id.isEmpty())
                    break;
                String patientEmail = line[PATIENT_EMAIL];
                String message = line[MESSAGE];
                String doctorEmail = line[DOCTOR_EMAIL];
                if(doctorEmail.equals(Session.getSession().getUser().getEmail())) {
                    Request request = new Request(Integer.parseInt(id), patientEmail, message);
                    requests.add(request);
                }
            }
            return Optional.of(requests);
        } catch (IOException | CsvValidationException e) {
            throw new PersistentLayerException(e);
        }
    }
}
