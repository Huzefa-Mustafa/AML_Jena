import java.io.IOException;
import java.io.InputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.VCARD;
import org.apache.jena.query.*;
import com.google.protobuf.Field;


public class App {
    App(){}

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

    /**
     * Reading an .rdf file.
     *
     * @param path String
     * @return model of Model class
    */
    public static Model readModel(String path) {
        
        Model model = ModelFactory.createDefaultModel();

        // use the RDFDataMgr to find the input file
        InputStream in = RDFDataMgr.open( path );
        if (in == null) {
            throw new IllegalArgumentException("File: " + path + " not found");
        }
        model.read(in,null);

        // printStatementsFromModel(model);
        return model;
    }

    /**
     * To create a default graph, or model. with given parameters
     * 
     * @param personURI String, 
     * @param givenName String,
     * @param familyName String
     *    
     * @return model Model
    */
    public static Model createModel(final String personURI, String givenName, String familyName) {
        String fullName     = givenName + " " + familyName;
        // create an empty Model
        Model model = ModelFactory.createDefaultModel();
        //   and add the properties cascading style
        //   creating default model in with specified properties 
        Resource res = model.createResource(personURI)
                                    // add the property
                                    .addProperty(VCARD.FN, fullName)
                                    .addProperty(VCARD.N, 
                                        model.createResource()
                                                .addProperty(VCARD.Given,givenName)
                                                .addProperty(VCARD.Family, familyName));

        // johnSmith.addProperty(VCARD.FN, fullName);
        
        // printStatementsFromModel(model);

        
        return model;
    }
    /**
     * To print statements from the model
     * 
     * @param model Model, 
     *    
     * @return model Model
    */
    public static void printStatementsFromModel(Model model){
        // check the statement in model and print them
        // list the statements in the Model
        StmtIterator iter = model.listStatements();
        // print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();      // get next statement
            Resource subject = stmt.getSubject();       // get the subject
            Resource predicate = stmt.getPredicate();   // get the predicate
            RDFNode  object = stmt.getObject();         // get the object

            if (object.isResource()) {
                Resource resource = object.asResource();
                System.out.println("Subject: "+ subject + "\nPredicate: " + predicate + "\nResource: " + resource);
            } else {
                // Handle literal value
                System.out.println("Subject: "+ subject + "\nPredicate: " + predicate + "\nLiteral:" + object.asLiteral().getValue());
            }
            System.out.println("###################################################");
          
        }
    }
    /**
     * Adding add property to model object
     * 
     * @param model Model, 
     * @param p Property,
     * @param s String,
     * @param uri String
    */
    public static void addResourceToModel(Model model,Property p , String s, String uri){
        Resource res = model.getResource(uri);
        res.addProperty(p,s);

    }
    /**
     * To write the model in RDF/XML format
     * 
     * @param model Model, 
     *    
     * @return model Model
    */
    public static void writeModelToXML(Model model) throws Exception {
        try {
            // model.write(System.out);
            // now write the model in a pretty form
            // not suitable for writing very large Models
            RDFDataMgr.write(System.out, model, Lang.RDFXML);

            //To write large files and preserve blank nodes, write in N-Triples format:
            // now write the model in N-TRIPLES form
            // RDFDataMgr.write(System.out, model, Lang.NTRIPLES);
            
        } catch (Exception e) {
            System.out.println(e);
            
        }
    }


    public static void main(String[] args) throws Exception {
        // writeModelToXML(createModel());
        Model dataModel = readModel("data/vc-db-1.rdf");
        
        Model newModel = createModel("http://somewhere/HuzefaMustafa", "Huzefa", "Mustafa");
            
            // merge the Models
        Model model = dataModel.union(newModel);
        try {
            // writeModelToXML(model);
            executeSPARQLQuery(model, "SELECT ?x ?fname\r\n" + //
                    "WHERE {?x  <http://www.w3.org/2001/vcard-rdf/3.0#FN>  ?fname}");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            model.close();
        }
        
    }
    
}
