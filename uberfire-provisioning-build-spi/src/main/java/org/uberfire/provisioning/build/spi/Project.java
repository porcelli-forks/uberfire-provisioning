/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.build.spi;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 *
 * @author salaboy
 * Generic Project type to be used by the Build service. 
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public interface Project {

    public String getId();

    public String getType();

    public String getName();
    
    public String getExpectedBinary();

    public String getRootPath();
    
    public String getPath();

    public void setName(String name);

    public void setPath(String path);
    
    public void setRootPath(String rootPath);  
    
    public void setExpectedBinary(String expectedBinary);  

}
