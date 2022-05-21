package jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import jpa.entity.Member;
import jpa.entity.MemberType;
import jpa.entity.Team;

public class Main {

	public static void main(String[] args) {
		System.out.printf("Hello JPA");
		
		// 엔티티 매니저 팩토리는 하나만 생성해서 애플리케이션 전체에서 공유
		// 엔티티 매니저는 쓰레드 간에 공유하면 안 된다(사용하고 버려야 한다)
		// JPA의 모든 데이터 변경은 트랜잭션 안에서 실행한다
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		
//		fieldToColumnMapping(em);
		
//		oneWayAssociationRelationshipMapping(em);
		
//		bothWayAssociationRelationshipMapping(em);
		
		bothWayAssociationRelationshipMappingMistake(em);
		
		emf.close();
	}
	
	public static void fieldToColumnMapping(EntityManager em) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		try {			
			tx.begin();
			
			Member member = new Member();		
//			member.setId(100L);
			member.setName("안녕하세요");
//			member.setMemberType(MemberType.USER);
			em.persist(member);
			
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		}
	}
	
	public static void oneWayAssociationRelationshipMapping(EntityManager em) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		try {
			Team teamA = new Team();
			teamA.setName("teamA");
			em.persist(teamA);
			
			Member member = new Member();	
			member.setName("안녕하세요");
//			member.setTeamId(team.getId());
			member.setTeam(teamA);
			em.persist(member);
			
			// 조회 SQL을 확인하기 위해
			em.flush();
			em.clear();
			
			// 조회
//			Member findMember = em.find(Member.class, member.getId());
//			Long teamId = findMember.getTeamId();
			
			// 객체를 테이블에 맞추어 데이터 중심으로 모델링했기 때문에 연관관계가 없음
//			Team findTeam = em.find(Team.class, teamId);
			
			// 조회			
			Member findMember = em.find(Member.class, member.getId());
			
			// 참조를 사용해서 연관관계 조회
			Team findTeam = findMember.getTeam();
			findTeam.getName();
			
			// 연관관계 수정
			Team teamB = new Team();
			teamB.setName("teamB");
			em.persist(teamB);
			findMember.setTeam(teamB);
			
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		}
	}	
	
	public static void bothWayAssociationRelationshipMapping(EntityManager em) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		try {
			Team teamA = new Team();
			teamA.setName("teamA");
			em.persist(teamA);
			
			Member member = new Member();
			member.setName("안녕하세요");
			member.setTeam(teamA);
			em.persist(member);
			
			// 조회 SQL을 확인하기 위해
			em.flush();
			em.clear();
			
			// 조회			
			Member findMember = em.find(Member.class, member.getId());
			Team findTeam = findMember.getTeam();
			
			List<Member> members = findTeam.getMembers();
			for (Member member1 : members) {				
				System.out.println("member1 = " + member1);
			}
			
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		}
	}
	
	// 객체의 양방향 관계는 사실 서로 다른 방향 관계 2개이다
	// 객체를 양방향으로 참조하려면 단방향 연관관계를 2개 만들어야 한다
	// 테이블은 외래 키 하나로 두 테이블의 연관관계를 관리한다
	// 외래 키 하나로 두 테이블의 연관관계를 관리한다
	// --
	// 둘 중 하나로 외래 키를 관리해야 한다
	// - Member에서도 변경이 발생하고 Team에서도 변경이 발생하면 혼란을 주게된다
	//  - 무엇을 신뢰해야할지?
	// --
	// 양방향 매핑 규칙
	// - 객체의 두 관계 중 하나를 연관관계의 주인으로 지정
	// - 연관관계의 주인만이 외래 키를 관리(등록, 수정)
	//  - 외래 키가 있는 곳을 주인으로 정함
	// - 주인이 아닌쪽은 읽기만 가능
	// - 주인은 mappedBy 속성 사용 X
	// - 주인이 아니면 mappedBy 속성으로 주인 지정
	// --
	// 양방향 매핑의 장점
	// - 단방향 매핑만으로도 이미 연관관계 매핑은 완료
	// - 양방향 매핑은 반대 방향으로 조회(객체 그래프 탐색) 기능이 추가된 것 뿐
	// - JPQL에서 역방향으로 탐색할 일이 많음
	// - 단방향 매핑을 잘하고 양방향은 필요할 때 추가해도 됨(테이블에 영향을 주지 않음) 
	public static void bothWayAssociationRelationshipMappingMistake(EntityManager em) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		try {
			Team teamA = new Team();
			teamA.setName("teamA");
			em.persist(teamA);
			
			Member member = new Member();
			member.setName("안녕하세요");
//			member.setTeam(teamA);
			em.persist(member);
			
			// 연관관계의 주인에 값을 입력하지 않음
			teamA.getMembers().add(member);
			// 순수한 객체 관계를 고려하면 항상 양쪽다 값을 입력해야 한다 
			member.setTeam(teamA);
			
			// 조회 SQL을 확인하기 위해
			em.flush();
			em.clear();
			
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		}
	}
}