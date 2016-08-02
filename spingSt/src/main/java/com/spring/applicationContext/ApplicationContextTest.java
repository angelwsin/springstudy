package com.spring.applicationContext;

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
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

public class ApplicationContextTest {
	final static Log logger = LogFactory.getLog(ApplicationContextTest.class);
	final static  DocumentLoader documentLoader = new DefaultDocumentLoader();
	 static ErrorHandler errorHandler = new SimpleSaxErrorHandler(logger);
	 static SourceExtractor sourceExtractor = new NullSourceExtractor();
	 private static ProblemReporter problemReporter = new FailFastProblemReporter();
		private static ReaderEventListener eventListener = new EmptyReaderEventListener();

	
	
	  public static void main(String[] args) throws Exception{
		  
		
		  //1.高级容器的ApplicationContext  是以DefaultListableBeanFactory为基础容器进行扩展
		  //spring  xml配置文件的加载 和解析逻辑
		  DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		  XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
			 PathMatchingResourcePatternResolver res = new PathMatchingResourcePatternResolver();
			 Resource[]  rs =  res.getResources("classpath*:/conf/opas-*.xml");
			  for(Resource resource:rs){
				  InputSource inputSource = new InputSource(resource.getInputStream());
				  ResourceLoader resourceLoader= beanDefinitionReader.getResourceLoader();
				  Document doc =documentLoader.loadDocument(inputSource, new ResourceEntityResolver(resourceLoader), errorHandler, XmlValidationModeDetector.VALIDATION_XSD, false);
				  BeanDefinitionDocumentReader documentReader =  BeanDefinitionDocumentReader.class.cast(BeanUtils.instantiateClass( DefaultBeanDefinitionDocumentReader.class));
				  NamespaceHandlerResolver  namespaceHandlerResolver =  new DefaultNamespaceHandlerResolver(resourceLoader.getClassLoader());
				  beanDefinitionReader.setNamespaceHandlerResolver(new DefaultNamespaceHandlerResolver(resourceLoader.getClassLoader()));
				  XmlReaderContext  xmlReaderContext=   new XmlReaderContext(resource, problemReporter, eventListener,sourceExtractor, beanDefinitionReader, namespaceHandlerResolver);
				  documentReader.registerBeanDefinitions(doc, xmlReaderContext);
			  }
			  
			  // Instantiate all remaining (non-lazy-init) singletons.
			  //spring  对 init-lazh属性的实现 
			  /**
			   * spring bean的创建 一般会在 getBean时才去创建bean的实例 同时会调用 init-method
			   * 当配置了 init-lazy = "false"  会在容器启动时就会创建bean的实例 同时会调用 init-method
			   */
				beanFactory.preInstantiateSingletons();
			
	  }

}
