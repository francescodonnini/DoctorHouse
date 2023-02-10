package ispw.uniroma2.doctorhouse.dao.prescriptions;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import ispw.uniroma2.doctorhouse.beans.PrescriptionBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;
import ispw.uniroma2.doctorhouse.model.Prescription;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PrescriptionFile implements PrescriptionDao{

    private final String path;

    public PrescriptionFile(String path) {
        this.path = path;
    }

    private int getLastKey(File file) throws PersistentLayerException {
        String [] line;
        String id = "";
        try (FileReader fileReader = new FileReader(file)) {
            CSVReader csvReader = new CSVReader(fileReader);
            while((line = csvReader.readNext()) != null)
                id = line[0];
            if(id.trim().isEmpty())
                id = "0";
            return Integer.parseInt(id);
        } catch (IOException | CsvValidationException e) {
            throw new PersistentLayerException(e);
        }
    }
    @Override
    public int savePrescription(PrescriptionBean bean) throws PersistentLayerException {
        File prescriptions = new File(path);
        StringBuilder builder = new StringBuilder();
        int lastId = getLastKey(prescriptions);
        //Create file writer
        try (FileWriter writer = new FileWriter(path, true)) {
            if(bean.getQuantity() != 0) {
                builder.append(lastId + 1).append(",").append("Drug").append(",").append(bean.getName()).append(",").append(bean.getQuantity()).append("\n");
            } else builder.append(lastId + 1).append(",").append("Visit").append(",").append(bean.getName()).append(",").append(0).append("\n");
            writer.write(builder.toString());
            return lastId + 1 ;
        } catch (IOException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public Prescription getPrescription(int prescriptionId) throws PersistentLayerException {
        String [] line;
        String id = "";
        String kind = "";
        String name = "";
        String quantity = "";
        Prescription prescription = null;
        try (FileReader fileReader = new FileReader(path)) {
            CSVReader csvReader = new CSVReader(fileReader);
            while((line = csvReader.readNext()) != null) {
                id = line[0];
                if(Integer.parseInt(id) == prescriptionId) {
                    kind = line[1];
                    name = line[2];
                    quantity = line[3];
                    if(quantity.isEmpty()) {
                        prescription = new Prescription(kind, name, 0);
                    } else prescription = new Prescription(kind, name, Integer.parseInt(quantity));
                    break;
                }
            }
            return prescription;
        } catch (IOException | CsvValidationException e) {
            throw new PersistentLayerException(e);
        }
    }
}
