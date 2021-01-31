package com.github.my.netflix.conductorboot.db.entities.embedded.oauth2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="oauth_role")
public class OAuthRole {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private int id;
	
	@Column(name = "name", nullable = false, unique = true)
	private String name;

	public OAuthRole() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OAuthRole(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "OAuthRole [id=" + id + ", name=" + name + "]";
	}	
}
