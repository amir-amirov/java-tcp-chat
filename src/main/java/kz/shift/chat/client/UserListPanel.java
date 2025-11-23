package kz.shift.chat.client;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserListPanel extends JPanel {
    private JList<String> userList;
    private DefaultListModel<String> userModel;

    public UserListPanel() {
        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        userModel = new DefaultListModel<>();
        userList = new JList<>(userModel);
        JScrollPane userScroll = new JScrollPane(userList);
        userScroll.setPreferredSize(new Dimension(150, 0));
        add(userScroll, BorderLayout.CENTER);
    }

    public void updateUsers(List<String> users) {
        SwingUtilities.invokeLater(() -> {
            userModel.clear();
            users.forEach(userModel::addElement);
        });
    }
}