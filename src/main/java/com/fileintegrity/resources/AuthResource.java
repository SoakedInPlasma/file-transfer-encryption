/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fileintegrity.resources;

import com.fileintegrity.model.UserCredentials;
import com.fileintegrity.services.AuthService;
import com.fileintegrity.repository.UserMemoryStore;
import jakarta.ws.rs.GET;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author marti
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final AuthService authService = new AuthService();

    //this method parses the register fields as a json entity 
    @POST
    @Path("/register")
    public Response registerRequest(UserCredentials userCredentials) {
        try {
            //boolean value to track if register method worked and saved to DB
            boolean isRegisterSuccessful = authService.registerUser(userCredentials);
            //if not valid return a bad request Response
            if (!isRegisterSuccessful) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\" : \"Registration failed. Username may already be taken or inputs are invalid..\"}").build();
            }
            //if it passes the conditonal, returns a CREATED RESPONSE
            return Response.status(Response.Status.CREATED).entity("{\"message\" : \"User registered successfully\"}").build();
        } //catch incase there is an error throughout process
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("\"error\" : \"Server error during registration...\"}").build();
        }

    }

    @POST
    @Path("/login")
    public Response loginRequest(UserCredentials userCredentials, @Context HttpServletRequest request) {
        //try/catch to validate login
        try {
            //boolean to track whether the login was successful
            boolean isValid = authService.loginUser(userCredentials);
            //if not valid, returns UNAUTH response to browser
            if (!isValid) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\" : \" Invalid user name or password...\"}").build();
            }
            //create HTTP session to track authenticated users log in
            HttpSession session = request.getSession(true);
            session.setAttribute("user", userCredentials.getUserName().trim());
            //returns 200 ok response when log in is successful
            return Response.ok("{\"message\" : \" Login Successful! \"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("\"error\" : \"Server error during registration...\"}").build();

        }

    }
    
    @POST
    @Path("/logout")
    public Response logoutRequest(@Context HttpServletRequest request){
        //fetch existing session data without creating new one
        HttpSession session = request.getSession(false);
        
        //invalidate the session id to log out
        if(session != null){
            session.invalidate();
        }
        
        return Response.ok("{\"message\" : \" Logout successful!!! \"}").build();
        }

        // Return registered usernames for the recipient dropdown
        @GET
        @Path("/users")
        public Response getRegisteredUsers(@Context HttpServletRequest request) {
            HttpSession session = request.getSession(false);

            // Only logged-in users can request the recipient list
            if (session == null || session.getAttribute("user") == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\":\"Please log in.\"}")
                .build();
            }

            String currentUser = (String) session.getAttribute("user");

            // Exclude the logged-in user from the recipient list
            List<String> usernames = UserMemoryStore.getAllUsernames()
                .stream()
                .filter(username -> !username.equals(currentUser))
                .toList();

            return Response.ok(usernames).build();
        }

}


