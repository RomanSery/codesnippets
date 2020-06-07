package org.coderdreams.dom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "car")
public class Car extends BaseEntity {
	private static final long serialVersionUID=1L;

	@Column(name = "car_year")
	private Integer year;

	@Column(name = "car_make")
	private String make;

	@Column(name = "car_model")
	private String model;

	@Column(name = "car_mileage")
	private Integer mileage;

	@Column(name = "car_condition")
	private String condition;

	public Car() {
		super();
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getMileage() {
		return mileage;
	}

	public void setMileage(Integer mileage) {
		this.mileage = mileage;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
}