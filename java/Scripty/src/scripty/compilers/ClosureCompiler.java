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
package scripty.compilers;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.lang.IllegalStateException;
import java.lang.RuntimeException;
import scripty.compilers.enums.CompilerAttribute;
import scripty.sources.ScriptSource;

public class ClosureCompiler implements IScriptyCompiler
{
    private List<ScriptSource> scripts;
    private CompilerAttribute action;
    private String moduleName;
    private String outputPrefix = ".";
    private String compilerBasePath = ".";
    private String moduleOutputPath;
    
    
    public ClosureCompiler()
    {
        scripts = new ArrayList<ScriptSource>();
        moduleName = "";
    }
    
    @Override
    public void addScript(ScriptSource script) 
    {
        scripts.add(script);
    }
    
    @Override
    public void addScript(List<ScriptSource> script) 
    {
        scripts.addAll(script);
    }

    @Override
    public void setAction(CompilerAttribute action) 
    {
        this.action = action;
    }
    
    @Override
    public void setCompilerBasePath(String value)
    {
        compilerBasePath = value;
    }

    @Override
    public void setModuleName(String name) 
    {
        moduleName = name;
    }
    
    @Override
    public void setModuleOutputPath(String value)
    {
        moduleOutputPath = value;
    }
    
    @Override
    public void setOutputPrefix(String value)
    {
        outputPrefix = value;
    }
    
    @Override
    public void execute() throws IllegalStateException
    {
        if(action == null) action = CompilerAttribute.MINIFY;
        
        switch(action)
        {
            case MINIFY:
                minify();
                break;
               
            case MODULE:
                modulify();
                break;
        }
    }
    
    
    private void minify()
    {
        try
        {
            for(ScriptSource script : scripts)
            {
                ArrayList<String> args = new ArrayList<String>();
                String minifyName;
                File scriptFile = new File(script.getPath());
                
                
                args.add("--js");
                args.add(script.getPath());
                
                
                minifyName = outputPrefix + "/" + scriptFile.getParent() + "/" + script.getMinifyName();
                
                args.add("--js_output_file");
                args.add(minifyName);
                
                
                IScriptyCompilerCommand command = new ClosureCompilerCommand();
                command.setArguments(args);
                command.setCompilerBasePath(compilerBasePath);
                command.execute();
            }
        }
        catch(Exception e)
        {
        
        }
    }
    
    private void modulify() throws IllegalStateException
    {
        if(moduleName.isEmpty())
        {
            throw new IllegalStateException("modulify called without module name");
        }
        
        try
        {
            ArrayList<String> args = new ArrayList<String>();
            args.add("--js_output_file");
            args.add(outputPrefix + "/" + moduleOutputPath + "/" + moduleName);
            
            for(ScriptSource script : scripts)
            {
                args.add("--js");
                args.add(script.getPath());
            }
            
            IScriptyCompilerCommand command = new ClosureCompilerCommand();
            command.setArguments(args);
            command.setCompilerBasePath(compilerBasePath);
            command.execute();
            
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
