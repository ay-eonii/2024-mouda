package mouda.backend.common.config;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.persistence.EntityManager;

public abstract class DatabaseCleaner {
	public static void clear(ApplicationContext applicationContext) {
		var entityManager = applicationContext.getBean(EntityManager.class);
		var jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
		var transactionTemplate = applicationContext.getBean(TransactionTemplate.class);

		transactionTemplate.execute(status -> {
			entityManager.clear();
			deleteAll(jdbcTemplate, entityManager);
			return null;
		});
	}

	private static void deleteAll(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
		for (String tableName : findDatabaseTableNames(jdbcTemplate)) {
			entityManager.createNativeQuery("DELETE FROM %s".formatted(tableName)).executeUpdate();
			entityManager.createNativeQuery("ALTER TABLE %s alter column id restart with 1".formatted(tableName))
				.executeUpdate();
		}
		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
	}

	private static List<String> findDatabaseTableNames(JdbcTemplate jdbcTemplate) {
		return jdbcTemplate
			.query("SHOW TABLES", (rs, rowNum) -> rs.getString(1))
			.stream().toList();
	}
}
