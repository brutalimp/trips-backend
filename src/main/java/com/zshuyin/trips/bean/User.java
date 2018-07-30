package com.zshuyin.trips.bean;

import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User {

    @Id
    private String id;
    
    private String userName;
    
    //@JsonIgnore
    private String password;

    @Builder.Default()
    private String role = "Standard";

    public String getId() {
		return id;
	}
 
	public void setId(String id) {
		this.id = id;
	}
 
	public String getName() {
		return userName;
	}
 
	public void setName(String userName) {
		this.userName = userName;
	}
 
	public String getPassword() {
		return password;
	}
 
	public void setPassword(String password) {
		this.password = password;
	}
 
    public String getRole() {
		return role;
	}
 
	public void setRole(String role) {
		this.role = role;
    }
    
}