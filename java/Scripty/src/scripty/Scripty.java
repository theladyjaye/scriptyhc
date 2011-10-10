/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
