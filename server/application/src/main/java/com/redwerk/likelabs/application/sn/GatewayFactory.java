package com.redwerk.likelabs.application.sn;

import com.redwerk.likelabs.domain.model.SocialNetworkType;


public interface GatewayFactory {

    SocialNetworkGateway getGateway(SocialNetworkType type);

}
