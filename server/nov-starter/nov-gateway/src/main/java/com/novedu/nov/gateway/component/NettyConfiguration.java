package com.novedu.nov.gateway.component;

import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class NettyConfiguration implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

    @Override
    public void customize(NettyReactiveWebServerFactory container) {
        container.addServerCustomizers(
                httpServer -> httpServer.httpRequestDecoder(
                        httpRequestDecoderSpec -> httpRequestDecoderSpec.maxHeaderSize(1000485760)
                )
        );
    }

}
