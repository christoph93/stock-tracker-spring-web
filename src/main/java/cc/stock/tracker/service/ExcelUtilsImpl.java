package cc.stock.tracker.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.el.parser.ParseException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.stock.tracker.document.Transaction;
import cc.stock.tracker.repository.TransactionRepository;

@Service
public class ExcelUtilsImpl implements ExcelUtils {
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	
	public List<Transaction> saveTransactionsToMongo(String path) {
		try {
			transactionRepository.deleteAll();
			transactionRepository.saveAll(readTransactionsExcel(path));
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return transactionRepository.findAll();
		
	}
	

	private ArrayList<Transaction> readTransactionsExcel(String path) throws NumberFormatException, ParseException {
		ArrayList<String[]> table;
		String line = "";
		ArrayList<Transaction> transactions = new ArrayList<>();

//		System.out.println("Calling readFile");

		table = transactionTableAsArrayList(path);
		/*
		 * for(String[] row : table){ line = ""; for(String s : row){ line += s + " "; }
		 * System.out.println(line); }
		 */

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);

		// table.get(i)[0].trim()

		for (int i = 1; i < table.size(); i++) {
			try {

				transactions.add(new Transaction(formatter.parse(table.get(i)[0].trim()), table.get(i)[2].trim(),
						table.get(i)[5].trim(), table.get(i)[6].trim(), Double.parseDouble(table.get(i)[7]),
						Double.parseDouble(table.get(i)[8]), Double.parseDouble(table.get(i)[9]),
						Date.from(Instant.now())));

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return transactions;

	}

	private ArrayList<String[]> transactionTableAsArrayList(String filePath) {

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
//						System.out.println("Found beginning of table!");
						tableStartColIndex = currentCell.getColumnIndex();
						tableEndColIndex = tableStartColIndex + 9;
						foundTable = true;
					}

					line += cellVal + " ";

					if (foundTable && currentCell.getColumnIndex() == tableStartColIndex && cellVal.equals("--#")) {
//						System.out.println("Found end of table!");
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
