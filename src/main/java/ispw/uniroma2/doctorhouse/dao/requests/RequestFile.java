package ispw.uniroma2.doctorhouse.dao.requests;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import ispw.uniroma2.doctorhouse.beans.DoctorRequestBean;
import ispw.uniroma2.doctorhouse.beans.PrescriptionRequestBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Session;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class RequestFile implements RequestDao {
    private static final int ID = 0;
    private static final int PATIENT_EMAIL = 2;
    private static final int MESSAGE = 3;

    private static final String PATH = Objects.requireNonNull(RequestFile.class.getResource("requests.csv")).getPath();

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
    public void addRequestOfPrescription(PrescriptionRequestBean requestBean) throws PersistentLayerException {
        //Create file object
        File requests = new File(PATH);
        StringBuilder builder = new StringBuilder();
        int lastId = getLastKey(requests);
            //Create file writer
        try (FileWriter writer = new FileWriter(PATH, true)) {
            builder.append(lastId+1).append(",").append(Session.getSession().getUser().getFamilyDoctor().orElseThrow().getEmail()).append(",").append(Session.getSession().getUser().getEmail()).append(",").append(requestBean.getMessage()).append("\n");
            writer.write(builder.toString());
        } catch (IOException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public Optional<List<DoctorRequestBean>> requestFinder() throws PersistentLayerException {
        List<DoctorRequestBean> doctorRequestBeans = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(PATH))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                String id = line[ID];
                String patientEmail = line[PATIENT_EMAIL];
                String message = line[MESSAGE];
                DoctorRequestBean bean = new DoctorRequestBean();
                bean.setId(Integer.parseInt(id));
                bean.setPatient(patientEmail);
                bean.setMessage(message);
                doctorRequestBeans.add(bean);
            }
            return Optional.of(doctorRequestBeans);
        } catch (IOException | CsvValidationException e) {
            throw new PersistentLayerException(e);
        }
    }
}
