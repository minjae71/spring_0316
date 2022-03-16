package com.spring.spring_0316.dto;

import com.spring.spring_0316.domain.entity.MemberEntity;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDto {
    private Long id;

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,10}", message = "아이디는 6~10자의 영문, 숫자를 모두 포함해야 합니다.")
    private String accountId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{6,10}", message = "비밀번호는 6~10자의 영문, 숫자, 특수문자를 모두 포함해야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Pattern(regexp = "[가-힣]{2,4}", message = "이름은 한글만 입력가능합니다.")
    private String name;

    @NotBlank(message = "생일은 필수 입력 값입니다.")
    private String birth;

    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String postcode;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;

    @NotBlank(message = "상세주소는 필수 입력 값입니다.")
    private String detailAddress;

    private String extraAddress;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @Pattern(regexp = "[0-9]*", message = "전화번호는 숫자만 입력 가능합니다.")
    private String phone;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .id(id)
                .accountId(accountId)
                .password(password)
                .name(name)
                .birth(birth)
                .email(email)
                .postcode(postcode)
                .address(address)
                .detailAddress(detailAddress)
                .extraAddress(extraAddress)
                .phone(phone)
                .build();
    }

    @Builder
    public MemberDto(Long id, String accountId, String password, String name, String birth, String email, String postcode, String address, String detailAddress, String extraAddress, String phone) {
        this.id = id;
        this.accountId = accountId;
        this.password = password;
        this.name = name;
        this.birth = birth;
        this.email = email;
        this.postcode = postcode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
        this.phone = phone;
    }
}
