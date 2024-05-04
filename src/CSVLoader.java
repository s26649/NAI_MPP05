import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVLoader {
    public static List<Data> loadDataFromCsv(String filePath) throws IOException {
        List<Data> dataList = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            throw new IOException("Plik nie istnieje lub podana sciezka nie jest plikiem: " + filePath);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Double[] attributes = new Double[values.length];
                for (int i = 0; i < values.length; i++) {
                    attributes[i] = Double.parseDouble(values[i]);
                }
                dataList.add(new Data(attributes));
            }
        }
        return dataList;
    }
}