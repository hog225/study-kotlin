package yg.study.kotlindatajdbc.config

import org.springframework.data.relational.core.dialect.H2Dialect
import org.springframework.data.relational.core.sql.IdentifierProcessing

class H2Dialect : H2Dialect() {

    override fun getIdentifierProcessing(): IdentifierProcessing {
        return IdentifierProcessing.create(
            IdentifierProcessing.Quoting("\""),
            IdentifierProcessing.LetterCasing.LOWER_CASE)
    }

}