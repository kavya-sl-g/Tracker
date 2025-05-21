//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SimpleExpenseTracker {

    enum TransactionType {
        INCOME, EXPENSE
    }

    static class Transaction {
        LocalDate date;
        TransactionType type;
        String category;
        double amount;

        public Transaction(LocalDate date, TransactionType type, String category, double amount) {
            this.date = date;
            this.type = type;
            this.category = category;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return date + "," + type + "," + category + "," + amount;
        }
    }

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void loadTransactionsFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 4) continue;
                LocalDate date = LocalDate.parse(parts[0], DATE_FORMAT);
                TransactionType type = TransactionType.valueOf(parts[1].toUpperCase());
                String category = parts[2];
                double amount = Double.parseDouble(parts[3]);
                transactions.add(new Transaction(date, type, category, amount));
            }
            System.out.println("Transactions loaded successfully from " + filename);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error parsing file: " + e.getMessage());
        }
    }

    public void displayMonthlySummary(int year, int month) {
        double totalIncome = 0;
        double totalExpense = 0;
        Map<String, Double> incomeCategories = new HashMap<>();
        Map<String, Double> expenseCategories = new HashMap<>();

        for (Transaction t : transactions) {
            if (t.date.getYear() == year && t.date.getMonthValue() == month) {
                if (t.type == TransactionType.INCOME) {
                    totalIncome += t.amount;
                    incomeCategories.put(t.category, incomeCategories.getOrDefault(t.category, 0.0) + t.amount);
                } else {
                    totalExpense += t.amount;
                    expenseCategories.put(t.category, expenseCategories.getOrDefault(t.category, 0.0) + t.amount);
                }
            }
        }

        System.out.println("\n--- Monthly Summary for " + year + "-" + String.format("%02d", month) + " ---");
        System.out.println("Total Income: ₹" + totalIncome);
        for (String category : incomeCategories.keySet()) {
            System.out.println("  " + category + ": ₹" + incomeCategories.get(category));
        }
        System.out.println("Total Expenses: ₹" + totalExpense);
        for (String category : expenseCategories.keySet()) {
            System.out.println("  " + category + ": ₹" + expenseCategories.get(category));
        }
        System.out.println("Net Savings: ₹" + (totalIncome - totalExpense));
    }

    public static void main(String[] args) {
        SimpleExpenseTracker tracker = new SimpleExpenseTracker();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Expense Tracker Menu ---");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Monthly Summary");
            System.out.println("4. Load Transactions from File");
            System.out.println("5. Exit");
            System.out.print("Choose an option (1-5): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    // Add Income
                    System.out.print("Enter date (YYYY-MM-DD): ");
                    String incomeDateStr = scanner.nextLine();
                    LocalDate incomeDate;
                    try {
                        incomeDate = LocalDate.parse(incomeDateStr, DATE_FORMAT);
                    } catch (Exception e) {
                        System.out.println("Invalid date format.");
                        break;
                    }
                    System.out.print("Enter income category (e.g., Salary, Business): ");
                    String incomeCategory = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    double incomeAmount;
                    try {
                        incomeAmount = Double.parseDouble(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount.");
                        break;
                    }
                    tracker.addTransaction(new Transaction(incomeDate, TransactionType.INCOME, incomeCategory, incomeAmount));
                    System.out.println("Income added successfully.");
                    break;
                case "2":
                    // Add Expense
                    System.out.print("Enter date (YYYY-MM-DD): ");
                    String expenseDateStr = scanner.nextLine();
                    LocalDate expenseDate;
                    try {
                        expenseDate = LocalDate.parse(expenseDateStr, DATE_FORMAT);
                    } catch (Exception e) {
                        System.out.println("Invalid date format.");
                        break;
                    }
                    System.out.print("Enter expense category (e.g., Food, Rent, Travel): ");
                    String expenseCategory = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    double expenseAmount;
                    try {
                        expenseAmount = Double.parseDouble(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount.");
                        break;
                    }
                    tracker.addTransaction(new Transaction(expenseDate, TransactionType.EXPENSE, expenseCategory, expenseAmount));
                    System.out.println("Expense added successfully.");
                    break;
                case "3":
                    // View Monthly Summary
                    System.out.print("Enter year (e.g., 2025): ");
                    int year;
                    try {
                        year = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid year.");
                        break;
                    }
                    System.out.print("Enter month (1-12): ");
                    int month;
                    try {
                        month = Integer.parseInt(scanner.nextLine());
                        if (month < 1 || month > 12) {
                            System.out.println("Month must be between 1 and 12.");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid month.");
                        break;
                    }
                    tracker.displayMonthlySummary(year, month);
                    break;
                case "4":
                    // Load Transactions from File
                    System.out.print("Enter filename to load transactions from: ");
                    String loadFilename = scanner.nextLine();
                    tracker.loadTransactionsFromFile(loadFilename);
                    break;
                case "5":
                    // Exit
                    System.out.println("Exiting Expense Tracker. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please choose between 1 and 5.");
            }
        }
    }
}
