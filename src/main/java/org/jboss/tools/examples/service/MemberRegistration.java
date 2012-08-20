package org.jboss.tools.examples.service;

import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.jboss.tools.examples.model.Member;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class MemberRegistration {

	@Inject
	private Logger log;

	@Inject
	private Event<Member> memberEventSrc;

	private Morphia morphia;
	private Mongo mongo;
	private Datastore ds;

	@SuppressWarnings("unused")
	@PostConstruct
	private void initDB() throws UnknownHostException {
		morphia = new Morphia();
		mongo = new Mongo();
		
		ds = morphia.createDatastore(mongo, "memberDB");
		morphia.map(Member.class);
	}

	@SuppressWarnings("unused")
	@PreDestroy
	private void closeDB() {
		mongo.close();
	}
	
	public void register(Member member) throws Exception {
		log.info("Registering " + member.getName());
		ds.save(member);
		
		memberEventSrc.fire(member);
	}

	public List<Member> listAllMembers() {
		return ds.find(Member.class).asList();
	}
	
	public Member findMemberById(String id) {
		return ds.find(Member.class).field("_id").equal(new ObjectId(id)).get();
	}
	
	public List<Member> listAllMembersOrderByName() {
		return ds.find(Member.class).order("name").asList();
	}

}
