package security.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.UUID;

/**
 * project name: domain-driven-transaction-sys
 *
 * description:
 *
 * @author magicliang
 *
 *         date: 2025-07-03 15:26
 */
public class JwtUtil {

    // 定义密钥 (在真实项目中，应从配置文件中读取，且更为复杂)
    private static final String SECRET_KEY = "your-super-secret-key-that-is-long-and-secure";

    // 定义签发者

    /*
     * Issuer和Subject的选择原则：
     *
     * Issuer：应该使用能唯一标识你的认证服务的字符串，通常可以是：
     *   - 你的应用域名（如auth.yourdomain.com）
     *   - 服务名称（如当前代码中的MyAppAuthService）
     *   - 在微服务架构中，建议使用服务注册中心的名称
     *
     * Subject：应该使用能唯一标识用户的字符串，通常可以是：
     *   - 用户ID（如当前代码中的userId）
     *   - 用户邮箱/用户名（如果它们是唯一的）
     *   - 不建议使用可变的用户信息作为Subject
     */
    private static final String ISSUER = "MyAppAuthService";

    // 定义过期时间 (例如：1小时)
    private static final long EXPIRATION_TIME_MS = 3600 * 1000;

    /**
     * 创建一个JWT.
     *
     * @param userId 用户的唯一标识
     * @param role 用户的角色
     * @return 生成的JWT字符串
     */
    public static String createToken(String userId, String role) {
        try {
            // 1. 选择签名算法
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            // 2. 生成JWT
            return JWT.create()
                    .withIssuer(ISSUER) // 签发者
                    .withSubject(userId) // 主题，通常是用户ID
                    /*
                     * 不要将所有状态放入JWT：JWT应该保持精简，只包含认证和授权必需的信息
                     * 推荐做法：
                     * public static String createToken(String userId, String role, Map<String, Object>
                     * additionalClaims) {    // ...其他代码不变...    JWT.create()        .withIssuer(ISSUER)
                     * .withSubject(userId)        .withClaim("role", role)        // 添加额外声明        .withClaim
                     * ("userData", additionalClaims)        // ...其他代码...}
                     * 替代方案：
                     *   - 只在JWT中存储引用ID（如购物车ID），然后在服务端存储完整数据
                     *   - 使用短期有效的JWT配合服务端会话存储
                     *   - 对于频繁变更的数据（如购物车），建议使用数据库存储
                     */
                    .withClaim("role", role) // 自定义声明 (payload)
                    .withIssuedAt(new Date()) // 签发时间
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS)) // 过期时间
                    .withJWTId(UUID.randomUUID().toString()) // JWT的唯一身份标识
                    /*
                     * - JWT大小限制：HTTP头部通常有大小限制（如8KB）
                     * - 安全性：敏感信息不应放在JWT中，因为JWT可以被解码（只是不能篡改）
                     * - 性能：每次请求都会携带JWT，过大的JWT会影响性能
                     *
                     * - JWT的设计初衷：
                     *      - JWT确实是为了实现无状态认证而设计的，但这里的"状态"主要指会话状态（如用户身份、权限等）
                     *      - 它解决了传统session需要服务端存储会话数据的问题，使得服务端只需验证JWT签名即可确认用户身份
                     *  - 实际应用中的权衡：
                     *      - 技术限制：
                     *          - HTTP头部大小限制（通常8KB）
                     *          - JWT一旦签发就无法修改（除非重新生成）
                     *      - 业务需求：
                     *          - 购物车、地址等数据频繁变化
                     *          - 这些数据通常需要与其他系统共享（如支付系统、物流系统）
                     */
                    .sign(algorithm); // 使用算法进行签名
        } catch (Exception e) {
            // 处理异常，例如记录日志
            System.err.println("Error creating JWT: " + e.getMessage());
            return null;
        }
    }

    /**
     * 验证一个JWT并解码.
     *
     * @param token JWT字符串
     * @return 解码后的JWT对象，如果验证失败则返回null
     */
    public static DecodedJWT verifyToken(String token) {
        try {
            // 1. 选择与创建时相同的签名算法
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            // 2. 构建验证器
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER) // 验证签发者
                    .build(); // 创建验证器实例

            // 3. 执行验证
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            // 验证失败 (例如：签名不匹配, token过期等)
            System.err.println("JWT Verification failed: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        // --- 模拟登录成功，生成Token ---
        String userId = "user123";
        String userRole = "admin";
        System.out.println("--- Generating Token ---");
        String token = createToken(userId, userRole);
        System.out.println("Generated Token: " + token);
        System.out.println();

        // --- 模拟后续请求，验证Token ---
        System.out.println("--- Verifying Token ---");
        if (token != null) {
            DecodedJWT decodedJWT = verifyToken(token);

            if (decodedJWT != null) {
                System.out.println("Verification Successful!");
                String verifiedUserId = decodedJWT.getSubject();
                String verifiedRole = decodedJWT.getClaim("role").asString();
                Date expiresAt = decodedJWT.getExpiresAt();

                System.out.println("User ID: " + verifiedUserId);
                System.out.println("User Role: " + verifiedRole);
                System.out.println("Expires At: " + expiresAt);
            } else {
                System.out.println("Verification Failed!");
            }
        }

        System.out.println("\n--- Verifying a Tampered Token ---");
        // 模拟一个被篡改的token (在payload部分添加了额外字符)
        String tamperedToken =
                token.substring(0, token.indexOf('.') + 10) + "tamper" + token.substring(token.indexOf('.') + 10);
        System.out.println("Tampered Token: " + tamperedToken);
        verifyToken(tamperedToken); // 这将会验证失败
    }
}
