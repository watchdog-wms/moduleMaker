package gui.parseOptions.parseExtraOptions;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReturnTypeElement {


    private StringProperty returnType = new SimpleStringProperty();
    
    private StringProperty parameterName = new SimpleStringProperty();
    
    public ReturnTypeElement(String name, String type) {
    	this.returnType = new SimpleStringProperty(type);
    	this.parameterName = new SimpleStringProperty(name);
    }

	public StringProperty nameReturnTypeProperty(){
        return this.returnType;
    }
    
    public java.lang.String getReturnTypeName() {
        return this.nameReturnTypeProperty().get();
    }

    public void setReturnTypeName(final java.lang.String name){
        this.nameReturnTypeProperty().set(name);
    }

    
    
    public StringProperty parameterNameProperty(){
        return this.parameterName;
    }
    
    public java.lang.String getParameterName() {
        return this.parameterNameProperty().get();
    }

    public void setType(final java.lang.String type){
        this.parameterNameProperty().set(type);
    }
	
}
