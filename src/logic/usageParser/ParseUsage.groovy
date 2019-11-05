package logic.usageParser


import java.util.Spliterators.AbstractDoubleSpliterator
import java.awt.Component.BaselineResizeBehavior
import java.awt.Container.AccessibleAWTContainer
import java.awt.event.ItemEvent

import javax.imageio.ImageIO.ContainsFilter

import logic.util.Debugger;

/**
 * Groocy class which parse the given usage. 
 * @author Amrei Menzel
 *
 */
class ParseUsage {

	//optional parameter in usageline
	private def optional
	//required parameter in usageline
	private def required

	//found and saved options
	private ArrayList<UsageOption> opts = new ArrayList<>();

	//name of long option
	private def lo
	//name of short option
	private def so
	//type of option
	private def type
	//default value of option
	private def defaultV
	//description of option
	private def description

	//map of path to temporary wm file and score of used regex file
	private def parseMap =[:]

	//counter for options with no name
	private int counter = 0;

	//current option
	private def opt = new UsageOption();

	//regexfile
	private def regex;

	//Path to regexlist
	private final String fileList =  "RegexFiles/RegexFiles.txt"

	//unlikely signs per option part
	private Map<String, String> unlikelySignsForOption = new HashMap<>();

	/*
	 * maxPages are defined by the number of all processed regexFiles
	 */
	private int maxPages =1;


	/**
	 * Constructor. Reads in the list of regexfiles to set the maximum of possible viewable results from the regex files
	 */
	public ParseUsage() {
		File files = new File(fileList)
		regex = files.readLines()
		maxPages = regex.size()
		setUnlikelySigns()
	}


	public int getMaxPages() {
		return maxPages;
	}

	public Map<String, Integer> getOptions() {
		return parseMap;
	}

	/*
	 * Save unlikey signs for variable names or description etc. It gives negative points for the score of Usages.
	 */
	private void setUnlikelySigns() {
		unlikelySignsForOption.put("short","[^A-Za-z0-9]")
		unlikelySignsForOption.put("long","[^A-Za-z0-9-_]")
		unlikelySignsForOption.put("def","[^A-Za-z0-9-_.]")
		unlikelySignsForOption.put("des","[^A-Za-z0-9-_.,'\",/\\[\\]\\(\\)]")
	}

	/**
	 * parse usage from a given regex File.
	 * @param toolCall : call of tool in commandline.
	 * @param usageFile : given regexFile
	 * @return Returns an integer which indicates if the parsing was successful. If not it's negative.
	 */
	public int parseUsage(String toolCall, String usageFile) {

		println "Start parsing Usage from tool with call $toolCall"

		def lines;
		opts = []

		try {
			lines = readLinesFromCommandLine(toolCall, lines);
		}catch(IOException ex) {
			if(Debugger.isEnabled()) {
				ex.printStackTrace();
			}
			return -1;
		}
		int returnValue=0;

		parseThis(usageFile, lines)
		//create resulting wm-file as tmp-File
		File tmp = File.createTempFile("tmp",".dat")
		tmp.deleteOnExit()
		parseMap.put(tmp.getAbsolutePath(), 0)
		new WriteUsage().writeInFile(tmp.getAbsolutePath(), opts)

		return returnValue;
	}



	/**
	 * parse usage from all files in the regexFiles list.
	 * @param toolCall : call of tool in commandline.
	 * @return Returns an integer which indicates if the parsing was successful. If not it's negative.
	 */
	public int findBestUsageAndParse_Usage(String toolCall) {

		println "Start parsing Usage from tool with call $toolCall"

		opts = [];

		def bestUsage ="";
		def bestValue = 0;
		def lines;

		try {
			lines = readLinesFromCommandLine(toolCall, lines);
		}catch(IOException ex) {
			if(Debugger.isEnabled()) {
				ex.printStackTrace();
			}
			return -1;
		}
		int returnValue=0;

		if(Debugger.isEnabled()) {
			Debugger.log("Starting search after best regex-File...")
		}

		regex.each { String line ->
			opts = [];
			def value = parseThis( line, lines);
			if(value > bestValue) {
				bestValue = value;
				bestUsage = line;
			}
			if(Debugger.isEnabled()) {
				Debugger.log("$line: $value points")
			}
			//create resulting wm-files as tmp-Files
			File tmp = File.createTempFile("tmp",".dat")
			tmp.deleteOnExit()
			parseMap.put(tmp.getAbsolutePath(), value)
			returnValue = new WriteUsage().writeInFile(tmp.getAbsolutePath(), opts)
		}
		//sort the list by best scores
		parseMap = parseMap.sort{-it.value}
		if(Debugger.isEnabled()) {
			Debugger.log("RegexFile from $bestUsage seems to fit the best.")
		}
		return returnValue;
	}

