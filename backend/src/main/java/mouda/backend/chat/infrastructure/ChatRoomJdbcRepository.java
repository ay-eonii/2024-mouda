package mouda.backend.chat.infrastructure;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import mouda.backend.chat.entity.ChatRoomEntity;

@Repository
@RequiredArgsConstructor
public class ChatRoomJdbcRepository {

	private final JdbcTemplate jdbcTemplate;

	public void bulkInsertChatRooms(List<ChatRoomEntity> chatRooms) {
		String sql = "INSERT INTO chat_room (target_id, darakbang_id, type) VALUES (?, ?, ?)";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ChatRoomEntity room = chatRooms.get(i);
				ps.setLong(1, room.getTargetId());
				ps.setLong(2, room.getDarakbangId());
				ps.setString(3, room.getType().name());  // Enum → String
			}

			@Override
			public int getBatchSize() {
				return chatRooms.size();
			}
		});
	}
}
