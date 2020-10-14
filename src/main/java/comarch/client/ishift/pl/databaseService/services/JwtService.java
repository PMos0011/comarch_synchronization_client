package comarch.client.ishift.pl.databaseService.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {

    static String getDbIDFromToken(String token) {
        token = token.replace("IShiftToken ","");

        int i = token.lastIndexOf('.');
        String withoutSignature = token.substring(0, i+1);
        Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);

        Claims body = untrusted.getBody();
        return (String) body.get("access");
    };
}