	/**
	 * Parse usage and save the lines.
	 * @param toolCall : call of tool in commandline.
	 * @param lines List of all read lines
	 * @return List of all read lines
	 */
	private List readLinesFromCommandLine(String toolCall, List lines) {
		Process p = new ProcessBuilder((toolCall).split(" ")).start();
		InputStream stdout = p.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
		InputStream stderr = p.getErrorStream();
		BufferedReader bre = new BufferedReader(new InputStreamReader(stderr));

		lines = br.readLines();

		//If no output in standard output it's possible that the usage is on the standard error stream.
		if(lines == null || lines.size() == 0) {
			lines = bre.readLines()
		}

		br.close();
		bre.close();
		p.closeStreams()
		return lines
	}


	/**
	 * parse usage from a file over all files in the regexFiles list.
	 * @param usagePath the Path to the file with the usage.
	 * @return Returns an integer which indicates if the parsing was successful. If not it's negative.
	 */
	public int findBestUsageAndParse_File(String usagePath) {

		println "Start parsing Usage from tool"

		opts = [];
		def bestUsage ="";
		def bestValue = 0;


		File file = new File(usagePath)
		if(!file.exists()) {
			return -3;
		}
		def lines = file.readLines()

		if(Debugger.isEnabled()) {
			Debugger.log("Starting search after best regex-File...")
		}

		File files = new File(fileList)
		int returnValue = 0;
		def regex = files.readLines()
		regex.each { String line ->
			opts = [];
			def value = parseThis(line, lines);
			if(value > bestValue) {
				bestValue = value;
				bestUsage = line;
			}
			if(Debugger.isEnabled()) {
				Debugger.log("$line: $value points")
			}
			//create resulting wm-files as tmp-Files
			File tmp = File.createTempFile("tmp",".dat")
			tmp.deleteOnExit()
			parseMap.put(tmp.getAbsolutePath(), value)
			returnValue = new WriteUsage().writeInFile(tmp.getAbsolutePath(), opts)
		}
		//sort the list by best scores
		parseMap = parseMap.sort{-it.value}
		if(Debugger.isEnabled()) {
			Debugger.log("RegexFile from $bestUsage seems to fit the best.")
		}
		return returnValue;
	}

	private ReadTemplate rt;

