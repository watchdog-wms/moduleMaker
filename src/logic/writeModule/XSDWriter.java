package logic.writeModule;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import gui.parseOptions.parseExtraOptions.ReturnTypeElement;
import javafx.collections.ObservableList;
import logic.usageParser.UsageOption;

/**
 * Writes parameter in XSD template.
 * 
 * @author Amrei Menzel
 *
 */
public class XSDWriter {

	// Placeholder for module name
	private final String templateModuleName = "{%MODULE%}";
	private String moduleName = "";

	// Placeholder for variables
	private final String templateArguments = "{%ARGUMENTS%}";
	private String arguments = "";

	// Placeholder for flags
	private final String templateFlags = "{%FLAGS%}";
	private String flags = "";

	// Placeholder for setting if arguments must exist (0 or 1)
	private final String templateParamParentOptional = "{%PARAM_PARENT_OPTIONAL%}";
	private String paramParentOptional = "";

	// Placeholder to set returnTypeElements
	private final String templateReturnTypeElements = "{%RETURN_TYPE_ELEMENTS%}";
	private String returnTypeElements = "";

	// Placeholder for restrictions
	private final String templateParamTypes = "{%NEW_PARAM_TYPES%}";
	private String paramTypes = "";

	// All usage options from the wm file
	private List<UsageOption> allVariables = new ArrayList<>();

	// Only required options fo the wm file
	private final List<String> required = new ArrayList<>();

	// All usage options with restrictions
	private final List<UsageOption> limitations = new ArrayList<>();

	// Map which assign to each unique usage name its type
	private final Map<String, String> nameToType = new HashMap<String, String>();

	// All returnTypeeElements coming from the user input
	private ObservableList<ReturnTypeElement> returnTypeElementsData;

	// Options with extra separation with more than one attribute. In Formname of
	// option, separation
	private Map<String, String> sepParas;

	public XSDWriter(final WriteModule writeModule) {
		this.moduleName = writeModule.getToolname().replaceAll(" ", "");
		this.allVariables = writeModule.getAllVariables();
		
		// remove return file path if present
		for (Iterator<UsageOption> it = this.allVariables.iterator(); it.hasNext(); ) {
			UsageOption uo = it.next();
			if(WriteModule.RETURN_FILE_PATH.equals(uo.getName())) {
				it.remove();
				break;
			}
		}
	}

	/**
	 * Constructs the arguments for placeholder "{%ARGUMENTS%}" and ""{%FLAGS%}".
	 */
	private void constructArguments() {
		for (UsageOption usageOption : allVariables) {
			if (nameToType.containsKey(usageOption.getName())) {
				flags += "<x:element name=\"" + usageOption.getName() + "\" type=\""
						+ nameToType.get(usageOption.getName()) + "\" minOccurs=\"" 
						+ usageOption.getMinO()
						+ "\" maxOccurs=\"" 
						+ usageOption.getMaxO() + "\" />\n";
			} else {
				if (usageOption.getType().equals("boolean")) {
					flags += "<x:element name=\"" + usageOption.getName() 
							+ "\" type=\"paramBoolean\" minOccurs=\""
							+ usageOption.getMinO() + "\" maxOccurs=\"" 
							+ usageOption.getMaxO() + "\" />\n";
				} else if (usageOption.getType().equals("integer")) {
					arguments += "<x:element name=\"" + usageOption.getName() 
							+ "\" type=\"paramInteger\" minOccurs=\""
							+ usageOption.getMinO() + "\" maxOccurs=\"" 
							+ usageOption.getMaxO() + "\" />\n";
				} else if (usageOption.getType().equals("AbsoluteFilePath")) {
					arguments += "<x:element name=\"" + usageOption.getName()
							+ "\" type=\"paramAbsoluteFilePath\" minOccurs=\""
							+ usageOption.getMinO()
							+ "\" maxOccurs=\"" + usageOption.getMaxO() + "\" />\n";
				} else if (usageOption.getType().equals("AbsoluteFolderPath")) {
					arguments += "<x:element name=\"" + usageOption.getName()
							+ "\" type=\"paramAbsoluteFolderPath\" minOccurs=\"" 
							+ usageOption.getMinO()
							+ "\" maxOccurs=\"" + usageOption.getMaxO() 
							+ "\" />\n";
				} else if (usageOption.getType().equals("double")) {
					arguments += "<x:element name=\"" + usageOption.getName() 
							+ "\" type=\"paramDouble\" minOccurs=\""
							+ usageOption.getMinO() + "\" maxOccurs=\"" 
							+ usageOption.getMaxO() + "\" />\n";
				} else {
					arguments += "<x:element name=\"" + usageOption.getName() 
							+ "\" type=\"paramString\" minOccurs=\""
							+ usageOption.getMinO() + "\" maxOccurs=\"" 
							+ usageOption.getMaxO() + "\" />\n";
				}
			}
			// If minOccurence >0 the arguments are required
			if (Integer.parseInt(usageOption.getMinO()) > 0) {
				required.add(usageOption.getName());
			}
		}
	}

