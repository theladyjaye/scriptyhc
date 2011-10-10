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
import scripty.sources.ScriptSource;
import scripty.compilers.enums.CompilerAttribute;


public interface IScriptyCompiler 
{
    public void setAction(CompilerAttribute action);
    public void setModuleName(String name);
    public void setModuleOutputPath(String value);
    public void setOutputPrefix(String value);
    public void addScript(ScriptSource script);
    public void addScript(List<ScriptSource> script);
    public void execute() throws IllegalStateException;
    
}
