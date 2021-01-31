package com.netflix.conductorboot.controller.rest.embedded.oauth2;

import java.security.Principal;

import com.netflix.conductorboot.constants.Constants;
import com.netflix.conductorboot.db.entities.embedded.oauth2.OAuthClientDetails;
import com.netflix.conductorboot.dtos.internal.embedded.oauth2.CustomPrincipalJsonConverted;
import com.netflix.conductorboot.dtos.request.embedded.oauth2.client.UserRequest;
import com.netflix.conductorboot.dtos.request.embedded.oauth2.client.UserResetPasswordRequest;
import com.netflix.conductorboot.dtos.response.embedded.oauth2.BaseResponseDTO;
import com.netflix.conductorboot.service.embedded.oauth2.OAuthUserActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

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
@RequestMapping("/oauth/user/")
@Tag(name = "OAuth2 User Actions Controller", description = "The API provides the interface for password update to User.")
public class OAuthUserActionsController {
	
	private Logger logger = LoggerFactory.getLogger(OAuthUserActionsController.class.getSimpleName());
	
	@Autowired
	private OAuthUserActionService oAuthUserActionService;

	@Operation(summary = "Provides User Profile Info", description = "Returns JSON formatted User Profile Info", tags = { "user" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully returned User Profile Info",
					content = @Content(schema = @Schema(implementation = CustomPrincipalJsonConverted.class))) ,
			@ApiResponse(responseCode = "401", description = "Invalid Access Token", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
	@GetMapping(value = "info", produces = "application/json")
	@ResponseBody
    public ResponseEntity<CustomPrincipalJsonConverted> user(Principal principal) {

		Gson gson = new Gson();

		String customPrincipalJsonStr = gson.toJson(principal);

		logger.info(customPrincipalJsonStr);

		CustomPrincipalJsonConverted customPrincipal = gson.fromJson(customPrincipalJsonStr, CustomPrincipalJsonConverted.class);
		
		return new ResponseEntity(customPrincipal, HttpStatus.OK);
    }
	
	@Operation(summary = "Update existing User i.e. onboard with Client ID and Email address", description = "Takes in the Username , Client ID, Email, Roles. Returns status of the action and error message if any", tags = { "user" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated User and persisted in Database", 
                content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))) ,
        @ApiResponse(responseCode = "400", description = "Invalid Client Id.", content = @Content()),
        @ApiResponse(responseCode = "404", description = "User Not Found.", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
	@PutMapping(value = "info", produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<BaseResponseDTO> updateUser(Principal principal, @RequestBody UserRequest request)
	{
		try {
			
			Gson gson = new Gson();
			
			String customPrincipalJsonStr = gson.toJson(principal);
			
			CustomPrincipalJsonConverted customPrincipal = gson.fromJson(customPrincipalJsonStr, CustomPrincipalJsonConverted.class);
			
			String client = customPrincipal.getStoredRequest().getClientId();
			
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
	@PatchMapping(value = "info", produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<BaseResponseDTO> resetUserPassword(Principal principal, @RequestBody UserResetPasswordRequest request)
	{
		try {
			
			BaseResponseDTO response = new BaseResponseDTO();
			
			Gson gson = new Gson();
			
			String customPrincipalJsonStr = gson.toJson(principal);
			
			CustomPrincipalJsonConverted customPrincipal = gson.fromJson(customPrincipalJsonStr, CustomPrincipalJsonConverted.class);  
			
			String client = customPrincipal.getStoredRequest().getClientId();
			
			String error = this.oAuthUserActionService.resetUserPassword(client, request.getUsername(), request.getPassword());
			
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
}
