package mouda.backend.chat.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mouda.backend.chat.domain.ChatRoomType;
import mouda.backend.chat.entity.ChatRoomEntity;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
	Optional<ChatRoomEntity> findByIdAndDarakbangId(Long chatRoomId, long darakbangId);

	Optional<ChatRoomEntity> findByTargetIdAndType(long targetId, ChatRoomType chatRoomType);

	boolean existsByTargetIdAndType(long targetId, ChatRoomType chatRoomType);
}