	/**
	 * parseUsage from given lines.
	 * @param usageFile
	 * @param lines
	 * @return Returns an integer which indicates if the parsing was successful. If not it's negative.
	 */
	private int parseThis(String regexFile, def lines) {
		counter=0;

		String name;

		def names = [];

		//Save shortOpts to avoid duplicates
		List<String> shortOpts = new ArrayList<String>();

		if(Debugger.isEnabled()) {
			Debugger.log("Parsing Regex from $regexFile and calculate value...")
		}
		int usageValue = 0;

		//Read the usage with help from regexFile
		rt = readRegexFile(regexFile)

		//usageLine is only possible for one time
		boolean usageLine = false
		//It could be, that the description is more than one line long
		boolean lastOptionOrDes = false;

		int negativeValue=1;

		lines.each { String line ->
			lo=null
			so=null
			type = null
			defaultV = null
			description = null
			name= null;

			//read usageline
			if (line.contains(rt.startUsageLine) && !usageLine) {
				line = readUsageLine(line)
				lastOptionOrDes=false
				//if usageLine is found usageValue increases
				usageValue++;
				usageLine = true;
			}

			else if(line =~ /$rt.longOP/ || line =~ /$rt.shortOP/ || line =~ /$rt.longOPb/ || line =~ /$rt.shortOPb/ ) {
				def isBoolean = true;
				opt = new UsageOption()
				def longO =null;
				//line has a long option and option wasn't found before
				if((lo = (line =~ /$rt.longOP/)) ) {
					if(!opts.collect{it.longO}.contains(lo[0][1])&& lo[0][1].toString().size()>0) {
						lastOptionOrDes = true
						isBoolean = false;
						longO= lo[0][1]
						//delete separator of short and long option
						if(longO.lastIndexOf(rt.sep) == longO.size()-1) {
							longO = longO.substring(0,longO.size()-rt.sep.size())
						}
						if(!opts.collect{it.longO}.contains(longO)) {
							//set long option of option
							opt.setLongO(longO)
							//calculate negativeValue for score
							negativeValue += countUnlikelySignsForOption(longO,"long")*20
							//delete special characters for the name
							setNameFromLongOption(longO, names)
						}
					}
				}
				//line has a boolean long option and option wasn't found before
				else if((lo = (line =~ /$rt.longOPb/))&& !opts.collect{it.longO}.contains(lo[0][1]) && lo[0][1].toString().size()>0) {
					lastOptionOrDes = true
					isBoolean = true;
					longO= lo[0][1]
					//delete separator of short and long option
					if(longO.lastIndexOf(rt.sep) == longO.size()-1) {
						longO = longO.substring(0,longO.size()-rt.sep.size())
					}
					if(!opts.collect{it.longO}.contains(longO)) {
						//set long option of option
						opt.setLongO(longO)
						//calculate negativeValue for score
						negativeValue += countUnlikelySignsForOption(longO,"long")*20
						//delete special characters for the name
						setNameFromLongOption(longO, names)
					}
				}
				//line has a short option and option wasn't found before
				if((so = (line =~ /$rt.shortOP/))&& !opts.collect{it.shortO}.contains(so[0][1]) && so[0][1].toString().size()>0) {
					lastOptionOrDes = true
					isBoolean = false;
					def shortO = so[0][1]
					//delete separator of short and long option
					if(shortO.lastIndexOf(rt.sep) == shortO.size()-1) {
						shortO = shortO.substring(0,shortO.size()-rt.sep.size())
					}
					if(!shortOpts.contains(shortO)) {
						def newShort = shortO
						def counter=1;
						while(shortOpts.contains(newShort) || newShort.equals("h")) {
							newShort = shortO+counter
							counter++;
						}
						opt.setShortO(newShort)
						shortOpts.add(newShort)
						negativeValue += countUnlikelySignsForOption(newShort,"short")*20
						if(!opt.hasLO) {
							counter = setNameFromShortOption(newShort, counter, names)
						}
					}
				}
				//No option was found before -> it could be a boolean option
				if(isBoolean) {
					//line has a boolean short option and option wasn't found before
					if((so = (line =~ /$rt.shortOPb/))&& !opts.collect{it.shortO}.contains(so[0][1]) && so[0][1].toString().size()>0 && opt.shortO == null) {
						lastOptionOrDes = true
						isBoolean = false;
						def shortO = so[0][1]
						//delete separator of short and long option
						if(shortO.lastIndexOf(rt.sep) == shortO.size()-1) {
							shortO = shortO.substring(0,shortO.size()-rt.sep.size())
						}
						if(!shortOpts.contains(shortO)) {
							def newShort = shortO
							def counter=1;
							while(shortOpts.contains(newShort) || newShort.equals("h")) {
								newShort = shortO+counter
								counter++;
							}
							opt.setShortO(newShort)
							shortOpts.add(newShort)
							negativeValue += countUnlikelySignsForOption(newShort,"short")*20
							if(!opt.hasLO) {
								counter = setNameFromShortOption(newShort, counter, names)
							}
						}
					}
				}
				def noOption=false
				if(!so.any() && !lo.any() || (so.any() &&  so[0][1].toString().size()==0 && !lo.any()) || opt.name == null) {
					noOption=true
				}
				setTypeforOption(line, isBoolean)
				if(/$rt.defaultO/ != "-" && (defaultV = line =~ /$rt.defaultO/)) {
					setDefaultForOption()
					negativeValue += countUnlikelySignsForOption(defaultV[0][1],"def")

				}
				if((description = line =~ /$rt.description/) && description.size() > 0 && description[0].size() > 1 ) {
					opt.setDescription(description[0][1].replace("\"","\\\"").replace("`", "'"))
					negativeValue += countUnlikelySignsForOption(description[0][1],"des")

				}

				opt.longC = "\'"+rt.longOC+"\'"
				opt.shortC = "\'"+rt.shortOC+"\'"
				opt.longCb = "\'"+rt.longOCb+"\'"
				opt.shortCb = "\'"+rt.shortOCb+"\'"

				if(!noOption)
					opts.add(opt)

				usageValue += opt.value



			}

			else if(lastOptionOrDes && opt.description != null) {
				if((description = line =~ /$rt.description/ )) {
					opt.setDescription(opt.description + " " + description[0][1].replace("\"","\\\"").replace("`", "'"))
				}
				else
					lastOptionOrDes=false
			}

		}


		def optionalA
		def requiredA

		if(optional != null && optional[0] != null && optional[0].size() > 1) {
			optionalA = optional.collect{it[1].trim()}
		}
		if(required != null && required.size() >= 1 && required[0] != null && required[0].size() >= 1)
			requiredA = required.collect{it[1].trim()}

		names = opts.collect{it.name}


		for(def opt: opts) {
			if (requiredA != null && requiredA.contains(opt)) {
				opt.required = true
			}
		}
		for(def req: requiredA) {
			req= req.replaceAll("([^a-zA-Z0-9_])", ""); //Sonderzeichen funktionieren nicht
			if(names.contains(req)) {
				def index = names.indexOf(req);
				opts[index].isRequired = true
				usageValue++;
			}
			else if(req.replaceAll("\\.", "").size() > 0) {
				opt = new UsageOption()
				def replacedName = this.replaceValues(req);
				if(replacedName.size() == 0) {
					opt.name = "noName"+counter;
					counter++;
				}
				else
					opt.name = replacedName
				opt.isRequired = true
				negativeValue += countUnlikelySignsForOption(req,"long")*20
				opts.add(opt)
			}
		}

		for(def optional: optionalA) {
			optional= optional.replaceAll("([^a-zA-Z0-9_])", ""); //Sonderzeichen funktionieren nicht
			if(optional.split(" ").size() > 1) {
				optional = optional.split(" ")[0].trim().replace(rt.longOC.substring(0, rt.longOC.indexOf("(")), "").replace(rt.shortOC.substring(0, rt.shortOC.indexOf("(")), "");
			}
			if(!names.contains(optional) && optional.replaceAll("\\.", "").size() > 0) {
				opt = new UsageOption()
				negativeValue += countUnlikelySignsForOption(optional,"long")*20
				def replacedName = this.replaceValues(optional);
				if(replacedName.size() == 0) {
					opt.name = "noName"+counter;
					counter++;
				}
				else
					opt.name = replacedName
				opts.add(opt)
			}
		}
		if(Debugger.isEnabled()) {
			def req = 0
			if(requiredA != null)
				req = requiredA.size()
			Debugger.log("Found " + opts.size() + " options. "+ req +" from them are required.")
		}
		return usageValue - negativeValue/5;
	}

