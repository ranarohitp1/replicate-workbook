package replicate.tabworkbook.service;
import java.util.HashMap;
import java.util.List;

import replicate.tabworkbook.model.Datasource;
import replicate.tabworkbook.model.Worksheet;

public interface WorkbookService {

	Datasource datasource();
	List<Worksheet> worksheets();
	HashMap<String, HashMap<String, String>> getColorFieldMap();
}
