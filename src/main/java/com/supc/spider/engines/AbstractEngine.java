package com.supc.spider.engines;

import com.supc.spider.Scheduler;
import com.supc.spider.utils.FileUtils;
import com.supc.spider.support.parser.ConfigurationParser;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by caisupeng on 15/10/15.
 */
public abstract class AbstractEngine {

    final Logger logger = LoggerFactory.getLogger(AbstractEngine.class);
    /**
     * The spider config file path
     */
    public static final String CONFIG_FILE_PATH = "/conf";
    /**
     * The suffix
     */
    private static final String CSPIDER_PREFIX = "cspider";
    /**
     * The properties suffix
     */
    private static final String PROPERTIES_SUFFIX = ".properties";
    /**
     * The xml suffix
     */
    private static final String XML_SUFFIX = ".xml";
    /**
     * Is the engine initialized
     */
    public static boolean initialized = false;
    /**
     * The engine application root
     */
    public static String applicationPath;
    /**
     * Is the engine started
     */
    public static boolean started = false;
    /**
     * The engine configuration
     */
    public static Properties configuration = new Properties();
    /**
     * The spider sites
     */
//    public static Map<String, Site> sites = new HashMap<>();
    /**
     * Schedulers
     */
    public static List<Scheduler> schedulers = new ArrayList<>();
    /**
     * The last time than the engine has started
     */
    public static long startedAt;
    /**
     *
     */
    protected GenericApplicationContext applicationContext;
    /**
     *
     */
    public static final String DEFAULT_SPRING_CONFIG_FILE = "spring-core-config.xml";
    /**
     *
     */
    public static final String DEFAULT_SPRING_PROPERTIES_FILE = "application.properties";
    /**
     *
     */
    public static final String springConfigFile = DEFAULT_SPRING_CONFIG_FILE;
    /**
     *
     */
    public static final String springPropertiesFile = DEFAULT_SPRING_PROPERTIES_FILE;

    public void init() {

        started = false;

        applicationPath = System.getProperty("user.dir");

        initSpring();

        createScheduler();

        initialized = true;
    }

    private void initSpring() {
        try {
            applicationContext = new GenericApplicationContext();
            XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(applicationContext);
            Resource resource = new ClassPathResource(springConfigFile);
            xmlReader.loadBeanDefinitions(resource);
            applicationContext.refresh();
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    private void readConfigurations() {
        List<File> files = FileUtils.findFiles(applicationPath, CSPIDER_PREFIX, new String[]{XML_SUFFIX, PROPERTIES_SUFFIX}, new ArrayList<>());
        logger.info("config files :" + files.size());
        for (File file : files) {
            readConfiguration(file, schedulers);
        }
    }

    private void createScheduler() {
        try {
            readConfigurations();
            for (Scheduler item : schedulers) {
                Scheduler scheduler = item;
                logger.info("scheduler name :" + scheduler.getName());
                scheduler.init(configuration);
            }
        } catch (Exception e) {
            logger.error("创建DownLoaders失败");
            logger.error(e.toString());
            stop();
        }
    }

    public void start() {
        if (!started) {
            if (!initialized) {
                init();
            }
            for (Scheduler item : schedulers) {
                Scheduler scheduler = item;
                scheduler.start();
            }
            started = true;
        }
    }

    public void stop() {
        if (started) {
            for (Scheduler item : schedulers) {
                Scheduler scheduler = item;
                scheduler.stop();
            }
            started = false;
        }
    }

    public void readConfiguration(File file, List<Scheduler> schedulers) {
        try {
            switch (FileUtils.getExtWithDot(file.getName())) {
                case PROPERTIES_SUFFIX:
                    configuration.load(new FileInputStream(file));
                    break;
                case XML_SUFFIX:
                    ConfigurationParser.fillSchedulerByXML(file, schedulers, applicationContext);
                    break;
            }
        } catch (IOException e) {
            logger.error("读取properties配置文件失败!");
            logger.error(e.getMessage());
            stop();

        } catch (DocumentException er) {
            logger.error("读取XML配置文件失败!");
            logger.error(er.getMessage());
            stop();
        }
    }

    /**
     * Search a File in the current application
     *
     * @param path Relative path from the application root
     * @return The file even if it doesn't exist
     */
    public static File getFile(String path) {
        return new File(applicationPath, path);
    }

    public static String getProperty(String key) {
        return configuration.getProperty(key);
    }

    public static String getProperty(String key, String def) {
        return configuration.getProperty(key, def);
    }
}
