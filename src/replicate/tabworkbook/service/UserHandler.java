package replicate.tabworkbook.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import replicate.tabworkbook.model.Datasource;
import replicate.tabworkbook.model.Panes;
import replicate.tabworkbook.model.Worksheet;

public class UserHandler extends DefaultHandler {

	public Datasource datsource;
	Panes pane;
	Worksheet worksheet;

	boolean bMarks = false;
	boolean bConnection = false;
	boolean bColormap = false;
	String name = null;
	boolean bParmater = false;
	public List<Worksheet> worksheetL  = new ArrayList<Worksheet>();
	XmlConstants constants = new XmlConstants();

	@Override
	public void startElement(String uri, 
			String localName, String qName, Attributes attributes)
					throws SAXException {
		if (qName.equalsIgnoreCase(constants.DATASOURCE)) {
			String isParameter = attributes.getValue(constants.HASCONNECTION)!=null?attributes.getValue(constants.HASCONNECTION):"NA";
			if(isParameter.equalsIgnoreCase("false")){
				bParmater = true;
				System.out.println("Parameter is true");
			}
		}
		else if (qName.equalsIgnoreCase(constants.CONNECTION)) {
			datsource = new Datasource();
			String authentication = attributes.getValue(constants.AUTHENTICATION)!=null?attributes.getValue(constants.AUTHENTICATION):"NA";
			String server = attributes.getValue(constants.SERVER)!=null?attributes.getValue(constants.SERVER):"NA";
			String dbName = attributes.getValue(constants.DBNAME)!=null?attributes.getValue(constants.DBNAME):"NA";
			String username = attributes.getValue(constants.USERNAME)!=null?attributes.getValue(constants.USERNAME):"NA";

			datsource.setAuthentication(authentication);
			datsource.setServer(server);
			datsource.setDbName(dbName);
			datsource.setUsername(username);
			//			bConnection = true;
		} else if (qName.equalsIgnoreCase(constants.DATASOURCE) && attributes.getValue(constants.NAME).equalsIgnoreCase("Parameters") ) {
			name = attributes.getValue(constants.NAME)!=null?attributes.getValue(constants.NAME):"NA";
			System.out.println("Parameter name " + name);
		}
		else if (qName.equalsIgnoreCase("worksheet")) {
			worksheet = new Worksheet();
			worksheet.setWorksheetName(attributes.getValue(constants.NAME)!=null?attributes.getValue(constants.NAME):"NA");
		}
		else if (qName.equalsIgnoreCase(constants.RELATION)) {
			datsource.setTableName(attributes.getValue(constants.TABLE)!=null?attributes.getValue(constants.TABLE):"NA");
		}
		else if (qName.equalsIgnoreCase(constants.TABLE)) {
			if(bParmater )
			{
				List<String> parmList = new ArrayList<String>();
				parmList.add(attributes.getValue(constants.CAPTION)!=null?attributes.getValue(constants.CAPTION):"NA");
				parmList.add(attributes.getValue(constants.DATATYPE)!=null?attributes.getValue(constants.DATATYPE):"NA");
				parmList.add(attributes.getValue(constants.NAME)!=null?attributes.getValue(constants.NAME):"NA");
				parmList.add(attributes.getValue(constants.ROLE)!=null?attributes.getValue(constants.ROLE):"NA");
				parmList.add(attributes.getValue(constants.VALUE)!=null?attributes.getValue(constants.VALUE):"NA");
				System.out.println("Parameter " + parmList);
			}

		} else if (qName.equalsIgnoreCase(constants.PANE)) {
			pane = new Panes();
			pane.setId(attributes.getValue(constants.ID)!=null?attributes.getValue(constants.ID):"NA");
			pane.setY_axis_name(attributes.getValue(constants.Y_AXIS_NAME)!=null?attributes.getValue(constants.Y_AXIS_NAME):"NA");
			pane.setX_axis_name(attributes.getValue(constants.X_AXIS_NAME)!=null?attributes.getValue(constants.X_AXIS_NAME):"NA");
		}
		else if (qName.equalsIgnoreCase(constants.BREAKDOWN)) {
			pane.setBreakdown(attributes.getValue(constants.VALUE)!=null?attributes.getValue(constants.VALUE):"NA");
		}
		else if (qName.equalsIgnoreCase("mark")) {
			pane.setChartTyp(attributes.getValue(constants._CLASS)!=null?attributes.getValue(constants._CLASS):"NA");
		}
		else if (qName.equalsIgnoreCase(constants.COLOR)) {
			pane.setColumn(attributes.getValue(constants.COLUMN)!=null?attributes.getValue(constants.COLUMN):"NA");
		}
		else if (qName.equalsIgnoreCase(constants.MAP)) {
			datsource.setMapTo(attributes.getValue(constants.TO)!=null?attributes.getValue(constants.TO):"NA");
		}

		else if (qName.equalsIgnoreCase(constants.MAP)) {
			bMarks = true;
		}
		else if (qName.equalsIgnoreCase(constants.ENCODING) && attributes.getValue(constants.ATTR).equalsIgnoreCase("color") ) {
			String field = attributes.getValue(constants.FIELD)!=null?attributes.getValue(constants.FIELD):"NA";
			datsource.setFieldName(field);
			bColormap = true;
			datsource.colorInfoMap = new HashMap<String,String>();
			
			System.out.println("field name " + field);
		}

	}

	@Override
	public void endElement(String uri, 
			String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase(constants.CONNECTION)) {
			System.out.println("End Element :" + datsource.getAuthentication() + "--" + datsource.getDbName());
		}
		if (qName.equalsIgnoreCase(constants.DATASOURCE)) {
			bParmater = false;
		}
		else if (qName.equalsIgnoreCase(constants.PANE)) {
			worksheet.setPanes(pane);
		}
		else if (qName.equalsIgnoreCase(constants.BREAKDOWN)) {
		}
		else if (qName.equalsIgnoreCase(constants.MARK)) {
		}
		else if (qName.equalsIgnoreCase(constants.COLOR)) {
		}
		else if (qName.equalsIgnoreCase(constants.WORKSHEET)) {
			worksheetL.add(worksheet);
		}
		else if (qName.equalsIgnoreCase(constants.ENCODING) && (bColormap)) {
//			datsource.setColorInfoMap().put();
			datsource.setColorFieldMap(datsource.getFieldName(), datsource.getColorInfoMap());
			bColormap = false;
		}
	}

	@Override
	public void characters(char ch[], 
			int start, int length) throws SAXException {
		if (bMarks) {
//			System.out.println("Key: " + new String(ch, start, length) + "Value::" + datsource.getMapTo());
			//			String content = String.copyValueOf(ch, start, length).trim();
			//		    content = content.replace("&quot;", " ");
			//			System.out.println("Content: " + content);
			datsource.colorInfoMap.put(new String(ch, start, length),datsource.getMapTo());
			bMarks = false;
		}

		new String(ch, start, length);
	}
}
