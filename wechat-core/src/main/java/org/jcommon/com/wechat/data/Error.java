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
package org.jcommon.com.wechat.data;

import org.jcommon.com.wechat.utils.ErrorType;

public class Error extends JsonObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errmsg;
	private int errcode;
	private ErrorType type;

	public Error(String json) {
		super(json);
		setType(ErrorType.getError(this.errcode));
	}

	public Error() {
		super();
	}

	public Error(ErrorType type) {
		super();
		this.type = type;
		if (type != null) {
			errmsg = type.message;
			errcode = type.error;
		}
	}

	public Error(String message, int errcode) {
		super();
		this.errmsg = message;
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return this.errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public int getErrcode() {
		return this.errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public ErrorType getType() {
		return this.type;
	}

	public void setType(ErrorType type) {
		this.type = type;
	}
}