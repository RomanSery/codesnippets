package org.coderdreams.dom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.coderdreams.enums.CarCondition;

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

	@Enumerated(value = EnumType.STRING)
	@Column(name = "car_condition")
	private CarCondition condition;

	@Column(name = "engine_starts")
	private Boolean engineStarts;

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

	public CarCondition getCondition() {
		return condition;
	}

	public void setCondition(CarCondition condition) {
		this.condition = condition;
	}

	public Boolean getEngineStarts() {
		return engineStarts;
	}

	public void setEngineStarts(Boolean engineStarts) {
		this.engineStarts = engineStarts;
	}
}