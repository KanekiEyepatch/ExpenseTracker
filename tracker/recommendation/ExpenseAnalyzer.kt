package tracker

import tracker.model.Expense
import tracker.service.ExpenseService

interface AnalysisStrategy {
    val name: String
    fun analyze(expenses: List<Expense>): List<Expense>
}

class HighSpendingStrategy : AnalysisStrategy {
    override val name = "High Spending"
    override fun analyze(expenses: List<Expense>): List<Expense> =
        expenses.sortedByDescending { it.amount }.take(3)
}

class RandomSuggestionStrategy : AnalysisStrategy {
    override val name = "Random Suggestion"
    override fun analyze(expenses: List<Expense>): List<Expense> =
        expenses.shuffled().take(3)
}

class ExpenseAnalyzer(private val service: ExpenseService) {
    var strategy: AnalysisStrategy = HighSpendingStrategy()

    fun recommend(limit: Int = 3, transform: (List<Expense>) -> List<Expense> = { it }): List<Expense> {
        val results = strategy.analyze(service.getAllExpenses())
        return transform(results).take(limit)
    }
}
