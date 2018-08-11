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
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * The producer endpoint that writes to the queue.
 *
 */
public class QueueProducer extends EndPoint {

	public QueueProducer(String exchange) {
		super(exchange, null);
	}

	public QueueProducer(String exchange, String routingKey) {
		super(exchange, routingKey);
	}

	public void sendMessage(Serializable object) throws IOException {
		try {
			super.init();
			if (StringUtils.isNotEmpty(exchange)) {
				channel.queueDeclare(exchange, false, false, false, null);
			} else if (StringUtils.isNotEmpty(routingKey)) {
				channel.queueDeclare(routingKey, false, false, false, null);
			}
		} catch (TimeoutException ex) {
			throw new RuntimeException(ex);
		}
		try {
			channel.basicPublish(exchange, routingKey, null, SerializationUtils.serialize(object));
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void sendBytesMessage(byte[] bytes) throws IOException {
		try {
			super.init();
			if (StringUtils.isNotEmpty(exchange)) {
				channel.queueDeclare(exchange, false, false, false, null);
			} else if (StringUtils.isNotEmpty(routingKey)) {
				channel.queueDeclare(routingKey, false, false, false, null);
			}
		} catch (TimeoutException ex) {
			throw new RuntimeException(ex);
		}
		try {
			channel.basicPublish(exchange, routingKey, null, bytes);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
