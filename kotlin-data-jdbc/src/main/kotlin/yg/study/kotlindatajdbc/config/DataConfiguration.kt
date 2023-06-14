package yg.study.kotlindatajdbc.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration
import org.springframework.context.annotation.*
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.data.relational.core.dialect.Dialect
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import javax.sql.DataSource

@Configuration
@EnableJdbcRepositories(basePackages = ["yg.study.kotlindatajdbc"])
@Import(value = [
    DataSourceAutoConfiguration::class,
    JdbcTemplateAutoConfiguration::class,
    SqlInitializationAutoConfiguration::class,
    TransactionAutoConfiguration::class,
])
class DataConfiguration : AbstractJdbcConfiguration() {

    @Bean
    @Primary
    fun finchDialect() : Dialect =
        H2Dialect()

    @Bean
    @ConditionalOnMissingBean
    fun transactionManager(datasource: DataSource) = DataSourceTransactionManager(datasource)

}