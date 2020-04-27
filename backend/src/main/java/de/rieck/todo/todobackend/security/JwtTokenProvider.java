package de.rieck.todo.todobackend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Payload;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.style.ToStringCreator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Collections;

@Component
public class JwtTokenProvider {

    public Authentication getValidatedAuthentication(String token) {
        String aws_cognito_region = "eu-central-1"; // Replace this with your aws cognito region
        String aws_user_pools_id = "eu-central-1_wr3AourNH"; // Replace this with your aws user pools id
        RSAKeyProvider keyProvider = new AwsCognitoRSAKeyProvider(aws_cognito_region, aws_user_pools_id);
        Algorithm algorithm = Algorithm.RSA256(keyProvider);
        JWTVerifier jwtVerifier = JWT.require(algorithm)
                .build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);

        JWTParser parser=new JWTParser();

        Payload payload=parser.parsePayload(new String(Base64.getDecoder().decode(decodedJWT.getPayload())));

        System.out.println("Authenticated : "+decodedJWT.getPayload());

        String username = payload.getSubject();
        UserDetails userDetails = new User(username, "", Collections.emptyList());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveTokenFromRequest(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
