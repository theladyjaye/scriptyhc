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
package scripty;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;
import scripty.sources.ScriptyRepository;
import scripty.cli.ScriptyArguments;
/**
 *
 * @author dev
 */
public class Scripty 
{    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        ScriptyArguments scriptyArgs = new ScriptyArguments();
        CmdLineParser argParser = new CmdLineParser(scriptyArgs);
        
        try 
        {
            argParser.parseArgument(args);
        } 
        catch (CmdLineException e) 
        {
            System.err.println(e.getMessage());
            argParser.printUsage(System.err);
        }
        
        if(scriptyArgs != null)
        {
            ScriptyRepository repository = new ScriptyRepository(scriptyArgs);
            
            repository.process();
            //repository.receipt();
            
        }
        else
        {
            argParser.printUsage(System.err);
        }
        
        
    }
}
