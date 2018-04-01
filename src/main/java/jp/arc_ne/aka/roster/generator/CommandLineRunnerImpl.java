/**
 * 
 */
package jp.arc_ne.aka.roster.generator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jp.arc_ne.aka.roster.ApplicationException;
import jp.arc_ne.aka.roster.dto.RosterInfo;

/**
 * @author akira
 *
 */
@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

	private ExcelTemplateLoader loader = new ExcelTemplateLoader();

	private RosterCsvReader csvReader = new RosterCsvReader();

	private RosterExcelWriter xlsWriter = new RosterExcelWriter();

	private static final String TEMPLATE_XLS = "templete.xls";

	private static final String EXTENSION = "xls";

	private static final char CHAR_PERIOD = '.';
	private static final char CHAR_PATH_SEPARATOR1 = '\\';
	private static final char CHAR_PATH_SEPARATOR2 = '/';

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
	 */
	@Override
	public void run(String... args) throws Exception {

		try {
			checkArgs(args);
			execute(args);
		} catch (ApplicationException e) {
			System.err.println(e.getLocalizedMessage());
		}

	}

	private void checkArgs(String[] args) throws ApplicationException {
		if (args.length == 0 || args.length > 2) {
			throw new ApplicationException("パラメータが異常です。");
		}
	}

	private void execute(String[] args) throws ApplicationException {

		FileInputStream fis;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(args[0]);
			reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

			csvReader.setReader(reader);
			RosterInfo info = csvReader.execute();

			// リソース読み込み
			InputStream is = ClassLoader
					.getSystemResourceAsStream(TEMPLATE_XLS);

			// テンプレート読み込み
			Sheet sheet = loader.execute(is);

			//
			String outPath;
			if (args.length >= 2) {
				outPath = args[1];
			} else {
				outPath = acquireOutputFilePath(args[0], EXTENSION);
			}

			xlsWriter.setSheet(sheet);
			xlsWriter.setInfo(info);
			xlsWriter.setOutputPath(outPath);
			xlsWriter.execute();
			System.out.println("Excelファイルの作成が完了しました！");

		} catch (FileNotFoundException e) {
			throw new ApplicationException("ファイルが開けません：" + args[0], e);
		} catch (Exception e) {
			throw new ApplicationException("内部エラーが発生しました", e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				throw new ApplicationException("ファイルが開けません：" + args[0], e);
			}
		}
	}

	private String acquireOutputFilePath(String filePath, String extention) {

		int extPoint = acquireExtensionPosision(filePath);
		return filePath.substring(0, extPoint) + "." + extention;
	}

	private int acquireExtensionPosision(String filePath) {

		int lastPeriod = filePath.lastIndexOf(CHAR_PERIOD);
		int lastSeparator = Math.max(
				filePath.lastIndexOf(CHAR_PATH_SEPARATOR1),
				filePath.lastIndexOf(CHAR_PATH_SEPARATOR2));

		if (lastPeriod < lastSeparator || lastPeriod < 0) {
			return filePath.length();
		}
		return lastPeriod;
	}
}
