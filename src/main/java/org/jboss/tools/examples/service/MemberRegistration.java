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

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
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

	private Mongo m;
	private DBCollection memberColl;

	@SuppressWarnings("unused")
	@PostConstruct
	private void initDB() throws UnknownHostException {
		m = new Mongo();
		DB db = m.getDB("memberDB");
		memberColl = db.getCollection("members");
		if (memberColl == null) {
			memberColl = db.createCollection("members", null);
		}
	}

	@SuppressWarnings("unused")
	@PreDestroy
	private void closeDB() {
		m.close();
	}
	
	public void register(Member member) throws Exception {
		log.info("Registering " + member.getName());
		BasicDBObject doc = member.toDBObject();
		memberColl.insert(doc);
		member.setId((ObjectId) doc.get("_id"));
		memberEventSrc.fire(member);
	}

	public List<Member> listAllMembers() {
		List<Member> members = new ArrayList<Member>();
		DBCursor cur = memberColl.find();
		
		for (DBObject dbo : cur.toArray()) {
			members.add(Member.fromDBObject(dbo));
		}
		
		return members;
	}
	
	public Member findMemberById(String id) {
		ObjectId oid = new ObjectId(id);
		return (Member) memberColl.findOne(new BasicDBObject().put("_id", oid));
	}

}
