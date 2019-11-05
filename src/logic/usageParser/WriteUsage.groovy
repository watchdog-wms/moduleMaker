package logic.usageParser

import gui.parseOptions.parseOverview.Option
import logic.util.Debugger

/**
 * Writes down the wm files.
 * @author Amrei Menzel
 *
 */
class WriteUsage {

	/**
	 * Writes down the option parameter in wm file.
	 * @param file path to the file
	 * @param opts options
	 * @return
	 */
	public int writeInFile (String file, def opts) {
		if(Debugger.isEnabled()) {
			Debugger.log("Writing in file $file.")
		}
				
		String header = "Name\tshortO\tlongO\tType\tDefault\tDescription\tMin\tMax\tCall\tCallType\tLimitations"
		
		def uFile = new File(file)
		uFile.write(header+"\n") 
		
		for(def opt :opts) {
			//Name
			String line = "$opt.name\t"
			//short
			if(opt.shortO == null || opt.shortO == "") {
				line += "-\t"
			}
			else {
				line += "$opt.shortO\t"
			}
			//longO
			if(opt.longO == null || opt.longO == "") {
				line += "-\t"
			}
			else {
				line += "$opt.longO\t"
			}
			//type
			if(opt.type == null || opt.type == "") {
				line += "string\t"
			}
			else {
				line += "$opt.type\t"
			}
			//Default
			if(opt.defaultV == null || opt.defaultV == "") {
				line += "-\t"
			}
			else {
				line += "$opt.defaultV\t"
			}
			//Description
			if(opt.description == null || opt.description == "") {
				line += "-\t"
			}
			else {
				line += "${opt.description.replace("\t"," ")}\t"
			}
			//Min
			if(opt.isRequired) {
				line += "1\t"
			}
			else {
				line += "0\t"
			}
			//Max
			line += "1\t" 
			//call
			if(opt.shortO == null || opt.shortO == "" || opt.shortO == "-") {
				if(opt.longO != null && opt.longO != ""  && opt.longO != "-") {
					if(opt.type == "boolean")
						line += "$opt.longCb\t"
					else
						line += "$opt.longC\t"
					//callType
					line += "long\t"
				}
				else {
					line += "-\t"
					line += "-\t"
				}
			}
			else {
				if(opt.type == "boolean")					
					line += "$opt.shortCb\t"
				else
					line += "$opt.shortC\t"
				line += "short\t"
				
			}
			//Limitation
			line += "-"
			uFile << line+"\n"			
		}
		return 0;
	}
	
	/**
	 * Writes down already read wm files to new wm files
	 * @param file path to file
	 * @param opts options
	 * @param keepFile parameter of file should be kept
	 */
	public void writeProcessedDataInFile (String file, List<Option> opts, boolean keepFile) {
		if(Debugger.isEnabled()) {
			Debugger.log("Writing in file $file.")
		}
		
		
		String header = "Name\tshortO\tlongO\tType\tDefault\tDescription\tMin\tMax\tCall\tCallType\tLimitations"
		
		def uFile = new File(file)
		if(!keepFile) {
			uFile.deleteOnExit()
		}
		uFile.write(header+"\n")
		
		for(Option opt :opts) {
			uFile << "${opt.getName()}\t${opt.getShortOpt()}\t${opt.getLongOpt()}\t${opt.getType()}\t${opt.getDefaultV()}\t${opt.getDescription()}\t${opt.getMin()}\t${opt.getMax()}\t${opt.getCall()}\t${opt.getCallType()}\t${opt.getRestriction()}\n"
		}
	}
	
}
