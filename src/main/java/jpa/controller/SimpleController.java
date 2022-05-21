package jpa.controller;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jpa.entity.Member;
import jpa.entity.QMember;
import jpa.repository.MemberRepository;

@RestController
public class SimpleController {

	@Autowired
	MemberRepository repository;
	
	@PostConstruct
	public void init() {
		repository.save(new Member("member1", 1));
		repository.save(new Member("member2", 2));
		repository.save(new Member("member3", 3));
		repository.save(new Member("member4", 4));
		repository.save(new Member("member5", 5));
	}
	
	@RequestMapping(value = "/member", method = RequestMethod.GET)
	public Page<Member> findByName() {
		PageRequest pageRequest = new PageRequest(0, 10);
		return repository.findByName("member1", pageRequest);
	}
	
	@RequestMapping(value = "/member-page", method = RequestMethod.GET)
	public Page<Member> findByNameWithPage(String name) {
		PageRequest pageRequest = new PageRequest(0, 10);
		return repository.findByName(name, pageRequest);
	}
	
	@Autowired
	EntityManager em;
	
	public void jpqa() {
		JPAQueryFactory factory = new JPAQueryFactory(em);
		QMember m = QMember.member;
		// 동적 SQL 생성 가능
		BooleanBuilder builder = new BooleanBuilder();
//		if () {
//			builder.and(m.name.contains())
//		}
		factory.selectFrom(m)
				.where(m.age.gt(18).and(m.name.contains("hello")))
//				.where(builder)
//				.where(builder, isServiceable())
				.orderBy(m.age.desc())
				.limit(10)
				.offset(10)
				.fetch();
	}
	
	// 제약조건 조립가능
	// - 가독성, 재사용
//	private BooleanExpression isServiceable() {
//		return 
//	}
}
