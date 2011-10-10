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
import java.io.PrintStream;
import com.google.javascript.jscomp.CommandLineRunner;
import com.google.javascript.jscomp.CompilerOptions;
/**
 * The docs said we should subclass CommandLineRunner and
 * not use it directly. Done!
 * @author dev
 */
public class ClosureRunner extends CommandLineRunner
{
    public ClosureRunner(String[] args) 
    {
        super(args);
    }
    
    public ClosureRunner(String[] args, PrintStream out, PrintStream err) 
    {
        super(args, out, err);
    }
    
    @Override
    protected CompilerOptions createOptions() 
    {
      CompilerOptions options = super.createOptions();
      options.setManageClosureDependencies(true);
      return options;
    }
}
