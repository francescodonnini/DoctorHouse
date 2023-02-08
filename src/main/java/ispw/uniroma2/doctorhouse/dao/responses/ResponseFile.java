package ispw.uniroma2.doctorhouse.dao.responses;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import ispw.uniroma2.doctorhouse.beans.DrugPrescriptionBean;
import ispw.uniroma2.doctorhouse.beans.ResponseBean;
import ispw.uniroma2.doctorhouse.beans.ResponsePatientBean;
import ispw.uniroma2.doctorhouse.beans.VisitPrescriptionBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.dao.prescriptions.PrescriptionDao;
import ispw.uniroma2.doctorhouse.dao.prescriptions.PrescriptionFile;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ResponseFile implements ResponseDao {

    private final PrescriptionDao prescriptionDao;

    private final String path;

    private final String prescriptionPath;

    public ResponseFile(PrescriptionDao prescriptionDao) {
        this.prescriptionDao = prescriptionDao;
        this.path = Objects.requireNonNull(ResponseFile.class.getResource("responses.csv").getPath());
        this.prescriptionPath = Objects.requireNonNull(PrescriptionFile.class.getResource("prescriptions.csv")).getPath();
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
    public void insertVisitResponse(ResponseBean responseBean, VisitPrescriptionBean visitPrescriptionBean) throws PersistentLayerException {
        int prescriptionId = prescriptionDao.addVisitPrescription(visitPrescriptionBean);
        File response = new File(path);
        StringBuilder builder = new StringBuilder();
        //Create file writer
        try (FileWriter writer = new FileWriter(path, true)) {
            builder.append(getLastKey(response) + 1 ).append(",").append(responseBean.getRequestId()).append(",").append(responseBean.getMessage()).append(",").append(prescriptionId).append("\n");
            writer.write(builder.toString());
        } catch (IOException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public void insertDrugResponse(ResponseBean responseBean, DrugPrescriptionBean drugPrescriptionBean) throws PersistentLayerException {
        int prescriptionId = prescriptionDao.addDrugPrescription(drugPrescriptionBean);
        File response = new File(path);
        StringBuilder builder = new StringBuilder();
        //Create file writer
        try (FileWriter writer = new FileWriter(path, true)) {
            builder.append(getLastKey(response) +1 ).append(",").append(responseBean.getRequestId()).append(",").append(responseBean.getMessage()).append(",").append(prescriptionId).append("\n");
            writer.write(builder.toString());
        } catch (IOException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public Optional<List<ResponsePatientBean>> responseFinder() throws PersistentLayerException {
        String id = "";
        String kind = "";
        String name = "";
        String quantity = "";
        List<ResponsePatientBean> responsePatientBeans = new ArrayList<>();
        try(CSVReader reader = new CSVReader(new FileReader(path))) {
            String [] line;
            while ((line = reader.readNext()) != null) {
                String message = line[2];
                String prescriptionId = line[3];
                try (CSVReader prescriptionReader = new CSVReader(new FileReader(prescriptionPath))) {
                    String [] l;
                    while((l = prescriptionReader.readNext()) != null) {
                        id = l[0];
                        kind = l[1];
                        name = l[2];
                        quantity = l[3];
                        if(!id.isEmpty() && id.equals(prescriptionId)) {
                            ResponsePatientBean responsePatientBean = new ResponsePatientBean();
                            responsePatientBean.setKind(kind);
                            responsePatientBean.setName(name);
                            if (quantity.isEmpty()) {
                                responsePatientBean.setQuantity(0);
                            } else responsePatientBean.setQuantity(Integer.parseInt(quantity));
                            responsePatientBean.setMessage(message);
                            responsePatientBeans.add(responsePatientBean);
                        }
                    }
                }
            }
            return Optional.of(responsePatientBeans);
        } catch (CsvValidationException | IOException e) {
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
