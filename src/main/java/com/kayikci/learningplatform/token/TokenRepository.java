package com.kayikci.learningplatform.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

  @Query(value = """
    select t from Token t 
    inner join t.user u
    where u.id = :id and (t.expired = false or t.revoked = false)
    """)
  List<Token> findAllValidTokenByUser(@Param("id") Long id);

  Optional<Token> findByToken(String token);

  boolean existsByToken(String token);
}
