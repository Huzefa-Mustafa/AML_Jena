import java.io.IOException;

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

import com.google.protobuf.Field;


public class App {
    App(){
        Model model = ModelFactory.createDefaultModel();
    }
    /**
     * Reading an .rdf file.
     *
     * @param path String
     * @return --
    */
    public static void readModel(String path) {
        
        Model model = ModelFactory.createDefaultModel();

        String rdfFile = path;
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
        
        // model.close();
    }

    /**
     * To create a default graph, or model with default properties
     * 
     * 
     * @return model Model
    */
    public static Model createModel() {
        final String personURI = "http://somewhere/JohnSmith";
        String givenName    = "John";
        String familyName   = "Smith";
        String fullName     = givenName + " " + familyName;
        // create an empty Model
        Model model = ModelFactory.createDefaultModel();
        //   and add the properties cascading style
        //   creating default model in with specified properties 
        Resource johnSmith = model.createResource(personURI)
                                    // add the property
                                    .addProperty(VCARD.FN, fullName)
                                    .addProperty(VCARD.N, 
                                        model.createResource()
                                                .addProperty(VCARD.Given,givenName)
                                                .addProperty(VCARD.Family, familyName));

        // johnSmith.addProperty(VCARD.FN, fullName);
        


        
        return model;
    }
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
    }
    public static void addResourceToModel(Model model,Property p , String s, String uri){
        Resource res = model.getResource(uri);
        res.addProperty(p,s);

    }
    public static void writeModelToXML(Model model) throws Exception {
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
        // writeModelToXML(createModel());
        Model myModel = createModel();
        Property pro = VCARD.EMAIL;
        String email = "johnsmith@gmail.com";
        printStatementsFromModel(myModel);
        System.out.println("####################################################");
        addResourceToModel(myModel, pro, email, "http://somewhere/JohnSmith");
        printStatementsFromModel(myModel);
        System.out.println("####################################################");
    }
    
}
