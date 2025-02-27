package mouda.backend.darakbangmember.domain;

import java.util.Objects;

import org.springframework.http.HttpStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mouda.backend.chat.domain.Author;
import mouda.backend.darakbang.domain.Darakbang;
import mouda.backend.darakbangmember.exception.DarakbangMemberErrorMessage;
import mouda.backend.darakbangmember.exception.DarakbangMemberException;

@Entity
@Getter
@NoArgsConstructor
@Table(
	name = "darakbang_member",
	uniqueConstraints = {
		@UniqueConstraint(
			columnNames = {"member_id", "darakbang_id"}
		)
	}
)
public class DarakbangMember {

	private static final int MAX_LENGTH = 12;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Darakbang darakbang;

	@Column(nullable = false)
	private Long memberId;

	@Column(nullable = false)
	private String nickname;

	private String profile;

	private String description;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private DarakBangMemberRole role;

	@Builder
	public DarakbangMember(Darakbang darakbang, Long memberId, String nickname, String profile, String description,
		DarakBangMemberRole role) {
		validateNickname(nickname);
		this.darakbang = darakbang;
		this.memberId = memberId;
		this.nickname = nickname;
		this.profile = profile;
		this.description = description;
		this.role = role;
	}

	private void validateNickname(String nickname) {
		if (nickname == null || nickname.isBlank()) {
			throw new DarakbangMemberException(HttpStatus.BAD_REQUEST, DarakbangMemberErrorMessage.NICKNAME_NOT_EXIST);
		}
		if (nickname.length() > MAX_LENGTH) {
			throw new DarakbangMemberException(HttpStatus.BAD_REQUEST, DarakbangMemberErrorMessage.INVALID_LENGTH);
		}
	}

	public boolean isNotManager() {
		return role != DarakBangMemberRole.MANAGER;
	}

	public DarakbangMember updateMyInfo(String nickname, String description) {
		validateNickname(nickname);
		this.nickname = nickname;
		this.description = description;

		return this;
	}

	public DarakbangMember updateMyInfo(String nickname, String description, String profile) {
		validateNickname(nickname);
		this.nickname = nickname;
		this.description = description;
		this.profile = profile;

		return this;
	}

	public boolean isSameMemberWith(DarakbangMember other) {
		return id.equals(other.getId());
	}

	public boolean isNotSameMemberWith(DarakbangMember other) {
		return !isSameMemberWith(other);
	}

	public boolean hasImage() {
		return profile != null;
	}

	public Author toAuthor() {
		return Author.builder()
			.darakbangMemberId(id)
			.memberId(memberId)
			.nickname(nickname)
			.profile(profile)
			.build();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DarakbangMember that = (DarakbangMember)o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
