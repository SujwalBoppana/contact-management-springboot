package de.zeroco.core.pojo;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name ="contact")
public class Contact {
	
	@ManyToOne(targetEntity = Company.class,cascade = CascadeType.DETACH)
	@JoinColumn(name = "company_id" , referencedColumnName = "pk_id")
	@JsonIgnore
	private Company company; 
	
	@Id
	@Column(name="pk_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String email;
	private String phone;
	private Date dob;
	private int age;
	private String address;
	private String gender;
	
	@Column(name = "created_time")
    private Timestamp createdTime;

    @Column(name = "updated_time")
    private Timestamp updatedTime;

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

	public Contact() {
		super();
	}
	
	public Contact(int id, String name, String email, String phone, Date dob, int age, String address, String gender,
			Company company) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.dob = dob;
		this.age = age;
		this.address = address;
		this.gender = gender;
		this.company =company;
	}
	
	public Contact( String name, String email, String phone, Date dob, int age, String address, String gender,
			Company companyId) {
		super();
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.dob = dob;
		this.age = age;
		this.address = address;
		this.gender = gender;
		this.company =companyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", dob=" + dob
				+ ", age=" + age + ", address=" + address + ", gender=" + gender + ", companyId=" + company + "]";
	}
	
}
