package logic.writeModule;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import gui.parseOptions.parseExtraOptions.ReturnTypeElement;
import java.util.ArrayList;
import java.util.List;

import logic.usageParser.UsageOption;

/**
 * Writes parameter in shell script template.
 * 
 * @author Amrei Menzel
 *
 */
public class SHWriter {

	// Placeholder for variables
	private final String templateVariables = "{%VARIABLES%}";
	private String variables = "variables";

	// Placeholder for mandatory variable checks
	private final String templateMandatoryVariables = "{%MANDATORY_VAR_CHECK%}";
	private String mandatoryVariables = "mandatoryVariables";

	// Placeholder for existence checks of files and folders
	private final String templateExistenceCheck = "{%FILE_EXISTENCE_CHECK%}";
	private String existenceCheck = "";

	// Placeholder for usedtools
	private final String templateUsedTools = "{%USED_TOOLS%}";
	private String usedTools = "'echo:printf:rm:grep:head:wc'"; // TODO

	// Placeholder for further parameter checks
	private final String templateFurtherParameterChecks = "{%FURTHER_PARAMETER_CHECKS%}";
	private String furtherParameterChecks = "furtherParamterChecks";

	// template for the functional part
	private final String templateFunctionalPart = "{%FUNCTIONAL_PART%}";
	private String functionalPart = "functionalPart";

	// template for the moduleName
	private final String templateModuleName = "{%MODULE_NAME%}";
	private String moduleName = "";

	// template for the return parameters
	private final String templateReturnParameter = "{%RETURN_PARAMETER%}";
	private String returnParameter = "";

	// All UsageOptions with limitations or restrictions
	private final List<UsageOption> hasLimitations = new ArrayList<>();

	// All UsageOption that are required
	private final List<UsageOption> requiredVariables = new ArrayList<>();

	// All flags
	private final List<UsageOption> booleanVariables = new ArrayList<>();

	// All Usage options
	private final List<UsageOption> allVariables;

	// Call of programm
	private final String call;

	public SHWriter(final WriteModule writeModule) {
		this.allVariables = writeModule.getAllVariables();
		this.call = writeModule.getCall();
		this.moduleName = writeModule.getToolname().replace(" ", "");
	}

	/**
	 * Construct variables. They consist of type, name, default value und short
	 * option.
	 */
	private void constructVariables() {
		variables = "";
		for (UsageOption usageOption : allVariables) {
			setType(usageOption);
			setDefault(usageOption);
			variables += " \"" + usageOption.getDescription() + "\"";
			String shortO = usageOption.getShortO();
			// the option has no short option
			if (!(shortO.equals("-") || shortO.equals(""))) {
				variables += " '" + shortO + "'";
			} else {
				variables += " ''";
			}
			variables += "\n";
			// Collect all flags for functional part
			if (usageOption.getType().equals("boolean")) {
				booleanVariables.add(usageOption);
			}
			// Collects all options with restrictions for further parameter checks
			if (!usageOption.getRestriction().isEmpty() && 
					!usageOption.getRestriction().equals("-")) {
				hasLimitations.add(usageOption);
			}
			// Collects all mandatory variables
			if (Integer.parseInt(usageOption.getMinO()) > 0) {
				requiredVariables.add(usageOption);
			}
		}

	}

	private void setDefault(final UsageOption usageOption) {
		if (usageOption.getDefaultV().equals("-")) {
			if (usageOption.getType().equals("boolean")) {
				variables += " '0'";
			} else {
				variables += " ''";
			}
		} else {
			// In default values qoutes aren't allowed
			variables += " '" + usageOption.getDefaultV()
			.replace("\"", "").replace("'", "") + "'";
		}
	}

	private void setType(final UsageOption usageOption) {
		if (usageOption.getType().equals("-") 
				|| usageOption.getType().equals("AbsoluteFilePath")
				|| usageOption.getType().equals("AbsoluteFolderPath")) {
			// If the type isn't set, string is default
			variables += "DEFINE_string '" + usageOption.getName() + "'"; 
		} else if (usageOption.getType().equals("double")) {
			variables += "DEFINE_float" + " '" + usageOption.getName() + "'";
		} else {
			variables += "DEFINE_" + usageOption.getType() + " '" 
		+ usageOption.getName() + "'";
		}
	}

	/**
	 * Creates the funtional part of the script. Because Flags doesn't need an
	 * attribute, they are treatend differently
	 */
	private void constructFunctionalPart() {
		functionalPart = "";
		String flagsAsString = "flagsAsString=\"\"\n";
		// flags have not attributes
		for (UsageOption uo : booleanVariables) {
			flagsAsString += "if [ \"FLAGS_" + uo.getName() + "\" == 0 ]; then\n";
			flagsAsString += " flagsAsString=\"$flagsAsString " 
			+ uo.getOptionCall() + " \"\n";
			flagsAsString += "fi\n";
		}
		String checkArguments = "";
		for (UsageOption uo : allVariables) {
			if (uo.getType().equals("boolean") || WriteModule.RETURN_FILE_PATH.equals(uo.getName())) {
				continue;
			}
			checkArguments += "if  [ ! -z \"$FLAGS_" + uo.getName() + "\" ]; then\n";
			checkArguments += "	flagsAsString=\"$flagsAsString " 
			+ uo.getOptionCall() + " $FLAGS_" + uo.getName()
					+ "\"\n";
			checkArguments += "fi\n";

		}

		functionalPart += flagsAsString + checkArguments;
		functionalPart += "# run it\n" + "MESSAGE=$(" + call + " $flagsAsString)";

	}

