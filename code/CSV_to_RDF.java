import com.github.jsonldjava.utils.Obj;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.tdb.TDBFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSV_to_RDF {

    private static final String CSV_PATH = "/home/tanaka/Documents/oscar/src/main/resources/movie_metadata.csv";
    private static final String MAP_PATH = "/home/tanaka/Documents/oscar/src/main/resources/mapping";
    private static final String OUT_PATH = "/home/tanaka/Documents/oscar/src/main/resources/turtle_output";
    private static final String TDB_PATH = "/home/tanaka/Documents/oscar/src/main/resources/database/";
    private static final String LocalURI = "http://www.semanticweb.org/rohanchhokra/ontologies/2019/2/oscar-ontology#";
    private static final String schemaURI = "https://schema.org/";

    public static void main(String[] args) {

        try {

            Reader _reader = new BufferedReader(new FileReader(CSV_PATH));
            CSVParser _parser = new CSVParser(_reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

            BufferedReader _reader2 = new BufferedReader(new FileReader(MAP_PATH));

            List<String> variables = new ArrayList<String>();
            List<String> rules = new ArrayList<String>();

            String line;
            _reader2.readLine();
            while (!(line = _reader2.readLine()).isEmpty()) {
                String words[] = line.split(", ");
                variables.add(words[1]);
            }

            int var_size = variables.size();

            _reader2.readLine();
            while ((line = _reader2.readLine()) != null) {
                rules.add(line);
            }

            Dataset _dataset = TDBFactory.createDataset(TDB_PATH);
            _dataset.begin(ReadWrite.WRITE);

            Model _model = _dataset.getDefaultModel();

            int counter_lmao = 0;
            for (CSVRecord _record : _parser) {
                counter_lmao++;
                for (String rule : rules) {
                    String rule_fields[] = rule.split(", ");
                    if (rule_fields.length == 2) {
                        if (rule_fields[0].startsWith("sweb")) {
                            Resource subj = _model.createResource(LocalURI + rule_fields[1] + counter_lmao);
                            Property pred = _model.createProperty(LocalURI + "type");
                            RDFNode obj = _model.createResource(LocalURI + rule_fields[0].substring(5));
                            _model.add(_model.createStatement(subj, pred, obj));
                        }
                        else {
                            Resource subj = _model.createResource(LocalURI + rule_fields[1] + counter_lmao);
                            Property pred = _model.createProperty(LocalURI + "type");
                            RDFNode obj = _model.createResource(schemaURI + rule_fields[0].substring(7));
                            _model.add(_model.createStatement(subj, pred, obj));
                        }
                    }
                    else if (rule_fields.length == 3) {
                        if (rule_fields[0].startsWith("sweb")) {
                            Property pred = _model.createProperty(LocalURI + rule_fields[0].substring(5));
                            Resource subj = _model.createResource(LocalURI + rule_fields[1] + counter_lmao);
                            RDFNode obj = _model.createResource(LocalURI + rule_fields[2] + counter_lmao);
                            if (StringUtils.isNumeric(rule_fields[2])) {
                                int xxx = Integer.parseInt(rule_fields[2]);
                                if (_record.get(xxx).equals("")) {
                                    continue;
                                }
                                if (variables.get(xxx).equals("string[]")) {
                                    String sx = _record.get(xxx);
                                    String sxs[] = sx.split("\\|");
                                    for (String sxsx : sxs) {
                                        Literal obx = _model.createLiteral(new String(sxsx));
                                        _model.add(_model.createStatement(subj, pred, obx));
                                    }
                                }
                                else if (variables.get(xxx).equals("string")) {
                                    String sx = _record.get(xxx);
                                    Literal obx = _model.createLiteral(new String(sx));
                                    _model.add(_model.createStatement(subj, pred, obx));
                                }
                                else if (variables.get(xxx).equals("float")) {
                                    String sx = _record.get(xxx);
                                    Literal obx = _model.createTypedLiteral(Float.parseFloat(sx));
                                    _model.add(_model.createStatement(subj, pred, obx));
                                }
                                else {
                                    String sx = _record.get(xxx);
                                    Literal obx = _model.createTypedLiteral(Long.parseLong(sx));
                                    _model.add(_model.createStatement(subj, pred, obx));
                                }
                            }
                            else {
                                _model.add(_model.createStatement(subj, pred, obj));
                            }
                        }
                        else if (rule_fields[0].startsWith("schema")) {
                            Property pred = _model.createProperty(schemaURI + rule_fields[0].substring(7));
                            Resource subj = _model.createResource(LocalURI + rule_fields[1] + counter_lmao);
                            RDFNode obj = _model.createResource(LocalURI + rule_fields[2] + counter_lmao);
                            if (StringUtils.isNumeric(rule_fields[2])) {
                                int xxx = Integer.parseInt(rule_fields[2]);
                                if (_record.get(xxx).equals("")) {
                                    continue;
                                }
                                if (variables.get(xxx).equals("string[]")) {
                                    String sx = _record.get(xxx);
                                    String sxs[] = sx.split("\\|");
                                    for (String sxsx : sxs) {
                                        Literal obx = _model.createLiteral(new String(sxsx));
                                        _model.add(_model.createStatement(subj, pred, obx));
                                    }
                                }
                                else if (variables.get(xxx).equals("string")) {
                                    String sx = _record.get(xxx);
                                    Literal obx = _model.createLiteral(new String(sx));
                                    _model.add(_model.createStatement(subj, pred, obx));
                                }
                                else if (variables.get(xxx).equals("float")) {
                                    String sx = _record.get(xxx);
                                    Literal obx = _model.createTypedLiteral(Float.parseFloat(sx));
                                    _model.add(_model.createStatement(subj, pred, obx));
                                }
                                else {
                                    String sx = _record.get(xxx);
                                    Literal obx = _model.createTypedLiteral(Long.parseLong(sx));
                                    _model.add(_model.createStatement(subj, pred, obx));
                                }
                            }
                            else {
                                _model.add(_model.createStatement(subj, pred, obj));
                            }
                        }
                    }
                }
                if (counter_lmao % 50 == 0)
                    System.out.println(counter_lmao + " done.");
            }
            FileWriter out = new FileWriter(OUT_PATH);
            RDFDataMgr.write(out, _model, RDFFormat.TURTLE);


            try (QueryExecution qExec = QueryExecutionFactory.create(
                    "SELECT (count(*) AS ?count) { ?s ?p ?o} LIMIT 10", _dataset)) {
                ResultSet rs = qExec.execSelect() ;
                ResultSetFormatter.out(rs) ;
            }

            _dataset.commit();
            _dataset.end();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
