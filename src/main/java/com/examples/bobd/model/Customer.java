package com.examples.bobd.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "customers", indexes = {
		@Index(columnList = "firstname"),
		@Index(columnList = "lastname"),
		@Index(columnList = "companyname")
})
@Value
@FieldDefaults(makeFinal = false, level = AccessLevel.PRIVATE) 
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", length = 36)
	String id;
	@Column(name = "firstname",	length = 50)
	String firstName;
	@Column(name = "lastname", length = 50)
	String lastName;
	@Column(name = "companyname", length = 50)
	String companyName;
}
