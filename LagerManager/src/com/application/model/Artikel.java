package com.application.model;

import java.io.File;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Artikel {

	private IntegerProperty id = new SimpleIntegerProperty();
	private StringProperty name = new SimpleStringProperty();
	private StringProperty maschine = new SimpleStringProperty();
	private StringProperty box = new SimpleStringProperty();
	private StringProperty hersteller = new SimpleStringProperty();
	private IntegerProperty stueck = new SimpleIntegerProperty();
	private StringProperty info = new SimpleStringProperty();
	private StringProperty author = new SimpleStringProperty();
	private StringProperty komponenteNr = new SimpleStringProperty();
	private BooleanProperty anhang = new SimpleBooleanProperty();
	private File picture;

	private int lagerortId;
	private ObjectProperty<Lagerort> lagerort = new SimpleObjectProperty<>();

	public IntegerProperty idProperty() {
		return this.id;
	}

	public int getId() {
		return this.idProperty().get();
	}

	public void setId(final int id) {
		this.idProperty().set(id);
	}

	public StringProperty nameProperty() {
		return this.name;
	}

	public String getName() {
		return this.nameProperty().get();
	}

	public void setName(final String name) {
		this.nameProperty().set(name);
	}

	public StringProperty herstellerProperty() {
		return this.hersteller;
	}

	public String getHersteller() {
		return this.herstellerProperty().get();
	}

	public void setHersteller(final String hersteller) {
		this.herstellerProperty().set(hersteller);
	}

	public IntegerProperty stueckProperty() {
		return this.stueck;
	}

	public int getStueck() {
		return this.stueckProperty().get();
	}

	public void setStueck(final int stueck) {
		this.stueckProperty().set(stueck);
	}

	public StringProperty infoProperty() {
		return this.info;
	}

	public String getInfo() {
		return this.infoProperty().get();
	}

	public void setInfo(final String info) {
		this.infoProperty().set(info);
	}

	@Override
	public String toString() {
		return "Artikel [name=" + name + ", info=" + info + "]";
	}

	public BooleanProperty anhangProperty() {
		return this.anhang;
	}

	public boolean isAnhang() {
		return this.anhangProperty().get();
	}

	public void setAnhang(final boolean anhang) {
		this.anhangProperty().set(anhang);
	}

	public StringProperty maschineProperty() {
		return this.maschine;
	}

	public String getMaschine() {
		return this.maschineProperty().get();
	}

	public void setMaschine(final String maschine) {
		this.maschineProperty().set(maschine);
	}

	public StringProperty boxProperty() {
		return this.box;
	}

	public String getBox() {
		return this.boxProperty().get();
	}

	public void setBox(final String box) {
		this.boxProperty().set(box);
	}

	public StringProperty authorProperty() {
		return this.author;
	}

	public String getAuthor() {
		return this.authorProperty().get();
	}

	public void setAuthor(final String author) {
		this.authorProperty().set(author);
	}

	public File getPicture() {
		return picture;
	}

	public void setPicture(File picture) {
		this.picture = picture;
	}

	public StringProperty komponenteNrProperty() {
		return this.komponenteNr;
	}

	public String getKomponenteNr() {
		return this.komponenteNrProperty().get();
	}

	public void setKomponenteNr(final String komponenteNr) {
		this.komponenteNrProperty().set(komponenteNr);
	}

	public ObjectProperty<Lagerort> lagerortProperty() {
		return this.lagerort;
	}

	public Lagerort getLagerort() {
		return this.lagerortProperty().get();
	}

	public void setLagerort(final Lagerort lagerort) {
		this.lagerortProperty().set(lagerort);
	}

	public int getLagerortId() {
		return lagerortId;
	}

	public void setLagerortId(int lagerortId) {
		this.lagerortId = lagerortId;
	}

}
