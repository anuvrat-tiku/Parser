import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

@SuppressWarnings("rawtypes")
	public class FieldVisitor extends VoidVisitorAdapter {
		public List<AttributeObjects> att= new ArrayList<AttributeObjects>();
		
		@Override
		public void visit(FieldDeclaration n, Object arg){
			if(ModifierSet.getAccessSpecifier(n.getModifiers()).toString().compareToIgnoreCase("public")==0 || ModifierSet.getAccessSpecifier(n.getModifiers()).toString().compareToIgnoreCase("private")==0){
				String AttributeType = n.getType().toString();
				for(Node v:n.getChildrenNodes()){
					if(v.getClass() == VariableDeclarator.class){
						AttributeObjects attribute = new AttributeObjects();
						attribute.name = ((VariableDeclarator)v).getId().toString();
						attribute.type = AttributeType;
						attribute.accessSpecifier = ModifierSet.getAccessSpecifier(n.getModifiers()).toString();						
						att.add(attribute);
					}
				}	
			}
		}
		
	}
	