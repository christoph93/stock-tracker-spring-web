package cc.stock.tracker.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import cc.stock.tracker.document.Dividend;
import cc.stock.tracker.document.Transaction;
import cc.stock.tracker.repository.DividendRepository;
import cc.stock.tracker.repository.TransactionRepository;

@Service
public class ExcelUtilsImpl implements ExcelUtils {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private DividendRepository dividendRepository;

	public List<Transaction> saveTransactionsToMongo(String path) throws ParseException {

		List<Transaction> transactions;

		try {

			File excelFile = new File(path);

			// check if it's CEI excel or simple excel
			if (isCEIExcel(excelFile)) {
				System.out.println("################# isCEI ");
				transactions = parseCEIExcel(excelFile);
			} else {				
				transactions = parseSimpleExcel(excelFile);
				System.out.println("#################");
				System.out.println(transactions);
			}

			
			System.out.println("TRANSACTION REPO");
			transactionRepository.deleteAll();
			transactionRepository.saveAll(transactions);

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return transactionRepository.findAll();

	}

	public List<Dividend> saveDividendsToMongo(String path) {
		try {
			dividendRepository.deleteAll();
			dividendRepository.saveAll(readDividendsExcel(path));

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return dividendRepository.findAll();

	}

	private ArrayList<Dividend> readDividendsExcel(String path) {
		ArrayList<String[]> table;
		ArrayList<Dividend> dividends = new ArrayList<>();

		table = dividendTableAsArrayList(path);

//		Date payDate, String description, String symbol, double grossValue, double taxValue, double netValue

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

		for (int i = 1; i < table.size(); i++) {
			try {

				if (!table.get(i)[0].trim().equals("--")) {

					dividends.add(new Dividend(formatter.parse(table.get(i)[0].trim()), // payDate
							table.get(i)[1].trim(), // description
							table.get(i)[2].trim(), // symbol
							Double.parseDouble(table.get(i)[3].replace("R$", "").trim().replace(",", ".")), // gross
							Double.parseDouble(table.get(i)[4].replace("R$", "").trim().replace(",", ".")), // tax
							Double.parseDouble(table.get(i)[5].replace("R$", "").trim().replace(",", ".")) // net
					));
				}

			} catch (NumberFormatException | java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dividends;
	}

	private ArrayList<String[]> dividendTableAsArrayList(String filePath) {

		String cellVal;
		String line;
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
					line += cellVal + " ";

				}

				if (!line.trim().isEmpty()) {
					String[] row = line.split("#");
					rows.add(row);
				}
			}

			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return rows;

	}

	private ArrayList<Transaction> readTransactionsList(ArrayList<String[]> table)
			throws NumberFormatException, ParseException {
		ArrayList<Transaction> transactions = new ArrayList<>();

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);

		for (int i = 1; i < table.size(); i++) {
			try {

				transactions.add(new Transaction("5f6cc420d0e0e00073c901f0", formatter.parse(table.get(i)[0].trim()),
						table.get(i)[1].trim(), table.get(i)[3].trim(), table.get(i)[4].trim(),
						Double.parseDouble(table.get(i)[5]), Double.parseDouble(table.get(i)[6]),
						Double.parseDouble(table.get(i)[7]), Date.from(Instant.now())));

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println(transactions);
		return transactions;

	}

	private ArrayList<String[]> simpleTransactionTableAsArrayList(File excelFile) {

		String cellVal;
		String line;		
		int tableStartColIndex = 0;
		int tableEndColIndex = 7;
		ArrayList<String[]> rows = new ArrayList<>();

		try {
			FileInputStream excelInput = new FileInputStream(excelFile);
			Workbook workbook = new HSSFWorkbook(excelInput);
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

					line += cellVal + " ";

				}

				String[] row = line.split("#");
				rows.add(row);

			}

			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rows;

	}

	private ArrayList<String[]> ceiTransactionTableAsArrayList(File excelFile) {

		String cellVal;
		String line;
		boolean foundTable = false;
		int tableStartColIndex = -1;
		int tableEndColIndex = 999;
		ArrayList<String[]> rows = new ArrayList<>();

		try {
			FileInputStream excelInput = new FileInputStream(excelFile);
			Workbook workbook = new HSSFWorkbook(excelInput);
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

					if (cellVal.contains("Data Neg�cio")) {
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
					String[] row = line.replace("--#", "") .split("#");
					rows.add(row);
				}				
			}

			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rows;

	}

	private boolean isCEIExcel(File file) {

		FileInputStream excelFile;
		try {
			excelFile = new FileInputStream(file);
			Workbook workbook = new HSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = datatypeSheet.iterator();

			String cellVal = "";

			while (rowIterator.hasNext()) {
				Row currentRow = rowIterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();

				while (cellIterator.hasNext()) {
					Cell currentCell = cellIterator.next();

					switch (currentCell.getCellType()) {
					case STRING:
						cellVal = currentCell.getStringCellValue().toUpperCase();
						break;
					default:
						cellVal = " UNKNOWN";
						break;
					}
				}

				if (cellVal.contains("DATA")) {
					return false;
				} else {
					return true;
				}
			}
			
			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<Transaction> parseSimpleExcel(File file) throws NumberFormatException, ParseException {
		System.out.println("parseSimpleExcel");
		System.out.println(file.getAbsolutePath());
		return readTransactionsList(simpleTransactionTableAsArrayList(file));
	}

	public List<Transaction> parseCEIExcel(File file) throws NumberFormatException, ParseException {
		System.out.println("parseCEIExcel");
		System.out.println(file.getAbsolutePath());
		return readTransactionsList(ceiTransactionTableAsArrayList(file));
	}

}
