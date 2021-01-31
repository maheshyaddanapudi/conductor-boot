package com.github.maheshyaddanapudi.netflix.conductorboot.dtos.response.embedded.oauth2;

public class BaseResponseDTO {
	
	private boolean status;
	private String message;
	
	public BaseResponseDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BaseResponseDTO(boolean status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "BaseResponseDTO [status=" + status + ", message=" + message + "]";
	}
}
