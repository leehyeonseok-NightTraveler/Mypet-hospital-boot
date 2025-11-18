package com.boot.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class PetWeightDTO {
	double weight_kg;
	Date record_date;
}
