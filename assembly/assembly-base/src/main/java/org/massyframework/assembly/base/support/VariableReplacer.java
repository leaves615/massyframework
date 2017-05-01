/**
* @Copyright: 2017 smarabbit studio. 
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
*   
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月23日
*/
package org.massyframework.assembly.base.support;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.massyframework.assembly.util.Asserts;

/**
 * 变量替换器
 */
public class VariableReplacer {
	
	private Pattern pattern;
	private Map<String, String> variables;

	/**
	 * 
	 */
	public VariableReplacer(Map<String, String> variables) {
		Asserts.notNull(variables, "variables cannot be null.");
		
		StringBuilder builder = new StringBuilder();
		builder.append("\\$\\{(");
		int size = variables.size();
		Iterator<String> it = variables.keySet().iterator();
		int count = 0;
		while (it.hasNext()){
			String key = it.next();
			count ++ ;
			builder.append(key);
			if (count != size -1){
				builder.append("|");
			}
		}
		builder.append(")\\}");
		
		String text = "\\$\\{(" +
	    		builder.toString() + ")\\}";
		this.pattern = Pattern.compile(text);
		this.variables = variables;	
	}
	
	public String replace(String text){
		if (text == null) return null;
		
		Matcher matcher = pattern.matcher(text);
	    StringBuffer sb = new StringBuffer();
	    while(matcher.find()) {
	        matcher.appendReplacement(sb, variables.get(matcher.group(1)));
	    }
	    matcher.appendTail(sb);
	    
	    return sb.toString();
	}

}
