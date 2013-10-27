package apache.conf.parser;

import java.util.ArrayList;

import apache.conf.global.Const;
import apache.conf.global.Utils;

public class Define {
	/**
	 * The Define directive has the following format:
	 * Define SRVROOT "/Apache24"
     * 
     * Defines gan be read in the configuration using the following:
     * ServerRoot "${SRVROOT}"
	 * 
	 */
	 
	private String name;
	private String value;
	
	public Define() {
		this.name="";
		this.value="";
	}
	
	public Define(String name, String value) {
		this.name=name;
		this.value=value;
	}
	
	/**
	 *
	 * 
	 * @param directiveValue - A String with a valid apache Define directive  value
	 */
	public Define(String directiveValue) {
		directiveValue=Utils.sanitizeLineSpaces(directiveValue);
		
		String parts[]=directiveValue.replaceAll("\\\\", "/").replaceFirst(" ", "@@").split("@@");
		name=parts[0];
		value=parts[1];
		
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Static function to get all of the configured Defines in apache.
	 * 
	 * @return an array of Define objects
	 * @throws Exception
	 */ 
	public static Define[] getAllDefine(DirectiveParser parser) throws Exception 
	{
		return (new Define().getAllConfigured(parser));
	}
	
	public Define[] getAllConfigured(DirectiveParser parser) throws Exception {
		
		ArrayList<Define> define = new ArrayList<Define>();
		
		String defines[]=parser.getDirectiveValue(Const.defineDirective);
		for(int i=0; i<defines.length; i++) {
			define.add(new Define(defines[i]));
		}
		
		return define.toArray(new Define[define.size()]);
		
	}
	
	@Override
	public boolean equals(Object define) 
	{
		Define target = (Define)define;
		
		if(this.name.equals(target.getName()) && 
		   this.value == target.getValue()) {
			return true;
		}
		
		return false;
	}
	
	public static String replaceDefinesInString(Define defines[], String line)
	{
		String newLine=line;
		
		for(int i=0; i<defines.length; i++) {
			newLine=newLine.replaceAll("\\$\\{ *" + defines[i].getName() + " *\\}", defines[i].getValue());
		}
		
		return newLine;
	}
}