	/**
	 * Controls the default  value for the option before it is set
	 */
	private setDefaultForOption() {
		def defaultValue = defaultV[0][1];
		//If the default value of integer or double isn't a number. it isn't set.
		if(opt.getType().equals("integer")) {
			boolean noNumber = false;
			try {
				Integer.parseInt(defaultValue)
			}
			catch(NumberFormatException ex) {
				noNumber = true;
			}
			if(!noNumber)
				opt.setDefaultV(defaultValue)
		}
		else if(opt.getType().equals("double")) {
			boolean noNumber = false;
			try {
				Double.parseDouble(defaultValue)
			}
			catch(NumberFormatException ex) {
				noNumber = true;
			}
			if(!noNumber)
				opt.setDefaultV(defaultValue)
		}
		//booleans have no default value
		else if(!opt.getType().equals("boolean"))
			opt.setDefaultV(defaultV[0][1])
	}

	/**
	 * If line has a type, this method sets the type for the option.
	 * @param line current line in usage
	 * @param isBoolean if the option was found because of the boolean regex it is a boolean
	 * @return
	 */
	private setTypeforOption(String line, boolean isBoolean) {
		if(rt.type != "-" && (type = line =~ /$rt.type/)) {
			def typeV = type[0][1]
			if(typeV.equals(rt.integerT))
				opt.setType("integer")
			else if(typeV.equals(rt.pathFileT))
				opt.setType("AbsoluteFilePath")
			else if(typeV.equals(rt.pathFileT))
				opt.setType("AbsoluteFolderPath")
			else if(typeV.equals(rt.booleanT))
				opt.setType("boolean")
			else if(typeV.equals(rt.doubleT))
				opt.setType("double")
			else
				opt.setType("string", false)
		}
		else if(isBoolean)
			opt.setType("boolean")
		else
			opt.setType("string", false)
	}

