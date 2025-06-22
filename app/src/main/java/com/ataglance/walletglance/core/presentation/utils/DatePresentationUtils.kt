package com.ataglance.walletglance.core.presentation.utils

import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.utils.fromTimestamp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char


fun Int.getGreetingsWidgetTitleRes(): Int {
    return when (this) {
        in 6..11 -> R.string.greetings_title_morning
        in 12..17 -> R.string.greetings_title_afternoon
        in 18..22 -> R.string.greetings_title_evening
        else -> R.string.greetings_title_night
    }
}

fun Int.getGreetingsWithUsernameWidgetTitleRes(): Int {
    return when (this) {
        in 6..11 -> R.string.greetings_title_morning_username
        in 12..17 -> R.string.greetings_title_afternoon_username
        in 18..22 -> R.string.greetings_title_evening_username
        else -> R.string.greetings_title_night_username
    }
}

fun Int.getMonthShortNameRes(): Int? {
    return when (this) {
        1 -> R.string.january_short
        2 -> R.string.february_short
        3 -> R.string.march_short
        4 -> R.string.april_short
        5 -> R.string.may_short
        6 -> R.string.june_short
        7 -> R.string.july_short
        8 -> R.string.august_short
        9 -> R.string.september_short
        10 -> R.string.october_short
        11 -> R.string.november_short
        12 -> R.string.december_short
        else -> null
    }
}


fun Long.formatTimestampAsDayMonthNameYear(
    resourceManager: ResourceManager,
    includeYear: Boolean = true
): String {
    val localDateTime = LocalDateTime.fromTimestamp(this)

    return if (includeYear) {
        localDateTime.formatAsDayMonthNameYear(resourceManager = resourceManager)
    } else {
        localDateTime.formatAsDayMonthName(resourceManager = resourceManager)
    }
}


fun LocalDateTime.formatByDefault(): String {
    return this.format(
        format = LocalDateTime.Format {
            dayOfMonth(); char('.'); monthNumber(); char('.'); year()
            char(' ')
            hour(); char(':'); minute()
        }
    )
}

fun LocalDateTime.formatAsDayMonth(): String {
    return this.format(
        format = LocalDateTime.Format {
            dayOfMonth(); char('.'); monthNumber()
        }
    )
}

fun LocalDateTime.formatAsDayMonthName(resourceManager: ResourceManager): String {
    val monthName = resourceManager.getString(id = monthNumber.getMonthShortNameRes())

    return this.format(
        format = LocalDateTime.Format {
            dayOfMonth(); char(' '); monthName
        }
    )
}

fun LocalDateTime.formatAsDayMonthNameYear(resourceManager: ResourceManager): String {
    val monthName = resourceManager.getString(id = monthNumber.getMonthShortNameRes())

    return this.format(
        format = LocalDateTime.Format {
            dayOfMonth(); char(' '); monthName; char(' '); year()
        }
    )
}

fun LocalDateTime.formatByRepeatingPeriod(
    period: RepeatingPeriod,
    resourceManager: ResourceManager
): String {
    return when (period) {
        RepeatingPeriod.Daily -> {
            "${hour}:${minute}"
        }
        RepeatingPeriod.Weekly, RepeatingPeriod.Monthly -> {
            "$dayOfMonth " + resourceManager.getString(id = monthNumber.getMonthShortNameRes())
        }
        RepeatingPeriod.Yearly -> {
            resourceManager.getString(id = monthNumber.getMonthShortNameRes()) + " $year"
        }
    }
}


fun LocalDate.formatAsDayMonth(): String {
    return this.format(
        format = LocalDate.Format {
            dayOfMonth(); char('.'); monthNumber()
        }
    )
}
