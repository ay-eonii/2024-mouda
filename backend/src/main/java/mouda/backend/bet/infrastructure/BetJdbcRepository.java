package mouda.backend.bet.infrastructure;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import mouda.backend.bet.entity.BetEntity;

@Repository
@RequiredArgsConstructor
public class BetJdbcRepository {

	private final JdbcTemplate jdbcTemplate;

	public void batchUpdateLoser(List<BetEntity> betEntities) {
		jdbcTemplate.batchUpdate(
			"UPDATE bet SET loser_darakbang_member_id = ? WHERE id = ?",
			new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					BetEntity dto = betEntities.get(i);
					ps.setLong(1, dto.getLoserDarakbangMemberId());
					ps.setLong(2, dto.getId());
				}

				@Override
				public int getBatchSize() {
					return betEntities.size();
				}
			}
		);
	}
}