	/**
	 * If the required size > 0 the options are not optional.
	 */
	private void constructParamParentOptional() {
		if (required.isEmpty()) {
			paramParentOptional = "1";
		} else {
			paramParentOptional = "0";
		}

	}

	/**
	 * Constructs returnTypeElements.
	 */
	private void constructReturnTypeElements() {
		if (returnTypeElementsData != null) {
			for (ReturnTypeElement rte : returnTypeElementsData) {
				returnTypeElements += "<x:element name=\"" + rte.getParameterName()
							+ "\" type=\"x:" + rte.getReturnTypeName() + "\"/>\n";
			}
		}

	}

	/**
	 * Constructs paramTypes for restrictions and definitions of separation if more
	 * than one attribute is possible. In the moment restriction for interger and
	 * double is only possible as regular expression.
	 */
	private void constructParamTypes() {
		int paramCounter = 0;

		// Get all usage options with restriction
		for (UsageOption uo : allVariables) {
			if (!uo.getRestriction().isEmpty() && !uo.getRestriction().equals("-")) {
				limitations.add(uo);
			}
		}

		// Set the restrictions
		for (UsageOption uo : limitations) {
			paramCounter = setRestriction(paramCounter, uo);
		}
		// Sets the separation for more than one attribute if the usage option have no
		// restriction
		if (sepParas != null && !sepParas.isEmpty()) {
			for (UsageOption uo : allVariables) {
				paramCounter = setSeparation(paramCounter, uo);
			}
		}

	}

	private int setSeparation(int paramCounter, final UsageOption usageOption) {
		String para;
		if (sepParas.containsKey(usageOption.getName())) {
			String type = usageOption.getType();
			String resBase = "paramString";
			if (type.equals("integer")) {
				resBase = "paramInteger";
			} else if (type.equals("double")) {
				resBase = "paramDouble";
			} else if (type.equals("AbsoluteFilePath")) {
				resBase = "paramAbsoluteFilePath";
			} else if (type.equals("AbsoluteFolderPath")) {
				resBase = "paramAbsoluteFolderPath";
			}
			para = Character.toUpperCase(usageOption.getName().charAt(0)) 
					+ usageOption.getName().substring(1);
			nameToType.put(usageOption.getName(), "param" + para 
					+ paramCounter + "_" + moduleName);
			paramTypes += "<x:complexType name=\"param" + para 
					+ paramCounter + "_" + moduleName + "\">\n";
			paramTypes += "	<x:simpleContent>\n";
			paramTypes += "		<x:restriction base = \"" + resBase + "\">\n";
			paramTypes += "			<x:pattern value=\"(.*)\" />\n";
			paramTypes += "			<x:attribute ref=\"separateFormat\" fixed=\"" 
					+ sepParas.get(usageOption.getName())
					+ "\"/>\n";
			paramTypes += "		</x:restriction>\n";
			paramTypes += "	</x:simpleContent>\n";
			paramTypes += "</x:complexType>\n";
			paramCounter++;
		}
		return paramCounter;
	}

