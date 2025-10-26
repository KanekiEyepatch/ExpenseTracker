package tracker.model

import java.time.LocalDateTime

data class Expense(
    val id: Int,
    val amount: Double,
    val category: Category,
    val description: String,
    val date: LocalDateTime = LocalDateTime.now()
)
