package org.springframework.core.type.classreading;

import java.util.Map;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 * Spring 包扫描
 */
public class SpringClassParse {
	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
	private static  String resourcePattern = DEFAULT_RESOURCE_PATTERN;
	public static void main(String[] args) throws Exception{
		
		
		//spring中的 路径解析逻辑
		String basePackage="com.spring";
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
				ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage)) + "/" + resourcePattern;
		
		PathMatchingResourcePatternResolver res = new PathMatchingResourcePatternResolver();
		 Resource[]  rs =  res.getResources(packageSearchPath);
		//spring中 class的扫描 实现逻辑
		  for(Resource resource:rs){
			  MetadataReader metadataReader =new SimpleMetadataReader(resource,SpringClassParse.class.getClassLoader());
			  Set<String>  set =  metadataReader.getAnnotationMetadata().getAnnotationTypes();
			  for(String st:set){
				 Map<String,Object> map =  metadataReader.getAnnotationMetadata().getAnnotationAttributes(st);
				  for(String key : map.keySet()){
					  System.out.println(key+"---"+map.get(key));
				  }
				  System.out.println(st);
			  }
		  }
	}

}
