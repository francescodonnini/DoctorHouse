package ispw.uniroma2.doctorhouse.dao.prescriptions;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import ispw.uniroma2.doctorhouse.beans.DrugPrescriptionBean;
import ispw.uniroma2.doctorhouse.beans.VisitPrescriptionBean;
import ispw.uniroma2.doctorhouse.dao.exceptions.PersistentLayerException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class PrescriptionFile implements PrescriptionDao{

    private static final String PATH = Objects.requireNonNull(PrescriptionFile.class.getResource("prescriptions.csv")).getPath();

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
    public int addDrugPrescription(DrugPrescriptionBean bean) throws PersistentLayerException {
        File prescriptions = new File(PATH);
        StringBuilder builder = new StringBuilder();
        int lastId = getLastKey(prescriptions);
        //Create file writer
        try (FileWriter writer = new FileWriter(PATH, true)) {
            builder.append(lastId+1).append(bean.getName()).append(",").append(bean.getQuantity()).append("\n");
            writer.write(builder.toString());
            return lastId + 1 ;
        } catch (IOException e) {
            throw new PersistentLayerException(e);
        }
    }

    @Override
    public int addVisitPrescription(VisitPrescriptionBean bean) throws PersistentLayerException {
        File prescriptions = new File(PATH);
        StringBuilder builder = new StringBuilder();
        int lastId = getLastKey(prescriptions);
        //Create file writer
        try (FileWriter writer = new FileWriter(PATH, true)) {
            builder.append(lastId+1).append(bean.getName()).append(",").append("\n");
            writer.write(builder.toString());
            return lastId + 1 ;
        } catch (IOException e) {
            throw new PersistentLayerException(e);
        }
    }
}
