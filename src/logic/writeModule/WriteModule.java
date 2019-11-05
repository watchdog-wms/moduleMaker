package logic.writeModule;

import gui.parseOptions.parseExtraOptions.ReturnTypeElement;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javafx.collections.ObservableList;
import logic.usageParser.UsageOption;
import logic.util.Debugger;

/**
 * Reads varibles ffrom wm file and writes xsd and sh file.
 * 
 * @author Amrei Menzel
 *
 */
public class WriteModule {

	private final String shTemplate = "moduleTemplates/module.template.sh";
	private final String xsdTemplate = "moduleTemplates/module.template.xsd";

	private String toolname = "";

	// required variables (minOccurence >=1)
	private final List<String> requiredVariables = new ArrayList<String>();

	// variables with restrictions
	private final Map<String, String> hasLimitations = new HashMap<>();

	// flags
	private final List<String> booleanVariables = new ArrayList<String>();

	// List of all variables from wm file
	private final List<UsageOption> allVariables = new ArrayList<>();

	private ObservableList<ReturnTypeElement> returnTypeElementsData;

	// call of original program
	private String call;
	
	public List<UsageOption> getAllVariables() {
		return allVariables;
	}

	public WriteModule(final String toolname) {
		this.toolname = toolname;
	}

	public WriteModule() {
	}

	public List<String> getRequiredVariables() {
		return requiredVariables;
	}

	public String getToolname() {
		return toolname;
	}

	public String getCall() {
		return call;
	}

	public Map<String, String> getLimitations() {
		return hasLimitations;
	}

	public List<String> getBooleanVariables() {
		return booleanVariables;
	}


	/**
	 * Read all variables from the wm file. Special variables like booleans,
	 * required variables and variables with restrictions are safed seperatly, too.
	 * 
	 * @param usageFile
	 *            Path to wm file
	 */
	public void readVariables(final String usageFile) {

		if (Debugger.isEnabled()) {
			Debugger.log("Read variables...");
		}

		// Indices of the wm file parameters
		int indexName;
		int indexShortO;
		int indexLongO;
		int indexType;
		int indexDefault;
		int indexDescription;
		int indexMin;
		int indexMax;
		int indexCall;
		int indexLimitations;
		int indexCallType;

		// the final call of option like -m or --threads.
		String optionCall;

		List<String> shorts = new ArrayList<>();

		String line;

		try {
			FileReader fileReader = new FileReader(usageFile);

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String firstLine = bufferedReader.readLine();
			if (firstLine == null) {
				System.err.print("Header of File corrupted");
				System.exit(1);
			}
			List<String> header = Arrays.asList(firstLine.split("\t"));

			// Sets the indices
			indexName = header.indexOf("Name");
			indexShortO = header.indexOf("shortO");
			indexLongO = header.indexOf("longO");
			indexType = header.indexOf("Type");
			indexDefault = header.indexOf("Default");
			indexDescription = header.indexOf("Description");
			indexMin = header.indexOf("Min");
			indexMax = header.indexOf("Max");
			indexCall = header.indexOf("Call");
			indexCallType = header.indexOf("CallType");
			indexLimitations = header.indexOf("Limitations");

			String[] variableArguments;

			UsageOption usageOption;

			while ((line = bufferedReader.readLine()) != null) {
				usageOption = new UsageOption();
				variableArguments = line.split("\t");

				// line is incomplete if not all parameters are found -> next line
				if (variableArguments.length < 10) {
					break;
				}
				String name = variableArguments[indexName].trim();
				// no points in names
				name = name.replaceAll(Pattern.quote("."), ""); 

				usageOption.setName(name);

				// checks if the option is required (minOccurence >=1)
				if (Integer.parseInt(variableArguments[indexMin].trim()) >= 1) {
					requiredVariables.add(name);
				}
				// it's a flag then the type is a boolean
				if (variableArguments[indexType].trim().equals("boolean")) {
					booleanVariables.add(name);
				}
				// the option has an restriction if
				if (!variableArguments[indexLimitations].trim().equals("-")) {
					hasLimitations.put(name, variableArguments[indexLimitations].trim());
				}

				String shortO = variableArguments[indexShortO].trim();
				// If no short option exist,take long option for call
				if (shortO.equals("-")) {
					if (!variableArguments[indexLongO].trim().equals("-")) {
						optionCall = variableArguments[indexCall].trim()
								.replaceFirst(Pattern.quote("(.*)"),
								variableArguments[indexLongO]);
						// if no long or short option exist, call is empty and only 
						// attribute will be given
					} else {
						optionCall = " ";
					}
				} else {
					optionCall = variableArguments[indexCall].trim()
							.replaceFirst(Pattern.quote("(.*)"),
							variableArguments[indexShortO]);
				}
				optionCall = optionCall.replaceAll(Pattern.quote("(.*)"), "");
				optionCall = optionCall.replaceAll(Pattern.quote("'"), "");
				optionCall = optionCall.replaceAll(Pattern.quote(","), "");

				if (shorts.contains(shortO) || shortO.equals("-")) {
					shortO = "-";
				}
				// save read usageoptions from wm file
				usageOption.setShortO(shortO);
				usageOption.setType(variableArguments[indexType]);
				usageOption.setOptionCall(optionCall);
				usageOption.setMinO(variableArguments[indexMin].trim());
				usageOption.setMaxO(variableArguments[indexMax].trim());
				usageOption.setDefaultV(variableArguments[indexDefault].trim());

				usageOption.setDescription(variableArguments[indexDescription].trim());
				usageOption.setLongO(variableArguments[indexLongO].trim());
				usageOption.setRestriction(variableArguments[indexLimitations].trim());
				usageOption.setCall(variableArguments[indexCall].trim());
				usageOption.setCallType(variableArguments[indexCallType].trim());

				allVariables.add(usageOption);

			}
			bufferedReader.close();
			fileReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Debugger.isEnabled()) {
			Debugger.log(allVariables.size() + " variables found. " 
		+ requiredVariables.size() + " are required.");
		}

	}

