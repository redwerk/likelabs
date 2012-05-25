package com.redwerk.likelabs.domain.service.sn;

import com.redwerk.likelabs.domain.model.SocialNetworkType;


public interface GatewayFactory {

    SocialNetworkGateway getGateway(SocialNetworkType type);

}
