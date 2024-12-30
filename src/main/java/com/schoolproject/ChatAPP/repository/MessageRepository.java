package com.schoolproject.ChatAPP.repository;

import com.schoolproject.ChatAPP.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    // Find messages between two users (private chats)
    @Query("{ '$or': [ { '$and': [ { 'sender': ?0 }, { 'recipient': ?1 } ] }, { '$and': [ { 'sender': ?1 }, { 'recipient': ?0 } ] } ] }")
    List<Message> findMessagesBetweenUsers(String senderId, String recipientId);

    // Find messages for a specific channel
    @Query("{ 'recipient': null, 'channelId': ?0 }")
    List<Message> findMessagesByChannelId(String channelId);

    // Find all messages sent by a specific user
    List<Message> findBySender(String senderId);

    // Find all messages received by a specific user
    List<Message> findByRecipient(String recipientId);

    // Find messages between two users, ordered by timestamp
    List<Message> findBySenderAndRecipientOrRecipientAndSenderOrderByTimestamps(String sender1, String recipient1, String sender2, String recipient2);
}
