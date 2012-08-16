package org.jboss.tools.examples.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.tools.examples.model.Member;
import org.jboss.tools.examples.service.MemberRegistration;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members table.
 */
@Path("/members")
@RequestScoped
public class MemberResourceRESTService {
   @Inject
   private MemberRegistration memberRegistration;

   @GET
   @Produces("text/xml")
   public List<Member> listAllMembers() {
      return memberRegistration.listAllMembers();
   }

   @GET
   @Path("/{id:[0-9a-f][0-9a-f]*}")
   @Produces("text/xml")
   public Member lookupMemberById(@PathParam("id") String id) {
      return memberRegistration.findMemberById(id);
   }
}
