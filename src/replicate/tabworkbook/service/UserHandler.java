package replicate.tabworkbook.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import replicate.tabworkbook.model.Calculations;
import replicate.tabworkbook.model.Datasource;
import replicate.tabworkbook.model.Panes;
import replicate.tabworkbook.model.Worksheet;

public class UserHandler extends DefaultHandler {

	public Datasource datsource;
	Panes pane;
	Worksheet worksheet;
	Calculations calculations;
	
	private boolean bMarks = false;
	private boolean bConnection = false;
	private boolean bColormap = false;
	private String name = null;
	private boolean bParmater = false;
	private boolean bColumn = false;
	private boolean bWorksheet = false;
	private boolean bcolorPalette = false;
	
	public List<Worksheet> worksheetL  = new ArrayList<Worksheet>();
//	XmlConstants constants = new XmlConstants();

	@Override
	public void startElement(String uri, 
			String localName, String qName, Attributes attributes)
					throws SAXException {
		if (qName.equalsIgnoreCase(XmlConstants.DATASOURCE)) {
			String isParameter = attributes.getValue(XmlConstants.HASCONNECTION)!=null?attributes.getValue(XmlConstants.HASCONNECTION):"NA";
			if(isParameter.equalsIgnoreCase("false")){
				bParmater = true;
				System.out.println("Parameter is true");
			}
		}
		else if (qName.equalsIgnoreCase(XmlConstants.CONNECTION)) {
			datsource = new Datasource();
			String authentication = attributes.getValue(XmlConstants.AUTHENTICATION)!=null?attributes.getValue(XmlConstants.AUTHENTICATION):"NA";
			String server = attributes.getValue(XmlConstants.SERVER)!=null?attributes.getValue(XmlConstants.SERVER):"NA";
			String dbName = attributes.getValue(XmlConstants.DBNAME)!=null?attributes.getValue(XmlConstants.DBNAME):"NA";
			String username = attributes.getValue(XmlConstants.USERNAME)!=null?attributes.getValue(XmlConstants.USERNAME):"NA";

			datsource.setAuthentication(authentication);
			datsource.setServer(server);
			datsource.setDbName(dbName);
			datsource.setUsername(username);
			//			bConnection = true;
		} else if (qName.equalsIgnoreCase(XmlConstants.DATASOURCE) && attributes.getValue(XmlConstants.NAME).equalsIgnoreCase("Parameters") ) {
			name = attributes.getValue(XmlConstants.NAME)!=null?attributes.getValue(XmlConstants.NAME):"NA";
			System.out.println("Parameter name " + name);
		}
		else if (qName.equalsIgnoreCase("worksheet")) {
			worksheet = new Worksheet();
			worksheet.setWorksheetName(attributes.getValue(XmlConstants.NAME)!=null?attributes.getValue(XmlConstants.NAME):"NA");
			bWorksheet = true;
		}
		else if (qName.equalsIgnoreCase(XmlConstants.RELATION)) {
			datsource.setTableName(attributes.getValue(XmlConstants.TABLE)!=null?attributes.getValue(XmlConstants.TABLE):"NA");
		}
		else if (qName.equalsIgnoreCase(XmlConstants.TABLE)) {
			if(bParmater )
			{
				List<String> parmList = new ArrayList<String>();
				parmList.add(attributes.getValue(XmlConstants.CAPTION)!=null?attributes.getValue(XmlConstants.CAPTION):"NA");
				parmList.add(attributes.getValue(XmlConstants.DATATYPE)!=null?attributes.getValue(XmlConstants.DATATYPE):"NA");
				parmList.add(attributes.getValue(XmlConstants.NAME)!=null?attributes.getValue(XmlConstants.NAME):"NA");
				parmList.add(attributes.getValue(XmlConstants.ROLE)!=null?attributes.getValue(XmlConstants.ROLE):"NA");
				parmList.add(attributes.getValue(XmlConstants.VALUE)!=null?attributes.getValue(XmlConstants.VALUE):"NA");
//				System.out.println("Parameter " + parmList);
			}

		} else if (qName.equalsIgnoreCase(XmlConstants.PANE)) {
			pane = new Panes();
			pane.setId(attributes.getValue(XmlConstants.ID)!=null?attributes.getValue(XmlConstants.ID):"NA");
			pane.setY_axis_name(attributes.getValue(XmlConstants.Y_AXIS_NAME)!=null?attributes.getValue(XmlConstants.Y_AXIS_NAME):"NA");
			pane.setX_axis_name(attributes.getValue(XmlConstants.X_AXIS_NAME)!=null?attributes.getValue(XmlConstants.X_AXIS_NAME):"NA");
		}
		else if (qName.equalsIgnoreCase(XmlConstants.BREAKDOWN)) {
			pane.setBreakdown(attributes.getValue(XmlConstants.VALUE)!=null?attributes.getValue(XmlConstants.VALUE):"NA");
		}
		else if (qName.equalsIgnoreCase(XmlConstants.MARK)) {
			pane.setChartTyp(attributes.getValue(XmlConstants._CLASS)!=null?attributes.getValue(XmlConstants._CLASS):"NA");
		}
		else if (qName.equalsIgnoreCase(XmlConstants.COLOR_PALETTE)) {
//			pane.setColumn(attributes.getValue(constants.COLUMN)!=null?attributes.getValue(constants.COLUMN):"NA");
			bcolorPalette = true;
		}
		else if (qName.equalsIgnoreCase(XmlConstants.COLOR) && !bcolorPalette) {
			pane.setColumn(attributes.getValue(XmlConstants.COLUMN)!=null?attributes.getValue(XmlConstants.COLUMN):"NA");
		}
		else if (qName.equalsIgnoreCase(XmlConstants.MAP)) {
			datsource.setMapTo(attributes.getValue(XmlConstants.TO)!=null?attributes.getValue(XmlConstants.TO):"NA");
		}

		else if (qName.equalsIgnoreCase(XmlConstants.MAP)) {
			bMarks = true;
		}
		else if (qName.equalsIgnoreCase(XmlConstants.ENCODING) && attributes.getValue(XmlConstants.ATTR).equalsIgnoreCase("color") ) {
			String field = attributes.getValue(XmlConstants.FIELD)!=null?attributes.getValue(XmlConstants.FIELD):"NA";
			datsource.setFieldName(field);
			bColormap = true;
			datsource.colorInfoMap = new HashMap<String,String>();

			//			System.out.println("field name " + field);
		}

		else if (qName.equalsIgnoreCase(XmlConstants.COLUMN ) && bWorksheet) {
			bColumn = true;
			calculations = new Calculations();
			calculations.setRole(attributes.getValue(XmlConstants.ROLE));
			calculations.setDatatype(attributes.getValue(XmlConstants.DATATYPE));
			calculations.setName(attributes.getValue(XmlConstants.NAME));
			calculations.setCaption(attributes.getValue(XmlConstants.CAPTION));
			calculations.setType(attributes.getValue(XmlConstants.TYPE));
//			System.out.println("Caption name:: " + attributes.getValue("caption") + "Data Type :: " + attributes.getValue("datatype") + "name:: " + attributes.getValue("name"));
		}

		else if (qName.equalsIgnoreCase(XmlConstants.CALCULATION) && bColumn && bWorksheet) {
			bColumn = false;
			
			calculations.setCal_Class(attributes.getValue(XmlConstants._CLASS));
			calculations.setFormula(attributes.getValue(XmlConstants.FORMULA));
//			System.out.println("class name:: " + attributes.getValue("class") + "formula :: " +attributes.getValue("formula"));
			worksheet.setTableCalculations(calculations);

		}
	}

