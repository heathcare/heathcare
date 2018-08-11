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
import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;

/**
 * The producer endpoint that writes to the queue.
 *
 */
public class PublishProducer extends EndPoint {

	public PublishProducer(String exchange) {
		super(exchange, null);
	}

	public PublishProducer(String exchange, String routingKey) {
		super(exchange, routingKey);
	}

	public void sendBytesMessage(String exchangeType, boolean mandatory, byte[] bytes) throws IOException {
		try {
			super.init();
			channel.exchangeDeclare(exchange, exchangeType);
		} catch (Throwable ex) {
			channel.exchangeDeclare(exchange, exchangeType, true);
		}
		try {
			switch (exchangeType) {
			case "fanout":
				channel.basicPublish(exchange, "", mandatory, null, bytes);
				break;
			case "topic":
				channel.basicPublish(exchange, routingKey, mandatory, null, bytes);
				break;
			default:
				channel.basicPublish("", routingKey, mandatory, null, bytes);
				break;
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void sendBytesMessage(String exchangeType, byte[] bytes) throws IOException {
		try {
			super.init();
			LOG.debug("exchange:" + exchange);
			channel.exchangeDeclare(exchange, exchangeType);
		} catch (Throwable ex) {
			channel.exchangeDeclare(exchange, exchangeType, true);
		}
		try {
			switch (exchangeType) {
			case "fanout":
				channel.basicPublish(exchange, routingKey, null, bytes);
				break;
			case "topic":
				channel.basicPublish(exchange, routingKey, null, bytes);
				break;
			default:
				channel.basicPublish("", routingKey, null, bytes);
				break;
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 发送消息
	 * 
	 * @param exchangeType
	 *            取值：direct、fanout和topic三种中的一种
	 * @param mandatory
	 *            受托管理的
	 * @param object
	 * @throws IOException
	 */
	public void sendMessage(String exchangeType, boolean mandatory, Serializable object) throws IOException {
		if (object instanceof String) {
			String str = (String) object;
			byte[] data = str.getBytes("UTF-8");
			this.sendBytesMessage(exchangeType, mandatory, data);
		} else if (object instanceof byte[]) {
			byte[] data = (byte[]) object;
			this.sendBytesMessage(exchangeType, mandatory, data);
		} else {
			try {
				super.init();
				channel.exchangeDeclare(exchange, exchangeType);
			} catch (Throwable ex) {
				channel.exchangeDeclare(exchange, exchangeType, true);
			}
			try {
				switch (exchangeType) {
				case "fanout":
					channel.basicPublish(exchange, "", mandatory, null, SerializationUtils.serialize(object));
					break;
				case "topic":
					channel.basicPublish(exchange, routingKey, mandatory, null, SerializationUtils.serialize(object));
					break;
				default:
					channel.basicPublish("", routingKey, mandatory, null, SerializationUtils.serialize(object));
					break;
				}
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	/**
	 * 发送消息
	 * 
	 * @param exchangeType
	 *            取值：direct、fanout和topic三种中的一种
	 * @param object
	 * @throws IOException
	 */
	public void sendMessage(String exchangeType, Serializable object) throws IOException {
		if (object instanceof String) {
			String str = (String) object;
			byte[] data = str.getBytes("UTF-8");
			this.sendBytesMessage(exchangeType, data);
		} else if (object instanceof byte[]) {
			byte[] data = (byte[]) object;
			this.sendBytesMessage(exchangeType, data);
		} else {
			try {
				super.init();
				channel.exchangeDeclare(exchange, exchangeType);
			} catch (Throwable ex) {
				channel.exchangeDeclare(exchange, exchangeType, true);
			}
			try {
				switch (exchangeType) {
				case "fanout":
					channel.basicPublish(exchange, "", null, SerializationUtils.serialize(object));
					break;
				case "topic":
					channel.basicPublish(exchange, routingKey, null, SerializationUtils.serialize(object));
					break;
				default:
					channel.basicPublish("", routingKey, null, SerializationUtils.serialize(object));
					break;
				}
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	}
}
