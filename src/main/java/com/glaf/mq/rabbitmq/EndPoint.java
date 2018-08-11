/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glaf.mq.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rabbitmq.client.Channel;

/**
 * Represents a connection with a queue
 *
 */
public abstract class EndPoint extends Thread {
	protected static final Log LOG = LogFactory.getLog(EndPoint.class);
	protected Channel channel;
	protected String host;
	protected int port;
	protected String username;
	protected String password;
	protected String exchange;
	protected String routingKey;
	private boolean _closed;

	public EndPoint() {
		_closed = false;
	}

	public EndPoint(String exchange, String routingKey) {
		this.exchange = exchange;
		this.routingKey = routingKey;
		_closed = false;
	}

	public EndPoint(String host, int port, String username, String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		_closed = false;
	}

	/**
	 * 关闭channel 。并非必须，因为隐含是自动调用的。
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		try {
			this.channel.close();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		_closed = true;
	}

	public void closeConnection() throws IOException {
		try {
			this.channel.close();
		} catch (Exception e) {
		}
		try {
			this.channel.getConnection().close();
		} catch (Exception e) {
		}
		_closed = true;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (!_closed) {
			close();
		}
	}

	public Channel getChannel() {
		return channel;
	}

	public String getHost() {
		return host;
	}

	public String getPassword() {
		return password;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public void init() throws IOException, TimeoutException {
		channel = RabbitmqConnectionFactory.getInstance().getChannel(host, port, username, password);
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
