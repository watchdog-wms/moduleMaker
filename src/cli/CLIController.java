package cli;

import gui.cli.CommandLineOutput;
import logic.usageParser.ParseUsage;
import logic.usageParser.WriteUsage;
import logic.writeModule.WriteModule;

/**
 * Controller for the command line program. 
 * With the help of the class that read out the commandLine input,
 * the two programs for parsing(ParseUsage) and creating the modules (WriteModule) are called.
 * @author Amrei Menzel
 *
 */
public class CLIController {
	
	/**
	 * Specifies if a call or a usage file is given. It's false if it is a call.
	 */
	private boolean usageFileCall;
	
	
	/**
	 * Calls the class ParseUsage with the required arguments. 
	 * The arguments are given from the comand line as input.
	 * @param cli object for usageoutput and input.
	 */
	public void callUsageParser(final CommandLineOutput cli) {
		final ParseUsage parseUsage = new ParseUsage();
		//A usageFile is given
		if(usageFileCall) {
			//certain regex file isn't set
			if(cli.getRegex() == null) {
				parseUsage.findBestUsageAndParse_File(cli.getUsageFile());
			//certain regex file is set
			} else {
				parseUsage.parseFile(cli.getUsageFile(),  cli.getRegex(), cli.getFolder()+"/"+cli.getModuleName()+".wm");
			}
		}
		//No usagefile is given -> a call is given
		else {
			//certain regex file isn't set
			if(cli.getRegex() == null) {
				parseUsage.findBestUsageAndParse_Usage(cli.getCall());
			//certain regex file is set
			} else {
				parseUsage.parseUsage(cli.getCall(), cli.getRegex());
			}
		}
		//After parsing write usage in wm-file.
		new WriteUsage().writeInFile(cli.getFolder()+"/"+cli.getModuleName()+".wm",parseUsage.getOptions().get(0)); 
		
	}
	
	/**
	 * Calls the class WriteModule with the required arguments. 
	 * The arguments are given from the command line as input. 
	 * The class WriteModule writes the modules.
	 * @param cli object for usageoutput and input.
	 */
	public void callModuleWriter(final CommandLineOutput cli) {
		final WriteModule writeModule = new WriteModule(cli.getModuleName());
		writeModule.writeInTemplates(cli.getFolder()+"/"+cli.getModuleName()+".wm", cli.getFolder()+"/"+cli.getModuleName()+".sh", cli.getFolder()+"/"+cli.getModuleName()+".xsd", cli.getCall());
	}

	/**
	 * Calls the class CommandLineOutput to get the input from the commandline. 
	 * After this the usage will be parsed and the modules will be written.
	 * @param args the input from the commandline
	 */
	public void getParameterFromCommandLine(final String... args) {
		usageFileCall = false;
		final CommandLineOutput cli = new CommandLineOutput(args);
		if(cli.getUsageFile() != null) {
			usageFileCall = true;
		}
		callUsageParser(cli);
		callModuleWriter(cli);
	}
	
	/**
	 * Calls the main-method, afterwards the input will be read.
	 * @param args the input from the commandline
	 */
	public static void main(final String[] args) {
		new CLIController().getParameterFromCommandLine(args);
	}
	
	
}
