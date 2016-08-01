import java.util.*;

import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class ClassObjects{
	
	public String className;
	public List<AttributeObjects> attributes = new ArrayList<AttributeObjects>();
	public List<MethodObjects> methods = new ArrayList<MethodObjects>();
	public List<ClassOrInterfaceType> implementList = new ArrayList<ClassOrInterfaceType>();
	public List<ClassOrInterfaceType> extendList = new ArrayList<ClassOrInterfaceType>();
	public boolean isInterface;
	public List<String> referencesList = new ArrayList<String>();
}
