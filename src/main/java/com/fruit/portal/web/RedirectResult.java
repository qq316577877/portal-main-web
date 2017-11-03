/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.web;

import com.ovfintech.arch.web.mvc.dispatch.UriMeta;
import com.ovfintech.arch.web.mvc.dispatch.result.DispatchResult;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
public class RedirectResult implements DispatchResult
{
	
	/**
	 * @param location
	 */
	public RedirectResult(String location) {
		super();
		this.location = location;
	}

	private String location;

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public void handle(UriMeta uriMeta,
			Object rawResult) throws IOException {
		HttpServletResponse resp = WebContext.getResponse();
		resp.sendRedirect(this.location);
	}


}
