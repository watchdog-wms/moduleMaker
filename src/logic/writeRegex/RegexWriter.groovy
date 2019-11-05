package logic.writeRegex

import java.security.Provider.EngineDescription

import groovy.xml.MarkupBuilder

/**
 * Class to write regex files.
 * 
 * @author Amrei Menzel
 *
 */
class RegexWriter {

	//Start of usage line
	private String usageLine;

	//regex of required parameter given in the in usage line
	private String required;

	//regex of optional parameter given in the usage line
	private String optional;

	//how to call parameter with short option
	private String shortOptionCall;

	//how to call parameter with long option
	private String longOptionCall;

	//how to call parameter with short option as flag
	private String shortOptionCallBoolean;

	//how to call parameter with long option as flag
	private String longOptionCallBoolean;

	//how to read the parameter with short option from the usage
	private String shortOptionUsage;

	//how to read the parameter with long option from the usage
	private String longOptionUsage;

	//how to read the parameter with short option from the usage as flag
	private String shortOptionUsageBoolean;

	//how to read the parameter with long option from the usage as flag
	private String longOptionUsageBoolean;

	//separation of long an dhort options
	private String sep;

	//regex of type of parameters
	private String type;

	//name of integer in parameters
	private String typeInteger;

	//name of string in parameters
	private String typeString;

	//name of boolean in parameters
	private String typeBoolean;

	//name of file in parameters
	private String typeFile;

	//name of folder in parameters
	private String typeFolder;

	//name of doubles in parameters
	private String typeDouble;

	//regex of defaultvalue of parameter
	private String defaultValue;

	//regex of description of parameter
	private String description;

	/**
	 * Get input values with keywords they need to prelace and make regex from them.
	 * @param ul usageline
	 * @param req requiered
	 * @param opt optional
	 * @param sc short option call
	 * @param lc long option call
	 * @param su short usage
	 * @param lu long usage
	 * @param sep separation of short and long option
	 * @param ty type
	 * @param tyI type integer
	 * @param tyS type string
	 * @param tyB type boolean
	 * @param tyFi type file
	 * @param tyFo type folder
	 * @param defV default value
	 * @param des description
	 * @param scb short option call boolean
	 * @param lcb long option call boolean
	 * @param sub short option usage boolean
	 * @param lub long option usage boolean
	 * @param tyDo type double
	 */
	public void parseInputValues(String ul,String req, String opt,
			String sc,String lc,String su,String lu,String sep,String ty,
			String tyI,String tyS,String tyB,String tyFi,String tyFo,
			String defV, String des, String scb, String lcb, String sub,
			String lub, String tyDo) {
		this.usageLine=ul;

		//in the required field the word "option" will be replaced with a regular expression
		if(req.contains("option") && "option".size()+1 < req.size()) {
			def tmp = req.split('option')
			this.required = "\\${tmp[0]}([^\\${tmp[0]}]*?)\\${tmp[1]}"
		}
		//if there is no sign before and after the "option" it is assumed that there are spaces before and after
		else if(req.equals("option")) {
			this.required = "\\s(.*?)\\s"
		}
		//if the string hasn't character before and after and doesn't equals "option" the string is taken over
		else
			this.required = req;

		//in the optional field the word "option" will be replaced with a regular expression
		if(opt.contains("option")&& "option".size()+1 < opt.size()) {
			def tmp = opt.split('option')
			this.optional = "\\${tmp[0]}([^\\${tmp[0]}]*?)\\${tmp[1]}"
		}
		//if there is no sign before and after the "option" it is assumed that there are spaces before and after
		else if(opt.contentEquals("option")) {
			this.optional = "\\s(.*?)\\s"
		}
		else
			//if the string hasn't character before and after and doesn't equals "option" the string is taken over

			this.optional = opt;

		//separation is between short and long option
		sep = sep.replace("short", "");
		sep = sep.replace("long", "");
		this.sep = sep;
		//options in usage doesn't consist of whitespaces
		shortOptionUsage = "(?:^|\\s)"+ su.replace("option", "(\\S*)")
		longOptionUsage = "(?:^|\\s|$sep)" + lu.replace("option", "(\\S*)")
		shortOptionUsageBoolean = "(?:^|\\s)"+ sub.replace("option", "(\\S*)")
		longOptionUsageBoolean = "(?:^|\\s|$sep)" + lub.replace("option", "(\\S*)")
		shortOptionCall = sc.replace("option", "(.*)").replace("parameter", "(.*)");
		longOptionCall = lc.replace("option", "(.*)").replace("parameter", "(.*)");
		shortOptionCallBoolean = scb.replace("option", "(.*)");
		longOptionCallBoolean = lcb.replace("option", "(.*)");

		this.typeInteger = tyI
		this.typeString = tyS
		this.typeBoolean = tyB
		this.typeFile = tyFi
		this.typeFolder = tyFo
		this.typeDouble=tyDo

		String[] allTypes = [
			tyI,
			tyS,
			tyB,
			tyFi,
			tyFo,
			tyDo
		];

		//defines the regular expression for types
		def types = "";
		for(String typeD : allTypes) {
			if(typeD.size() > 0 && typeD != "-") {
				if(types.size() > 0)
					types += "|"
				types += typeD
			}
		}


		if(ty.contains("type")) {
			def tmp = ty.split('(type)(?![a-zA-T])');
			this.type = "${tmp[0]}($types)${tmp[1]}"
		}
		else if (types.size()>0)
			type = "($types)";
		else
			type = "-"

		if(defV.contains("def")) {
			def tmp = defV.split('(def)(?![a-zA-T])');
			if(tmp.size() > 0)
				this.defaultValue = "\\${tmp[0]}(.*)\\${tmp[1]}"
		}
		else
			defaultValue = defV;

		if(des.contains("des")) {
			def tmp = des.split('(des)(?![a-zA-T])')
			if(tmp.size() > 0)
				this.description = "\\${tmp[0]}(.*)\\${tmp[1]}"
			else if (des.equals("des")) {
				this.description = '(.*)'
			}
			else
				this.description =''
		}
		else
			description = des;
	}

	/**
	 * Writes the prepared regex in the new regex file.
	 * @param outFile the new regex file
	 */
	public void writeXMLString(String outFile) {
		def fileWriter = new FileWriter(outFile)
		def regexBuilder = new MarkupBuilder(fileWriter)
		regexBuilder.Usages{
			Usage("usageLine":usageLine){
				required(required)
				optional(optional)
			}
			Options(){
				shortOptC(shortOptionCall)
				longOptC(longOptionCall)
				shortOptCb(shortOptionCallBoolean)
				longOptCb(longOptionCallBoolean)
				shortOptP(shortOptionUsage)
				longOptP(longOptionUsage)
				shortOptPb(shortOptionUsageBoolean)
				longOptPb(longOptionUsageBoolean)
				sep(sep)
				type(type)
				integer(typeInteger)
				filepath(typeFile)
				folderpath(typeFolder)
				'boolean'(typeBoolean)
				string(typeString)
				'double'(typeDouble)
				'default'(defaultValue)
				description(description)
			}
		}
		fileWriter.close()
	}


}
