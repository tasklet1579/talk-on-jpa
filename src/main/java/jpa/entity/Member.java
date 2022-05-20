package jpa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Member {

	// 권장하는 식별자 전략
	// 기본 키 제약 조건 : null 아님, 유일, 변하면 안됨
	// 미래까지 이 조건을 만족하는 자연키는 찾기 어렵다 대리키(대체키)를 사용하자
	// 예를 들어 주민등록번호도 기본 키로 적절하지 않다
	// 권장 : Long + 대체키 + 키 생성전략 사용
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "USERNAME", nullable = false, length = 20)
	private String name;
	private int age;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date regDate;
	
	@Enumerated(EnumType.STRING)
	private MemberType memberType;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public MemberType getMemberType() {
		return memberType;
	}
	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}
}