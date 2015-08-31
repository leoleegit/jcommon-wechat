// ========================================================================
// Copyright 2012 leolee<workspaceleo@gmail.com>
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//     http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ========================================================================
package org.jcommon.com.wechat.utils;

import java.security.NoSuchAlgorithmException;

public class MD5 {
	 public static String getMD5(byte[] source) throws NoSuchAlgorithmException {
		 String s = null;
		 char hexDigits[] = {     
				 '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',  'e', 'f'}; 
		 java.security.MessageDigest md = java.security.MessageDigest.getInstance( "MD5" );
		 md.update( source );
		 byte tmp[] = md.digest();                                 
		 char str[] = new char[16 * 2];   
		 int k = 0;                               
		 for (int i = 0; i < 16; i++) {                                  
			byte byte0 = tmp[i];                
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];                                       
			str[k++] = hexDigits[byte0 & 0xf];            
		 } 
		 s = new String(str);                               
		 return s;
	 }
}