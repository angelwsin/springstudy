package org.springframework.beans.factory.xml;

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
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

import com.spring.applicationContext.ApplicationContextTest;

public class SpringNameSpaceParse {
	final static Log logger = LogFactory.getLog(ApplicationContextTest.class);
	final static DocumentLoader documentLoader = new DefaultDocumentLoader();
	static ErrorHandler errorHandler = new SimpleSaxErrorHandler(logger);
	static SourceExtractor sourceExtractor = new NullSourceExtractor();
	private static ProblemReporter problemReporter = new FailFastProblemReporter();
	private static ReaderEventListener eventListener = new EmptyReaderEventListener();
	private static final DocumentDefaultsDefinition defaults = new DocumentDefaultsDefinition();
	public static final String ALIAS_ELEMENT = "alias";
	public static final String NAME_ATTRIBUTE = "name";
	public static final String ALIAS_ATTRIBUTE = "alias";
	public static final String IMPORT_ELEMENT = "import";
	public static final String RESOURCE_ATTRIBUTE = "resource";
	public static final String BEAN_ELEMENT = BeanDefinitionParserDelegate.BEAN_ELEMENT;
	public static void main(String[] args)throws Exception {
		
		/**
		 * spring  对 xml中 命名空间的处理逻辑
		 */
		
		 //spring  xml配置文件的加载 和解析逻辑
		  DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		  XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
			 PathMatchingResourcePatternResolver res = new PathMatchingResourcePatternResolver();
			 Resource[]  rs =  res.getResources("classpath*:/conf/opas-*.xml");
			  for(Resource resource:rs){
				  InputSource inputSource = new InputSource(resource.getInputStream());
				  ResourceLoader resourceLoader= beanDefinitionReader.getResourceLoader();
				  Document doc =documentLoader.loadDocument(inputSource, new ResourceEntityResolver(resourceLoader), errorHandler, XmlValidationModeDetector.VALIDATION_XSD, false);
				  DefaultBeanDefinitionDocumentReader documentReader =  DefaultBeanDefinitionDocumentReader.class.cast(BeanUtils.instantiateClass( DefaultBeanDefinitionDocumentReader.class));
				  //String DEFAULT_HANDLER_MAPPINGS_LOCATION = "META-INF/spring.handlers"  在创建namespaceHandlerResolver指定命名空间解析的映射所在文件位置
				  /**
				   * 自定义命名空间的解析需要 把映射关键放在此目录下面
				   */
				  NamespaceHandlerResolver  namespaceHandlerResolver =  new DefaultNamespaceHandlerResolver(resourceLoader.getClassLoader());
				  beanDefinitionReader.setNamespaceHandlerResolver(new DefaultNamespaceHandlerResolver(resourceLoader.getClassLoader()));
				  XmlReaderContext  xmlReaderContext=   new XmlReaderContext(resource, problemReporter, eventListener,sourceExtractor, beanDefinitionReader, namespaceHandlerResolver);
				  Element root = doc.getDocumentElement();
				  BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate(xmlReaderContext);
				  //设置默认值
				  delegate.populateDefaults(defaults, null,root);
				  xmlReaderContext.fireDefaultsRegistered(defaults);
				  if (delegate.isDefaultNamespace(delegate.getNamespaceURI(root))) {
						NodeList nl = root.getChildNodes();
						for (int i = 0; i < nl.getLength(); i++) {
							Node node = nl.item(i);
							if (node instanceof Element) {
								Element ele = (Element) node;
								String namespaceUri = delegate.getNamespaceURI(ele);
								//对bean的命名空间的解析
								if (delegate.isDefaultNamespace(namespaceUri)) {
									if (delegate.nodeNameEquals(ele, IMPORT_ELEMENT)) {
										documentReader.importBeanDefinitionResource(ele);
									}
									else if (delegate.nodeNameEquals(ele, ALIAS_ELEMENT)) {
										documentReader.processAliasRegistration(ele);
									}
									else if (delegate.nodeNameEquals(ele, BEAN_ELEMENT)) {
										documentReader.processBeanDefinition(ele, delegate);
									}
								}
								else {
									//对除了bean命名空间以外的自定义命名空间的解析
									/**
									 * handlerMappings 命名空间的解析类的映射保存在handlerMappings中
									 * 根据namespaceUri得到 对命名空间元素的解析
									 */
							NamespaceHandler handler = xmlReaderContext.getNamespaceHandlerResolver().resolve(namespaceUri);
									if (handler == null) {
										continue ;
									}
									 handler.parse(ele, new ParserContext(xmlReaderContext, delegate, null));
								}
							}
						}
					}
					else {
						delegate.parseCustomElement(root,null);
					}
			  }
			 
		
	}

}
