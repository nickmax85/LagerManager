package com.application.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Table(name = "mailconfig")
public class Mailconfig {

	private IntegerProperty id = new SimpleIntegerProperty();
	private StringProperty empfaenger = new SimpleStringProperty();

	public IntegerProperty idProperty() {
		return this.id;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return this.idProperty().get();
	}

	public void setId(final int id) {
		this.idProperty().set(id);
	}

	public StringProperty empfaengerProperty() {
		return this.empfaenger;
	}

	public String getEmpfaenger() {
		return this.empfaengerProperty().get();
	}

	public void setEmpfaenger(final String empfaenger) {
		this.empfaengerProperty().set(empfaenger);
	}

}
