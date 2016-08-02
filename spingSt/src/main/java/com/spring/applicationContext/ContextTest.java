package com.spring.applicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.parsing.EmptyReaderEventListener;
import org.springframework.beans.factory.parsing.FailFastProblemReporter;
import org.springframework.beans.factory.parsing.NullSourceExtractor;
import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.beans.factory.parsing.ReaderEventListener;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.BeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DefaultNamespaceHandlerResolver;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.beans.factory.xml.NamespaceHandlerResolver;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

import com.spring.service.UserService;

public class ContextTest {
	final static Log logger = LogFactory.getLog(ContextTest.class);
	final static  DocumentLoader documentLoader = new DefaultDocumentLoader();
	 static ErrorHandler errorHandler = new SimpleSaxErrorHandler(logger);
	 static SourceExtractor sourceExtractor = new NullSourceExtractor();
	 private static ProblemReporter problemReporter = new FailFastProblemReporter();
		private static ReaderEventListener eventListener = new EmptyReaderEventListener();
	  private static Properties  pros = new Properties();
	
	       static{
	    	   
	    	  InputStream is =  Context.class.getClassLoader().getResourceAsStream("conf/load.properties");
	    	  try {
				pros.load(is);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       }
	
	  public static void main(String[] args) throws Exception{
		  
		  //1高级容器的ApplicationContext 
		  FileSystemXmlApplicationContext  context = new FileSystemXmlApplicationContext("classpath*:/conf/opas-*.xml");
		  
		  //通过此方法可以得到系统的参数 如果没有可以指定默认值 格式为：${java.home:d:\java\home"} 其中:后为默认值
		  String x =    SystemPropertyUtils.resolvePlaceholders("${java.hom:d:/java/home}",false);
		  System.out.println(x);
		
		  //2.高级容器的ApplicationContext  是以DefaultListableBeanFactory为基础容器进行扩展
		  DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		  XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
		  
		  
		    /*
		     * 把不同来源的bean定义文件转换成Resouce
	/** Pseudo URL prefix for loading from the class path: "classpath:" 
	        public static final String CLASSPATH_URL_PREFIX = "classpath:";
	        /** URL prefix for loading from the file system: "file:" 
	        public static final String FILE_URL_PREFIX = "file:";
	        /** URL protocol for a file in the file system: "file" 
	        public static final String URL_PROTOCOL_FILE = "file";
	        /** URL protocol for an entry from a jar file: "jar" 
	        public static final String URL_PROTOCOL_JAR = "jar";
	        /** URL protocol for an entry from a zip file: "zip" 
	        public static final String URL_PROTOCOL_ZIP = "zip";
	        /** URL protocol for an entry from a JBoss jar file: "vfszip" 
	        public static final String URL_PROTOCOL_VFSZIP = "vfszip";
	        /** URL protocol for a JBoss VFS resource: "vfs" 
	        public static final String URL_PROTOCOL_VFS = "vfs";
	        /** URL protocol for an entry from a WebSphere jar file: "wsjar" 
	        public static final String URL_PROTOCOL_WSJAR = "wsjar";
	        /** URL protocol for an entry from an OC4J jar file: "code-source" 
	        public static final String URL_PROTOCOL_CODE_SOURCE = "code-source";
	        /** Separator between JAR URL and file path within the JAR 
	        public static final String JAR_URL_SEPARATOR = "!/";
		     */
			 PathMatchingResourcePatternResolver res = new PathMatchingResourcePatternResolver();
			 Resource[]  rs =  res.getResources("classpath*:/conf/opas-*.xml");
			  
			         
			 /**
			  *         ANT方式的通配符有三种：

	                 ?（匹配任何单字符），*（匹配0或者任意数量的字符），**（匹配0或者更多的目录）
			  */
			   
			   AntPathMatcher pathMatcher = new AntPathMatcher();
			  boolean  match =  pathMatcher.match("/**/example", "/app/example, /app/foo/example");
			  System.out.println(match);
			  for(Resource resource:rs){
				  InputSource inputSource = new InputSource(resource.getInputStream());
				  ResourceLoader resourceLoader= beanDefinitionReader.getResourceLoader();
				  Document doc =documentLoader.loadDocument(inputSource, new ResourceEntityResolver(resourceLoader), errorHandler, XmlValidationModeDetector.VALIDATION_XSD, false);
				  BeanDefinitionDocumentReader documentReader =  BeanDefinitionDocumentReader.class.cast(BeanUtils.instantiateClass( DefaultBeanDefinitionDocumentReader.class));
				  NamespaceHandlerResolver  namespaceHandlerResolver =  new DefaultNamespaceHandlerResolver(resourceLoader.getClassLoader());
				  beanDefinitionReader.setNamespaceHandlerResolver(new DefaultNamespaceHandlerResolver(resourceLoader.getClassLoader()));
				  XmlReaderContext  readerContext=   new XmlReaderContext(resource, problemReporter, eventListener,sourceExtractor, beanDefinitionReader, namespaceHandlerResolver);
				//  BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate(readerContext);
				  documentReader.registerBeanDefinitions(doc, readerContext);
			  }
			  UserService userService = (UserService) beanFactory.getBean("userService");
			 
	}
	  
	  
}
