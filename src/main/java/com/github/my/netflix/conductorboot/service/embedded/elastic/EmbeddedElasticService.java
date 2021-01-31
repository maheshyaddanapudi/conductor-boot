package com.github.my.netflix.conductorboot.service.embedded.elastic;

import org.springframework.stereotype.Service;

import com.github.my.netflix.conductorboot.lib.embedded.elastic.EmbeddedElastic;

@Service
public class EmbeddedElasticService {

	private EmbeddedElastic embeddedElastic;

	public EmbeddedElastic getEmbeddedElastic() {
		return embeddedElastic;
	}

	public void setEmbeddedElastic(EmbeddedElastic embeddedElastic) {
		this.embeddedElastic = embeddedElastic;
	}
	
}
