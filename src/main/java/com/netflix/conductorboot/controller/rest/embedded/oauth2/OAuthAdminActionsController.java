package com.netflix.conductorboot.controller.rest.embedded.oauth2;

import com.netflix.conductorboot.constants.Constants;
import com.netflix.conductorboot.db.entities.embedded.oauth2.OAuthClientDetails;
import com.netflix.conductorboot.dtos.request.embedded.oauth2.admin.*;
import com.netflix.conductorboot.dtos.response.embedded.oauth2.AutogeneratedPasswordResponseDTO;
import com.netflix.conductorboot.dtos.response.embedded.oauth2.BaseResponseDTO;
import com.netflix.conductorboot.service.embedded.oauth2.OAuthUserActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Profile(Constants.EMBEDDED_OAUTH2)
@RestController
@SuppressWarnings({ "unchecked", "rawtypes" })
@CrossOrigin(origins = "*")
@RequestMapping("/oauth/admin/")
@Tag(name = "OAuth2 Admin Actions Controller", description = "The API provides the interface for adding / removing or updating roles or users to database.")
public class OAuthAdminActionsController {
	
private Logger logger = LoggerFactory.getLogger(OAuthAdminActionsController.class.getSimpleName());
	
	@Autowired
	private OAuthUserActionService oAuthUserActionService;

