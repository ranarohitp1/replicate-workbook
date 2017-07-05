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

	private Datasource datsource;
	private Panes pane;
	private Worksheet worksheet;

	private boolean bMarks = false;
	private boolean bConnection = false;
	private boolean bColormap = false;
	private String name = null;
	private boolean bParmater = false;
	private  List<Worksheet> worksheetL  = new ArrayList<Worksheet>();

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
				System.out.println("Parameter " + parmList);
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
		else if (qName.equalsIgnoreCase("mark")) {
			pane.setChartTyp(attributes.getValue(XmlConstants._CLASS)!=null?attributes.getValue(XmlConstants._CLASS):"NA");
		}
		else if (qName.equalsIgnoreCase(XmlConstants.COLOR)) {
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
			
			System.out.println("field name " + field);
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
		}
		else if (qName.equalsIgnoreCase(XmlConstants.ENCODING) && (bColormap)) {
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

	public Datasource getDatsource() {
		return datsource;
	}

	public void setDatsource(Datasource datsource) {
		this.datsource = datsource;
	}

	public Panes getPane() {
		return pane;
	}

	public void setPane(Panes pane) {
		this.pane = pane;
	}

	public Worksheet getWorksheet() {
		return worksheet;
	}

	public void setWorksheet(Worksheet worksheet) {
		this.worksheet = worksheet;
	}

	public boolean isbMarks() {
		return bMarks;
	}

	public void setbMarks(boolean bMarks) {
		this.bMarks = bMarks;
	}

	public boolean isbConnection() {
		return bConnection;
	}

	public void setbConnection(boolean bConnection) {
		this.bConnection = bConnection;
	}

	public boolean isbColormap() {
		return bColormap;
	}

	public void setbColormap(boolean bColormap) {
		this.bColormap = bColormap;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isbParmater() {
		return bParmater;
	}

	public void setbParmater(boolean bParmater) {
		this.bParmater = bParmater;
	}

	public List<Worksheet> getWorksheetL() {
		return worksheetL;
	}

	public void setWorksheetL(List<Worksheet> worksheetL) {
		this.worksheetL = worksheetL;
	}

	
}
