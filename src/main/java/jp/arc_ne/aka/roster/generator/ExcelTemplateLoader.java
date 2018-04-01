/**
 * 
 */
package jp.arc_ne.aka.roster.generator;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * @author akira
 *
 */
public class ExcelTemplateLoader {

	public Sheet execute(InputStream in) throws InvalidFormatException, IOException {

		Workbook wb = WorkbookFactory.create(in);
		Sheet sheet = wb.getSheetAt(0);

		return sheet;
	}
}
