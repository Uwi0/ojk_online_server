package com.kakakpo.ojk_online_server.authentication

import com.kakakpo.ojk_online_server.user.entity.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Encoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.beans.Encoder
import java.util.*
import java.util.stream.Collectors

@EnableMethodSecurity
@Configuration
class JWTConfig {

    @Autowired
    private lateinit var authenticationFilter: AuthenticationFilter

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()
                .addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
                .authorizeHttpRequests()
                .requestMatchers(*requestPermit.toTypedArray())
                .permitAll()
                .anyRequest()
                .authenticated()

        return http.build()
    }

    companion object {
        val requestPermit = listOf("/api/ping", "/api/user/login", "/api/user/register")

        fun generateToken(user: User): String {
            val subject = user.id
            val expired = Date(System.currentTimeMillis() + (60_000 * 60 * 24))
            val granted = AuthorityUtils.commaSeparatedStringToAuthorityList(user.username)
            val grantedStream = granted.stream().map { it.authority }.collect(Collectors.toList())
            return Jwts.builder()
                    .setSubject(subject)
                    .claim(AuthenticationConstant.CLAIMS, grantedStream)
                    .setExpiration(expired)
                    .signWith(Keys.hmacShaKeyFor(AuthenticationConstant.SECRET.toByteArray()), SignatureAlgorithm.HS256)
                    .compact()
        }

        fun isPermit(request: HttpServletRequest): Boolean{
            val path = request.servletPath
            return requestPermit.contains(path)
        }
    }


}
