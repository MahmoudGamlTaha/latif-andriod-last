package com.service.khdmaa.utiles

import org.apache.commons.jexl3.JexlBuilder
import org.apache.commons.jexl3.JexlException
import org.apache.commons.jexl3.MapContext

class ExpressionEvaluator {
    private val jexlEngine = JexlBuilder().create()
    public val jexlContext = MapContext()

    fun evaluate(expression: String): Any? = try {
        val jexlExpression = jexlEngine.createExpression(expression)
        jexlExpression.evaluate(jexlContext)
    } catch (e: JexlException) {
        e.printStackTrace()
    }

    fun evaluateAsBoolean(expression: String): Boolean? {
        val boolean = evaluate(expression) as? Boolean
        if (boolean == null) {
           return false
        }

        return boolean
    }
}