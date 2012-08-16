package org.jboss.tools.examples.service;

import java.net.UnknownHostException;
import java.util.ArrayList;
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
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
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
	//private DBCollection memberColl;

	@SuppressWarnings("unused")
	@PostConstruct
	private void initDB() throws UnknownHostException {
		morphia = new Morphia();
		mongo = new Mongo();
		//DB db = m.getDB("memberDB");
		
		ds = morphia.createDatastore(mongo, "memberDB");
		morphia.map(Member.class);
		
		
		/*memberColl = db.getCollection("members");
		if (memberColl == null) {
			memberColl = db.createCollection("members", null);
		}*/
	}

	@SuppressWarnings("unused")
	@PreDestroy
	private void closeDB() {
		mongo.close();
	}
	
	public void register(Member member) throws Exception {
		log.info("Registering " + member.getName());
		ds.save(member).getId();
		
		/*BasicDBObject doc = member.toDBObject();
		memberColl.insert(doc);
		member.setId(doc.get("_id").toString());*/
		memberEventSrc.fire(member);
	}

	public List<Member> listAllMembers() {
		return ds.find(Member.class).asList();
		/*List<Member> members = new ArrayList<Member>();
		DBCursor cur = memberColl.find();
		
		for (DBObject dbo : cur.toArray()) {
			members.add(Member.fromDBObject(dbo));
		}
		
		return members;*/
	}
	
	public Member findMemberById(String id) {
		return ds.find(Member.class).field("_id").equal(new ObjectId(id)).get();
		/*ObjectId oid = new ObjectId(id);
		DBObject res = memberColl.findOne(new BasicDBObject("_id", oid));
		return (Member) Member.fromDBObject(res);*/
	}
	
	public List<Member> listAllMembersOrderByName() {
		return ds.find(Member.class).order("name").asList();
		/*List<Member> members = new ArrayList<Member>();
		DBCursor cur = memberColl.find();
		cur.sort(new BasicDBObject("name", 1));
		
		for (DBObject dbo : cur.toArray()) {
			members.add(Member.fromDBObject(dbo));
		}
		
		return members;*/		
	}

}
