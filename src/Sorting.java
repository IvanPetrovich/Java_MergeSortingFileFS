import java.util.ArrayList;
import java.util.List;

public class Sorting {

	public static List<InputFileReader> SortFusion(List<InputFileReader> inputArray) {
		if (inputArray.size() <= 1) {
			return inputArray;
		} else {
			int middle = (int) (inputArray.size() / 2);
			List<InputFileReader> right = SortFusion(inputArray.subList(middle, inputArray.size()));
			List<InputFileReader> left = SortFusion(inputArray.subList(0, middle));
			return Merge(left, right);
		}
	}

	static List<InputFileReader> Merge(List<InputFileReader> left, List<InputFileReader> right) {
		List<InputFileReader> res = new ArrayList<InputFileReader>(0);

		while (left.size() > 0 && right.size() > 0) {
			if (Start.type == Start.dataType.STRING) {
				if (left.get(0).s.compareTo(right.get(0).s) < 0 ^ Start.sort == Start.sortingType.DESCENDING) {
					res.add(left.get(0));
					left = left.subList(1, left.size());
				} else {
					res.add(right.get(0));
					right = right.subList(1, right.size());
				}
			} else {
				if ((left.get(0).i < right.get(0).i ^ Start.sort == Start.sortingType.DESCENDING)
						|| left.get(0).i == right.get(0).i) {
					res.add(left.get(0));
					left = left.subList(1, left.size());
				} else {
					res.add(right.get(0));
					right = right.subList(1, right.size());
				}
			}
		}

		if (left.size() > 0) {
			res.addAll(left);
		} else if (right.size() > 0) {
			res.addAll(right);
		}

		return res;
	}

	static List<String> SortFusionStringList(List<String> inputArray) {

		if (inputArray.size() <= 1) {
			return inputArray;
		} else {
			int middle = (int) (inputArray.size() / 2);
			List<String> right = SortFusionStringList(inputArray.subList(middle, inputArray.size()));
			List<String> left = SortFusionStringList(inputArray.subList(0, middle));
			return MergeStringList(left, right);
		}
	}

	static List<String> MergeStringList(List<String> left, List<String> right) {
		List<String> res = new ArrayList<String>(0);

		while (left.size() > 0 && right.size() > 0) {
			if (Start.type == Start.dataType.STRING) {
				if (left.get(0).compareTo(right.get(0)) < 0 ^ Start.sort == Start.sortingType.DESCENDING) {
					res.add(left.get(0));
					left = left.subList(1, left.size());
				} else {
					res.add(right.get(0));
					right = right.subList(1, right.size());
				}
			} else {// Для чисел
				Integer leftElement;
				Integer rightElement;
				try {
					leftElement = Integer.parseInt(left.get(0));
				} catch (NumberFormatException e) {
					System.out.println("Неверный формат числа " + e.getLocalizedMessage() + "\n\"" + left.get(0)
							+ "\" В сортировке участвовать не будет!");
					left = left.subList(1, left.size());
					continue;
				}

				try {
					rightElement = Integer.parseInt(right.get(0));
				} catch (NumberFormatException e) {
					System.out.println("Неверный формат числа " + e.getLocalizedMessage() + "\n\"" + right.get(0)
							+ "\" В сортировке участвовать не будет!");
					right = right.subList(1, right.size());
					continue;
				}

				if ((leftElement < rightElement ^ Start.sort == Start.sortingType.DESCENDING)
						|| leftElement == rightElement) {
					res.add(left.get(0));
					left = left.subList(1, left.size());
				} else {
					res.add(right.get(0));
					right = right.subList(1, right.size());
				}

			}
		}
		if (left.size() > 0) {
			res.addAll(left);
		} else if (right.size() > 0) {
			res.addAll(right);
		}
		return res;
	}

}
