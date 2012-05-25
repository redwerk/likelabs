package com.redwerk.likelabs.infrastructure.sn;

import com.redwerk.likelabs.domain.service.sn.GatewayFactory;
import com.redwerk.likelabs.domain.service.sn.SocialNetworkGateway;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GatewayFactoryImpl implements GatewayFactory {

    @Autowired
    FacebookGateway facebookGateway;

    @Autowired
    VKontakteGateway vKontakteGateway;

    @Override
    public SocialNetworkGateway getGateway(SocialNetworkType type) {
        switch (type){
            case FACEBOOK:  return facebookGateway;
            case VKONTAKTE: return vKontakteGateway;
            default: throw new IllegalArgumentException("unknown social network type");
        }
    }

}
