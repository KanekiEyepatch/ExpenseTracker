package tracker.service

import tracker.model.Expense
import tracker.model.Category
import kotlin.properties.Delegates

class ExpenseService {

    private val expenses = mutableListOf<Expense>()

    private var nextId: Int by Delegates.observable(1) { _, _, new ->
        println("Next expense ID will be $new")
    }

    fun addExpense(amount: Double, category: Category, description: String = "No description"): Expense {
        val expense = Expense(nextId++, amount, category, description)
        expenses.add(expense)
        return expense
    }

    fun getAllExpenses(): List<Expense> = expenses.toList()

    fun getExpensesByCategory(category: Category): List<Expense> =
        expenses.filter { it.category == category }

    fun totalExpenses(): Double = expenses.sumOf { it.amount }

    fun queryExpenses(filter: (Expense) -> Boolean): List<Expense> =
        expenses.filter(filter)
}
