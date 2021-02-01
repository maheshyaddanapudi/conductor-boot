package com.github.maheshyaddanapudi.netflix.conductorboot.service.embedded.elastic;

import com.github.maheshyaddanapudi.netflix.conductorboot.lib.embedded.elastic.EmbeddedElastic;
import org.springframework.stereotype.Service;

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
