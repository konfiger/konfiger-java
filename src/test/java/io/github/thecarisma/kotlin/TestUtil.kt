package io.github.thecarisma.kotlin

import io.github.thecarisma.KonfigerFieldsExposer
import org.junit.Assert
import org.junit.Test

class TestUtil {
    @Test
    fun Test_Check_Escape_And_Unescape_Seperator() {
        val actualStr = "\\,Hello¬W\n-\t-\torld"
        val t1 = KonfigerFieldsExposer.getKonfigerUtil_escapeString(actualStr, '¬')
        val t2 = KonfigerFieldsExposer.getKonfigerUtil_escapeString(actualStr)
        Assert.assertNotEquals(actualStr, t1)
        Assert.assertEquals(t1, "\\,Hello/¬W\n-\t-\torld")
        Assert.assertNotEquals(t1, KonfigerFieldsExposer.getKonfigerUtil_unEscapeString(t1, '¬'))
        Assert.assertNotEquals(actualStr, KonfigerFieldsExposer.getKonfigerUtil_unEscapeString(t1))
        Assert.assertEquals(KonfigerFieldsExposer.getKonfigerUtil_unEscapeString(t1, '¬'), actualStr)
        Assert.assertNotEquals(t1, t2)
        Assert.assertEquals(t2, "\\,Hello¬W\n-\t-\torld")
        Assert.assertNotEquals(t2, KonfigerFieldsExposer.getKonfigerUtil_unEscapeString(t1))
        Assert.assertEquals(actualStr, KonfigerFieldsExposer.getKonfigerUtil_unEscapeString(t2))
        Assert.assertEquals(KonfigerFieldsExposer.getKonfigerUtil_unEscapeString(t1, '¬'), actualStr)
    }
}