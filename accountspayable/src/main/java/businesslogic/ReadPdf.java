package businesslogic;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReadPdf {
	Connection connect=null; 
    public void ReadAndStore(Connection connect,File file) throws InvalidPasswordException, IOException {
    	int k=1;
    	String Invoiceno = null,Invoicedate=null,soldto=null, shipto=null,remitto=null;
    	String customerpo=null, totalinvoice=null, keyword=null;
    	
    	try (PDDocument document = PDDocument.load(file)) {

            document.getClass();

            if (!document.isEncrypted()) {
			
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);
                PDFTextStripper tStripper = new PDFTextStripper();
                String pdfFileInText = tStripper.getText(document);
                // split by whitespace
                String lines[] = pdfFileInText.split("\\r?\\n");
                for (int i=0;i<lines.length;i++) {
                	keyword=lines[i].trim().toUpperCase();
                	//data=lines[i+1];
                	switch(keyword)
                    {
                    case "INVOICENO":
                    	Invoiceno=lines[i+1];
                    	break;
                    case "INVOICEDATE":
                    	Invoicedate=lines[i+1];
                    	break;
                    case "SOLDTO":
                    	soldto=lines[i+1];
                    	break;
                    case "SHIPTO":
                    	shipto=lines[i+1];
                    	break;
                    case "REMITTO":
                    	remitto=lines[i+1];
                    	break;
                    case "CUSTOMERP.O.":
                    	customerpo=lines[i+1];
                    	break;
                    case "CREDIT":
                    	totalinvoice=lines[i+2];
                    	break;
                    case "TOTALINVOICE":
                    {	if(k==3)
                			{totalinvoice=lines[i+4];}
                		else
                			{totalinvoice=lines[i+3];}
                       	try{
                        	System.out.println();
                  			System.out.println();
                  	      	String query = " insert into INVOICE_DETAILS (Invoiceno, Invoicedate, Soldto, Shipto, Remitto, Customerpo, Totinvoice, Status)"
                   		        + " values (?, ?, ?, ?, ?, ?, ?, ?)";
                  	      	PreparedStatement preparedStmt = connect.prepareStatement(query);
                  	      	preparedStmt.setString (1, Invoiceno);
                  	      	preparedStmt.setString (2, Invoicedate);
                  	      	preparedStmt.setString (3, soldto);
                  	      	preparedStmt.setString (4, shipto);
                  	      	preparedStmt.setString (5, remitto);
                  	      	preparedStmt.setString (6, customerpo);
                  	      	preparedStmt.setString (7, totalinvoice);
                  	      	preparedStmt.setString (8, "NOTAPPROVED");
                  	       // execute the preparedstatement
                  	      	preparedStmt.execute();
                      	 }             
                    	catch(SQLException e){
                            System.out.println("EXCEPTION DURING READING AND STORING");
                         	  e.printStackTrace();
                           }
                			break;
                          }
                         default:
                        	 //System.out.println(" ");
                        	 break;
                    }
                }
            }
    	}
                catch(InvalidPasswordException e){
            		System.out.println("INVALID PASSWORD EXCEPTION");
            		e.printStackTrace();
            	}
            	catch(IOException e){
            		System.out.println("IOEXception occurred");
            		e.printStackTrace();
            	}
            	
            	
            }
        }
    	

       

