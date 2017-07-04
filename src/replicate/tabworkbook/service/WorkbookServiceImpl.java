package replicate.tabworkbook.service;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import replicate.tabworkbook.model.Datasource;
import replicate.tabworkbook.model.Worksheet;

public class WorkbookServiceImpl implements WorkbookService {

	Datasource datsources = null;
	List<Worksheet> worksheets = null;
	
	public WorkbookServiceImpl()
	{

		try
		{	
			String xmlFilePath = "D:\\Users\\rohit.ra\\Rohit\\Documents\\Test1.xml";

			// We may need different approach for this, below code is for replacing &quot; to get values.
			Path path = Paths.get(xmlFilePath);
			Charset charset = StandardCharsets.UTF_8;
			String content = new String(Files.readAllBytes(path), charset);			 
			content = content.replaceAll( "&quot;", "");

			Pattern pattern = Pattern.compile("&quot;");
			Matcher matcher = pattern.matcher(content);
			while (matcher.find()) {
				content = content.replaceAll(matcher.group(1), matcher.group(1).replace("&quot;", ""));
			}

			// code ends here.

			File inputFile = new File(xmlFilePath);

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			UserHandler userhandler = new UserHandler();
			saxParser.parse(inputFile, userhandler);     

			worksheets= userhandler.worksheetL;
			datsources = userhandler.datsource;

			//			System.out.println("Size "+ datsource.getColorInfoMap().size());

			//			Iterator it = datsource.getColorInfoMap().entrySet().iterator();
			//			while (it.hasNext()) {
			//				Map.Entry pair = (Map.Entry)it.next();
			////				System.out.println(pair.getKey() + " = " + pair.getValue());
			//				it.remove(); // avoids a ConcurrentModificationException
			//			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Datasource datasource() {
		// TODO Auto-generated method stub
		return datsources;
	}

	@Override
	public List<Worksheet> worksheets() {
		// TODO Auto-generated method stub
		return worksheets;
	}

	@Override
	public HashMap<String, HashMap<String, String>> getColorFieldMap() {
		// TODO Auto-generated method stub
		Iterator it1 = datsources.getColorFieldMap().entrySet().iterator();
		while (it1.hasNext()) {
			Map.Entry pair = (Map.Entry)it1.next();
			System.out.println( " Key = " +pair.getKey() + "= size" + pair.getValue().toString());
			Map objMap = (Map)pair.getValue();
			Iterator it2 =  objMap.entrySet().iterator();
			/*while (it2.hasNext()) {
					Map.Entry pair2 = (Map.Entry)it2.next();
					System.out.println(pair2.getKey() + " = Color" + pair2.getValue());
					it2.remove(); // avoids a ConcurrentModificationException
				}*/

			it1.remove(); // avoids a ConcurrentModificationException
		}
		
		return datsources.getColorFieldMap();
	}

}
