package replicate.tabworkbook.main;
import java.util.List;

import replicate.tabworkbook.model.Panes;
import replicate.tabworkbook.model.Worksheet;
import replicate.tabworkbook.service.WorkbookService;
import replicate.tabworkbook.service.WorkbookServiceImpl;

public class WorkbookServiceExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WorkbookService workbook =  new WorkbookServiceImpl();
		workbook.getColorFieldMap();
		List<Worksheet> worksheets = workbook.worksheets();

		for(int i=0;i<worksheets.size();i++)
		{
			System.out.println("Worksheet Name "+worksheets.get(i).getWorksheetName());

			List<Panes> paneL = worksheets.get(i).getPanes();

			for(int j=0;j<paneL.size();j++)
			{
				Panes pane = paneL.get(j);

				System.out.println("Worksheet ID "+pane.getId());
				System.out.println("Worksheet Chart "+pane.getChartTyp());
				System.out.println("Worksheet Column "+pane.getColumn());
				System.out.println("Worksheet "+pane.getY_axis_name());
			}

		}

	}

}
