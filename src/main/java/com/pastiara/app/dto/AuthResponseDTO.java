package com.pastiara.app.dto;

public class AuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer";
    
    public AuthResponseDTO() {}
    
    

    public AuthResponseDTO(String accessToken, String tokenType) {
		super();
		this.accessToken = accessToken;
		this.tokenType = tokenType;
	}



	public AuthResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }



	public String getAccessToken() {
		return accessToken;
	}



	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}



	public String getTokenType() {
		return tokenType;
	}



	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}



	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AuthResponseDTO [accessToken=");
		builder.append(accessToken);
		builder.append(", tokenType=");
		builder.append(tokenType);
		builder.append("]");
		return builder.toString();
	}
	
	
}