package com.fileintegrity.resources;

import java.util.List;

import com.fileintegrity.model.FileDownloadResponse;
import com.fileintegrity.model.FileUploadRequest;
import com.fileintegrity.model.PackageSummaryDTO;
import com.fileintegrity.services.FileTransferService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/* 
 * @author Martin
 */

@Path("/files")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FileResource {
    private final FileTransferService fileTransferService = new FileTransferService();

    //endpoint for sending files, takes in a FileUploadRequest object and the HttpServletRequest object to get the authenticated user
    @POST 
    @Path("/send")
    public Response sendFile(FileUploadRequest uploadRequest, @Context HttpServletRequest request){
        String currentUser = getAuthenticatedUser(request);
        //if the user is not authenticated, return an UNAUTHORIZED response
        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Unauthorized. Please log in.\"}")
                    .build();
        }

        //try catch to handle any exceptions thrown by the sendFile method in FileTransferService
        try {
            String packageId = fileTransferService.sendFile(uploadRequest, currentUser);
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"File encrypted, signed, and sent!\", \"packageId\": \"" + packageId + "\"}")
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Server error while encrypting and sending file.\"}")
                    .build();
        }        
    }

    //endpoint for getting the inbox for the authenticated user, returns a list of PackageSummaryDTO objects
    @GET
    @Path("/inbox")
    public Response getInbox(@Context HttpServletRequest request) {
        String currentUser = getAuthenticatedUser(request);
        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Unauthorized. Please log in.\"}")
                    .build();
        }

        List<PackageSummaryDTO> inbox = fileTransferService.getInboxForUser(currentUser);
        return Response.ok(inbox).build();
    }

    //endpoint for downloading and verifying a file, takes in the package id and the HttpServletRequest object to get the authenticated user
    @GET
    @Path("/download/{id}")
    public Response downloadFile(@PathParam("id") String packageId, @Context HttpServletRequest request) {
        String currentUser = getAuthenticatedUser(request); //get the authenticated user from the session
        //if the user is not authenticated, return an UNAUTHORIZED response
        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Unauthorized. Please log in.\"}")
                    .build();
        }
        //try catch to handle any exceptions thrown by the downloadAndVerify method in FileTransferService
        try {
            FileDownloadResponse responseDto = fileTransferService.downloadAndVerify(packageId, currentUser);
            if (responseDto == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"File package not found.\"}")
                        .build();
            }

            return Response.ok(responseDto).build();

        } catch (SecurityException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to decrypt or verify file.\"}")
                    .build();
        }
    }
    //helper method to get the authenticated user from the session
    private String getAuthenticatedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (String) session.getAttribute("user") : null;
  
    }
    

}
