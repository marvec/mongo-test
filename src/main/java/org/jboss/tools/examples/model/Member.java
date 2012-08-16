package org.jboss.tools.examples.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Entity
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Member implements Serializable {
	/** Default value included to remove warning. Remove or modify at will. **/
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private String id = null;

	@NotNull
	@Size(min = 1, max = 25)
	@Pattern(regexp = "[A-Za-z ]*", message = "must contain only letters and spaces")
	private String name;

	@NotNull
	@NotEmpty
	@Email
	private String email;

	@NotNull
	@Size(min = 10, max = 12)
	@Digits(fraction = 0, integer = 12)
	@Column(name = "phone_number")
	private String phoneNumber;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public BasicDBObject toDBObject() {
		BasicDBObject doc = new BasicDBObject();

		if (id != null) {
			doc.put("_id", new ObjectId(id));
		}
		doc.put("name", name);
		doc.put("email", email);
		doc.put("phone", phoneNumber);
		
		return doc;
	}

	public static Member fromDBObject(DBObject doc) {
		Member m = new Member();

		m.id = doc.get("_id").toString();
		System.out.println(doc.get("_id"));
		m.name = (String) doc.get("name");
		m.email = (String) doc.get("email");
		m.phoneNumber = (String) doc.get("phone");

		return m;
	}
}