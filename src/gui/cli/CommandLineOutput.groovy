package gui.cli

import groovyjarjarcommonscli.CommandLine

/**
 * Writes Usage and read input from commandline.
 * @author Amrei Menzel
 *
 */
class CommandLineOutput {

	/**
	 * Usageline
	 */
	private def cli = new CliBuilder(usage:'java -jar ModuleMakerCommandline.jar options', 
		  					 header: '\nAvailable options (use -h for help):\n',)
	
	/**
	 * Make options for usage.
	 */
	private void makeCLI() {
		cli.h(longOpt:'help', 'Show this help.')
		cli.f(longOpt:'UsageFile', args:1, argName:'file',  "For testing. Instead of call, give path to file with copied usage.")
		cli.e(longOpt:'extendedCall', args:1,  argName:'string', "If the Usage only appear with given parameter, write the name of the parameter here. (i.e. '-help')")
		cli.c(longOpt:'call', args:1, argName:'string', "How to call the Tool (i.e. 'java -jar ModuleMaker.jar') (required)", required:'true')
		cli.m(longOpt:'modulename', args:1, argName:'string', 'Set name of module (required)', required:'true')
		cli.r(longOpt:'regex', args:1,  argName:'file', 'Set regexFile. If not set, best fitting regexFile will be chosen.')
		cli.d(longOpt:'debug', 'Enable debug output')
		cli.o(longOpt:'folder', args:1, argName:'folder', 'Outputfolder where files should be written to')		
	}
	

	public String getUsageFile() {
		return usageFile;
	}

	public String getExtendedCall() {
		return extendedCall;
	}

	public String getCall() {
		return call;
	}

	public String getModuleName() {
		return moduleName;
	}

	public String getRegex() {
		return regex;
	}

	public String getFolder() {
		return folder;
	}	

	private String usageFile
	private String extendedCall
	private String call
	private String moduleName
	private String regex
	private String folder = "./"
	
	
	/**
	 * Writes down usage and parse options from the input
	 * @param args
	 */
	public CommandLineOutput(String[] args) {
		makeCLI();
		parseOptions(args);
	}
	
	/***
	 * Parsing options from the input
	 * @param args input from the commandline
	 */
	private void parseOptions(def args) {
		def options = cli.parse(args)
		if(!options)
			System.exit(0)
		if(options.h || !options.c || !options.m) {
			cli.usage()
			System.exit(0)
		}
		if(options.f) {
			usageFile = options.f
			if(!new File(usageFile).exists()) {
				cli.getUsage()
				System.exit(0)
			}
		}
		call = options.c
		if(options.e)
			extendedCall = options.e
		moduleName = options.m
		if(options.r)
			regex = options.r
		if(options.o) {
			folder = options.o
			if(!new File(folder).exists()) {
				cli.getUsage()
				System.exit(0)
			}
		}
 	}
	
}
