/*
 * package br.com.cdb.MeuBancoDigitalCompleto.service;
 * 
 * public class JwtTokenProvider {
 * 
 * private String secretKey = "mySecretKey"; // Você pode externalizar isso em
 * um arquivo de propriedades
 * 
 * public String createToken(String username, String role) { return
 * Jwts.builder() .setSubject(username) .claim("role", role) .setIssuedAt(new
 * Date()) .setExpiration(new Date(System.currentTimeMillis() + 86400000)) //
 * Token expira após 1 dia .signWith(SignatureAlgorithm.HS512, secretKey)
 * .compact(); }
 * 
 * public String getUsernameFromToken(String token) { return Jwts.parser()
 * .setSigningKey(secretKey) .parseClaimsJws(token) .getBody() .getSubject(); }
 * 
 * public boolean validateToken(String token) { try {
 * Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token); return true; }
 * catch (Exception e) { return false; } } }
 */