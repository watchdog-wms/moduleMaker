package gui.parseOptions.parseOverview;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;

public class Option {
	
    //private BooleanProperty checkedBox = new SimpleBooleanProperty(false);
    
    private StringProperty name = new SimpleStringProperty();
    
    private StringProperty shortOpt = new SimpleStringProperty();

    private StringProperty longOpt = new SimpleStringProperty();
    
    private StringProperty comboBox = new SimpleStringProperty("string");
     
    private StringProperty max = new SimpleStringProperty();
    
    private StringProperty min = new SimpleStringProperty();

    private StringProperty defaultV = new SimpleStringProperty();
    
    private StringProperty restriction = new SimpleStringProperty();

    private StringProperty description = new SimpleStringProperty();
    
    private String call;
    
    private StringProperty callType = new SimpleStringProperty();

    private StringProperty index = new SimpleStringProperty();
    
    private boolean isMod = false;

    
    
    /*public Option() {
    	super();
    }*/
        
    public Option(String index, String name, String shortOpt, String longOpt, String comboBoxValue, String min, String max, String defaultV, String restriction, String description, String call, String callType){
       // this.checkedBox = new SimpleBooleanProperty(checkedBox);
        this.shortOpt = new SimpleStringProperty(shortOpt);
        this.longOpt = new SimpleStringProperty(longOpt);
        this.name = new SimpleStringProperty(name);
        this.comboBox = new SimpleStringProperty(comboBoxValue);
        this.min = new SimpleStringProperty(min);
        this.max = new SimpleStringProperty(max);
        this.defaultV = new SimpleStringProperty(defaultV);
        this.restriction = new SimpleStringProperty(restriction);
        this.description = new SimpleStringProperty(description);
        this.call = call;
        this.callType = new SimpleStringProperty(callType);
        this.index = new SimpleStringProperty(index);
        
        ChangeListener<String> modListener = (a, b, c) -> this.isMod = true;
        this.shortOpt.addListener(modListener);
        this.longOpt.addListener(modListener);
        this.name.addListener(modListener);
        this.comboBox.addListener(modListener);
        this.min.addListener(modListener);
        this.max.addListener(modListener);
        this.defaultV.addListener(modListener);
        this.restriction.addListener(modListener);
        this.callType.addListener(modListener);
        this.index.addListener(modListener);

    }
    
    public boolean isModified() {
    	return this.isMod;
    }
    
    public void resetIsMod() {
    	this.isMod = false;
    }
    
    public String getCall() {
    	return call;
    }
    
    public String getType() {
    	return this.comboBoxProperty().get();
    }
    
  /*  public BooleanProperty checkedBoxProperty(){
        return this.checkedBox;
    }
    
    public java.lang.Boolean getSelectBox() {
        return this.checkedBoxProperty().get();
    }

    public void setSelectBox(final java.lang.Boolean checkedBox){
        this.checkedBoxProperty().set(checkedBox);
    }*/
    
    public StringProperty indexProperty() {
    	return this.index;
    }
    
    public String getIndex() {
    	return this.indexProperty().get();
    }
    
    public void setIndex(final String index) {
    	this.indexProperty().get();
    }
    
    public StringProperty shortOptProperty(){
        return this.shortOpt;
    }
    
    public java.lang.String getShortOpt() {
        return this.shortOptProperty().get();
    }

    public void setShortOpt(final java.lang.String shortOpt){
        this.shortOptProperty().set(shortOpt);
    }
    
    public StringProperty longOptProperty(){
        return this.longOpt;
    }
    
    public java.lang.String getLongOpt() {
        return this.longOptProperty().get();
    }

    public void setLongOpt(final java.lang.String longOpt){
        this.longOptProperty().set(longOpt);
    }
    
    public StringProperty nameProperty(){
        return this.name;
    }
    
    public java.lang.String getName() {
        return this.nameProperty().get();
    }

    public void setName(final java.lang.String name){
        this.nameProperty().set(name);
    }

    
    
    public StringProperty comboBoxProperty(){
        return this.comboBox;
    }
    
    public java.lang.String getComboBox() {
        return this.comboBoxProperty().get();
    }

    public void setComboBox(final java.lang.String comboBox){
        this.comboBoxProperty().set(comboBox);
    }

    public StringProperty minProperty(){
        return this.min;
    }
    
    public java.lang.String getMin() {
        return this.minProperty().get();
    }

    public void setMin(final java.lang.String min){
        this.minProperty().set(min);
    }
    
    public StringProperty maxProperty(){
        return this.max;
    }
    
    public java.lang.String getMax() {
        return this.maxProperty().get();
    }

    public void setMax(final java.lang.String max){
        this.maxProperty().set(max);
    }
    
    public StringProperty defaultVProperty(){
        return this.defaultV;
    }
    
    public java.lang.String getDefaultV() {
        return this.defaultVProperty().get();
    }

    public void setDefaultV(final java.lang.String defaultV){
        this.defaultVProperty().set(defaultV);
    }
    
    public StringProperty restrictionProperty(){
        return this.restriction;
    }
    
    public java.lang.String getRestriction() {
        return this.restrictionProperty().get();
    }

    public void setRestriction(final java.lang.String restriction){
        this.restrictionProperty().set(restriction);
    }
    
    public StringProperty descriptionProperty(){
        return this.description;
    }
    
    public java.lang.String getDescription() {
        return this.descriptionProperty().get();
    }

    public void setDescription(final java.lang.String description){
        this.descriptionProperty().set(description);
    }
    
    public StringProperty callTypeProperty(){
        return this.callType;
    }

    public java.lang.String getCallType() {
        return this.callTypeProperty().get();
    }

    public void setcallType(final java.lang.String callType){
        this.callTypeProperty().set(callType);
    }

	public void setCall(String text) {
		this.call = text;
		this.isMod = true;
	}



}
