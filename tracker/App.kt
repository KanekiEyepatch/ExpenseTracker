package tracker

import tracker.model.Category
import tracker.service.ExpenseService
import tracker.util.prompt

fun main() {
    val service = ExpenseService()
    val analyzer = ExpenseAnalyzer(service)

    println("Welcome to Kotlin Expense Tracker!")

    while (true) {
        println("\nMenu: [1] Add expense [2] List expenses [3] Recommend [4] Custom query [0] Exit")
        when (prompt("Choose option:")) {
            "1" -> addExpenseFlow(service)
            "2" -> listExpenses(service)
            "3" -> recommendExpenses(analyzer)
            "4" -> customQueryFlow(service)
            "0" -> break
            else -> println("Invalid choice")
        }
    }
}

fun addExpenseFlow(service: ExpenseService) {
    val amount = try {
        prompt("Amount:").toDouble()
    } catch (e: NumberFormatException) {
        println("Invalid amount")
        return
    }

    val categoryInput = prompt("Category [FOOD, TRANSPORT, ENTERTAINMENT, UTILITIES, OTHER]:")
    val category = try {
        Category.valueOf(categoryInput.uppercase())
    } catch (e: IllegalArgumentException) {
        Category.OTHER
    }

    val description = prompt("Description:")
    val expense = service.addExpense(amount, category, description)
    println("Added expense: $expense")
}

fun listExpenses(service: ExpenseService) {
    val expenses = service.getAllExpenses()
    if (expenses.isEmpty()) println("No expenses yet")
    else expenses.forEach { println(it) }
}

fun recommendExpenses(analyzer: ExpenseAnalyzer) {
    println("Using strategy: ${analyzer.strategy.name}")
    analyzer.recommend().forEach { println(it) }

    if (prompt("Switch to random strategy? (y/n)").lowercase() == "y") {
        analyzer.strategy = RandomSuggestionStrategy()
        println("Random suggestions:")
        analyzer.recommend().forEach { println(it) }
    }
}

fun customQueryFlow(service: ExpenseService) {
    val categoryInput = prompt("Category to filter [FOOD, TRANSPORT, ENTERTAINMENT, UTILITIES, OTHER] (leave empty for all):")
    val minAmountInput = prompt("Minimum amount (leave empty for 0):")

    val filteredCategory: (tracker.model.Expense) -> Boolean = if (categoryInput.isNotBlank()) {
        val cat = try { Category.valueOf(categoryInput.uppercase()) } catch (e: Exception) { Category.OTHER }
        { it.category == cat }
    } else { { true } }

    val minAmount = minAmountInput.toDoubleOrNull() ?: 0.0
    val filteredAmount: (tracker.model.Expense) -> Boolean = { it.amount >= minAmount }

    val results = service.queryExpenses { filteredCategory(it) && filteredAmount(it) }

    results.takeIf { it.isNotEmpty() }
        ?.forEach { println(it) }
        ?: println("No expenses matched your query.")
}
