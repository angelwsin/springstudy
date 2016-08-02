package com.spring.asm;

import java.io.BufferedInputStream;
import java.io.InputStream;

import org.springframework.asm.ClassReader;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

/**
   * Spring  ASM ASM是一个java字节码操纵框架，它能被用来动态生成类或者增强既有类的功能。
   * @author angel
   *
   */
public class SpringAsm {
	
	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
	private static  String resourcePattern = DEFAULT_RESOURCE_PATTERN;
	public static void main(String[] args) throws Exception{
		
		
		//spring中的 路径解析逻辑
		String basePackage="com.spring";
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
				ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage)) + "/" + resourcePattern;
		
		PathMatchingResourcePatternResolver res = new PathMatchingResourcePatternResolver();
		 Resource[]  rs =  res.getResources(packageSearchPath);
		 
		 for(Resource resource : rs ){
			 InputStream is = new BufferedInputStream(resource.getInputStream());
				ClassReader classReader;
				try {
					classReader = new ClassReader(is);
				}
				catch (IllegalArgumentException ex) {
					throw new NestedIOException("ASM ClassReader failed to parse class file - " +
							"probably due to a new Java class file version that isn't supported yet: " + resource, ex);
				}
				finally {
					is.close();
				}

				
				/**
				 * spring 读取注解
				 */
				AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor(SpringAsm.class.getClassLoader());
				classReader.accept(visitor, ClassReader.SKIP_DEBUG);
				
				
			 
		 }
		 
	}

}
