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
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.domain.ServerEntity;
import com.glaf.core.security.SecurityUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitmqConnectionFactory {
	private static class MQConnectionHolder {
		public static RabbitmqConnectionFactory instance = new RabbitmqConnectionFactory();
	}

	protected static final Log LOG = LogFactory.getLog(RabbitmqConnectionFactory.class);

	protected static ConcurrentMap<String, Connection> connectionMap = new ConcurrentHashMap<String, Connection>();

	/**
	 * 构建锁
	 */
	private static final Lock LOCK = new ReentrantLock();

	private static final int MAX_RETRIES = 10;

	public static RabbitmqConnectionFactory getInstance() {
		return MQConnectionHolder.instance;
	}

	private RabbitmqConnectionFactory() {

	}

	/**
	 * 关闭channel。并非必须，因为隐含是自动调用的。
	 * 
	 * @throws IOException
	 */
	public void close(Channel channel) {
		if (channel != null) {
			try {
				channel.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取Channel
	 * 
	 * @param server
	 *            MQ主机配置信息
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public Channel getChannel(ServerEntity server) throws IOException, TimeoutException {
		Channel channel = null;
		String host = server.getHost();
		int port = server.getPort();
		String username = server.getUser();
		String password = server.getPassword();
		try {
			if (StringUtils.isNotEmpty(server.getKey())) {
				password = SecurityUtils.decode(server.getKey(), server.getPassword());
			}
		} catch (Exception ex) {
		}
		Connection connection = this.getConnection(host, port, username, password);
		if (connection != null && connection.isOpen()) {
			channel = connection.createChannel();
		}
		return channel;
	}

	/**
	 * 获取Channel
	 * 
	 * @param host
	 *            MQ主机
	 * @param port
	 *            MQ端口
	 * @param username
	 *            MQ用户名
	 * @param password
	 *            MQ密码
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public Channel getChannel(String host, int port, String username, String password)
			throws IOException, TimeoutException {
		Channel channel = null;
		Connection connection = this.getConnection(host, port, username, password);
		if (connection != null && connection.isOpen()) {
			channel = connection.createChannel();
		}
		return channel;
	}

	/**
	 * 获取Channel
	 * 
	 * @param server
	 *            MQ主机配置信息
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public Connection getConnection(ServerEntity server) throws IOException, TimeoutException {
		String host = server.getHost();
		int port = server.getPort();
		String username = server.getUser();
		String password = server.getPassword();
		try {
			if (StringUtils.isNotEmpty(server.getKey())) {
				password = SecurityUtils.decode(server.getKey(), server.getPassword());
			}
		} catch (Exception ex) {
		}
		Connection connection = this.getConnection(host, port, username, password);
		return connection;
	}

	/**
	 * 获取Connection
	 * 
	 * @param host
	 *            MQ主机
	 * @param port
	 *            MQ端口
	 * @param username
	 *            MQ用户名
	 * @param password
	 *            MQ密码
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public Connection getConnection(String host, int port, String username, String password)
			throws IOException, TimeoutException {
		String cacheKey = host + "_" + port;

		if (username != null && password != null) {
			cacheKey = cacheKey + "_" + username;
		}
		Connection connection = connectionMap.get(cacheKey);
		if (connection != null && connection.isOpen()) {
			LOG.debug("get rabbitmq connection from cache...");
			return connection;
		} else {
			int retries = 0;
			Channel channel = null;
			while (true) {
				try {
					boolean lockSuccess = LOCK.tryLock(20, TimeUnit.MILLISECONDS);
					if (lockSuccess) {
						// Create a connection factory
						ConnectionFactory factory = new ConnectionFactory();
						// hostname of your rabbitmq server
						factory.setHost(host != null ? host : "localhost");
						if (port > 0) {
							factory.setPort(port);
						}
						if (username != null && password != null) {
							factory.setUsername(username);
							factory.setPassword(password);
						}

						factory.setAutomaticRecoveryEnabled(true);
						factory.setNetworkRecoveryInterval(10000);

						LOG.debug("rabbitmq host:" + host);
						LOG.debug("rabbitmq port:" + port);
						LOG.debug("rabbitmq user:" + username);
						LOG.debug("rabbitmq create connection...");
						// getting a connection
						connection = factory.newConnection();
						// creating a channel
						channel = connection.createChannel();
						if (channel != null) {
							connectionMap.put(cacheKey, connection);
							LOG.debug("put rabbitmq connection into cache.");
							return connection;
						}
					}
					return connection;
				} catch (java.lang.Throwable ex) {
					if (retries++ == MAX_RETRIES) {
						throw new RuntimeException(ex);
					}
					try {
						TimeUnit.MILLISECONDS.sleep(200 + new Random().nextInt(1000));// 活锁
					} catch (InterruptedException e) {
					}
				} finally {
					this.close(channel);
					LOCK.unlock();
				}
			}
		}
	}

}
