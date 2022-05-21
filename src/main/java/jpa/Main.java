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
		
		bothWayAssociationRelationshipMapping(em);
		
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
}