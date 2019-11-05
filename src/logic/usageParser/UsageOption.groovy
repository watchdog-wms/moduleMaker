package logic.usageParser

/**
 * Class that represents a UsageOption.
 * @author Amrei Menzel
 *
 */
class UsageOption {
	
	//It's ture if the option has an long option
	private boolean hasLO = false;
	
	//It's true if the option is required
	private boolean isRequired = false;


	//the long option
	private String longO
	
	//the call of the short option
	private String shortC
	
	//the call of the long option
	private String longC
	
	//the call of the short option (boolean)
	private String shortCb
	
	//the call of the long option (boolean)
	private String longCb
	
	//the type of the option, it could be string, boolean, integer, double, AbsoluteFilePath and AbsoluteFolderPath
	private String type
	
	//the description of the option
	private String description
	
	//the default value of the option
	private String defaultV
	
	//the restriction of the option
	private String restriction
	
	//the name of the option
	private String name
	
	//the start of the usageline like "Usage: "
	private String startUsageLine

	//the value of the option to calculate the value of the regex
	private double value=0;

	//the short option
	private String shortO
	
	//the minimum of attributes
	private String minO;
	
	//the maximum of attributes
	private String maxO;
	
	//the final call of option like -m or --threads.
	private String optionCall;
	
	//the call of option like -(.*) (,*)
	private String call
	
	//the type of option. short, long or none
	private String callType;
	
	public boolean isHasLO() {
		return hasLO;
	}

	public void setHasLO(boolean hasLO) {
		this.hasLO = hasLO;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public String getShortC() {
		return shortC;
	}

	public void setShortC(String shortC) {
		this.shortC = shortC;
	}

	public String getLongC() {
		return longC;
	}

	public void setLongC(String longC) {
		this.longC = longC;
	}

	public String getShortCb() {
		return shortCb;
	}

	public void setShortCb(String shortCb) {
		this.shortCb = shortCb;
	}

	public String getLongCb() {
		return longCb;
	}

	public void setLongCb(String longCb) {
		this.longCb = longCb;
	}

	public String getRestriction() {
		return restriction;
	}

	public void setRestriction(String restriction) {
		this.restriction = restriction;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartUsageLine() {
		return startUsageLine;
	}

	public void setStartUsageLine(String startUsageLine) {
		this.startUsageLine = startUsageLine;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getShortO() {
		return shortO;
	}

	public String getLongO() {
		return longO;
	}

	public String getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	public String getDefaultV() {
		return defaultV;
	}


	public void setShortO(final String shortO) {
		this.shortO = shortO;
		value += 3;
	}

	public void setLongO(final String longO) {
		this.longO = longO;
		hasLO = true;
		value += 3;
	}


	public void setType(final String type) {
		this.type = type;
		value += 3;
	}

	public void setType(final String type, final boolean isParsed) {
		this.type = type;
		if(isParsed)
			value += 3;
	}


	public void setDescription(final String description) {
		this.description = description;
		value += 1;
	}


	public void setDefaultV(final String defaultV) {
		this.defaultV = defaultV;
		value += 2;
	}


	public void setOptionCall(String call) {
		this.optionCall = call;
	}

	public String getOptionCall() {
		return optionCall;
	}

	public void setMinO(String minO) {
		this.minO = minO;
	}

	public String getMinO() {
		return minO;
	}

	public void setMaxO(String maxO) {
		this.maxO = maxO;
	}

	public String getMaxO() {
		return maxO;
	}


	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getCallType() {
		return callType;
	}

	public void setCall(String call2) {
		this.call = call2;
	}

	public String getCall() {
		return call
	}
}
