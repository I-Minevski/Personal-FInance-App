package PFA;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortingUtils {
	
	public SortingUtils() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
     * Sorts the given list of transactions using the merge sort algorithm.
     *
     * @param transactions the list of transactions to be sorted
     * @param comparator   the comparator used to compare transactions
     */
	
	public static void mergeSort(List<Transaction> transactions, Comparator<Transaction> comparator) {
        if (transactions.size() <= 1) {
            return;
        }

        int middle = transactions.size() / 2;
        List<Transaction> left = new ArrayList<>(transactions.subList(0, middle));
        List<Transaction> right = new ArrayList<>(transactions.subList(middle, transactions.size()));

        mergeSort(left, comparator);
        mergeSort(right, comparator);

        merge(transactions, left, right, comparator);
    }
	
	/**
     * Merges two sorted lists of transactions into one sorted list.
     *
     * @param transactions the list to store the merged result
     * @param left         the left sublist of transactions
     * @param right        the right sublist of transactions
     * @param comparator   the comparator used to compare transactions
     */

    private static void merge(List<Transaction> transactions, List<Transaction> left, List<Transaction> right, Comparator<Transaction> comparator) {
        int leftIndex = 0;
        int rightIndex = 0;
        int currentIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            Transaction leftTransaction = left.get(leftIndex);
            Transaction rightTransaction = right.get(rightIndex);

            if (comparator.compare(leftTransaction, rightTransaction) <= 0) {
                transactions.set(currentIndex++, leftTransaction);
                leftIndex++;
            } else {
                transactions.set(currentIndex++, rightTransaction);
                rightIndex++;
            }
        }

        while (leftIndex < left.size()) {
            transactions.set(currentIndex++, left.get(leftIndex++));
        }

        while (rightIndex < right.size()) {
            transactions.set(currentIndex++, right.get(rightIndex++));
        }
    }
}

class DateComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        // Compare transactions based on date
        if (t1.getDate().compareTo(t2.getDate()) < 0) {
            return -1;
        } else if (t1.getDate().compareTo(t2.getDate()) > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}

class AmountComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        // Compare transactions based on amount
        if (t1.getAmount() < t2.getAmount()) {
            return -1;
        } else if (t1.getAmount() > t2.getAmount()) {
            return 1;
        } else {
            return 0;
        }
    }
}
