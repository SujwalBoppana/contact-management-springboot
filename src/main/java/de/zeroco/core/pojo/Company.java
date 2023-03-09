package de.zeroco.core.pojo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "company")
public class Company {
	@Id
	@Column(name = "pk_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Contact> contacts;

	private String name;
	private String code;
	private String email;
	private String phone;
	private String website;
	private String address;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(name = "updated_time")
	private Timestamp updatedTime;

	public Company() {
		super();
	}

	public Company(int id, String name, String code, String email, String phone, String website, String address) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.email = email;
		this.phone = phone;
		this.website = website;
		this.address = address;
	}

	public Company(String name, String code, String email, String phone, String website, String address) {
		super();
		this.name = name;
		this.code = code;
		this.email = email;
		this.phone = phone;
		this.website = website;
		this.address = address;
	}

	@PrePersist
	protected void onCreate() {
		createdTime = Timestamp.from(Instant.now());
		updatedTime = createdTime;
	}

	@PreUpdate
	protected void onUpdate() {
		updatedTime = Timestamp.from(Instant.now());
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + ", code=" + code + ", email=" + email + ", phone=" + phone
				+ ", website=" + website + ", address=" + address + "]";
	}

}
