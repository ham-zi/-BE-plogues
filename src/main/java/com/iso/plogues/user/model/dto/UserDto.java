package com.iso.plogues.user.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDto {
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message="아이디 값은 영어 / 숫자만 사용가능합니다.")
	@Size(min = 4, max = 20, message="아이디 값은 4글자 이상 20글자 이하만 사용할 수 있습니다.")
	@NotBlank(message="아이디 값은 비어있을 수 없습니다.")
	private String userId;
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message="비밀번호 값은 영어 / 숫자만 사용가능합니다.")
	@Size(min = 4, max = 20, message="비밀번호 값은 4글자 이상 20글자 이하만 사용할 수 있습니다.")
	@NotBlank(message="비밀번호 값은 비어있을 수 없습니다.")
	private String userPwd;
	@Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message="이름은 영어/한글/숫자만 가능합니다.")
	@Size(min = 2, max = 20, message="이름은 2글자 이상 20글자 이하만 사용할 수 있습니다.")
	@NotBlank(message="이름은 비어있을 수 없습니다.")
	private String userName;
	@Email(message="올바른 이메일 형식이 아닙니다.")
	@Size(max = 20, message="이메일은 20글자 이하만 사용할 수 있습니다.")
	@NotBlank(message="이메일은 비어있을 수 없습니다.")
	private String email;
	@Pattern(regexp = "^[0-9]*$", message="전화번호는 숫자만 입력 가능합니다.")
	@Size(max = 11, message="전화번호는 11자리 이하로 입력하세요.")
	@NotBlank(message="전화번호는 비어있을 수 없습니다.")
	private String phone;
	@Size(max = 100, message="주소는 100자 이하만 사용할 수 있습니다.")
	@NotBlank(message="주소값은 비어있을 수 없습니다.")
	private String address;
	private String info;
	private String role;
	private String deleted;
}
