import org.apache.jena.query.*;
import org.apache.jena.tdb.TDBFactory;

import java.io.BufferedReader;
import java.io.FileReader;

public class TDB_SPARQL {

    private static final String QRY_PATH = "/home/tanaka/Documents/oscar/src/main/resources/query_in/query_";
    private static final String TDB_PATH = "/home/tanaka/Documents/oscar/src/main/resources/database/";

    public static void main(String[] args) {

        try {

            Dataset _dataset = TDBFactory.createDataset(TDB_PATH);
            _dataset.begin(ReadWrite.WRITE);

            int i = 10;
                BufferedReader reader = new BufferedReader(new FileReader(QRY_PATH + i));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                String ls = System.getProperty("line.separator");
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                reader.close();

                String qry = stringBuilder.toString();


                if (qry.startsWith("SELECT")) {
                    try (QueryExecution qExec = QueryExecutionFactory.create(qry, _dataset)) {
                        ResultSet rs = qExec.execSelect();
                        ResultSetFormatter.out(rs);
                    }
                }
                if (qry.startsWith("ASK")) {
                    try (QueryExecution qExec = QueryExecutionFactory.create(qry, _dataset)) {
                        boolean rs = qExec.execAsk();
                        System.out.println(rs);
                    }
                }
            _dataset.commit();
            _dataset.end();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
