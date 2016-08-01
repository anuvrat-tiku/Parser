import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

@SuppressWarnings("rawtypes")
	public class ClassVisitor extends VoidVisitorAdapter{
		public ClassObjects classObj;
		@SuppressWarnings("unchecked")
		@Override
		
		public void visit(ClassOrInterfaceDeclaration c_i_dec, Object arg) {			
			classObj = new ClassObjects();
			
			classObj.className = c_i_dec.getName();
			classObj.isInterface = c_i_dec.isInterface();
			classObj.extendList = c_i_dec.getExtends();
			
			FieldVisitor fv = new FieldVisitor();			
			fv.visit(c_i_dec, arg);
			classObj.attributes = fv.att;
			
			MethodVisitor mv = new MethodVisitor();
			classObj.methods=mv.methods;
			mv.visit(c_i_dec, arg);
			
			if(!c_i_dec.isInterface()){
			classObj.implementList = c_i_dec.getImplements();
			
			getFinalDependencies(c_i_dec, classObj);
			}
		}
		
		private void getFinalDependencies(Node traveller, ClassObjects classObj){
			if(traveller.getClass() == ReferenceType.class){
				classObj.referencesList.add(((ReferenceType)traveller).getType().toString());
			}
			else if(traveller.getChildrenNodes()!=null)
				for(Node innerNode : traveller.getChildrenNodes())
					getFinalDependencies(innerNode, classObj);
		}
		
	}