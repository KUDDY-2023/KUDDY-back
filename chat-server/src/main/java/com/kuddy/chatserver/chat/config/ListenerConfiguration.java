	package com.kuddy.chatserver.chat.config;

	import java.util.HashMap;
	import java.util.Map;

	import org.apache.kafka.clients.consumer.ConsumerConfig;
	import org.apache.kafka.common.serialization.StringDeserializer;
	import org.springframework.beans.factory.annotation.Value;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.kafka.annotation.EnableKafka;
	import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
	import org.springframework.kafka.core.ConsumerFactory;
	import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

	import org.springframework.kafka.support.serializer.JsonDeserializer;

	import com.kuddy.common.chat.domain.Message;
	import lombok.RequiredArgsConstructor;

	@EnableKafka
	@Configuration
	@RequiredArgsConstructor
	public class ListenerConfiguration {
		@Value("${spring.kafka.aggregation.group}")
		private String group;

		@Value("${spring.kafka.aggregation.broker}")
		private String broker;

		// KafkaListener 컨테이너 팩토리를 생성하는 Bean 메서드
		@Bean
		ConcurrentKafkaListenerContainerFactory<String, Message> kafkaListenerContainerFactory() {
			ConcurrentKafkaListenerContainerFactory<String, Message> factory = new ConcurrentKafkaListenerContainerFactory<>();
			factory.setConsumerFactory(consumerFactory());
			return factory;
		}

		// Kafka ConsumerFactory를 생성하는 Bean 메서드
		@Bean
		public ConsumerFactory<String, Message> consumerFactory() {
			JsonDeserializer<Message> deserializer = new JsonDeserializer<>();
			// 패키지 신뢰 오류로 인해 모든 패키지를 신뢰하도록 작성
			deserializer.addTrustedPackages("*");

			// Kafka Consumer 구성을 위한 설정값들을 설정 -> HashMap을 이용하여 설정
			Map<String, Object> consumerConfigurations = new HashMap<>();
			consumerConfigurations.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
			consumerConfigurations.put(ConsumerConfig.GROUP_ID_CONFIG, group);
			consumerConfigurations.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
			consumerConfigurations.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
			consumerConfigurations.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

			return new DefaultKafkaConsumerFactory<>(consumerConfigurations, new StringDeserializer(), deserializer);
		}

	}
