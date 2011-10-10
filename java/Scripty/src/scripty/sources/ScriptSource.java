/*
 * Copyright 2011 Adam Venturella
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package scripty.sources;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.EnumSet;
import java.io.File;
import scripty.sources.enums.ScriptSourceType;
import scripty.compilers.enums.CompilerAttribute;

public class ScriptSource 
{
    
    private String path;
    private String sourceTypeString;
    private String module;
    private ScriptSourceType sourceType;
    private String rawScriptElement;
    private String outputPrefix = ".";
    private String pathPrefix = "";
    private String defaultModuleName = "";
    private String moduleOutputPath = "";
    
    private EnumSet<CompilerAttribute> attributes;
    
    
    public ScriptSource()
    {
        attributes = EnumSet.noneOf(CompilerAttribute.class);
        sourceType = ScriptSourceType.UNKNOWN;
    }
    
    public ScriptSource(String scriptElement)
    {
        attributes = EnumSet.noneOf(CompilerAttribute.class);
        sourceType = ScriptSourceType.UNKNOWN;
        rawScriptElement = scriptElement;
        
    }
    
    public void process()
    {
        processScriptElement(rawScriptElement);
        rawScriptElement = null;
    }
    
    private void processScriptElement(String element)
    {
        processScriptTypeForElement(element);
        
        if(getType() == ScriptSourceType.JAVASCRIPT)
        {
            processScriptSourceForElement(element);
            processAttributesForElement(element);
        }
        else if(getType() == ScriptSourceType.UNKNOWN)
        {
            processScriptSourceForElement(element);
            
            if(getPath() != null && getPath().endsWith(".js"))
            {
                setType(ScriptSourceType.JAVASCRIPT);
                processAttributesForElement(element);
            }
        }
    }
    
    private void processScriptSourceForElement(String element)
    {
        Pattern srcPattern = Pattern.compile("src=[\"']([^\"']+)", Pattern.CASE_INSENSITIVE);
        Matcher srcMatcher = srcPattern.matcher(element);
                   
        if(srcMatcher.find())
        {
            setPath(srcMatcher.group(1));
        }
    }
    
    private void processAttributesForElement(String element)
    {
        Pattern modulePattern = Pattern.compile("module", Pattern.CASE_INSENSITIVE);
        Pattern moduleNamePattern = Pattern.compile("module=[\"']([^\"']+)", Pattern.CASE_INSENSITIVE);
        Pattern minifyPattern = Pattern.compile("minify", Pattern.CASE_INSENSITIVE);
        
        Matcher moduleMatcher = modulePattern.matcher(element);
        
        Matcher minifyMatcher;
                   
        if(moduleMatcher.find())
        {
            Matcher moduleNameMatcher = moduleNamePattern.matcher(element);
            
            if(moduleNameMatcher.find())
            {
                setModule(moduleNameMatcher.group(1));
            }
            else
            {
                setModule(defaultModuleName);
            }
            
            getAttributes().add(CompilerAttribute.MODULE);
            return;
        }
        
        minifyMatcher = minifyPattern.matcher(element);
        
        if(minifyMatcher.find())
        {
            getAttributes().add(CompilerAttribute.MINIFY);
        }
    }
    
    private void processScriptTypeForElement(String element)
    {
        Pattern typePattern = Pattern.compile("type=[\"']([^\"']+)", Pattern.CASE_INSENSITIVE);
        Matcher typeMatcher = typePattern.matcher(element);
                   
        if(typeMatcher.find())
        {
            setTypeString(typeMatcher.group(1));
            setSourceTypeForString(sourceTypeString);
        }
        else
        {
            setType(ScriptSourceType.UNKNOWN);
        }
    }
    
    private void setSourceTypeForString(String value)
    {
        ScriptSourceType candidate;
        
        
        if(value.contains("javascript"))
        {
            candidate = ScriptSourceType.JAVASCRIPT;
        }
        else if (value.contains("mustache"))
        {
            candidate = ScriptSourceType.MUSTACHE;
        }
        else
        {
            candidate = ScriptSourceType.UNKNOWN;
        }
        
        setType(candidate);
    }
    
    public String getPath()
    {
        return pathPrefix + path;
    }
    
    public String getMinifyName()
    {
        String filename = getFile().getName();
        String ext;
        
        int extDot = filename.lastIndexOf(".");
        
        ext = filename.substring(extDot + 1);
        filename = filename.substring(0, extDot);
        
        return filename + ".min." + ext;
    }
    
    public String getModuleElement()
    {
        int lastSlash = path.lastIndexOf("/");
        String pathOnly = path.substring(0, lastSlash);
                
        String src = moduleOutputPath + "/" + getModule();
        
        return String.format("<script src=\"%s\"></script>", src);
    }
    
    public String getMinifyElement()
    {
        int lastSlash = path.lastIndexOf("/");
        String pathOnly = path.substring(0, lastSlash);
                
        String src = pathOnly + "/" + getMinifyName();
        
        return String.format("<script src=\"%s\"></script>", src);
    }
    
    public void setModuleOutputPath(String value)
    {
        moduleOutputPath = value;
    }
    
    public void setDefaultModuleName(String value)
    {
        defaultModuleName = value;
    }
    
    public void setOutputPrefix(String value)
    {
        outputPrefix = value; 
    }
    
    public String getOutputPrefix()
    {
        return outputPrefix; 
    }
    
    public void setPath(String value)
    {
        path = value;
    }
    
    public File getFile()
    {
        return new File(getPath());
    }
    
    public void setPathPrefix(String value)
    {
        pathPrefix = value; 
    }
    
    public ScriptSourceType getType()
    {
        return sourceType;
    }
    
    public void setType(ScriptSourceType value)
    {
        sourceType = value;
    }
    
    public String getTypeString()
    {
        return sourceTypeString;
    }
    
    public void setTypeString(String value)
    {
        sourceTypeString = value;
    }
    
    public EnumSet<CompilerAttribute> getAttributes()
    {
        return attributes;
    }
    
    public void setAttributes(EnumSet<CompilerAttribute> value)
    {
        attributes = value;
    }
    
    public String getModule()
    {
        return module;
    }
    
    public void setModule(String value)
    {
        module = value;
    }
}
