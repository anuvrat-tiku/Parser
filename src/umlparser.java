
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import net.sourceforge.plantuml.SourceStringReader;


public class umlparser{
	public static List<String> interfaceList = new ArrayList<String>();

	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception{
		List<ClassObjects> AllClasses = new ArrayList<ClassObjects>();
		
		String path = null;
		String output = null;
		if (args.length == 2) {
		path = args[0];
		output = args[1];
		}
		
		File[] fl;
		fl = new File(path).listFiles();
		for (File file : fl){

			if (!file.isDirectory() && file.getName().contains(".java")){
             		
				 FileInputStream in = new FileInputStream(file);
				 CompilationUnit cu;
			        try {
			            cu = JavaParser.parse(in);
			        } finally {
			            in.close();
			        }
			        
			        ClassVisitor visitor = new ClassVisitor();
			        visitor.visit(cu, null);			        
			        
			        if(visitor.classObj!=null)
			        AllClasses.add(visitor.classObj);			        
			 }
		}
		getInterfaces(AllClasses);
		
		
		String source = "@startuml\n";
		source += PlantUmlSyntax(AllClasses);
		source += drawDependencies(AllClasses);
		source += "\n@enduml\n";
		System.out.println(source);
		
		OutputStream png = null;
		try {
			png = new FileOutputStream(output);
		}
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		
		SourceStringReader r = new SourceStringReader(source);
		
		try {
			@SuppressWarnings("unused")
			String img = r.generateImage(png);
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}

	
	private static String PlantUmlSyntax(List<ClassObjects> classList){
		
		String PlantCode = "";
		Boolean first = true;
		
		for(ClassObjects obj : classList){
			
			if(!obj.isInterface)
				PlantCode += "class " + obj.className;
			else
				PlantCode += "Interface " + obj.className;
			
			
			if(obj.extendList != null && !obj.extendList.isEmpty()){
				PlantCode += " extends ";
				first = true;
				for(ClassOrInterfaceType c : obj.extendList)
				{
					if(first){
						PlantCode += c.getName();
						first = false;
					}
					else
						PlantCode += ", "+c.getName();
				}
			}
			
			
			if(obj.implementList!= null && !obj.implementList.isEmpty() && !obj.isInterface){
				PlantCode += " implements ";
				first = true;
				for(ClassOrInterfaceType c : obj.implementList)
				{
					if(first){
						PlantCode += c.getName();
						first = false;
					}
					else
						PlantCode += ", "+c.getName();
				}
			}
			
			PlantCode += " {\n";
			for(AttributeObjects attribute : obj.attributes)
				PlantCode += "+"+attribute.name+":"+attribute.type+"\n";
			for(MethodObjects method : obj.methods)
				PlantCode += "+"+method.name+"()"+ ":"+method.returnType +"\n";
			PlantCode += "}\n";			
		}
		
		return PlantCode;
	}
	
	
	public static String drawDependencies(List<ClassObjects> classList){
		String dependencyString = "";
		List<String> dependencyList = new ArrayList<String>();
		for(ClassObjects classObj : classList){
			for(String s : classObj.referencesList)
				if(!dependencyList.contains(classObj.className +" ..> "+s) && interfaceList.contains(s))
					dependencyList.add(classObj.className +" ..> "+s);
			for(AttributeObjects attrObj : classObj.attributes){
				if(interfaceList.contains(attrObj.type)){
					dependencyString = classObj.className +" ..> "+attrObj.type;
					if(!dependencyList.contains(dependencyString))
						dependencyList.add(dependencyString);
				}
			}
			
			for(MethodObjects methodObj: classObj.methods){
				String methodInterfaceList = methodObj.getDependencies(interfaceList);
				for(String iteratorString : methodInterfaceList.split(",")){
					if (interfaceList.contains(iteratorString)){
						dependencyString = classObj.className +" ..> "+iteratorString;
						if(!dependencyList.contains(dependencyString))
							dependencyList.add(dependencyString);
					}
				}
			}			
		}
		return dependencyString;
		
	}

	public static void getInterfaces(List<ClassObjects> classList){
		for(ClassObjects obj : classList)
			if(obj.isInterface)
				interfaceList.add(obj.className);
	}
	
}