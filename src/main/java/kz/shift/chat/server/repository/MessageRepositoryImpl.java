package kz.shift.chat.server.repository;

import kz.shift.chat.server.exception.InternalServerException;
import kz.shift.chat.server.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessageRepositoryImpl implements MessageRepository {
    private static final Logger logger = LoggerFactory.getLogger(MessageRepositoryImpl.class);
    private final DataSource dataSource;

    public MessageRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Message save(Message message) {
        String sql = """
                INSERT INTO messages (sender, text)
                VALUES (?, ?)
                RETURNING id, created_at;
                """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, message.getSender());
            statement.setString(2, message.getText());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                message.setId(
                        resultSet.getLong("id")
                );
                message.setCreatedAt(
                        resultSet.getTimestamp("created_at").toLocalDateTime()
                );
            }
            return message;
        } catch (SQLException e) {
            logger.error("Failed to save message {} in DB", message, e);
            throw new InternalServerException();
        }
    }

    @Override
    public List<Message> findAll() {
        String sql = "SELECT id, sender, text, created_at FROM messages";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery(sql);
            List<Message> messageList = new ArrayList<>();
            while (resultSet.next()) {
                Message message = new Message();
                message.setId(
                        resultSet.getLong("id")
                );
                message.setSender(
                        resultSet.getString("sender")
                );
                message.setText(
                        resultSet.getString("text")
                );
                message.setCreatedAt(
                        resultSet.getTimestamp("created_at").toLocalDateTime()
                );
                messageList.add(message);
            }
            return messageList;
        } catch (SQLException e) {
            logger.error("Failed to get message list from DB", e);
            throw new InternalServerException();
        }
    }
}
