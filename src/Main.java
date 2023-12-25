import java.io.*;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;
import org.xmldb.api.*;
import org.apache.xindice.xml.dom.*;
import org.apache.xindice.client.xmldb.services.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


// import org.apache.xindice.client.xmldb.XindiceDatabaseManager;
// import org.xmldb.api.base.Collection;
// import org.xmldb.api.base.Database;
// import org.xmldb.api.base.XMLDBException;

public class Main {

    public static void main(String[] args) throws Exception {
        String driver = "org.apache.xindice.client.xmldb.DatabaseImpl";
        Class c = Class.forName(driver);
        Database db = (Database) c.newInstance();
        DatabaseManager.registerDatabase(db);
        Collection col = null;
		//Collection col2 = null;
		
        String collectionName = "Shverdiakov_collection";

        try {
            Collection init_col = DatabaseManager.getCollection("xmldb:xindice:///db/");

            CollectionManager service =
                    (CollectionManager) init_col.getService("CollectionManager", "1.0");

            // Build up the Collection XML configuration.
            String collectionConfig =
                    "<collection compressed=\"true\" " +
                            "            name=\"" + collectionName + "\">" +
                            "   <filer class=\"org.apache.xindice.core.filer.BTreeFiler\"/>" +
                            "</collection>";

            col = service.createCollection(collectionName, DOMParser.toDocument(collectionConfig));
			//col2 = service.createCollection(collectionName, DOMParser.toDocument(collectionConfig));
            System.out.println("Collection " + collectionName + " created.");
            init_col.close();
        }
        catch (XMLDBException e) {
         System.err.println("XML:DB Exception occured " + e.errorCode + " " +
            e.getMessage());
		}
        try {
            col = DatabaseManager.getCollection("xmldb:xindice:///db/"+collectionName);

            String data = readFileFromDisk(".\\files\\books.xml");
			// String data = readFileFromDisk("C:\\DB\\Shverdiakov_9\\files\\books.xml");
            XMLResource document = (XMLResource) col.createResource(null, "XMLResource");
            document.setContent(data);
            col.storeResource(document);

            System.out.println("XML");
            String[] listResources = col.listResources();
            for (String id: listResources )
            {
                Resource resourse = col.getResource(id);
                System.out.println(resourse.getContent());
            }
        }
		catch (XMLDBException e) {
         System.err.println("XML:DB Exception occured " + e.errorCode + " " +
            e.getMessage());
		}
		/*try {
            col2 = DatabaseManager.getCollection("xmldb:xindice:///db/"+collectionName);

            String dataJson = readFileFromDisk("C:\\DB\\shverdiakov_lab4_p2\\files\\books2.json");
            XMLResource csvDocument = (XMLResource) col2.createResource(null, "XMLResource");
            csvDocument.setContent(dataJson);
            col2.storeResource(csvDocument);
	
            System.out.println("JSON");
            String[] csvListResources = col2.listResources();
            for (String id: csvListResources )
            {
                Resource csvResourse = col2.getResource(id);
                System.out.println(csvResourse.getContent());
            }
        }
		catch (XMLDBException e) {
         System.err.println("XML:DB Exception occured " + e.errorCode + " " +
            e.getMessage());
		}*/
		/*try {
			String csvFile = "files/books.csv";

			BufferedReader br = new BufferedReader(new FileReader(csvFile));
			String line;
			while ((line = br.readLine()) != null) {
				// Process each line of the CSV file
				String[] data = line.split(",");
				// Perform operations on the data
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}*/
        finally {
            if (col != null) {
                col.close();
            }
        }
    }

    public static String readFileFromDisk(String fileName) throws Exception {
        File file = new File(fileName);
        FileInputStream insr = new FileInputStream(file);
        byte[] fileBuffer = new byte[(int)file.length()];
        insr.read(fileBuffer);
        insr.close();
        return new String(fileBuffer);
    }
}