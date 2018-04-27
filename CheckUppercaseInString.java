class CheckUppercaseInString{
	public static void main(String[] args) {
		setPropertyValue("enableSessionExpires",true);
	}

	public static void setPropertyValue(String propertyName,boolean value){
		if (propertyName==null || propertyName.isEmpty()) return ;
		System.out.println("Retrived Orginal Property Name is: "+getPropertyName(propertyName));
	}

	private static String getPropertyName(String propertyName){
		String returnValue="";
		for(int i=0;i<propertyName.length();i++){
			char c=propertyName.charAt(i);
			if(Character.isUpperCase(c))
				returnValue+="_"; 
			returnValue+=c;
		}
	}
}
