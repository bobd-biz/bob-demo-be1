package com.examples.bobd.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
		@Index(columnList = "firstName"),
		@Index(columnList = "lastName"),
		@Index(columnList = "companyName")
})
@Value
@FieldDefaults(makeFinal = false, level = AccessLevel.PRIVATE) 
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
	
	@Id
	String id;
	@Column(name = "firstname")
	String firstName;
	@Column(name = "lastname")
	String lastName;
	@Column(name = "companyname")
	String companyName;
}
