package com.minis.batis;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.jdbc.core.JdbcTemplate;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author mqz
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory{

    @Autowired
    JdbcTemplate jdbcTemplate;



    String mapperLocations;

    public String getMapperLocations() {
        return this.mapperLocations;
    }

    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    Map<String, MapperNode> mapperNodeMap = new HashMap<>();

    public Map<String, MapperNode> getMapperNodeMap() {
        return mapperNodeMap;
    }

    public DefaultSqlSessionFactory() {}


    /**
     * Declare this method as an initialization method in the configuration file
     */
    public void init() {
        scanLocation(this.mapperLocations);
    }



    @Override
    public SqlSession openSession() {
        SqlSession sqlSession = new DefaultSqlSession();
        sqlSession.setSqlSessionFactory(this);
        sqlSession.setJdbcTemplate(jdbcTemplate);
        return sqlSession;
    }

    @Override
    public MapperNode getMapperNode(String name) {
        return this.mapperNodeMap.get(name);
    }

    private void scanLocation(String location) {
        String sLocationPath = this.getClass().getClassLoader().getResource("").getPath() + location;

        System.out.println("mapper location : "+sLocationPath);
        File dir = new File(sLocationPath);
        for (File file : dir.listFiles()) {
            if(file.isDirectory()){
                scanLocation(location+"/"+file.getName());
            }else{
                buildMapperNodes(location+"/"+file.getName());
            }
        }
    }

    private Map<String, MapperNode> buildMapperNodes(String filePath) {
        SAXReader reader = new SAXReader();
        URL xmlPath = this.getClass().getClassLoader().getResource(filePath);
        try {
            Document document = reader.read(xmlPath);
            Element rootElement = document.getRootElement();
            String namespace = rootElement.attributeValue("namespace");
            Iterator<Element> elementIterator = rootElement.elementIterator();
            while (elementIterator.hasNext()) {
                Element node = elementIterator.next();
                String id = node.attributeValue("id");
                String parameterType = node.attributeValue("parameterType");
                String resultType = node.attributeValue("resultType");
                String sql = node.getText();

                MapperNode selectNode = new MapperNode();
                selectNode.setNamespace(namespace);
                selectNode.setId(id);
                selectNode.setParameterType(parameterType);
                selectNode.setResultType(resultType);
                selectNode.setSql(sql);
                selectNode.setParameter("");

                this.mapperNodeMap.put(namespace + "." + id, selectNode);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return this.mapperNodeMap;
    }



}
