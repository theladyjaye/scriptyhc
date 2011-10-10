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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import scripty.sources.enums.ScriptSourceType;
import scripty.compilers.enums.CompilerAttribute;
import scripty.helpers.ScriptyHelpers;

public class HtmlSource 
{
    private String path;
    private String pathPrefix = "";
    private String javascriptPathPrefix = "";
    private String javascriptDefaultModuleName = "";
    private String javascriptModuleOutputPath = "";
    private String outputPrefix = ".";
    
    private ArrayList<ScriptSource> scripts;
    private ArrayList<String> replacedScripts;
    
    
    public HtmlSource(String path)
    {
        this.path = path;
    }
    
    public void process()
    {
        processSource();
    }
    
    public void setJavaScriptModuleOutputPath(String value)
    {
        javascriptModuleOutputPath = value;
    }
    
    public void setJavaScriptDefaultModuleName(String value)
    {
        javascriptDefaultModuleName = value;
    }
    public void setOutputPrefix(String value)
    {
        outputPrefix = value; 
    }
    
    public void setPathPrefix(String value)
    {
        pathPrefix = value; 
    }
    
    public void setJavaScriptPathPrefix(String value)
    {
        javascriptPathPrefix = value;
    }
    
    public String getPath()
    {
        return pathPrefix + "/" + path;
    }
    
    
    private void processSource()
    {
        scripts = new ArrayList<ScriptSource>(); 
        File inputFile = new File(getPath());
        
        BufferedReader reader;
        FileInputStream fileStream;
        InputStreamReader streamReader;
        
        File rewriteFile;
        FileWriter rewriteStream = null;
        BufferedWriter bufferedOut = null;
        boolean shouldWriteFile;
        boolean withinHtmlComment  = false;
        
        try 
        {

            String line;
            
            inputFile = new File(path);
            
            rewriteFile = new File( outputPrefix + "/" + path);
            
            ScriptyHelpers.createPathWithString(rewriteFile.getParent());
            
            fileStream = new FileInputStream(inputFile.getAbsolutePath());
            streamReader = new InputStreamReader(fileStream);
            reader = new BufferedReader(streamReader);
            
            replacedScripts = new ArrayList<String>();
            
            
            if(rewriteFile.exists())
            {
                rewriteFile.delete();    
            }
            
            
            shouldWriteFile = rewriteFile.createNewFile();
            
            if(shouldWriteFile)
            {
                rewriteStream = new FileWriter(rewriteFile.getPath());
                bufferedOut = new BufferedWriter(rewriteStream);
            }
            
            while ((line = reader.readLine()) != null) 
            {
                String candidate = line.trim();
                boolean writeLine = true;
                String lineEnding = "\n";
                String whiteSpace;
                
                // readLine does not include the line termination, so add it back.
                // this is a primative way of doing this, we should probably check
                // for the kind of line ending the file is saved with.
                line = line + lineEnding; 
               
               if(candidate.startsWith("<!--"))
               {
                   withinHtmlComment = true;
               }
               
               if(withinHtmlComment && candidate.endsWith("-->"))
               {
                   withinHtmlComment = false;
                   continue;
               }
               
               if(withinHtmlComment) continue;
               
               if(candidate.startsWith("<script") && candidate.endsWith("</script>"))
               {
                   ScriptSource script = new ScriptSource(candidate);
                   script.setPathPrefix(javascriptPathPrefix);
                   script.setOutputPrefix(outputPrefix);
                   script.setDefaultModuleName(javascriptDefaultModuleName);
                   script.setModuleOutputPath(this.javascriptModuleOutputPath);
                   script.process();
                  
                   if(script.getType() == ScriptSourceType.JAVASCRIPT)
                   {
                       if(script.getAttributes().contains(CompilerAttribute.MINIFY) || 
                          script.getAttributes().contains(CompilerAttribute.MODULE))
                       {
                           whiteSpace = line.substring(0, line.indexOf("<"));
                           
                           if(script.getAttributes().contains(CompilerAttribute.MINIFY))
                           {
                               String minifyName = script.getMinifyName();
                               
                               if(replacedScripts.contains(minifyName) == true)
                               {
                                   writeLine = false;
                               }
                               else
                               {
                                   replacedScripts.add(minifyName);
                                   line = whiteSpace + script.getMinifyElement() + lineEnding;
                               }
                           }
                           // MODULE
                           else
                           {
                               if(replacedScripts.contains(script.getModule()) == true)
                               {
                                   writeLine = false;
                               } 
                               else
                               {
                                   replacedScripts.add(script.getModule());
                                   line = whiteSpace + script.getModuleElement() + lineEnding;
                               }
                           }
                           
                           scripts.add(script);
                           
                           if(writeLine && shouldWriteFile)
                           {
                               bufferedOut.write(line);
                           }
                       }
                       else
                       {
                           // it's a <script> but not JavaScript
                           if(shouldWriteFile && writeLine)
                           {
                               bufferedOut.write(line);
                           }
                       }
                   }
                   else 
                   {
                       // any ol line in the file.
                       if(shouldWriteFile && writeLine)
                       {
                           bufferedOut.write(line);
                       }
                   }
               }
               // doesn't start with <script
               else
               {
                   if(shouldWriteFile)
                   {
                       bufferedOut.write(line);
                   } 
               }
            }
            
            if(shouldWriteFile)
            {
                bufferedOut.close();
            }
        } 
        catch (FileNotFoundException ex) 
        {
            //Logger.getLogger(HtmlSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex)
        {
            //Logger.getLogger(HtmlSource.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    public ArrayList<ScriptSource> getScripts()
    {
        return scripts;
    }
}
