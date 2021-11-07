package com.example.dare.pedometer

import java.text.DecimalFormat

object Util {
    private val decimalFormat = DecimalFormat("#.##")

    fun calculateKilometers(steps: Int): String{
        return decimalFormat.format((steps.toDouble()/2000)*1.609)
    }

    fun calculateCalories(steps: Int): String{
        return decimalFormat.format((steps.toDouble()/2000)*1.609*55)
    }

    fun addAxisValuesToArray(axisValues: ArrayList<Int>, i: Int) {
        axisValues.add(i)
    }
}