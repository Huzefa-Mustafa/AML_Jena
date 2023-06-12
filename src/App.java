import java.io.IOException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.VCARD;


public class App {


    public void readModel() {
        
        Model model = ModelFactory.createDefaultModel();

        String rdfFile = "src/animals.rdf";
        model.read(rdfFile);

        StmtIterator iter = model.listStatements();

        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            Resource subject = stmt.getSubject();
            Resource predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();
            // System.out.println(subject + " " + predicate + " " + object);
            if (object.isResource()) {
                Resource resource = object.asResource();
                System.out.println(subject + " " + predicate + " " + resource);
            } else {
                // Handle literal value
                System.out.println(subject + " " + predicate + " " + object.asLiteral().getValue());
            }
        }
        
        model.close();
    }
/**
     * To create this graph, or model.
     *
     * @param --
     * @return --
    */
    public static Model createModel() {
        final String personURI = "http://somewhere/JohnSmith";
        String givenName    = "John";
        String familyName   = "Smith";
        String fullName     = givenName + " " + familyName;
        // create an empty Model
        Model model = ModelFactory.createDefaultModel();
        // create the resource
        // Resource johnSmith = model.createResource(personURI);
        // create the resource
        //   and add the properties cascading style
        Resource johnSmith = model.createResource(personURI)
                                    // add the property
                                    .addProperty(VCARD.FN, fullName)
                                    .addProperty(VCARD.N, 
                                        model.createResource()
                                                .addProperty(VCARD.Given,givenName)
                                                .addProperty(VCARD.Family, familyName));

        // johnSmith.addProperty(VCARD.FN, fullName);


        // check the statement in model and print them
        // list the statements in the Model
        StmtIterator iter = model.listStatements();
        // print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();      // get next statement
            Resource subject = stmt.getSubject();       // get the subject
            Resource predicate = stmt.getPredicate();   // get the predicate
            RDFNode  object = stmt.getObject();         // get the object

            System.out.print(subject.toString());
            System.out.print(" " + predicate.toString() + " ");
            if (object instanceof Resource) {
                System.out.print(object.toString());
             } else {
                 // object is a literal
                 System.out.print(" \"" + object.toString() + "\"");
            }
            System.out.println(" .");    
        }
        
        return model;
    }
    public static void writeModelToFile(Model model) throws Exception {
        try {
            // model.write(System.out);
            RDFDataMgr.write(System.out, model, Lang.RDFXML);
            model.close();
        } catch (Exception e) {
            System.out.println(e);
            model.close();
        }
    }
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        writeModelToFile(createModel());
    }
    
}
