package com.kusitms.forpet.repository;

import com.kusitms.forpet.domain.Community;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    @Query(value= "SELECT * FROM community c " +
            "WHERE c.address LIKE %:addr1% OR c.address LIKE %:addr2% OR c.address LIKE %:addr3% " +
            "ORDER BY c.thumbs_up_cnt DESC", nativeQuery = true)
    List<Community> findOrderByThumbsUpCntAndAddress(@Param("addr1")String addr1,
                                                      @Param("addr2")String addr2,
                                                      @Param("addr3")String addr3);

    @Query(value= "SELECT * FROM community c " +
            "WHERE c.category = :category " +
            "AND (c.address LIKE %:addr1% OR c.address LIKE %:addr2% OR c.address LIKE %:addr3%) " +
            "ORDER BY c.date DESC", nativeQuery = true)
    List<Community> findByCategoryAndAddress(@Param("category") String category,
                                             @Param("addr1")String addr1,
                                             @Param("addr2")String addr2,
                                             @Param("addr3")String addr3);

    @Query(value= "SELECT * FROM community c " +
            "WHERE c.category LIKE %:category% " +
            "AND (c.address LIKE %:addr1% OR c.address LIKE %:addr2% OR c.address LIKE %:addr3%) " +
            "ORDER BY c.date DESC", nativeQuery = true)
    List<Community> findByCategoryAndAddress(@Param("category") String category,
                                             @Param("addr1")String addr1,
                                             @Param("addr2")String addr2,
                                             @Param("addr3")String addr3,
                                             Pageable pageable);

    @Query(value= "SELECT * FROM community c " +
            "WHERE (c.title LIKE %:keyword% OR c.content LIKE %:keyword% OR c.user_id LIKE %:keyword%) " +
            "AND (c.address LIKE %:addr1% OR c.address LIKE %:addr2% OR c.address LIKE %:addr3%) " +
            "ORDER BY c.date DESC", nativeQuery = true)
    List<Community> findByKeyword(@Param("keyword") String keyword,
                                  @Param("addr1")String addr1,
                                  @Param("addr2")String addr2,
                                  @Param("addr3")String addr3,
                                  Pageable pageable);
}