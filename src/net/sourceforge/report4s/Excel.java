package net.sourceforge.report4s;

import java.io.*;
import java.util.jar.*;
import org.apache.poi.xssf.usermodel.*;

/**
 * Utility class to generate the test report in Excel 2007 xls format.
 * @author Harmin Parra Rueda
 */
class Excel {

	/**
	 * Extract the Excel template from report4s.jar file.
	 * @param model The template to use :
	 * 			1 if the test doesn't have "test failed within success percentage" logs.
	 * 			2 if the test have "test failed within success percentage" logs.
	 */
	private static void extractExcelFromJAR(int template) {
		try {
			String classPath = System.getProperty("java.class.path");
			String[] pathElements = classPath.split(System.getProperty("path.separator"));
			String jar_file = null;
			String template_file = (template == 1) ? "Test report1.xlsx" : "Test report2.xlsx";
			for( String element : pathElements ) {
				if( element.endsWith(Report4s.jarfile) ) {
					jar_file = element;
					break;
				}
			}
			JarFile jar = new JarFile(jar_file);
			JarEntry file = jar.getJarEntry("resources/excel/" + template_file);
			File f = new File(Report4s.report_dir + File.separator + Report4s.report_excel);
			InputStream is = jar.getInputStream(file);   // get the input stream
			FileOutputStream fos = new FileOutputStream(f);
			while( is.available() > 0 )   // write contents of 'is' to 'fos'
				fos.write(is.read());
			fos.close();
			is.close();
			jar.close();
		} catch (Exception e) {
			System.err.println("Failed to extract Excel template from " + Report4s.jarfile);
			e.printStackTrace();
		}	
	}

	/**
	 * Generate the Excel report.
	 */
	static void generateExcelReport() {
		//Excel cell references
		int total_row = 7, total_column = 10;
		int suite_row = 21, suite_column = 1, time_column = 10, passed_column = 11, failed_column = 12,
				failed_pct_column = 13, skipped_column, conf_failed_column, conf_skipped_column;
		
		//Extract Excel template
		String workspace_dir = System.getProperty("user.dir");
		if( Metadata.tests_failed_pct == 0 ) {
			extractExcelFromJAR(1);
			skipped_column=13;
			conf_failed_column=14;
			conf_skipped_column=15;
		}
		else {
			extractExcelFromJAR(2);
			skipped_column=14;
			conf_failed_column=15;
			conf_skipped_column=16;
		}
		File file = new File(workspace_dir + File.separator + "report" + File.separator + Report4s.report_excel);

		//Write data to the Excel file
		try {
			FileInputStream in = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(in);
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			XSSFCellStyle style = workbook.createCellStyle();
			style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
			
			//Write the total aggregations
			float total = Metadata.tests_passed + Metadata.tests_failed + Metadata.tests_failed_pct + Metadata.tests_skipped;
			XSSFRow row = sheet.getRow(total_row++);
			XSSFCell cell = row.createCell(total_column);
			cell.setCellValue(Metadata.tests_passed);
			cell = row.createCell(total_column+1);
			cell.setCellValue(Math.round(100*Metadata.tests_passed/total) + "%");
			cell.setCellStyle(style);
			
			row = sheet.getRow(total_row++);
			cell = row.createCell(total_column);
			cell.setCellValue(Metadata.tests_failed);
			cell = row.createCell(total_column+1);
			cell.setCellValue(Math.round(100*Metadata.tests_failed/total) + "%");
			cell.setCellStyle(style);
			
			if( Metadata.tests_failed_pct > 0 ) {
				row = sheet.getRow(total_row++);
				cell = row.createCell(total_column);
				cell.setCellValue(Metadata.tests_failed_pct);
				cell = row.createCell(total_column+1);
				cell.setCellValue(Math.round(100*Metadata.tests_failed_pct/total) + "%");
				cell.setCellStyle(style);
			}
			
			row = sheet.getRow(total_row++);
			cell = row.createCell(total_column);
			cell.setCellValue(Metadata.tests_skipped);
			cell = row.createCell(total_column+1);
			cell.setCellValue(Math.round(100*Metadata.tests_skipped/total) + "%");
			cell.setCellStyle(style);
			
			//write suite details
			int suite_count = Metadata.size();
			for( int i = 0; i != suite_count; i++, suite_row++ ) {
				SuiteMetadata suite_md = Metadata.get(i);
				row = sheet.createRow(suite_row);
				cell = row.createCell(suite_column);
				cell.setCellValue(suite_md.name);
				cell = row.createCell(time_column);
				cell.setCellValue(suite_md.time.replace("seconds", "s"));
				cell.setCellStyle(style);
				cell = row.createCell(passed_column);
				cell.setCellValue(suite_md.tests_passed);
				cell = row.createCell(failed_column);
				cell.setCellValue(suite_md.tests_failed);
				if( Metadata.tests_failed_pct > 0 ) {
					cell = row.createCell(failed_pct_column);
					cell.setCellValue(suite_md.tests_failed_pct);
				}
				cell = row.createCell(skipped_column);
				cell.setCellValue(suite_md.tests_skipped);
				cell = row.createCell(conf_failed_column);
				cell.setCellValue(suite_md.conf_failed);
				cell = row.createCell(conf_skipped_column);
				cell.setCellValue(suite_md.conf_skipped);
			}			
			//Close resources
			in.close();
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
			workbook.close();
		}
		catch (Exception e) {
			System.err.println("Failed to generate Excel report");
		}
	}

}
