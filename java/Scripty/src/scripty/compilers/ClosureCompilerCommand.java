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

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

public class ClosureCompilerCommand implements IScriptyCompilerCommand
{
    private String compiler = "compiler.jar";
    private String compilerBasePath = "";
    
    private List<String> arguments;
    
    public ClosureCompilerCommand()
    {
        arguments = new ArrayList<String>();
    }
    
    @Override
    public void setCompilerBasePath(String value)
    {
       compilerBasePath = value; 
    }
    
    @Override
    public void setArguments(List<String> value) 
    {
        arguments = value;
    }

    @Override
    public void execute() 
    {
        try 
        {
            //System.out.println(getCommandWithArguments());
            Runtime.getRuntime().exec(getCommandWithArguments());
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ClosureCompilerCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        /*
        String[] compilerArgs = arguments.toArray(new String[arguments.size()]);    
        ClosureRunner runner = new ClosureRunner(compilerArgs, System.out, System.err);
            
        if (runner.shouldRunCompiler()) 
        {
            runner.run();
        }*/
    }
    
    private String getCommandWithArguments()
    {
        
        return "java -jar " + compilerBasePath + "/" + compiler +  " " + StringUtils.join(arguments, " ");
    }
    
}
