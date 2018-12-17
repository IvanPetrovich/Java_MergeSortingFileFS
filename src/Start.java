import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

public class Start {
	public enum sortingType {
		ASCENDING, DESCENDING
	};

	public enum dataType {
		INTEGER, STRING
	};

	static List<InputFileReader> listStreamReader = new ArrayList<InputFileReader>();
	public static sortingType sort = sortingType.ASCENDING;// по возрастанию
	public static dataType type = null;// тип данных
	static String outputFile = "";// выходной файл
	static FileWriter fwOut;


	void soting() throws IOException {

		fillDataToSort(type);
		List<InputFileReader> sorted = Sorting.SortFusion(listStreamReader);
		String lastStr = sorted.get(0).s;
		Integer lastInt = sorted.get(0).i;

		do {
			sorted = Sorting.SortFusion(listStreamReader);
			if (type == dataType.STRING) {
				if ((lastStr.compareTo(sorted.get(0).s) < 0 ^ sort == sortingType.DESCENDING)
						|| lastStr.equals(sorted.get(0).s)) {
					stringToFile(sorted.get(0).s, fwOut);
					lastStr = sorted.get(0).s;
				} else {
					System.out.println("Неотсортированный элемент будет пропущен: " + sorted.get(0).s);
				}
			} else {
				if ((lastInt < sorted.get(0).i ^ sort == sortingType.DESCENDING) || lastInt == sorted.get(0).i) {
					stringToFile(sorted.get(0).i.toString(), fwOut);
					lastInt = sorted.get(0).i;
				} else {
					System.out.println("Неотсортированный элемент будет пропущен: " + sorted.get(0).s);
				}
			}
			sorted.get(0).s = null;
			sorted.get(0).i = null;
		} while (fillDataToSort(type) > 0);

		System.out.println("Готово! Можно проверить файл \"" + outputFile + "\"");

	}

	boolean fillParameters(String[] args) {
		int argsLen = args.length;
		if (argsLen < 3) {
			System.out.println("Не хватает аргументов!\nПрочтите документацию!");
			return false;
		} else {
			int i = 2;// один аргумент и выходной файл
			if (parseParam(args[0])) {
				if (parseParam(args[1])) {
					outputFile = args[2];
					i = 3;
				} else {
					outputFile = args[1];
				}
			}
			
			if (type == null) {
				System.out.println("Не указан тип данных!\nПрочтите документацию!");
				return false;
			}

			if (!fileExist(outputFile)) {
				if (!createOutputFile(outputFile)) {
					return false;
				}
			}
			
			try {
				fwOut = new FileWriter(new File(outputFile));
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

			for (; i < argsLen; i++) {
				if (fileExist(args[i])) {
					try {
						FileInputStream fstream = new FileInputStream(args[i]);
						BufferedReader bufReader = new BufferedReader(new InputStreamReader(fstream));
						InputFileReader ifr = new InputFileReader(args[i], bufReader);
						listStreamReader.add(ifr);
					} catch (Exception e) {
						System.out.println("Ошибка: " + e.getLocalizedMessage());
						return false;
					}
				} else {
					System.out.println("Входной файл \"" + args[i] + "\" не найден!");
					return false;
				}
			}
		}
		return true;
	}

	static int fillDataToSort(dataType type) throws IOException {
		for (int i = 0; i < listStreamReader.size(); i++) {
			if (type == dataType.STRING) {
				if (listStreamReader.get(i).s == null) {
					if ((listStreamReader.get(i).s = listStreamReader.get(i).bufReader.readLine()) != null) {

					} else {
						listStreamReader.remove(i);
						i--;
					}
				}
			} else {
				if (listStreamReader.get(i).i == null) {
					if ((listStreamReader.get(i).s = listStreamReader.get(i).bufReader.readLine()) != null) {
						try {
							listStreamReader.get(i).i = Integer.parseInt(listStreamReader.get(i).s);
						} catch (Exception e) {
							System.out.println("Ошибка: " + e.getLocalizedMessage());
							i--;// повторно считываем из того же входного файла
						}
					} else {
						listStreamReader.remove(i);// дошли до конца одного из файлов, больше его не читаем
						i--;
					}
				}
			}
		}
		return listStreamReader.size();
	}

	static boolean stringToFile(String s, FileWriter fw) {
		String lineSeparator = System.getProperty("line.separator");
		try {
			fw.write(s + lineSeparator);
			fw.flush();
		} catch (Exception e) {
			System.out.println("Ошибка записи в файл: " + e.getLocalizedMessage());
			return false;
		}
		return true;
	}

	static void listToFile(List<String> l, FileWriter fw) {
		for (int i = 0; i < l.size(); i++) {
			stringToFile(l.get(i), fw);
		}
	}

	static ArrayList<String> getLines(String fileName) {

		ArrayList<String> mas = new ArrayList<String>();

		try {
			FileInputStream fstream = new FileInputStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				System.out.println(strLine);
				mas.add(strLine);
			}
			br.close();
		} catch (IOException e) {
			System.out.println("Ошибка " + e.getLocalizedMessage());
		}
		return mas;
	}

	static boolean fileExist(String path) {
		File file = new File(path);
		return file.exists();
	}

	static boolean createOutputFile(String path) {
		File file = new File(path);
		try {
			return file.createNewFile();
		} catch (Exception e) {
			System.out.println("Не удалось создать выходной файл \"" + path + "\"");
			return false;
		}
	}

	public static boolean parseParam(String parameter) {
		if (parameter.equalsIgnoreCase("-i")) {
			type = dataType.INTEGER;
		} else if (parameter.equalsIgnoreCase("-s")) {
			type = dataType.STRING;
		} else if (parameter.equalsIgnoreCase("-a")) {
			sort = sortingType.ASCENDING;
		} else if (parameter.equalsIgnoreCase("-d")) {
			sort = sortingType.DESCENDING;
		} else {
			return false;
		}
		return true;
	}

	boolean sortInputFiles() {
		boolean allFilesSorted = true;
		for (InputFileReader sr : listStreamReader) {

			String strLine;
			List<String> dataList = new ArrayList<String>();
			try {
				FileInputStream fstream = new FileInputStream(sr.fileName);
				BufferedReader bufReader = new BufferedReader(new InputStreamReader(fstream));

				while ((strLine = bufReader.readLine()) != null) {
					dataList.add(strLine);
				}
				bufReader.close();
				dataList = Sorting.SortFusionStringList(dataList);
				FileWriter fw = new FileWriter(new File(sr.fileName));
				listToFile(dataList, fw);
			} catch (IOException e) {
				System.out.println(
						"Ошибка предварительной сортировки файла \"" + sr.fileName + "\"" + e.getLocalizedMessage());
				allFilesSorted = false;
			}

		}
		return allFilesSorted;
	}

	public static void main(String[] args) {

		try {

			Start app = new Start();

			if (!app.fillParameters(args)) {
				return;
			}
			if (!app.sortInputFiles()) {
				System.out.println("Не все файлы предварительно отсортированы");
			}
			app.soting();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
