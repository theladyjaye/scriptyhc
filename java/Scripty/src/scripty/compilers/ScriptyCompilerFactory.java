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

public class ScriptyCompilerFactory {
 
    public static IScriptyCompiler compilerFromString(String compiler)
    {
        if(compiler.equals("closure"))
        {
            return closureCompiler();
        }
        
        if(compiler.equals("yui"))
        {
            return yuiCompiler();
        }
        
        if(compiler.equals("uglify"))
        {
            return uglifyCompiler();
        }
        
        return closureCompiler();
    }
    
    public static IScriptyCompiler closureCompiler()
    {
        return new ClosureCompiler();
    }
    
    public static IScriptyCompiler yuiCompiler()
    {
        return new ClosureCompiler();
    }
    
    public static IScriptyCompiler uglifyCompiler()
    {
        return new ClosureCompiler();
    }
}
