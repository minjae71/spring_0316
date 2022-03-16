package com.spring.spring_0316.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "member")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String accountId;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 12, nullable = false)
    private String birth;

    @Column(length = 30, nullable = false)
    private String email;

    @Column(length = 8, nullable = false)
    private String postcode;

    @Column(length = 50, nullable = false)
    private String address;

    @Column(length = 50, nullable = false)
    private String detailAddress;

    @Column(length = 50)
    private String extraAddress;

    @Column(length = 15, nullable = false)
    private String phone;

    @Builder
    public MemberEntity(Long id, String accountId, String password, String name, String birth, String email, String postcode, String address, String detailAddress, String extraAddress, String phone) {
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