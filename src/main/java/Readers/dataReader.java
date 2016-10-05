





/*package Readers;

        import java.io.BufferedReader;
        import java.io.FileInputStream;
        import java.io.InputStreamReader;
        import java.util.ArrayList;
        import java.util.List;

public class dataReader {
    public List<String> data;

    public dataReader(String dataFile) {
        data = new ArrayList<String>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile)));

            String str;
            while ((str = br.readLine()) != null) {
                data.add(str);
            }

            br.close();
        }catch (Exception e) {}
    }
}
*/
