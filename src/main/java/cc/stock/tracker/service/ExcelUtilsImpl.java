package cc.stock.tracker.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

@Service
public class ExcelUtilsImpl implements ExcelUtils {

	public ArrayList<String[]> findTableInFile(String filePath) {

		String cellVal;
		String line;
		boolean foundTable = false;
		int tableStartColIndex = -1;
		int tableEndColIndex = 999;
		ArrayList<String[]> rows = new ArrayList<>();

		try {
			FileInputStream excelFile = new FileInputStream(new File(filePath));
			Workbook workbook = new HSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = datatypeSheet.iterator();

			while (rowIterator.hasNext()) {
				cellVal = "";
				line = "";

				Row currentRow = rowIterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();

				while (cellIterator.hasNext()) {
					Cell currentCell = cellIterator.next();

					if (currentCell.getColumnIndex() >= tableStartColIndex
							&& currentCell.getColumnIndex() <= tableEndColIndex) {

						switch (currentCell.getCellType()) {
						case BLANK:
							cellVal = "--#";
							break;
						case STRING:
							cellVal = currentCell.getStringCellValue() + "#";
							break;
						case NUMERIC:
							cellVal = String.valueOf(currentCell.getNumericCellValue()) + "#";
							break;
						default:
							cellVal = " UNKNOWN";
							break;
						}
					}

					if (cellVal.contains("Data Negócio")) {
						System.out.println("Found beginning of table!");
						tableStartColIndex = currentCell.getColumnIndex();
						tableEndColIndex = tableStartColIndex + 9;
						foundTable = true;
					}

					line += cellVal + " ";

					if (foundTable && currentCell.getColumnIndex() == tableStartColIndex && cellVal.equals("--#")) {
						System.out.println("Found end of table!");
						foundTable = false;
					}
				}

				if (foundTable) {
					String[] row = line.split("#");
					rows.add(row);
				}
				;
			}

			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return rows;

	}

}
