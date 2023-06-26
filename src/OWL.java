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
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.util.iterator.ExtendedIterator;
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

        // create the reasoning model using the base
        OntModel inf = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF, base );

        // create a dummy paper for this example
        OntClass paper = base.getOntClass( NS + "Paper" );
        Individual p1 = base.createIndividual( NS + "paper1", paper );
        
        // // list the asserted types
        // for (Iterator<Resource> i = p1.listRDFTypes(true); i.hasNext(); ) {
        //     SysteontM.out.println( p1.getURI() + " is asserted in class " + i.next() );
        // }
        // // list the inferred types
        // p1 = inf.getIndividual( NS + "paper1" );
        // for (Iterator<Resource> i = p1.listRDFTypes(true); i.hasNext(); ) {
        //     SysteontM.out.println( p1.getURI() + " is inferred to be in class " + i.next() );
        // }
        return base;
    }
    /**
     * To print statements from the model
     * 
     * @param model Model, 
     * @param uri String uri of the resource
     * @return model Model
    */
    public static void printSubClasses(OntModel model){
        // check the statement in model and print them
        // list the statements in the Model
        for ( Iterator<OntProperty> i= model.listAllOntProperties(); i.hasNext(); ) {
                OntProperty c = i.next();
                System.out.println(c.getURI());
                System.out.println("###################################################");

            }
    }
    public static void main(String[] args) {
        // Model model = RDFModel.readRDFModel("data/vc-db-1.rdf");
        try { 
            // Load and parse the OWL ontology file
            String path = "src/data/eswc-2006-09-21.rdf";
            // create the base model
            String SOURCE = "http://www.eswc2006.org/technologies/ontology";
            String NS = SOURCE + "#";
            OntModel ontM = readOntModel(path, SOURCE, NS);
            // RDFModel.writeModelToXML(ontM);

            OntClass cls = ontM.getOntClass(NS + "Artefact");
            Resource r = ontM.getResource( NS + "Artefact" );
            
            // RDFModel.printPropertiesFromResourse(r, SOURCE);
            
            OntClass programme = ontM.createClass( NS + "Programme" );
            OntClass orgEvent = ontM.createClass( NS + "OrganizedEvent" );

            ObjectProperty hasProgramme = ontM.createObjectProperty( NS + "hasProgramme" );

            hasProgramme.addDomain( orgEvent );
            hasProgramme.addRange( programme );
            hasProgramme.addLabel( "has programme", "en" );


            DatatypeProperty subDeadline = ontM.getDatatypeProperty( NS + "hasSubmissionDeadline" );
            DatatypeProperty deadline = ontM.createDatatypeProperty( NS + "deadline" );
            deadline.addDomain( ontM.getOntClass( NS + "Call" ) );
            deadline.addRange( XSD.dateTime );

            deadline.addSubProperty( subDeadline );
            // printSubClasses(ontM);
            // SysteontM.out.println( paper );
            // // Perform reasoning and inference
            Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
            reasoner = reasoner.bindSchema(ontM);
            InfModel infModel = ModelFactory.createInfModel(reasoner, ontM);
            Resource a = infModel.getResource(NS + "Artefact");
            System.out.println("Statement: " + a.getProperty(subDeadline));
            // Perform consistency checking
            boolean isConsistent = infModel.validate().isValid();
            
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