	@Override
	public void endElement(String uri, 
			String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase(XmlConstants.CONNECTION)) {
			System.out.println("End Element :" + datsource.getAuthentication() + "--" + datsource.getDbName());
		}
		if (qName.equalsIgnoreCase(XmlConstants.DATASOURCE)) {
			bParmater = false;
		}
		else if (qName.equalsIgnoreCase(XmlConstants.PANE)) {
			worksheet.setPanes(pane);
		}
		else if (qName.equalsIgnoreCase(XmlConstants.BREAKDOWN)) {
		}
		else if (qName.equalsIgnoreCase(XmlConstants.MARK)) {
		}
		else if (qName.equalsIgnoreCase(XmlConstants.COLOR)) {
		}
		else if (qName.equalsIgnoreCase(XmlConstants.WORKSHEET)) {
			worksheetL.add(worksheet);
			bWorksheet = false;
		}
		else if (qName.equalsIgnoreCase(XmlConstants.ENCODING) && (bColormap)) {
			//			datsource.setColorInfoMap().put();
			datsource.setColorFieldMap(datsource.getFieldName(), datsource.getColorInfoMap());
			bColormap = false; 
		}
		else if (qName.equalsIgnoreCase("color-palette ")) {
//			pane.setColumn(attributes.getValue(constants.COLUMN)!=null?attributes.getValue(constants.COLUMN):"NA");
			bcolorPalette = false;
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
