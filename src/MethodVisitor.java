import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

@SuppressWarnings("rawtypes")
	public class MethodVisitor extends VoidVisitorAdapter {
		public List<MethodObjects> methods = new ArrayList<MethodObjects>();
		
		@Override
		public void visit(MethodDeclaration n, Object arg){			
			MethodObjects mo = new MethodObjects();
			
			mo.name = n.getName();
			mo.parameters = n.getParameters();
			mo.accessSpecifier = ModifierSet.getAccessSpecifier(n.getModifiers()).toString();
			mo.returnType = n.getType().toString();
			mo.declarationNode = n;
			methods.add(mo);			
		}
	}