	/**
	 * Constructs further parameter checks for restriction. Restriction for integer
	 * and double only possible with regex in the moment.
	 */
	private void constructFurtherParameterChecks() {
		furtherParameterChecks = "";
		for (UsageOption uo : hasLimitations) {
			// if(uo.getType().equals("integer")) { //to do integer limitations
			/*
			 * if [ "$FLAGS_quality" -gt 9 ] || [ "$FLAGS_quality" -lt 1 ]; then echoError
			 * "Parameter -q must be between [1, 9]. (see --help for details)"; exit
			 * $EXIT_INVALID_ARGUMENTS fi
			 */
			// }
			// else {
			furtherParameterChecks += "	PARA=$(echo \"$FLAGS_" 
			+ uo.getName() + "\" | grep -P -c \""
					+ uo.getRestriction() + "\")\n";
			furtherParameterChecks += "if [ $PARA -ne 1 ]; then\n";
			furtherParameterChecks += "	echoError \"" + uo.getName() 
			+ " must match pattern '" + uo.getRestriction()
					+ "'\".\n";
			furtherParameterChecks += "	exit $EXIT_INVALID_ARGUMENTS\n";
			furtherParameterChecks += "fi\n";
			// }
		}
	}

	/**
	 * construct mandatory variable checks.
	 */
	private void constructTemplateVariables() {
		mandatoryVariables = "";
		for (UsageOption uo : requiredVariables) {
			mandatoryVariables += "if [ -z \"$FLAGS_" + uo.getName() + "\" ]; then \n";
			mandatoryVariables += "	echoError \"Parameter " + uo.getName()
					+ " must be set. (see --help for details)\"; \n";
			mandatoryVariables += "	exit $EXIT_MISSING_ARGUMENTS\n";
			mandatoryVariables += "fi\n";
		}
	}

	/**
	 * Constructs existence checks of needed files and folders.
	 * 
	 * @param existenceCheckNeededFiles
	 * @param existenceCheckNeededFolders
	 */
	private void constructExistenceCheck(final List<String> existenceCheckNeededFiles,
			final List<String> existenceCheckNeededFolders) {
		existenceCheck = "";
		for (String file : existenceCheckNeededFiles) {
			existenceCheck += "verifyFileExistence \"$FLAGS_" + file + "\"\n";
		}
		for (String folder : existenceCheckNeededFolders) {
			existenceCheck += "verifyFolderExistence \"$FLAGS_" + folder + "\"\n";
		}
	}

	/**
	 * Constructs returnParameter
	 * 
	 * @param returnParams
	 */
	private void constructReturnParameter(final List<ReturnTypeElement> returnParams) {
		String tab = "";
		for (ReturnTypeElement returnParam : returnParams) {
			returnParameter += tab + "writeParam2File \"$FLAGS_returnFilePath\" \""
					+ returnParam.getParameterName() + "\" \"TODO: add value\"\n";
			tab = "\t\t";
		}
		returnParameter += tab + "blockUntilFileIsWritten \"$FLAGS_returnFilePath\"\n";
	}

	/**
	 * Writes constructetstrings for template in template
	 * 
	 * @param inFile
	 *            empty templatefile
	 * @param outFile
	 *            filled templatefile
	 * @param existenceCheckNeededFiles
	 *            files which needed existence checks
	 * @param existenceCheckNeededFolders
	 *            folders whcih needed existence checks
	 * @param returnParams
	 *            returnparameter
	 */
	public void writeInTemplate(final String inFile, final String outFile, 
			final List<String> existenceCheckNeededFiles,
			final List<String> existenceCheckNeededFolders,
			final List<ReturnTypeElement> returnParams) {
		constructExistenceCheck(existenceCheckNeededFiles, existenceCheckNeededFolders);
		if (returnParams != null && returnParams.size() > 0) {
			constructReturnParameter(returnParams);
		}
		writeInTemplate(inFile, outFile);
	}

	/**
	 * Writes constructetstrings for template in template.
	 * 
	 * @param inFile
	 *            empty templatefile
	 * @param outFile
	 *            filled templatefile
	 */
	public void writeInTemplate(final String inFile, final String outFile) {
		constructVariables();
		constructTemplateVariables();
		constructFurtherParameterChecks();
		constructFunctionalPart();

		String line;

		try {
			FileReader fileReader = new FileReader(inFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			BufferedWriter bufferedWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(outFile), 
							StandardCharsets.UTF_8));
			while ((line = bufferedReader.readLine()) != null) {
				line = line.replace(templateVariables, variables);
				line = line.replace(templateMandatoryVariables, mandatoryVariables);
				line = line.replace(templateExistenceCheck, existenceCheck);
				line = line.replace(templateUsedTools, usedTools);
				line = line.replace(templateFurtherParameterChecks, 
						furtherParameterChecks);
				line = line.replace(templateFunctionalPart, functionalPart);
				line = line.replace(templateModuleName, moduleName);
				line = line.replace(templateReturnParameter, returnParameter);

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
		String[] makeExecutable = new String[] { "chmod", "+x", outFile };
		try {
			Runtime.getRuntime().exec(makeExecutable);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