	/**
	 * Sets the name from the short option. This only happens if no long option exists.
	 * The method cecks if the short option doesn't consist of special characters.
	 * @param newShort 
	 * @param counter 
	 * @param names list of names, to check if some names already exist
	 * @return
	 */
	private int setNameFromShortOption(String newShort, int counter, List names) {
		//remoce special chracters
		def replacedName = this.replaceValues(newShort);
		//if the replacedname is empty, a new name will be set
		if(replacedName.size() == 0) {
			opt.name = "noName"+counter;
			counter++;
		}
		else {
			int nameCounter =1
			def newName = replacedName;
			//short option -h is not allowed
			while(names.contains(newName) || newShort.equals("h")) {
				newName = replacedName + nameCounter;
				nameCounter++
			}
			opt.name = newName;
			names.add(newName);
		}
		return counter
	}

	/**
	 * Sets the name from the short option. This only happens if no long option exists.
	 * The method cecks if the short option doesn't consist of special characters.
	 * @param longO
	 * @param names
	 * @return
	 */
	private setNameFromLongOption(String longO, List names) {
		def replacedName = this.replaceValues(longO);
		//if name is empty after deleting special characters, set the name as a number
		if(replacedName.size() == 0) {
			opt.name = "noName"+counter;
			counter++;
		}
		//if the name equals 'debug' it must be changed because the shscript has this option, too
		else if(replacedName.equals("debug")){
			opt.name = replacedName + counter;
			counter++;
		}
		else {
			//set name of option. If name already taken, it will be numbered.
			int nameCounter =1
			def newName = replacedName;
			while(names.contains(newName)) {
				newName = replacedName + nameCounter;
				nameCounter++
			}
			opt.name = newName;
			names.add(newName);
		}
	}

	/**
	 * read optional and required options from usageline.
	 * @param line current line
	 * @return current line
	 */
	private String readUsageLine(String line) {
		if(line =~ /$rt.optional/ )
			optional = (line =~ /$rt.optional/)
		if(line =~ /$rt.required/ )
			required = (line =~ /$rt.required/)
		return line
	}
	
/**
 * Read the required regex from the regexfiles.
 * @param regexFile path to regexFile
 * @return ReadTemplate with required regex
 */
	private ReadTemplate readRegexFile(String regexFile) {
		ReadTemplate rt = new ReadTemplate()
		rt.readFile(regexFile)
		rt.saveArguments()
		return rt
	}

	/**
	 * Remove special signs
	 * @param string String from which the special signs are deleted
	 * @return
	 */
	private String replaceValues(String string) {
		String returnName = string.replaceAll("([^a-zA-Z0-9_])", "");
		return returnName
	}


	/**
	 * Parse usage from file with regexlist.
	 * @param path
	 * @param usageFile
	 * @param outFile
	 * @return
	 */
	int parseFile(String path, String usageFile, String outFile) {
		println "Start parsing Usage from tool"

		File file = new File(path)
		if(!file.exists()) {
			return -3;
		}
		def lines = file.readLines()
		def opts = parseThis( usageFile, lines);
		File tmp = File.createTempFile("tmp",".dat")
		tmp.deleteOnExit()
		parseMap.put(tmp.getAbsolutePath(), value)
		new WriteUsage().writeInFile(tmp.getAbsolutePath(), opts)
		new WriteUsage().writeInFile(outFile, opts)
		return 0;

	}


	/**
	 * Counts the unikely signs in option parts.
	 * @param string
	 * @param option
	 * @return
	 */
	private int countUnlikelySignsForOption(String string, String option) {
		int result = string.findAll(unlikelySignsForOption.get(option)).size()
		if(result != null)
			return result
		return 0
	}
}
