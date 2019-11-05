package gui.parseOptions.parseExtraOptions;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReturnTypeElement {


    private StringProperty nameReturnType = new SimpleStringProperty();
    
    private StringProperty parameterName = new SimpleStringProperty();
    
    public ReturnTypeElement(String text, String string) {
    	this.nameReturnType = new SimpleStringProperty(text);
    	this.parameterName = new SimpleStringProperty(string);
    }

	public StringProperty nameReturnTypeProperty(){
        return this.nameReturnType;
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