	@Operation(summary = "Add new Client i.e. onboard with generic Client ID and Client Secret", description = "Taken in the new Client ID , Client Secret, Access token expiration & Refresh token expiration times. Returns a status of the action with a message.", tags = { "client" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully onboarded new Client and persisted in Database", 
                content = @Content(schema = @Schema(implementation = AutogeneratedPasswordResponseDTO.class))) ,
        @ApiResponse(responseCode = "404", description = "Duplicate Client ID Found.", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
	@PostMapping(value = "client", produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<AutogeneratedPasswordResponseDTO> addClient(@RequestBody ClientRequest request)
	{
		try {
			
			AutogeneratedPasswordResponseDTO response = new AutogeneratedPasswordResponseDTO();
			OAuthClientDetails clientDetails = this.oAuthUserActionService.getClientDetailsByClientId(request.getClientId());
			
			if(null!=clientDetails && clientDetails.getId() > 0)
			{
				logger.warn("Found duplicate: "+clientDetails.toString());
				response.setStatus(false);
				response.setMessage("Duplicate client ID found.");
				return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
			}
			else
			{
				String scope = Constants.STRING_INITIALIZR;
				
				for(String aScope: request.getScope())
				{
					if(!Constants.STRING_INITIALIZR.equalsIgnoreCase(scope))
					{
						scope = scope + Constants.COMMA + aScope;
					}
					else
						scope = aScope;
				}
				
				String autoGeneratedClientSecret = this.oAuthUserActionService.createNewClient(request.getClientId(), scope, request.getAccessTokenValidityInSeconds(), request.getRefreshTokenValidityInSeconds());
				
				if(null!=autoGeneratedClientSecret)
				{
					response.setStatus(true);
					response.setAutoGeneratedPassword(autoGeneratedClientSecret);
					response.setMessage("Client Onboarded successfully.");
					return new ResponseEntity(response, HttpStatus.OK);
				}
			
			}
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		
		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Operation(summary = "Update existing Client i.e. Client ID and rest of details to update", description = "Taken in the new Client ID , Client Secret, Access token expiration & Refresh token expiration times. Returns a status of the action with a message.", tags = { "client" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Client and persisted in Database", 
                content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))) ,
        @ApiResponse(responseCode = "400", description = "Client ID Not Found", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
	@PutMapping(value = "client", produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<BaseResponseDTO> updateClient(@RequestBody ClientRequest request)
	{
		try {
			
			BaseResponseDTO response = new BaseResponseDTO();
			OAuthClientDetails clientDetails = this.oAuthUserActionService.getClientDetailsByClientId(request.getClientId());
			
			if(null==clientDetails)
			{
				response.setStatus(false);
				response.setMessage("Client ID NOT found.");
				return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
			}
			else
			{
				String scope = Constants.STRING_INITIALIZR;
				
				for(String aScope: request.getScope())
				{
					if(!Constants.STRING_INITIALIZR.equalsIgnoreCase(scope))
					{
						scope = scope + Constants.COMMA + aScope;
					}
					else
						scope = aScope;
				}
				
				boolean result = this.oAuthUserActionService.updateExistingClient(request.getClientId(), scope, request.getAccessTokenValidityInSeconds(), request.getRefreshTokenValidityInSeconds());
				
				if(result)
				{
					response.setStatus(result);
					response.setMessage("Client Updated successfully.");
					return new ResponseEntity(response, HttpStatus.OK);
				}
			
			}
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		
		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Operation(summary = "Reset existing Client secret by Client ID", description = "Taken in the Client ID. Returns a status of the action with a message.", tags = { "client" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully reset password.", 
                content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))) ,
        @ApiResponse(responseCode = "400", description = "Client ID Not Found", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
	@PatchMapping(value = "client", produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<BaseResponseDTO> resetClientSecret(@RequestBody ClientResetPasswordWithClienIdRequest request)
	{
		try {
			
			BaseResponseDTO response = new BaseResponseDTO();
			
			OAuthClientDetails clientDetails = this.oAuthUserActionService.getClientDetailsByClientId(request.getClientId());
			
			if(null==clientDetails)
			{
				response.setStatus(false);
				response.setMessage("Client ID NOT found.");
				return new ResponseEntity(response, HttpStatus.NOT_FOUND);
			}
			else
			{	
				boolean result = this.oAuthUserActionService.resetExistingClientPassword(request.getClientId(), request.getClientSecret());
			
				if(result)
				{
					response.setStatus(result);
					response.setMessage("Client Secret Reset successfully.");
					return new ResponseEntity(response, HttpStatus.OK);
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		
		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Operation(summary = "Delete existing Client by Client ID", description = "Taken in the Client ID. Returns a status of the action with a message.", tags = { "client" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted client", 
                content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))) ,
        @ApiResponse(responseCode = "400", description = "Client ID Not Found", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
	@DeleteMapping(value = "client", produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<BaseResponseDTO> deleteClient(@RequestBody ClientDeleteRequest request)
	{
		try {
			
			BaseResponseDTO response = new BaseResponseDTO();
			
			OAuthClientDetails clientDetails = this.oAuthUserActionService.getClientDetailsByClientId(request.getClientId());
			
			if(null==clientDetails)
			{
				response.setStatus(false);
				response.setMessage("Client ID NOT found.");
				return new ResponseEntity(response, HttpStatus.NOT_FOUND);
			}
			else
			{	
				boolean result = this.oAuthUserActionService.deleteExistingClient(request.getClientId());
			
				if(result)
				{
					response.setStatus(result);
					response.setMessage("Client Secret Deleted successfully.");
					return new ResponseEntity(response, HttpStatus.OK);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}
		
	@Operation(summary = "Add new User i.e. onboard with Client ID and Email address", description = "Takes in the new Username , Client ID, Email. Returns status of the action and message with an auto generated password which user should change on first login.", tags = { "user" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully onboarded new User and persisted in Database", 
                content = @Content(schema = @Schema(implementation = AutogeneratedPasswordResponseDTO.class))) ,
        @ApiResponse(responseCode = "400", description = "Duplicate User ID Found or Invalid Client Id.", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
	@PostMapping(value = "user", produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<AutogeneratedPasswordResponseDTO> addUser(@RequestBody UserWithClientIDRequest request)
	{
		try {
			
			String client = request.getClient();
			
			AutogeneratedPasswordResponseDTO response = new AutogeneratedPasswordResponseDTO();
			OAuthClientDetails clientDetails = this.oAuthUserActionService.getClientDetailsByClientId(client);
			
			if(null==clientDetails)
			{
				response.setStatus(false);
				response.setMessage("Client ID Not Found.");
				return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
			}
			else
			{
				String autoGeneratedPassword = this.oAuthUserActionService.createNewUser(request.getUsername(), request.getEmail(), client, request.getRoles(), true, false, false, false, true);
				
				if(null != autoGeneratedPassword)
				{
					response.setStatus(true);
					response.setMessage("User Onboarded successfully");
					response.setAutoGeneratedPassword(autoGeneratedPassword);
					return new ResponseEntity(response, HttpStatus.OK);
				}
				else
				{
					response.setStatus(false);
					response.setMessage("Duplicate Username found.");
					return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		
		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Operation(summary = "Update existing User i.e. onboard with Client ID and Email address", description = "Takes in the Username , Client ID, Email, Roles. Returns status of the action and error message if any", tags = { "user" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated User and persisted in Database", 
                content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))) ,
        @ApiResponse(responseCode = "400", description = "Invalid Client Id.", content = @Content()),
        @ApiResponse(responseCode = "404", description = "User Not Found.", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
	@PutMapping(value = "user", produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<BaseResponseDTO> updateUser(@RequestBody UserWithClientIDRequest request)
	{
		try {
			
			String client = request.getClient();
			
			BaseResponseDTO response = new BaseResponseDTO();
			OAuthClientDetails clientDetails = this.oAuthUserActionService.getClientDetailsByClientId(client);
			
			if(null==clientDetails)
			{
				response.setStatus(false);
				response.setMessage("Client ID Not Found.");
				return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
			}
			else
			{
				String error = this.oAuthUserActionService.updateUser(client, request.getUsername(), request.getEmail(), request.getRoles(), true);
				
				if(null != error)
				{
					response.setStatus(true);
					response.setMessage("User updated successfully");
					return new ResponseEntity(response, HttpStatus.OK);
				}
				else
				{
					response.setStatus(false);
					response.setMessage(error);
					return new ResponseEntity(response, HttpStatus.NOT_FOUND);
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		
		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Operation(summary = "Reset existing User's password by username", description = "Taken in the username and new password. Returns a status of the action with a message.", tags = { "user" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully reset password.", 
                content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))) ,
        @ApiResponse(responseCode = "400", description = "Invalid Client Id.", content = @Content()),
        @ApiResponse(responseCode = "404", description = "User Not Found", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
	@PatchMapping(value = "user", produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<BaseResponseDTO> resetUserPassword(@RequestBody UserResetPasswordWithClienIdRequest request)
	{
		try {
			
			BaseResponseDTO response = new BaseResponseDTO();
			
			String error = this.oAuthUserActionService.resetUserPassword(request.getClientId(), request.getUsername(), request.getPassword());
			
			if(null==error)
			{
				response.setStatus(true);
				response.setMessage("User password updated successfully.");
				return new ResponseEntity(response, HttpStatus.OK);
			}
			else if("User NOT found.".equalsIgnoreCase(error))
			{
				response.setStatus(false);
				response.setMessage(error);
				return new ResponseEntity(response, HttpStatus.NOT_FOUND);
			}
			else
			{
				response.setStatus(false);
				response.setMessage(error);
				return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
			
			}
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		
		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Operation(summary = "Delete existing User by Client ID and Username", description = "Taken in the Client ID and Username. Returns a status of the action with a message.", tags = { "user" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted user", 
                content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))) ,
        @ApiResponse(responseCode = "400", description = "Client ID Not Matched", content = @Content()),
        @ApiResponse(responseCode = "404", description = "User Not Found.", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
	@DeleteMapping(value = "user", produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<BaseResponseDTO> deleteUser(@RequestBody UserDeleteWithClienIdRequest request)
	{
		try {
			
			BaseResponseDTO response = new BaseResponseDTO();
			
			String error = this.oAuthUserActionService.deleteExistingUser(request.getClientId(), request.getUsername());
			
			if(null!=error)
			{
				if("User NOT found.".equalsIgnoreCase(error))
				{
					response.setStatus(false);
					response.setMessage(error);
					return new ResponseEntity(response, HttpStatus.NOT_FOUND);
				}
				else
				{
					response.setStatus(false);
					response.setMessage(error);
					return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
				
				}
			}
			else
			{
				response.setStatus(true);
				response.setMessage("User Deleted successfully.");
				return new ResponseEntity(response, HttpStatus.OK);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}