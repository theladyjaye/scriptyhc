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
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.lang.IllegalStateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import scripty.compilers.enums.CompilerAttribute;
import scripty.compilers.ScriptyCompilerFactory;
import scripty.compilers.IScriptyCompiler;
import scripty.cli.ScriptyArguments;
import scripty.sources.ScriptSource;

public class ScriptyRepository 
{
    private HashMap<String, ArrayList<ScriptSource>> candidatesModules;
    private ArrayList<ScriptSource> candidatesMinify;
    private ArrayList<HtmlSource> htmlSources;
    private ScriptyArguments cliArguments;
    
    public ScriptyRepository(ScriptyArguments arguments)
    {
        htmlSources = new ArrayList<HtmlSource>();
        candidatesModules = new HashMap<String, ArrayList<ScriptSource>>();
        candidatesMinify  = new ArrayList<ScriptSource>();
        cliArguments = arguments;
        
        if(cliArguments.minify.size() > 0)
        {
            processMinify();
        }
        
        if(cliArguments.getHtmlFiles().size() > 0)
        {
            processHtmlSources();
        }
    }
    
    public void addHtmlSource(HtmlSource html)
    {
        htmlSources.add(html);
        processModuleCandidatesForHtmlSource(html);
        processHtmlCandidatesForHtmlSource(html);
    }
    
    private void processMinify()
    {
        for(String source : cliArguments.minify)
        {
            ScriptSource script = new ScriptSource();
            script.getAttributes().add(CompilerAttribute.MINIFY);
            script.setPath(source);
            candidatesMinify.add(script);
        }
        
        processMinifyCandidates();
    }
    
    private void processHtmlSources()
    {   
        for(String arg : cliArguments.getHtmlFiles())
        {
                    
            HtmlSource source = new HtmlSource(arg);
            source.setPathPrefix(cliArguments.getHtmlSourcePrefix());
            source.setJavaScriptPathPrefix(cliArguments.getJavaScriptSourcePrefix());
            source.setOutputPrefix(cliArguments.getOutputPrefix());
            source.setJavaScriptDefaultModuleName(cliArguments.defaultModuleName);
            source.setJavaScriptModuleOutputPath(cliArguments.getModuleOutputPath());
            source.process();
            
            if(source.getScripts().size() > 0)
            {
                addHtmlSource(source);
            }
        }
    }
    
    private void processModuleCandidatesForHtmlSource(HtmlSource html)
    {
        for(ScriptSource script : html.getScripts())
        {
            if(script.getAttributes().contains(CompilerAttribute.MODULE))
            {
                if(script.getModule() != null)
                {
                    if(candidatesModules.containsKey(script.getModule()) == false)
                    {
                       candidatesModules.put(script.getModule(), new ArrayList<ScriptSource>()); 
                       candidatesModules.get(script.getModule()).add(script);
                       continue;
                    }

                    if(containsScript(CompilerAttribute.MODULE, script) == false)
                    {
                       candidatesModules.get(script.getModule()).add(script); 
                    }
                }
            }
        }
    }
    
    private void processHtmlCandidatesForHtmlSource(HtmlSource html)
    {
        for(ScriptSource script : html.getScripts())
        {
            if(script.getAttributes().contains(CompilerAttribute.MINIFY))
            {
                
                if(containsScript(CompilerAttribute.MINIFY, script) == false)
                {
                   candidatesMinify.add(script); 
                }
            }
        }
    }
    
    private boolean containsScript(CompilerAttribute collectionType, ScriptSource script)
    {
        boolean result = false;
        
        switch(collectionType)
        {
            case MODULE:
                result = moduleCandidatesContainsScript(script);
                break;
            
            case MINIFY:
                result =  minifyCandidatesContainsScript(script);
                break;
        }
        
        return result;
    }
    
    private boolean moduleCandidatesContainsScript(ScriptSource script)
    {
        ArrayList<ScriptSource> collection = candidatesModules.get(script.getModule());
        
        for(ScriptSource candidate : collection)
        {
            if(candidate.getPath().equals(script.getPath()))
            {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean minifyCandidatesContainsScript(ScriptSource script)
    {
        for(ScriptSource candidate : candidatesMinify)
        {
            if(candidate.getPath().equals(script.getPath()))
            {
                return true;
            } 
        }
        
        return false;
    }
    
    public List<HtmlSource> getHtmlSources()
    {
        return htmlSources;
    }
    
    public void process()
    {
        processModuleCandidates();
        processMinifyCandidates();
    }
    
    private void processModuleCandidates()
    {
        IScriptyCompiler compiler;
        
        for (String key : candidatesModules.keySet())
        {
            List<ScriptSource> scripts = candidatesModules.get(key);
            compiler = ScriptyCompilerFactory.compilerFromString(cliArguments.compiler);
            compiler.addScript(scripts);
            compiler.setAction(CompilerAttribute.MODULE);
            compiler.setModuleName(key);
            compiler.setModuleOutputPath(cliArguments.getModuleOutputPath());
            compiler.setOutputPrefix(cliArguments.getOutputPrefix());
            
            try 
            {
                compiler.execute();    
            } 
            catch (IllegalStateException ex) 
            {
                // Logger.getLogger(ScriptyRepository.class.getName()).log(Level.SEVERE, null, ex);
                // the compiler was not properly configured.
            }
            
        }
    }
    
    private void processMinifyCandidates()
    {
        IScriptyCompiler compiler;
        
        for(ScriptSource script : candidatesMinify)
        {
            compiler = ScriptyCompilerFactory.compilerFromString(cliArguments.compiler);
            compiler.addScript(script);
            compiler.setAction(CompilerAttribute.MINIFY);
            compiler.setOutputPrefix(cliArguments.getOutputPrefix());
            
            try 
            {
                compiler.execute();    
            } 
            catch(Exception ex)
            {
                
            }
        }
    }
    
    public void receipt()
    {
        System.out.println("\n");
        System.out.println("Modules:");
        System.out.println("========================\n");
        
        for(String key : candidatesModules.keySet())
        {
            ArrayList<ScriptSource> scripts = candidatesModules.get(key);
            System.out.println(key);
            System.out.println("------------------------");
            
            for(ScriptSource script : scripts)
            {
                System.out.println(script.getPath());
            }
            
            System.out.println("\n");
            
        }
        
        System.out.println("Minify:");
        System.out.println("========================");
        
        for(ScriptSource script : candidatesMinify)
        {
            System.out.println(script.getPath());
        }

        System.out.println("\n");
                
    }
}
