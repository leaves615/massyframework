/**
* @Copyright: 2017 smarabbit studio. All rights reserved.
*  
* @作   者： 黄开晖<kaimohkh@gmail.com> 
* @日   期:  2017年4月11日
*
* 注意：本内容仅限学习和传阅，禁止用于其他的商业目的
*/
package org.massyframework.assembly.base.support;

import java.util.Comparator;

/**
 * 字符串比较
 * @author huangkaihui
 *
 */
public class StringComparator implements Comparator<String> {

	public static final StringComparator COMPARATOR = new StringComparator();

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(String s1, String s2) {
		int n1 = s1.length();
        int n2 = s2.length();
        int min = n1 < n2 ? n1 : n2;
        for ( int i = 0; i < min; i++ )
        {
            char c1 = s1.charAt( i );
            char c2 = s2.charAt( i );
            if ( c1 != c2 )
            {
                // Fast check for simple ascii codes
                if ( c1 <= 128 && c2 <= 128 )
                {
                    c1 = toLowerCaseFast(c1);
                    c2 = toLowerCaseFast(c2);
                    if ( c1 != c2 )
                    {
                        return c1 - c2;
                    }
                }
                else
                {
                    c1 = Character.toUpperCase( c1 );
                    c2 = Character.toUpperCase( c2 );
                    if ( c1 != c2 )
                    {
                        c1 = Character.toLowerCase( c1 );
                        c2 = Character.toLowerCase( c2 );
                        if ( c1 != c2 )
                        {
                            // No overflow because of numeric promotion
                            return c1 - c2;
                        }
                    }
                }
            }
        }
        return n1 - n2;
	}
	
	/**
	 * 快速小写转换
	 * @param ch
	 * @return
	 */
	private static char toLowerCaseFast( char ch ){
        return ( ch >= 'A' && ch <= 'Z' ) ? ( char ) ( ch + 'a' - 'A' ) : ch;
    }
}
