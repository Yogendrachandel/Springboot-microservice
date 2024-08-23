package com.example.learn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;


//Here we are using Webflux security not web security -because spring gateway server built on top of reactive webflux
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {


    /*
     STEP1 ; In this method ,we just protected our endpoints(POST,PUT,DELETE) with access token
     */
    /*
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        //only GET endpoint is permit all but others need to authenticated,here we give first prefrence to GET endpoint
        serverHttpSecurity.authorizeExchange(exchanges -> exchanges.pathMatchers(HttpMethod.GET).permitAll()
                //all below endpoint with GET one permitALL but anothers endpoint like PUT,post delete will be authenticated.
                .pathMatchers("/accounts/**").authenticated()
                .pathMatchers("/cards/**").authenticated())
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        //default one
                        .jwt(Customizer.withDefaults()));

        //here we disable CSRF becuase we have not browser web based application -like UI
        serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());

        return serverHttpSecurity.build();
    }
    */


      /*
     STEP1 ; In this method ,we just protected our endpoints(POST,PUT,DELETE) with access token
     */
    /*
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        //only GET endpoint is permit all but others need to authenticated,here we give first prefrence to GET endpoint
        serverHttpSecurity.authorizeExchange(exchanges -> exchanges.pathMatchers(HttpMethod.GET).permitAll()
                //all below endpoint with GET one permitALL but anothers endpoint like PUT,post delete will be authenticated.
                .pathMatchers("/accounts/**").authenticated()
                .pathMatchers("/cards/**").authenticated())
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        //default one
                        .jwt(Customizer.withDefaults()));

        //here we disable CSRF becuase we have not browser web based application -like UI
        serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());

        return serverHttpSecurity.build();
    }*/



    /*
    STEP2:1)In this method ,we just protected our endpoints(POST,PUT,DELETE) with access token plus RoleBased
    for this we need to create the Roles in KeyCloak for the client-ID (bank-call-center-cc) for which we are generating
    access-token .
    2) we need to fetch the roles from the ROLES array(realm_access.roles) - we can pass the access-token in JWT decode site and we
    can see multiple roles within array,so we need to extract ROLE here also.


     */

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        //only GET endpoint is permit all but others need to authenticated,here we give first prefrence to GET endpoint
        serverHttpSecurity.authorizeExchange(exchanges -> exchanges.pathMatchers(HttpMethod.GET).permitAll()
                        //all below endpoint with GET one permitALL but anothers endpoint like PUT,post delete will be authenticated.
                        .pathMatchers("/accounts/**").hasRole("ACCOUNTS") //This ACCOUNTS and CARDS role is created inside the KeyCloack and assigned to the client.
                        .pathMatchers("/cards/**").hasRole("CARDS"))
                       .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                    .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));

        //here we disable CSRF becuase we have not browser web based application -like UI
        serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());
        return serverHttpSecurity.build();
    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter =
                new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter
                (new KeycloakRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

}
