package com.examples.bobd.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "companies", indexes = {
		@Index(columnList = "companyName")
})
@Value
@FieldDefaults(makeFinal = false, level = AccessLevel.PRIVATE) 
@AllArgsConstructor
@NoArgsConstructor
public class Company {
	
	@Id
	@GeneratedValue
	Long id;
	String companyName;
}
