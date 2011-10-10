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


package scripty.cli;
import java.util.List;
import java.util.ArrayList;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.Argument;

public class ScriptyArguments 
{
    private String htmlSourcePrefix = ".";
    private String javascriptSourcePrefix = ".";
    private String outputPrefix = ".";
    private String moduleOutputPath = "resources/js";
    
    @Option(name="--minify", 
            usage="Sets JavaScript sources to be minified. Will create a new file with a .min.js extension in the same location as the target")
    public List<String> minify = new ArrayList<String>();
    
    @Option(name="--html", 
            usage="Sets html sources" )
    public List<String> html = new ArrayList<String>();
    
    @Option(name="--compiler", 
            usage="Sets the compiler to use. Default is Google closure, you can also choose yui* or uglify* -- *not yet supported" )
    public String compiler = "closure";
    
    @Option(name="--js_default_module_name", 
            usage="Sets the default module name if a module is not specified" )
    public String defaultModuleName = "project-modules.min.js";
    
    @Option(name="--js_output_module_path", 
            usage="Path relative to the output_prefix where modules should be saved. The default path is resources/js" )
    public void setModuleOutputPath(String value)
    {
        if(value.endsWith("/"))
        {
            value = value.substring(0, value.length() - 1);
        }
        
        moduleOutputPath = value;
    }
    
    public String getModuleOutputPath()
    {
        return moduleOutputPath;
    }
    
    @Option(name="--output_prefix", 
            usage="When saving any file, html or js, etc, this is the path prefix that should be applied to any path." )
    public void setOutputPrefix(String value)
    {
        if(value.endsWith("/"))
        {
            value = value.substring(0, value.length() - 1);
        }
        
        outputPrefix = value;
    }
    
    public String getOutputPrefix()
    {
        return outputPrefix;
    }
    
    
    @Option(name="--js_source_prefix", 
            usage="Sets the path value appended to the JavaScript source files" )
    public void setJavaScriptSourcePrefix(String value)
    {
        if(value.endsWith("/"))
        {
            value = value.substring(0, value.length() - 1);
        }
        
        javascriptSourcePrefix = value;
    }
    
    public String getJavaScriptSourcePrefix()
    {
        return javascriptSourcePrefix;
    }
    
    @Option(name="--html_source_prefix", 
            usage="Sets the path value appended to the HTML source files" )
    public void setHtmlSourcePrefix(String value)
    {
       
        if(value.endsWith("/"))
        {
            value = value.substring(0, value.length() - 1);
        }
        
        htmlSourcePrefix = value;
    }
    
    public String getHtmlSourcePrefix()
    {
        return htmlSourcePrefix;
    }
    
    @Argument
    public List<String> arguments = new ArrayList<String>();
    
    
    public List<String> getHtmlFiles() 
    {
      List<String> allHtmlInputs = new ArrayList<String>(html.size() + arguments.size());
      allHtmlInputs.addAll(html);
      allHtmlInputs.addAll(arguments);
      return allHtmlInputs;
    }
}