	/**
	 * Writes usageoptions ub templates
	 * 
	 * @param moduleFile
	 *            wm file
	 * @param shPath
	 *            shellscript template
	 * @param xsdFile
	 *            xsd template
	 * @param call
	 *            of original program
	 */
	public void writeInTemplates(final String moduleFile, final String shPath, 
			final String xsdFile, final String call) {
		readVariables(moduleFile);
		this.call = call;
		if (Debugger.isEnabled()) {
			Debugger.log("Write in sh-Template...");
		}
		SHWriter shw = new SHWriter(this);
		shw.writeInTemplate(shTemplate, shPath);
		if (Debugger.isEnabled()) {
			Debugger.log("Write in xsd-Template...");
		}
		XSDWriter xsdw = new XSDWriter(this);
		xsdw.writeInTemplate(xsdTemplate, xsdFile);
	}

	/**
	 * Writes usageoptions ub templates
	 * 
	 * @param moduleFile
	 *            wm file
	 * @param shPath
	 *            shell script template
	 * @param xsdFile
	 *            path to xsd template
	 * @param call
	 *            call of original program
	 * @param existenceNeededFiles
	 *            files which must be exist
	 * @param existenceNeededFolders
	 *            folders which must be exist
	 * @param sepParas
	 *            how attributes of options with more than one attribute should
	 *            separated
	 */
	public void writeInTemplates(final String moduleFile, final String shPath,
			 final String xsdFile, final String call,
			final List<String> existenceNeededFiles, final List<String> existenceNeededFolders,
			final Map<String, String> sepParas) {
		readVariables(moduleFile);
		this.call = call;
		if (Debugger.isEnabled()) {
			Debugger.log("Write in sh-Template...");
		}
		SHWriter shw = new SHWriter(this);
		shw.writeInTemplate(shTemplate, shPath, existenceNeededFiles, 
				existenceNeededFolders, returnTypeElementsData);
		if (Debugger.isEnabled()) {
			Debugger.log("Write in xsd-Template...");
		}
		XSDWriter xsdw = new XSDWriter(this);
		if (returnTypeElementsData != null) {
			xsdw.setReturnTypeElements(returnTypeElementsData);
		}
		if (sepParas != null && sepParas.isEmpty()) {
			xsdw.setSepParas(sepParas);
		}
		xsdw.writeInTemplate(xsdTemplate, xsdFile);
	}

	public void setReturnTypeElements(final ObservableList<ReturnTypeElement> returnTypeElementsData) {
		this.returnTypeElementsData = returnTypeElementsData;
	}

}