	private int setRestriction(int paramCounter, final UsageOption usageOption) {
		String para;
		para = Character.toUpperCase(usageOption.getName().charAt(0)) 
				+ usageOption.getName().substring(1);
		String type = usageOption.getType();
		String resBase = "paramString";
		if (type.equals("integer")) {
			resBase = "paramInteger";
		} else if (type.equals("double")) {
			resBase = "paramDouble";
		} else if (type.equals("AbsoluteFilePath")) {
			resBase = "paramAbsoluteFilePath";
		} else if (type.equals("AbsoluteFolderPath")) {
			resBase = "paramAbsoluteFolderPath";
		}
		paramTypes += "<x:complexType name=\"param" + para + paramCounter 
				+ "_" + moduleName + "\">\n";
		paramTypes += "	<x:simpleContent>\n";
		paramTypes += "		<x:restriction base = \"" + resBase + "\">\n";
		/*
		 * if(para.equals("Integer")) { paramTypes +=
		 * "			<x:assert test=\"matches($value '(\\$\\{[A-Za-z_]+\\})|(\\$\\(.+\\))|([\\[\\(\\{](\\$[A-Za-z_]+(,\\s*){0,1}){0,1}([0-9]+(,\\S*){0,1}){0,1}[\\]\\)\\}])') or $value "
		 * + uo.getRestriction() +
		 * " xerces:message='Parameter with name '{$tag}' must be fit limitation: "+
		 * uo.getRestriction()+".'\" />\n"; }
		 */
		// else {
		paramTypes += "			<x:assert test=\"matches($value, "
				+ "'(\\$\\{[A-Za-z_]+\\})|(\\$\\(.+\\))|([\\[\\(\\{](\\$[A-Za-z_]+(,\\s*){0,1}){0,1}([0-9]+(,\\S*){0,1}){0,1}[\\]\\)\\}])') "
				+ "or matches($value, '"
				+ usageOption.getRestriction() + "')\" xerces:message=\"Parameter with name '" 
				+ usageOption.getName()
				+ "' must be fit limitation: " + usageOption.getRestriction() 
				+ ".'\" />\n";
		// }
		// if usage option has an extra sepertation for more thaan one attribute, it
		// must be et, too
		if (sepParas != null && !sepParas.isEmpty() && sepParas.containsKey(usageOption.getName())) {
			paramTypes += "			<x:attribute ref=\"separateFormat\" fixed=\"" 
					+ sepParas.get(usageOption.getName())
					+ "\"/>";
			sepParas.remove(usageOption.getName());
		}
		paramTypes += "		</x:restriction>\n";
		paramTypes += "	</x:simpleContent>\n";
		paramTypes += "</x:complexType>\n";
		nameToType.put(usageOption.getName(), "param" + para 
				+ paramCounter + "_" + moduleName);
		paramCounter++;
		return paramCounter;
	}

	/**
	 * Writes the strings for the placeholder in the template
	 * 
	 * @param inFile
	 *            template file
	 * @param outFile
	 *            changed template file with replaced placeholder
	 */
	public void writeInTemplate(final String inFile, final String outFile) {
		constructParamTypes();
		constructArguments();
		constructParamParentOptional();
		constructReturnTypeElements();

		String line;

		try {
			FileReader fileReader = new FileReader(inFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			BufferedWriter bufferedWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(outFile), 
							StandardCharsets.UTF_8));
			while ((line = bufferedReader.readLine()) != null) {
				line = line.replace(templateModuleName, moduleName);
				line = line.replace(templateArguments, arguments);
				line = line.replace(templateFlags, flags);
				line = line.replace(templateParamParentOptional, paramParentOptional);
				line = line.replace(templateReturnTypeElements, returnTypeElements);
				line = line.replace(templateParamTypes, paramTypes);

				bufferedWriter.write(line);
				bufferedWriter.newLine();
			}

			bufferedWriter.close();

			bufferedReader.close();
			fileReader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setReturnTypeElements(final ObservableList<ReturnTypeElement> returnTypeElementsData) {
		this.returnTypeElementsData = returnTypeElementsData;
	}

	public void setSepParas(final Map<String, String> sepParas) {
		this.sepParas = sepParas;
	}
}
