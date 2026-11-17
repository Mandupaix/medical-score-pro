package com.example

import com.example.domain.ClinicalScoreEngine
import com.example.domain.MedicalCalculatorCatalog
import com.example.domain.SeverityLevel
import com.example.i18n.AppLanguage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ClinicalScoreEngineTest {

    @Test
    fun glasgowComaScale_computesExactTotalAndSeverity() {
        val severe = ClinicalScoreEngine.calculate(
            definition = MedicalCalculatorCatalog.glasgowScore,
            numericInputs = mapOf("eye" to 1.0, "verbal" to 2.0, "motor" to 3.0),
            booleanInputs = emptyMap(),
            language = AppLanguage.FR
        )
        assertEquals(6.0, severe.numericValue, 0.001)
        assertEquals("6 / 15", severe.displayValue)
        assertEquals(SeverityLevel.SEVERE, severe.severityLevel)

        val normal = ClinicalScoreEngine.calculate(
            definition = MedicalCalculatorCatalog.glasgowScore,
            numericInputs = mapOf("eye" to 4.0, "verbal" to 5.0, "motor" to 6.0),
            booleanInputs = emptyMap(),
            language = AppLanguage.FR
        )
        assertEquals(15.0, normal.numericValue, 0.001)
        assertEquals("15 / 15", normal.displayValue)
        assertEquals(SeverityLevel.LOW, normal.severityLevel)
    }

    @Test
    fun euroScoreII_computesCardiacRiskPercentAndGuidance() {
        val result = ClinicalScoreEngine.calculate(
            definition = MedicalCalculatorCatalog.euroScoreII,
            numericInputs = mapOf("age" to 78.0, "renal" to 2.0, "nyha" to 3.0, "lvef" to 2.0),
            booleanInputs = mapOf("female" to true, "urgent" to true, "iddm" to true),
            language = AppLanguage.FR
        )
        assertNotNull(result.cardiacRiskPercent)
        assertTrue(result.cardiacRiskPercent!! > 8.0)
        assertEquals(SeverityLevel.SEVERE, result.severityLevel)
        assertNotNull(result.cardiacGuidance)
    }

    @Test
    fun cha2ds2Vasc_computesStrokeRisk() {
        val result = ClinicalScoreEngine.calculate(
            definition = MedicalCalculatorCatalog.cha2ds2Vasc,
            numericInputs = mapOf("age_band" to 2.0),
            booleanInputs = mapOf("htn" to true, "stroke" to true, "female" to true),
            language = AppLanguage.EN
        )
        assertEquals(6.0, result.numericValue, 0.001)
        assertEquals("6 / 9", result.displayValue)
        assertEquals(9.7, result.cardiacRiskPercent!!, 0.01)
    }

    @Test
    fun catalogHasExactly31SpecialtiesAnd152UniqueEvaluatableCalculators() {
        val specialties = MedicalCalculatorCatalog.all31Specialties
        val allScores = MedicalCalculatorCatalog.all152Scores

        assertEquals(31, specialties.size)
        assertEquals(152, allScores.size)
        assertEquals(152, allScores.map { it.scoreId }.distinct().size)
        assertEquals(152, specialties.sumOf { it.calculatorCountMcp })

        allScores.forEach { def ->
            assertNotNull("findScoreById failed for ${def.scoreId}", MedicalCalculatorCatalog.findScoreById(def.scoreId))
            val result = ClinicalScoreEngine.calculate(
                definition = def,
                numericInputs = emptyMap(),
                booleanInputs = emptyMap(),
                language = AppLanguage.FR
            )
            assertTrue("Empty displayValue for ${def.scoreId}", result.displayValue.isNotBlank())
            assertTrue("Empty severityLabel for ${def.scoreId}", result.severityLabel.isNotBlank())
            assertTrue("Empty breakdown for ${def.scoreId}", result.breakdownLines.isNotEmpty())
        }
    }

    @Test
    fun formulaCalculators_computeAccurateClinicalValues() {
        val qtcDef = MedicalCalculatorCatalog.findScoreById("qtc_bazett")!!
        val qtcRes = ClinicalScoreEngine.calculate(
            qtcDef, mapOf("qt_ms" to 400.0, "hr" to 60.0), emptyMap(), AppLanguage.EN
        )
        assertEquals(400.0, qtcRes.numericValue, 0.5)
        assertEquals("400 ms", qtcRes.displayValue)

        val bmiDef = MedicalCalculatorCatalog.findScoreById("bmi_calc")!!
        val bmiRes = ClinicalScoreEngine.calculate(
            bmiDef, mapOf("weight" to 80.0, "height" to 200.0), emptyMap(), AppLanguage.FR
        )
        assertEquals(20.0, bmiRes.numericValue, 0.01)
        assertEquals(SeverityLevel.LOW, bmiRes.severityLevel)

        val aspectsDef = MedicalCalculatorCatalog.findScoreById("aspects")!!
        val aspectsRes = ClinicalScoreEngine.calculate(
            aspectsDef, emptyMap(), mapOf("caudate" to true, "m1" to true, "m2" to true, "m3" to true, "m4" to true, "m5" to true), AppLanguage.FR
        )
        assertEquals(4.0, aspectsRes.numericValue, 0.01)
        assertEquals(SeverityLevel.SEVERE, aspectsRes.severityLevel)
    }
}
