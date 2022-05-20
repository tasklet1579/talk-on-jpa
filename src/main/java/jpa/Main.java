package jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import jpa.entity.Member;
import jpa.entity.MemberType;

public class Main {

	public static void main(String[] args) {
		System.out.printf("Hello JPA");
		
		// 엔티티 매니저 팩토리는 하나만 생성해서 애플리케이션 전체에서 공유
		// 엔티티 매니저는 쓰레드 간에 공유하면 안 된다(사용하고 버려야 한다)
		// JPA의 모든 데이터 변경은 트랜잭션 안에서 실행한다
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		try {
			Member member = new Member();		
//			member.setId(100L);
			member.setName("안녕하세요");
			member.setMemberType(MemberType.USER);
			
			em.persist(member);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		
		emf.close();
	}
}