import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;

public class SPARQL {
    /**
     * Function for testing usage of SPARQL queries in Java Jena API.
     *
     * @param model Model
     * @param queryStr String
     * @return -
    */
    public static void executeSPARQLQuery(Model model, String queryStr){

        
        // Create a Query object from the SPARQL query string
        Query query = QueryFactory.create(queryStr);
        // Create a QueryExecution object with the query and the model
        QueryExecution qexec = QueryExecution.create(query, model);

        try {
            // Execute the query and obtain the result set
            ResultSet rs = qexec.execSelect();
            // Process and print the results
            while (rs.hasNext()) {
                QuerySolution soln = rs.nextSolution();
                // Resource res = solution.getResource("x");
                RDFNode x = soln.get("x");
                if (x != null) {
                    System.out.println(x.toString());
                }

            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            qexec.close();
        }
        

    }

    
    public static void main(String[] args) {
        Model model = RDFModel.readRDFModel("data/vc-db-1.rdf");
        try {
            RDFModel.writeModelToXML(model);
    
            // executeSPARQLQuery(model, "SELECT ?x ?fname\r\n" + //
            //         "WHERE {?x  <http://www.w3.org/2001/vcard-rdf/3.0#FN>  ?fname}");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            model.close();
        }
        
    }
}
