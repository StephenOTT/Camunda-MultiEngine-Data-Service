package com.github.stephenott.camunda.multienginedataservice.security

class VariableReplacer {

    private val variableRegex: Regex = Regex("\\$\\{(.*?)}")

    fun replaceVariables(content: String, variables: Map<String, Any>): String{

        return variableRegex.replace(content){
            val variable = it.destructured.component1()
            variables.getOrDefault(variable, it.value).toString()
        }

    }


    //@TODO variable restrictions?
//    val result = VariableReplacer().replaceVariables("""
//    {
//     "svar": "${'$'}{v1}",
//     "tfvar": ${'$'}{v2},
//     "numvar": ${'$'}{v3}
//    }
//""".trimIndent(), mapOf(Pair("v1", "myString"), Pair("v2", true), Pair("v3", 1234)))


}