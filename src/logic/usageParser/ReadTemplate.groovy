package logic.usageParser

import groovy.util.XmlSlurper

/**
 * Class that parse the regex from the regex files
 * @author Amrei Menzel
 *
 */
class ReadTemplate {

	private String required
	private String optional
	private String choice
	
	private String shortOC 
	private String longOC
	private String shortOCb
	private String longOCb
	private String shortOP
	private String longOP
	private String shortOPb
	private String longOPb
	private String sep
	private String type
	private String defaultO
	private String description
	
	private String integerT
	private String booleanT
	private String pathFileT
	private String pathFolderT
	private String stringT
	private String doubleT
	
	private String startUsageLine;
	
		
	private def list;
	
	void readFile(String usageFile) {
		list = new XmlSlurper().parse(new File(usageFile))
		
		assert list instanceof groovy.util.slurpersupport.GPathResult
		assert list.Usage.name()
	}	
	
	void saveArguments() {
		required = list.Usage.required
		optional = list.Usage.optional
		
		shortOC = list.Options.shortOptC
		longOC = list.Options.longOptC
		shortOCb = list.Options.shortOptCb
		longOCb = list.Options.longOptCb
		shortOP = list.Options.shortOptP
		longOP = list.Options.longOptP
		shortOPb = list.Options.shortOptPb
		longOPb = list.Options.longOptPb
		sep = list.Options.sep
		type = list.Options.type
		defaultO = list.Options.default
		description = list.Options.description
		
		integerT = list.Options.integer;
		booleanT = list.Options.boolean;
		pathFileT = list.Options.filepath;
		pathFolderT = list.Options.folderpath;
		stringT = list.Options.string;
		doubleT = list.Options.double;
		
		startUsageLine = list.Usage.@usageLine;
	}
	
	static main(args) {	
		ReadTemplate rt = new ReadTemplate()
		rt.readFile()
		rt.saveArguments()	
	}

	public boolean isInUsage() {
		return inUsage;
	}
	
}
