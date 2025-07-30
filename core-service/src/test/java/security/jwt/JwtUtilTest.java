package security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

public class JwtUtilTest {

    @Test
    public void testCreateTokenWithValidInput() {
        String userId = "user123";
        String role = "admin";
        String token = JwtUtil.createToken(userId, role);
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    public void testVerifyTokenWithValidToken() {
        String userId = "user123";
        String role = "admin";
        String token = JwtUtil.createToken(userId, role);
        DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
        assertNotNull(decodedJWT);
        assertEquals(userId, decodedJWT.getSubject());
        assertEquals(role, decodedJWT.getClaim("role").asString());
    }

    @Test
    public void testVerifyTokenWithInvalidToken() {
        String invalidToken = "invalid.token.here";
        DecodedJWT decodedJWT = JwtUtil.verifyToken(invalidToken);
        assertNull(decodedJWT);
    }

    @Test
    public void testVerifyTokenWithTamperedToken() {
        String userId = "user123";
        String role = "admin";
        String token = JwtUtil.createToken(userId, role);
        String tamperedToken =
                token.substring(0, token.indexOf('.') + 1) + "tampered" + token.substring(token.indexOf('.') + 1);
        DecodedJWT decodedJWT = JwtUtil.verifyToken(tamperedToken);
        assertNull(decodedJWT);
    }

    @Test
    public void testVerifyTokenWithExpiredToken() {
        // 模拟一个过期的Token
        String userId = "user123";
        String role = "admin";
        String token = JwtUtil.createToken(userId, role);
        // 模拟Token过期
        try {
            Thread.sleep(2 * 1000); // 等待Token过期
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
        assertNull(decodedJWT);
    }
}