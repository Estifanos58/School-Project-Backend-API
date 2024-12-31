package com.schoolproject.ChatAPP.repository;

import com.schoolproject.ChatAPP.model.Channel;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends MongoRepository<Channel, String> {

    // Find channels by admin ID
    List<Channel> findByAdmin_Id(String adminId);



    // self
    Optional<Channel> findById(String channelId);

    // Find channels by member ID
    List<Channel> findByMembers_Id(String memberId);

    // Search channels by name (case-insensitive)
    List<Channel> findByNameRegex(String name);

    // Find all channels containing a specific message
    List<Channel> findByMessages_Id(String messageId);

    // Find channels by admin or members containing user ID
    List<Channel> findByAdminOrMembersContaining(String adminId, String memberId);
}

