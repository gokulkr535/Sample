class CheckUppercaseInString{
	public static void main(String[] args) {
		setPropertyValue("enableSessionExpires",true);
	}

	public static void setPropertyValue(String propertyName,boolean value){
		if (propertyName==null || propertyName.isEmpty()) return ;
		System.out.println("Retrived Orginal Property Name is: "+getPropertyName(propertyName));
	}

	private static String getPropertyName(String propertyName){
		char[] propertyNameInCharArray=propertyName.toCharArray();
		String returnValue="";
		for(int i=0;i<propertyNameInCharArray.length;i++){
			if(propertyNameInCharArray[i]>=65&&propertyNameInCharArray[i]<=91) //ascii value in between 65 and 91 is A to Z
				returnValue+="_"; 
			returnValue+=propertyNameInCharArray[i];
		}
		return returnValue.toUpperCase();
	}
}