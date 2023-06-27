import javax.naming.spi.DirStateFactory.Result;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
public class SPARQL {
    /**
     * Function for testing usage of SPARQL queries in Java Jena API.
     *
     * @param model Model
     * @param queryStr String
     * @return -
    */
    public static void executeSPARQLQuery(Model model, String queryStr) throws QueryException{
        // Creating a dataset 
        Dataset dataset = DatasetFactory.create() ;
        dataset.setDefaultModel(model) ;
        // Create a Query object from the SPARQL query string
        Query query = QueryFactory.create(queryStr);
        // Create a QueryExecution object with the query and the model
        QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
        try {
            // Execute the query and obtain the result set
            ResultSet rs = qexec.execSelect();
            
            // Process and print the results
            while (rs.hasNext()) {
                QuerySolution soln = rs.nextSolution();
                RDFNode x = soln.get("x") ;       // Get a result variable by name.
                RDFNode fname = soln.get("fname") ; // Get a result variable by
                if (x != null) {
                    System.out.println(x.toString());
                    System.out.println(fname.toString());
                    // System.out.println("Subject: "+ x + "\nResourse: " + r + "\nLiteral: " + l);
                }
            
            }
            // Use ResultSetFormatter to print out the fomatted resultset
            // ResultSetFormatter.out(System.out, rs, query);
            
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
            //Query strings
            String q1 = "SELECT ?x ?fname\r\n" + //
                             "WHERE {?x  <http://www.w3.org/2001/vcard-rdf/3.0#FN>  ?fname}";
            
            
            
            String q2 = "PREFIX vcard:      <http://www.w3.org/2001/vcard-rdf/3.0#>\r\n" + //
                            "SELECT ?y ?givenName \r\n" + //
                            "WHERE \r\n" + //
                            "{ ?y vcard:Family 'Smith' . \r\n" + //
                            "?y vcard:Given  ?givenName . \r\n" + //
                            "}";
            // _:. This isn’t the internal label for the blank node - it is ARQ printing them out that assigned the _:b0, _:b1 to show when two blank nodes are the same. 

            executeSPARQLQuery(model, q1);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            model.close();
        }
        
    }
}
