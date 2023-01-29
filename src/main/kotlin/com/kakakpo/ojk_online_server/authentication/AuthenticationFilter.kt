package com.kakakpo.ojk_online_server.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import com.kakakpo.ojk_online_server.base.entity.BaseResponse
import com.kakakpo.ojk_online_server.base.entity.Empty
import com.kakakpo.ojk_online_server.exception.OjkException
import com.kakakpo.ojk_online_server.user.service.UserService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.UnsupportedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.ErrorResponse
import org.springframework.web.filter.OncePerRequestFilter
import java.util.stream.Collectors

@Component
class AuthenticationFilter : OncePerRequestFilter() {

    @Autowired
    private lateinit var userService: UserService

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            if (JWTConfig.isPermit(request)) {
                filterChain.doFilter(request, response)
            } else {
                val claim = validate(request)
                if (claim[AuthenticationConstant.CLAIMS] != null) {
                    setupAuthentication(claim){
                        filterChain.doFilter(request, response)
                    }
                } else {
                    SecurityContextHolder.clearContext()
                    throw OjkException("token required")
                }
            }
        } catch (e: Exception) {
            val errorResponse = BaseResponse<Empty>()
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = TYPE_JSON
            when (e) {
                is UnsupportedJwtException -> {
                    errorResponse.message = "error unsupported"
                    sendErrorResponse(errorResponse, response)
                }

                else -> {
                    errorResponse.message = e.message ?: "Token invalid"
                    sendErrorResponse(errorResponse, response)
                }
            }
        }
    }

    private fun validate(request: HttpServletRequest): Claims {
        val jwtToken = request.getHeader(AUTHORIZATION)
        return Jwts.parserBuilder()
                .setSigningKey(AuthenticationConstant.SECRET.toByteArray())
                .build()
                .parseClaimsJws(jwtToken)
                .body
    }

    private fun setupAuthentication(claims: Claims, doOnNext: () -> Unit) {
        val authorities = claims[AuthenticationConstant.CLAIMS] as List<String>
        val authStream = authorities.stream().map { SimpleGrantedAuthority(it) }.collect(Collectors.toList())

        val auth = UsernamePasswordAuthenticationToken(claims.subject, null, authStream)
        SecurityContextHolder.getContext().authentication = auth
        doOnNext.invoke()
    }

    private fun sendErrorResponse(errorResponse: BaseResponse<Empty>, response: HttpServletResponse){
        val responseString = ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(errorResponse)
        response.writer.println(responseString)
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val TYPE_JSON = "application/json"
    }
}