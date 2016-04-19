package com.hframe.generator.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * class的数据对象
 * @author zhangqh6
 *
 */
public class Class {

	private String srcFilePath;

	//包路径
	private String annotation;

	//包路径
	private String classPackage;

	//类名称
	private String className;

	//类类型 type:class interface
	private String type = "class";
	//父类
	private String superClass;

	private String superClassStr = null;

	private List<String> interfaceList = new ArrayList<String>();

	private String implementsStr = null;

	//导入类列表
	private List<String> importClassList = new ArrayList<String>();

	//类属性列表
	private List<Field> fieldList = new ArrayList<Field>();

	//构造器列表
	private List<Constructor> constructorList = new ArrayList<Constructor>();

	//方法列表
	private List<Method> methodList = new ArrayList<Method>();

	private String extMethodStr ;

	public String getFilePath() {
		return srcFilePath + classPackage.replaceAll("\\u002E", "/") + "/" + className + ".java";
	}


	public String getSrcFilePath() {
		return srcFilePath;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAnnotation() {
		return annotation;
	}


	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public void addAnnotation(String annotation) {
		if(this.annotation == null) {
			this.annotation = annotation;
		}else {
			this.annotation += ("\n" + annotation);
		}

	}

	public void setSrcFilePath(String srcFilePath) {
		this.srcFilePath = srcFilePath;
	}




	public String getExtMethodStr() {
		return extMethodStr;
	}


	public void setExtMethodStr(String extMethodStr) {
		this.extMethodStr = extMethodStr;
	}


	public String getImplementsStr() {
		return implementsStr;
	}


	public void setImplementsStr(String implementsStr) {
		this.implementsStr = implementsStr;
	}


	//添加接口
	public void addInterface(String interfaceName) {
		interfaceList.add(interfaceName);
		if(implementsStr == null) {
			implementsStr = "implements ";
		}else {
			implementsStr += ", ";
		}

		implementsStr += interfaceName;
	}

	//添加导入类
	public void addImportClass(String importClass) {
		if(!importClassList.contains(importClass)) {
			importClassList.add(importClass);
		}
	}

	//添加类属性
	public void addField(Field field) {
		fieldList.add(field);
	}

	//添加构造方法
	public Constructor addConstructor(Constructor constructor) {
		constructorList.add(constructor);
		return constructor;
	}

	//添加构造方法
	public Constructor addConstructor() {
		Constructor constructor = new Constructor();
		constructorList.add(constructor);
		return constructor;
	}

	//添加方法
	public void addMethod(Method method) {
		methodList.add(method);
	}

	public String getClassPackage() {
		return classPackage;
	}

	public void setClassPackage(String classPackage) {
		this.classPackage = classPackage;
	}

	public String getClassPath() {
		return classPackage + "." + className;
	}

	public void setClassPath(String classPath) {
		classPackage = classPath.substring(0,classPath.lastIndexOf("."));
		className = classPath.substring(classPath.lastIndexOf(".")+1);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSuperClass() {
		return superClass;
	}

	public void setSuperClass(String superClass) {
		this.superClass = superClass;
		superClassStr = "extends " + superClass;
	}

	public List<String> getInterfaceList() {
		return interfaceList;
	}

	public List<String> getImportClassList() {
		return importClassList;
	}


	public List<Field> getFieldList() {
		return fieldList;
	}

	public List<Constructor> getConstructorList() {
		return constructorList;
	}

	public List<Method> getMethodList() {
		return methodList;
	}


	public String getSuperClassStr() {
		return superClassStr;
	}


	public void setSuperClassStr(String superClassStr) {
		this.superClassStr = superClassStr;
	}





//	//成员变量列表-单个类型成员变量
//	List<List<String>> sigleVarList = new ArrayList<List<String>>();
//	
//	//成员变量列表-集合类型成员变量
//	List<List<String>> multiVarList = new ArrayList<List<String>>();
//	
//	//构造方法类表
//	List<List<List<String>>> constrauctorListList = new ArrayList<List<List<String>>>();
//	

//	/**
//	 * 添加引入类
//	 * @param importClass
//	 */
//	public void addImportClass(String importClass){
//		importClassList.add(importClass);
//	}
//	
//	/**
//	 * 添加成员变量-单个
//	 * @param varInfo
//	 */
//	public void addSigleVar(String[] varInfo){
//		sigleVarList.add(Arrays.asList(varInfo));
//	}
//	
//	/**
//	 * 添加成员变量-单个
//	 * @param varInfo
//	 */
//	public void addSigleVar(List<String> varInfo){
//		sigleVarList.add(varInfo);
//	}
//
//	/**
//	 * 添加成员变量-列表
//	 * @param varInfo
//	 */
//	public void addMultiVar(String[] varInfo){
//		multiVarList.add(Arrays.asList(varInfo));
//	}
//	
//	/**
//	 * 添加成员变量-列表
//	 * @param varInfo
//	 */
//	public void addMultiVar(List<String> varInfo){
//		multiVarList.add(varInfo);
//	}

//	/**
//	 * 获得一个构造器
//	 * @return
//	 */
//	public Constrauctor getConstrauctor(){
//		Constrauctor constrauctor = new Constrauctor();
//		constrauctorListList.add(constrauctor.getConstrauctorList());
//		return constrauctor;
//	}
//	
//	class  Constrauctor{
//		List<List<String>> constrauctorList = new ArrayList<List<String>>();
//
//		public List<List<String>> getConstrauctorList() {
//			return constrauctorList;
//		}
//		
//		public void addInVar(String[] varInfo){
//			constrauctorList.add(Arrays.asList(varInfo));
//		}
//		
//		public void addInVar(List varInfo){
//			constrauctorList.add(varInfo);
//		}
//		
//		public void addCodeLine(String[] code){
//			constrauctorList.add(Arrays.asList(code));
//		}
//		
//		public void addCodeLine(List code){
//			constrauctorList.add(code);
//		}
//		
//	}

	/**
     * class的数据对象
     * @author zhangqh6
     *
     */
    public static class ClassDescData {

        //引用类列表
        List<String> importClassList = new ArrayList<String>();

        //成员变量列表-单个类型成员变量
        List<List<String>> sigleVarList = new ArrayList<List<String>>();

        //成员变量列表-集合类型成员变量
        List<List<String>> multiVarList = new ArrayList<List<String>>();

        //构造方法类表
        List<List<List<String>>> constrauctorListList = new ArrayList<List<List<String>>>();


        /**
         * 添加引入类
         * @param importClass
         */
        public void addImportClass(String importClass){
            importClassList.add(importClass);
        }

        /**
         * 添加成员变量-单个
         * @param varInfo
         */
        public void addSigleVar(String[] varInfo){
            sigleVarList.add(Arrays.asList(varInfo));
        }

        /**
         * 添加成员变量-单个
         * @param varInfo
         */
        public void addSigleVar(List<String> varInfo){
            sigleVarList.add(varInfo);
        }

        /**
         * 添加成员变量-列表
         * @param varInfo
         */
        public void addMultiVar(String[] varInfo){
            multiVarList.add(Arrays.asList(varInfo));
        }

        /**
         * 添加成员变量-列表
         * @param varInfo
         */
        public void addMultiVar(List<String> varInfo){
            multiVarList.add(varInfo);
        }

        /**
         * 获得一个构造器
         * @return
         */
        public Constrauctor getConstrauctor(){
            Constrauctor constrauctor = new Constrauctor();
            constrauctorListList.add(constrauctor.getConstrauctorList());
            return constrauctor;
        }

        class  Constrauctor{
            List<List<String>> constrauctorList = new ArrayList<List<String>>();

            public List<List<String>> getConstrauctorList() {
                return constrauctorList;
            }

            public void addInVar(String[] varInfo){
                constrauctorList.add(Arrays.asList(varInfo));
            }

            public void addInVar(List varInfo){
                constrauctorList.add(varInfo);
            }

            public void addCodeLine(String[] code){
                constrauctorList.add(Arrays.asList(code));
            }

            public void addCodeLine(List code){
                constrauctorList.add(code);
            }

        }
    }
}
