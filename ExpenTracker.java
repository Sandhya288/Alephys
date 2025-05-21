import java.io.*;
import java.util.*;

public class ExpenTracker {
    // Transaction class
    static class Transaction implements Serializable {
        private static final long serialVersionUID = 1L;
        enum Type { INCOME, EXPENSE }
        Type type;
        String category;
        double amount;
        int month;  // 1-12

        Transaction(Type type, String category, double amount, int month) {
            this.type = type;
            this.category = category;
            this.amount = amount;
            this.month = month;
        }

        public String toString() {
            return String.format("%s - %s: %.2f (Month: %d)", type, category, amount, month);
        }
    }

    static List<Transaction> transactions = new ArrayList<Transaction>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to Expense Tracker!");
        while (true) {
            System.out.println("\nExpense Tracker Menu:");
            System.out.println("1. Add Transaction");
            System.out.println("2. View Monthly Summary");
            System.out.println("3. Load from File");
            System.out.println("4. Save to File");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = readInt();

            switch (choice) {
                case 1: addTransaction(); break;
                case 2: viewSummary(); break;
                case 3: loadFromFile(); break;
                case 4: saveToFile(); break;
                case 5: 
                    System.out.println("Exiting. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void addTransaction() {
        System.out.println("\nIs this Income or Expense?");
        System.out.println("1. Income");
        System.out.println("2. Expense");
        System.out.print("Enter choice: ");
        int t = readInt();
        Transaction.Type type;
        if (t == 1) {
            type = Transaction.Type.INCOME;
        } else if (t == 2) {
            type = Transaction.Type.EXPENSE;
        } else {
            System.out.println("Invalid type selected.");
            return;
        }

        String category = chooseCategory(type);
        if (category == null) return;

        System.out.print("Enter amount: ");
        double amount = readDouble();

        System.out.print("Enter month (1-12): ");
        int month = readInt();
        if (month < 1 || month > 12) {
            System.out.println("Invalid month.");
            return;
        }

        transactions.add(new Transaction(type, category, amount, month));
        System.out.println("Transaction added successfully!");
    }

    private static String chooseCategory(Transaction.Type type) {
        if (type == Transaction.Type.INCOME) {
            System.out.println("Choose Income Category:");
            System.out.println("1. Salary");
            System.out.println("2. Business");
            System.out.print("Enter choice: ");
            int c = readInt();
            if (c == 1) return "Salary";
            else if (c == 2) return "Business";
            else {
                System.out.println("Invalid category.");
                return null;
            }
        } else {
            System.out.println("Choose Expense Category:");
            System.out.println("1. Food");
            System.out.println("2. Rent");
            System.out.println("3. Travel");
            System.out.print("Enter choice: ");
            int c = readInt();
            if (c == 1) return "Food";
            else if (c == 2) return "Rent";
            else if (c == 3) return "Travel";
            else {
                System.out.println("Invalid category.");
                return null;
            }
        }
    }

    private static void viewSummary() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions recorded.");
            return;
        }

        System.out.print("Enter month to view summary (1-12): ");
        int month = readInt();
        if (month < 1 || month > 12) {
            System.out.println("Invalid month.");
            return;
        }

        double totalIncome = 0;
        double totalExpense = 0;

        Map<String, Double> incomeCategories = new HashMap<String, Double>();
        Map<String, Double> expenseCategories = new HashMap<String, Double>();

        for (Transaction tr : transactions) {
            if (tr.month == month) {
                if (tr.type == Transaction.Type.INCOME) {
                    totalIncome += tr.amount;
                    incomeCategories.put(tr.category, incomeCategories.getOrDefault(tr.category, 0.0) + tr.amount);
                } else {
                    totalExpense += tr.amount;
                    expenseCategories.put(tr.category, expenseCategories.getOrDefault(tr.category, 0.0) + tr.amount);
                }
            }
        }

        System.out.println("\nMonthly Summary for month " + month + ":");
        System.out.printf("Total Income: %.2f\n", totalIncome);
        for (String cat : incomeCategories.keySet()) {
            System.out.printf("  %s: %.2f\n", cat, incomeCategories.get(cat));
        }

        System.out.printf("Total Expenses: %.2f\n", totalExpense);
        for (String cat : expenseCategories.keySet()) {
            System.out.printf("  %s: %.2f\n", cat, expenseCategories.get(cat));
        }

        System.out.printf("Net Savings: %.2f\n", totalIncome - totalExpense);
    }

    private static void saveToFile() {
        System.out.print("Enter filename to save data: ");
        String filename = scanner.next();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(transactions);
            System.out.println("Data saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private static void loadFromFile() {
        System.out.print("Enter filename to load data: ");
        String filename = scanner.next();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            transactions = (List<Transaction>) in.readObject();
            System.out.println("Data loaded from " + filename);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    private static int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static double readDouble() {
        while (!scanner.hasNextDouble()) {
            System.out.print("Please enter a valid amount: ");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}
