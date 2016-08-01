import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;

public class MethodObjects {
	
	public String name, returnType, accessSpecifier;
	public MethodDeclaration declarationNode;
	List<Parameter> parameters = new ArrayList<>();
	
	public String getDependencies(List<String> interfaceList){
		String value="";

		for(FieldDeclaration fieldsDecln : getVariableDeclarations((Node)declarationNode)){
			String tempStr = fieldsDecln.getType().toString();
			if(interfaceList.contains(tempStr))
				value += tempStr+",";
		}
		return value;
	}
	
	public List<FieldDeclaration> getVariableDeclarations(Node parseTree){
		List<FieldDeclaration> returnList = new ArrayList<FieldDeclaration>(),tempList = new ArrayList<FieldDeclaration>(); 
		
		for(Node iteratorNode : parseTree.getChildrenNodes()){
			tempList = new ArrayList<FieldDeclaration>();
			if(iteratorNode.getClass() == FieldDeclaration.class)
				returnList.add((FieldDeclaration) iteratorNode);
			else
				if(tempList.addAll(getVariableDeclarations(iteratorNode)))
					returnList.addAll(tempList);			
		}
		
		return returnList;
	}
	
}
