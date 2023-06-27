import java.io.InputStream;
import java.util.Iterator;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.reasoner.ValidityReport.Report;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.util.PrintUtil;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.ReasonerVocabulary;
import org.apache.jena.vocabulary.XSD;

public class OWL {




    public static OntModel readOntModel(String path, String SOURCE, String NS) {
        
        OntModel base = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
        InputStream in = RDFDataMgr.open( path );
        if (in == null) {
            throw new IllegalArgumentException("File: " + path + " not found");
        }
        base.read( in, "RDF/XML" );

        return base;
    }
    
    /**
     * Perform reasoning on the data using the provided schema.
     * 
     * @param schema Model representing the OWL schema
     * @param data Model representing the data
     */
    public static void performReasoning(OntModel model){
        // Load the schema and data models
        Model schema = RDFDataMgr.loadModel("file:src/data/owlDemoSchema.rdf");
        Model data;
        // Create and bind the reasoner to the schema
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        if (model != null) {
            reasoner = reasoner.bindSchema(model);
            data = model;
        }else{
            reasoner = reasoner.bindSchema(schema);
            data = RDFDataMgr.loadModel("file:src/data/owlDemoData.rdf");
        }
        
        // Create an inference model using the reasoner and data
        InfModel infmodel = ModelFactory.createInfModel(reasoner, data);
        
        // Get and print statements related to the resource "nForce"
        Resource nForce = infmodel.getResource("urn:x-hp:eg/nForce");
        System.out.println("nForce *:");
        printStatements(infmodel, nForce, null, null);
        
        // Check if the resource "whiteBox" is recognized as a gaming computer
        Resource gamingComputer = infmodel.getResource("urn:x-hp:eg/GamingComputer");
        Resource whiteBox = infmodel.getResource("urn:x-hp:eg/whiteBoxZX");
        if (infmodel.contains(whiteBox, RDF.type, gamingComputer)) {
            System.out.println("White box recognized as gaming computer");
        } else {
            System.out.println("Failed to recognize white box correctly");
        }
        
        // Check the validity of the inference model
        // checkValidity(infmodel);
        
    }
    /**
     * Check the validity of the inferred model.
     * 
     * @param infmodel InfModel to be checked for validity
     */
    public static void checkValidity(InfModel infmodel){
        // Validate the inference model
        ValidityReport validity = infmodel.validate();
        if (validity.isValid()) {
            System.out.println("OK");
        } else {
            System.out.println("Conflicts");
            // Print the validity report
            for (Iterator<Report> i = validity.getReports(); i.hasNext(); ) {
                ValidityReport.Report report = (ValidityReport.Report)i.next();
                System.out.println(" - " + report);
            }
        }
    }
    /**
     * To print statements from the model
     * 
     * @param m Model to be printed
     * @param s Resource subject of the statement (can be null for any)
     * @param p Property predicate of the statement (can be null for any)
     * @param o Resource object of the statement (can be null for any)
     */
    public static void printStatements(Model m, Resource s, Property p, Resource o) {
        // Iterate over the statements in the model and print them
        for (StmtIterator i = m.listStatements(s,p,o); i.hasNext(); ) { 
            Statement stmt = i.nextStatement(); 
            System.out.println(" - " + PrintUtil.print(stmt)); 
        } 
    }
    /**
     * To print subclasses of the given class
     * 
     * @param ontClass OntClass to print
     * 
    */
    public static void printSubClasses(OntClass ontClass){
        for ( Iterator<OntClass> i= ontClass.listSubClasses(); i.hasNext(); ) {
                OntClass c = i.next();
                System.out.println(c.getURI());
                System.out.println("###################################################");
    
            }
    }
    public static void main(String[] args) {
        
        try { 
            
            // // Load and parse the OWL ontology file
            // String path = "src/data/eswc-2006-09-21.rdf";
            // String SOURCE = "http://www.eswc2006.org/technologies/ontology";
            // String NS = SOURCE + "#";
            // OntModel ontM = readOntModel(path, SOURCE, NS);

            // // Create an OntClass for "Artefact"
            // OntClass artefact = ontM.getOntClass(NS + "Artefact");
            // // printStatements(ontM, artefact, null, null);
            // // Create a new OntClass for "Programme"
            // OntClass programme = ontM.createClass(NS + "Programme");

            // // Create a new OntClass for "OrganizedEvent"
            // OntClass orgEvent = ontM.createClass(NS + "OrganizedEvent");

            // // Create an ObjectProperty "hasProgramme"
            // ObjectProperty hasProgramme = ontM.createObjectProperty(NS + "hasProgramme");
            // hasProgramme.addDomain(orgEvent);
            // hasProgramme.addRange(programme);
            // hasProgramme.addLabel("has programme", "en");
            // // printStatements(ontM, hasProgramme, null, null);
            
            
            
            // // Create DatatypeProperties for deadlines
            // DatatypeProperty subDeadline = ontM.getDatatypeProperty(NS + "hasSubmissionDeadline");
            // DatatypeProperty notifyDeadline = ontM.getDatatypeProperty(NS + "hasNotificationDeadline");
            // DatatypeProperty cameraDeadline = ontM.getDatatypeProperty(NS + "hasCameraReadyDeadline");

            // // Create a DatatypeProperty "deadline" with domain "Call" and range XSD.dateTime
            // DatatypeProperty deadline = ontM.createDatatypeProperty(NS + "deadline");
            // deadline.addDomain(ontM.getOntClass(NS + "Call"));
            // deadline.addRange(XSD.dateTime);

            // // // Connect the sub-properties to the main property "deadline"
            // deadline.addSubProperty(subDeadline);
            // deadline.addSubProperty(notifyDeadline);
            // deadline.addSubProperty(cameraDeadline);
            // printStatements(ontM, subDeadline, null, null);
            
            // // Perform reasoning and inference using an OWLReasoner
            // Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
            // reasoner = reasoner.bindSchema(ontM);
            // InfModel infModel = ModelFactory.createInfModel(reasoner, ontM);

            // // Retrieve a resource and print its statements
            // Resource a = infModel.getResource(NS + "hasSubmissionDeadline");
            // // printStatements(infModel, a, null, null);
            // checkValidity(infModel);
            // // Perform consistency checking
            
            // Example 2 for testing reasoner
            // performReasoning(null);
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
