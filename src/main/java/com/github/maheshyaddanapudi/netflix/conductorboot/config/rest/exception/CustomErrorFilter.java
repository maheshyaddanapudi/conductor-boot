package com.github.maheshyaddanapudi.netflix.conductorboot.config.rest.exception;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomErrorFilter extends ZuulFilter {

        private static final Logger LOG = LoggerFactory.getLogger(CustomErrorFilter.class);

        private static final String FILTER_TYPE = "error";
        private static final String THROWABLE_KEY = "throwable";
        private static final int FILTER_ORDER = -1;

        @Override
        public String filterType() {
            return FILTER_TYPE;
        }

        @Override
        public int filterOrder() {
            return FILTER_ORDER;
        }

        @Override
        public boolean shouldFilter() {
            return true;
        }

        @Override
        public Object run() {
            final RequestContext context = RequestContext.getCurrentContext();
            final Object throwable = context.get(THROWABLE_KEY);

            if (throwable instanceof ZuulException) {
                final ZuulException zuulException = (ZuulException) throwable;
                LOG.debug("Zuul failure detected - "+context.getRequest().getServletPath()+": " + zuulException.getMessage());

                if(context.getRequest().getServletPath().equalsIgnoreCase("/api/health"))
                {
                    // remove error code to prevent further error handling in follow up filters
                    context.remove(THROWABLE_KEY);

                    context.setResponseStatusCode(404);
                }

            }
            return null;
        }
    }